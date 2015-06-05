package com.motion9studios.weatherservice.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.motion9studios.weatherservice.R;
import com.motion9studios.weatherservice.operations.WeatherOps;
import com.motion9studios.weatherservice.operations.WeatherOpsImpl;


public class MainActivity extends LifecycleLoggingActivity {



    /**
     * Provides weather-related operations.
     */
    private WeatherOps mWeatherOps;

    // The layout which displays the weather details of the city

    private LinearLayout resultLinearLayout;

    // The edit text field where the user keys in the city followed by the country

    private EditText city;
    /**
     * Hook method called when a new instance of Activity is created.
     * One time initialization code goes here, e.g., runtime
     * configuration changes.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Always call super class for necessary
        // initialization/implementation.
        super.onCreate(savedInstanceState);

        // inflate the layout xml file
        setContentView(R.layout.main_activity);

        // Create an instance of weather ops
        mWeatherOps = new WeatherOpsImpl(this);

        //Initiate the service binding protocol
        mWeatherOps.bindService();

        //Initially the result linear layout will be invisible

        resultLinearLayout = (LinearLayout) findViewById(R.id.resultLinearLayout);
        resultLinearLayout.setVisibility(View.INVISIBLE);



    }

    /**
     * Hook method called by Android when this Activity is
     * destroyed.
     */
    @Override
    protected void onDestroy() {
        // Unbind from the Service.
        mWeatherOps.unbindService();

        // Always call super class for necessary operations when an
        // Activity is destroyed.
        super.onDestroy();
    }

    /**
     * Handle hardware reconfigurations, such as rotating the display.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

    }


    /*
     * Initiate the synchronous acronym lookup when the user presses
     * the "Look Up Sync" button.
     */
    public void getWeatherSync(View v) {


        // refresh the result linear layout
        resultLinearLayout.setVisibility(View.INVISIBLE);

        //retrieve the city name from the edit text
        city = (EditText) findViewById(R.id.editText1);

        //retrieve results synchronously
        mWeatherOps.getWeatherSync(city.getText().toString());



    }

    /*
     * Initiate the asynchronous acronym lookup when the user presses
     * the "Look Up Async" button.
     */
    public void getWeatherAsync(View v) {


        // refresh the result linear layout
        resultLinearLayout.setVisibility(View.INVISIBLE);

        //retrieve the city name from the edit text
        city = (EditText) findViewById(R.id.editText1);

        //retrieve results asynchronously
        mWeatherOps.getWeatherAsync(city.getText().toString());

    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
