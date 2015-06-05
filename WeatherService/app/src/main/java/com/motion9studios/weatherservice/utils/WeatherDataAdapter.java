package com.motion9studios.weatherservice.utils;

import android.content.Context;
import android.provider.CalendarContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.motion9studios.weatherservice.R;
import com.motion9studios.weatherservice.activities.MainActivity;
import com.motion9studios.weatherservice.aidl.WeatherData;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Fathima on 5/31/2015.
 */
public class WeatherDataAdapter {


    // Elements of result linear layout
    private TextView cityTextView;

    private TextView countryTextView;

    private TextView dayTextView;

    private TextView currentTemperatureTextView;

    private TextView currentHumidityTextView;

    private TextView currentWindSpeedTextView;

    private LinearLayout resultLinearLayout;



    public WeatherDataAdapter(WeakReference<MainActivity> mActivity) {

        // initializing the result linear layout
        resultLinearLayout = (LinearLayout) mActivity.get().findViewById(R.id.resultLinearLayout);

        //initializing the result linear layout elements
        cityTextView = (TextView) mActivity.get().findViewById(R.id.cityTextView);
        countryTextView = (TextView) mActivity.get().findViewById(R.id.countryTextView);
        dayTextView = (TextView) mActivity.get().findViewById(R.id.dayTextView);
        currentTemperatureTextView = (TextView) mActivity.get().findViewById(R.id.currentTemperatureTextView);
        currentHumidityTextView = (TextView) mActivity.get().findViewById(R.id.currentHumidityTextView);
        currentWindSpeedTextView = (TextView) mActivity.get().findViewById(R.id.currentWindSpeedTextView);

    }

    public void displayResults(WeatherData weatherData) {

        // Show the result linear layout which houses the weather details
        resultLinearLayout.setVisibility(View.VISIBLE);

        // set all the elements of the layout with values retrieved from weather data object
        cityTextView.setText(weatherData.getmName()+",");
        countryTextView.setText(weatherData.getmCountry());
        dayTextView.setText(weatherData.getmDate());
        currentTemperatureTextView.setText(String.valueOf(weatherData.getmTemp())+" deg Celsius");
        currentHumidityTextView.setText(String.valueOf(weatherData.getmHumidity())+" %");
        currentWindSpeedTextView.setText(String.valueOf(weatherData.getmSpeed())+ " m/s");
    }


}
