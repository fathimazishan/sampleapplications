package com.motion9studios.weatherservice.jsonweather;

/**
 * Created by Fathima on 6/3/2015.
 */
public class Rain {

    /**
     * Various tags corresponding to clouds object
     */
    public final static String three_hour_JSON = "3h";


    public double getThreeHour() {
        return threeHour;
    }

    public void setThreeHour(double threeHour) {
        this.threeHour = threeHour;
    }

    /**

     * Various fields corresponding to temperature, pressure, and
     * humidity data downloaded in Json from the Weather Service.
     */
    private double threeHour;
}
