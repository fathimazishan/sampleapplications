package com.motion9studios.weatherservice.operations;

import android.content.res.Configuration;
import android.view.View;

import com.motion9studios.weatherservice.activities.MainActivity;

/**
 * Created by Fathima on 5/29/2015.
 */
public interface WeatherOps {
    /*
    Initiate service unbinding protocol
     */
    void unbindService();
    /*
       Initiate service binding protocol
        */
    void bindService();


     /*
    Initiates retrieving weather details synchronously
     */

    void getWeatherSync(String city);
    /*
   Initiate retrieving weather details asynchronously
    */
    void getWeatherAsync(String city);


}
