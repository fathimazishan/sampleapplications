package com.motion9studios.weatherservice.aidl;

import com.motion9studios.weatherservice.aidl.WeatherData;
import java.util.List;

/**
 * Interface defining the method implemented within WeatherServiceSync
 * that provides synchronous access to the Weather Service web
 * service.
 */
interface WeatherCall {
   /**
    * A two-way (blocking) call that retrieves information about the
    * current weather from the Weather Service web service and returns
    * a list of WeatherData objects containing the results from the
    * Weather Service web service back to the WeatherActivity.
    */
    List<WeatherData> getCurrentWeather(in String Weather); 
}
