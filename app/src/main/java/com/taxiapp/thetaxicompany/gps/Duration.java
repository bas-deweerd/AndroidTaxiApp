package com.taxiapp.thetaxicompany.gps;

public class Duration {
    public String text;
    public int value;

    /**
     *
     * @param text Google standard textual representation, f.e. "1 hour 3 mins"
     * @param value is the duration in seconds
     */
    public Duration(String text, int value) {
        this.text = text;
        this.value = value;
    }
}
