package com.taxiapp.thetaxicompany.gps;

public class Distance {
    public String text;
    public int value;

    /**
     *
     * @param text Google standard textual representation, f.e. "1.3 km"
     * @param value is the distance in meters
     */
    public Distance(String text, int value) {
        this.text = text;
        this.value = value;
    }
}