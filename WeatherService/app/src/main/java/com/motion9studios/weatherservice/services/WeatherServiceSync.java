package com.motion9studios.weatherservice.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.motion9studios.weatherservice.activities.MainActivity;
import com.motion9studios.weatherservice.aidl.WeatherCall;
import com.motion9studios.weatherservice.aidl.WeatherData;
import com.motion9studios.weatherservice.utils.GenericServiceConnection;
import com.motion9studios.weatherservice.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fathima on 5/29/2015.
 */
public class WeatherServiceSync extends LifecycleLoggingService {
    /**
     * Factory method that makes an Intent used to start the
     * WeatherServiceSync when passed to bindService().
     *
     * @param context
     *            The context of the calling component.
     */
    public static Intent makeIntent(Context context) {
        return new Intent(context,
                WeatherServiceSync.class);
    }

    /**
     * Called when a client (e.g., MainActivity) calls
     * bindService() with the proper Intent.  Returns the
     * implementation of WeatherCall, which is implicitly cast as an
     * IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mWeatherCallImpl;
    }

    /**
     * The concrete implementation of the AIDL Interface WeatherCall,
     * which extends the Stub class that implements WeatherCall,
     * thereby allowing Android to handle calls across process
     * boundaries.  This method runs in a separate Thread as part of
     * the Android Binder framework.
     *
     * This implementation plays the role of Invoker in the Broker
     * Pattern.
     */
    WeatherCall.Stub mWeatherCallImpl = new WeatherCall.Stub() {
        /**
         * Implement the AIDL WeatherCall getCurrentWeather() method,
         * which forwards to Utils getResults() to obtain
         * the results from the Weather Web service and then
         * sends the results to the WeatherActivity.
         */
        @Override
        public List<WeatherData> getCurrentWeather(String city)
                throws RemoteException {

            // Call the Weather Web service to get the current weather
            List<WeatherData> weatherResults =
                    Utils.getResults(city);

            if (weatherResults != null) {
                Log.d(TAG, ""
                        + weatherResults.size()
                        + " results for city: "
                        + city);

                // Return the weather details
                return weatherResults;
            } else {
                // Create a zero-sized weatherResults object to
                // indicate to the caller that the service could not retrieve any weather details
                weatherResults = new ArrayList<WeatherData>();
                return weatherResults;
            }
        }
    };
}
