package com.motion9studios.weatherservice.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.util.JsonReader;
import java.util.GregorianCalendar;

import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.motion9studios.weatherservice.aidl.WeatherData;
import com.motion9studios.weatherservice.jsonweather.JsonWeather;
import com.motion9studios.weatherservice.jsonweather.Weather;
import com.motion9studios.weatherservice.jsonweather.WeatherJSONParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @class Utils
 *
 * @brief Handles the actual downloading of weather details
 */
public class Utils {
    /**
     * Logging tag used by the debugger.
     */
    private final static String TAG = Utils.class.getCanonicalName();

    /**
     * URL to the Weather web service.
     */
    private final static String sWeather_Web_Service_URL =
            "http://api.openweathermap.org/data/2.5/weather?q=";

    /**
     * Obtain the Acronym information.
     *
     * @return The information that responds to your current acronym search.
     */

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static List<WeatherData> getResults(final String city) {
        // Create a List that will return the WeatherData obtained
        // from the Weather Service web service.
        final List<WeatherData> returnList =
                new ArrayList<WeatherData>();


        // A List of JsonWeather objects.
        List<JsonWeather> jsonWeathers = null;



        try {
            // Append the location to create the full URL.
            final URL url =
                    new URL(sWeather_Web_Service_URL
                            + city);

            // Opens a connection to the Acronym Service.
            HttpURLConnection urlConnection =
                    (HttpURLConnection) url.openConnection();



            // Sends the GET request and reads the Json results.
            try (InputStream in =
                         new BufferedInputStream(urlConnection.getInputStream())) {



                // Create the parser.
                final WeatherJSONParser parser =
                        new WeatherJSONParser();


                jsonWeathers = parser.parseJsonStream(in);


            }  finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (jsonWeathers != null && jsonWeathers.size() > 0) {
            // Convert the JsonWeather data objects to our WeatherData
            // object, which can be passed between processes.
            for (JsonWeather jsonWeather : jsonWeathers)

                returnList.add(new WeatherData(jsonWeather.getName(),
                                   jsonWeather.getSys().getCountry(),
                                   getToday(),
                                   jsonWeather.getWind().getSpeed(),
                                   jsonWeather.getWind().getDeg(),jsonWeather.getMain().getTemp(),
                                   jsonWeather.getMain().getHumidity(),jsonWeather.getSys().getSunrise(),
                                   jsonWeather.getSys().getSunset()));
     }



        // Return the List of WeatherData.

        return returnList;

    }

    private static String getToday(){
        GregorianCalendar rightNow = (GregorianCalendar) GregorianCalendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");

        String strDate = sdf.format(rightNow.getTime());

        Log.d("DATE", strDate);
        return strDate;
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    /**
     * This method is used to hide a keyboard after a user has
     * finished typing the url.
     */
    public static void hideKeyboard(Activity activity,
                                    IBinder windowToken) {
        InputMethodManager mgr =
                (InputMethodManager) activity.getSystemService
                        (Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken,
                0);
    }

    /**
     * Show a toast message.
     */
    public static void showToast(Context context,
                                 String message) {
        Toast.makeText(context,
                message,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Ensure this class is only used as a utility.
     */
    private Utils() {
        throw new AssertionError();
    }
}
