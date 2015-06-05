package com.motion9studios.weatherservice.operations;


import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import android.content.Context;

import android.content.res.Configuration;
import android.os.AsyncTask;

import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import com.motion9studios.weatherservice.activities.MainActivity;
import com.motion9studios.weatherservice.aidl.WeatherCall;
import com.motion9studios.weatherservice.aidl.WeatherData;
import com.motion9studios.weatherservice.aidl.WeatherRequest;
import com.motion9studios.weatherservice.aidl.WeatherResults;
import com.motion9studios.weatherservice.services.WeatherServiceAsync;
import com.motion9studios.weatherservice.services.WeatherServiceSync;
import com.motion9studios.weatherservice.utils.GenericServiceConnection;
import com.motion9studios.weatherservice.utils.Utils;
import com.motion9studios.weatherservice.utils.WeatherDataAdapter;


/**
 * This class implements all the acronym-related operations defined in
 * the AcronymOps interface.
 */
public class WeatherOpsImpl implements WeatherOps {
    /**
     * Debugging tag used by the Android logger.
     */
    protected final String TAG = getClass().getSimpleName();

    /**
     * Used to enable garbage collection.
     */
    protected WeakReference<MainActivity> mActivity;

    /**
     * This GenericServiceConnection is used to receive results after
     * binding to the AcronymServiceSync Service using bindService().
     */
    private GenericServiceConnection<WeatherCall> mServiceConnectionSync;

    /**
     * This GenericServiceConnection is used to receive results after
     * binding to the AcronymServiceAsync Service using bindService().
     */
    private GenericServiceConnection<WeatherRequest> mServiceConnectionAsync;

    /**
     * List of results to display (if any).
     */
    protected List<WeatherData> mResults;

    /**
     * This Handler is used to post Runnables to the UI from the
     * mWeatherResults callback methods to avoid a dependency on the
     * Activity, which may be destroyed in the UI Thread during a
     * runtime configuration change.
     */
    private final Handler mDisplayHandler = new Handler();



    /**
     * The implementation of the WeatherResults AIDL Interface, which
     * will be passed to the Weather Web service using the
     * WeatherRequest.getCurrentWeather() method.
     *
     * This implementation of WeatherResults.Stub plays the role of
     * Invoker in the Broker Pattern since it dispatches the upcall to
     * sendResults().
     */
    private final WeatherResults.Stub weatherResults =
            new WeatherResults.Stub() {
                /**
                 * This method forwards the weather details to display results method
                 * and displays the retrieved information using a WeatherDataAdapter
                 */
                @Override
                public void sendResults(final List<WeatherData> weatherDataList)
                        throws RemoteException {
                    // Since the Android Binder framework dispatches this
                    // method in a background Thread we need to explicitly
                    // post a runnable containing the results to the UI
                    // Thread, where it's displayed.  We use the
                    // mDisplayHandler to avoid a dependency on the
                    // Activity, which may be destroyed in the UI Thread
                    // during a runtime configuration change.
                    mDisplayHandler.post(new Runnable() {
                        public void run() {

                            try {
                                mResults = weatherDataList;
                                displayResults(weatherDataList);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }

                /**
                 * This method forwards null value to display results method
                 */
                @Override
                public void sendError(final String reason)
                        throws RemoteException {
                    // Since the Android Binder framework dispatches this
                    // method in a background Thread we need to explicitly
                    // post a runnable containing the results to the UI
                    // Thread, where it's displayed.  We use the
                    // mDisplayHandler to avoid a dependency on the
                    // Activity, which may be destroyed in the UI Thread
                    // during a runtime configuration change.
                    mDisplayHandler.post(new Runnable() {
                        public void run() {
                            try {
                                displayResults(null);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            };

    /*
     * Displays the result using Weather Data Adapter in the result linear layout
     */
    private void displayResults(List<WeatherData> weatherDataList) throws IOException{

        if(weatherDataList!=null && weatherDataList.size() > 0){


            WeatherDataAdapter weatherDataAdapter = new WeatherDataAdapter(mActivity);
            for (int count = 0; count < weatherDataList.size(); count++){
                WeatherData weatherData = weatherDataList.get(count);


                weatherDataAdapter.displayResults(weatherData);
            }
        }

        else{
            Utils.showToast(mActivity.get().getApplicationContext(),"No results available");
        }
    }

    /**
     * Constructor initializes the fields.
     */
    public WeatherOpsImpl(MainActivity activity) {
        // Initialize the WeakReference.
        mActivity = new WeakReference<>(activity);


        // Initialize the GenericServiceConnection objects.
        mServiceConnectionSync =
                new GenericServiceConnection<WeatherCall>(WeatherCall.class);

        mServiceConnectionAsync =
                new GenericServiceConnection<WeatherRequest>(WeatherRequest.class);





    }




    /**
     * Initiate the service binding protocol.
     */
    @Override
    public void bindService() {
        Log.d(TAG,
                "calling bindService()");

        // Launch the Weather Bound Services if they aren't already
        // running via a call to bindService(), which binds this
        // activity to the WeatherService* if they aren't already
        // bound.
        if (mServiceConnectionSync.getInterface() == null)
            mActivity.get().getApplicationContext().bindService
                    (WeatherServiceSync.makeIntent(mActivity.get()),
                            mServiceConnectionSync,
                            Context.BIND_AUTO_CREATE);

        if (mServiceConnectionAsync.getInterface() == null)
            mActivity.get().getApplicationContext().bindService
                    (WeatherServiceAsync.makeIntent(mActivity.get()),
                            mServiceConnectionAsync,
                            Context.BIND_AUTO_CREATE);
    }

    /**
     * Initiate the service unbinding protocol.
     */
    @Override
    public void unbindService() {
        if (mActivity.get().isChangingConfigurations())
            Log.d(TAG,
                    "just a configuration change - unbindService() not called");
        else {
            Log.d(TAG,
                    "calling unbindService()");

            // Unbind the Async Service if it is connected.
            if (mServiceConnectionAsync.getInterface() != null)
                mActivity.get().getApplicationContext().unbindService
                        (mServiceConnectionAsync);

            // Unbind the Sync Service if it is connected.
            if (mServiceConnectionSync.getInterface() != null)
                mActivity.get().getApplicationContext().unbindService
                        (mServiceConnectionSync);
        }
    }

    /*
     * Initiate the asynchronous acronym lookup when the user presses
     * the "Look Up Async" button.
     */
    public void getWeatherAsync(String city) {




        final WeatherRequest weatherRequest =
                mServiceConnectionAsync.getInterface();


        if (weatherRequest != null ) {
            try {
                // Invoke a one-way AIDL call, which does not block
                // the client.  The results are returned via the
                // sendResults() method of the mAcronymResults
                // callback object, which runs in a Thread from the
                // Thread pool managed by the Binder framework.
                weatherRequest.getCurrentWeather(city,
                        weatherResults);
            } catch (RemoteException e) {
                Log.e(TAG,
                        "RemoteException:"
                                + e.getMessage());
            }
        } else {
            Log.d(TAG,
                    "weatherRequest was null.");
        }
    }

    /*
     * Initiate the synchronous acronym lookup when the user presses
     * the "Look Up Sync" button.
     */
    public void getWeatherSync(String city) {
        final WeatherCall weatherCall =
                mServiceConnectionSync.getInterface();




        if (weatherCall != null ) {
            // Use an anonymous AsyncTask to download the Acronym data
            // in a separate thread and then display any results in
            // the UI thread.
            new AsyncTask<String, Void, List<WeatherData>>() {
                /**
                 * Acronym we're trying to expand.
                 */
                private String city;

                /**
                 * Retrieve the expanded acronym results via a
                 * synchronous two-way method call, which runs in a
                 * background thread to avoid blocking the UI thread.
                 */
                protected List<WeatherData> doInBackground(String... cities) {
                    try {
                        city = cities[0];
                        return weatherCall.getCurrentWeather(city);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                /**
                 * Display the results in the UI Thread.
                 */
                protected void onPostExecute(List<WeatherData> weatherDataList) {

                    if(weatherDataList!=null) {
                        try {
                            mResults = weatherDataList;
                            displayResults(weatherDataList);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
                // Execute the AsyncTask to expand the acronym without
                // blocking the caller.
            }.execute(city);
        } else {
            Log.d(TAG, "weatherCall was null.");
        }
    }
}
