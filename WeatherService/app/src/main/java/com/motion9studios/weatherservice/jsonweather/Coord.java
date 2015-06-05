package com.motion9studios.weatherservice.jsonweather;

/**
 * Created by Fathima on 6/2/2015.
 */
public class Coord {

    /**
     * Various tags corresponding to temperature, pressure, and
     * humidity data downloaded in Json from the Weather Service.
     */
    public final static String lon_JSON = "lon";
    public final static String lat_JSON = "lat";


    /**
     * Various fields corresponding to temperature, pressure, and
     * humidity data downloaded in Json from the Weather Service.
     */
    private double lon;
    private double lat;

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
