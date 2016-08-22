package com.taxiapp.thetaxicompany.gps;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Model for a route between two locations.
 */
public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}
