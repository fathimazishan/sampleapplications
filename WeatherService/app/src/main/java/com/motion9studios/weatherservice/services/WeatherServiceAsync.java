package com.motion9studios.weatherservice.services;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.motion9studios.weatherservice.aidl.WeatherData;
import com.motion9studios.weatherservice.aidl.WeatherRequest;
import com.motion9studios.weatherservice.aidl.WeatherResults;
import com.motion9studios.weatherservice.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class WeatherServiceAsync extends LifecycleLoggingService {
    /**
     * Factory method that makes an Intent used to start the
     * WeatherServiceAsync when passed to bindService().
     *
     * @param context
     *            The context of the calling component.
     */


    public static Intent makeIntent(Context context) {
        return new Intent(context,
               WeatherServiceAsync.class);
    }

    /**
     * Called when a client (e.g., MainActivity) calls
     * bindService() with the proper Intent.  Returns the
     * implementation of WeatherRequest, which is implicitly cast as
     * an IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {


        return mWeatherRequestImpl;
    }

    /**
     * The concrete implementation of the AIDL Interface
     * WeatherRequest, which extends the Stub class that implements
     * WeatherRequest, thereby allowing Android to handle calls across
     * process boundaries.  This method runs in a separate Thread as
     * part of the Android Binder framework.
     *
     * This implementation plays the role of Invoker in the Broker
     * Pattern.
     */

    WeatherRequest.Stub mWeatherRequestImpl = new WeatherRequest.Stub() {
        /**
         * Implement the AIDL WeatherRequest getCurrentWeather()
         * method, which forwards to Utils getResults() to
         * obtain the results from the Weather Web service and
         * then sends the results back to the Activity via a
         * callback.
         */
        @Override
        public void getCurrentWeather(String city,
                                  WeatherResults callback)
                throws RemoteException {

            // Call the Acronym Web service to get the list of
            // possible expansions of the designated acronym.
            List<WeatherData> weatherResults =
                    Utils.getResults(city);



            // Invoke a one-way callback to send list of acronym
            // expansions back to the AcronymActivity.
            if (weatherResults != null) {
                Log.d(TAG, ""
                        + weatherResults.size()
                        + " results for city: "
                        + city);
                try {
                    callback.sendResults(weatherResults);
                }catch(Exception e){
                    e.printStackTrace();
                }

            } else

                callback.sendError("No weather details for "
                        + city
                        + " found");
        }
    };

}
