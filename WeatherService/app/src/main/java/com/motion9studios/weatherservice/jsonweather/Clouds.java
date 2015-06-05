package com.motion9studios.weatherservice.jsonweather;

/**
 * Created by Fathima on 6/3/2015.
 */
public class Clouds {
    /**
     * Various tags corresponding to clouds object
     */
    public final static String all_JSON = "all";



    /**
     * Various fields corresponding to temperature, pressure, and
     * humidity data downloaded in Json from the Weather Service.
     */
    private long all;

    public long getAll() {
        return all;
    }

    public void setAll(long all) {
        this.all = all;
    }
}
