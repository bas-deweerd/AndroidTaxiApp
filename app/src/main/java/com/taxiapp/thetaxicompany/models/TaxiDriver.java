package com.taxiapp.thetaxicompany.models;

import android.location.Location;
import android.location.LocationManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by merve on 01.05.2016.
 */
public class TaxiDriver {

    private int id;
    private int phoneNumber;
    private double locationLatitude;
    private double locationLongitude;
    private boolean isActive;
    private boolean isAvailable;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Location getLocation(){
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLongitude(locationLongitude);
        location.setLatitude(locationLatitude);
        return location;
    }

  public static TaxiDriver toTaxiDriver(JSONObject jsonObject){
    TaxiDriver driver = new TaxiDriver();
    try {
      driver.setId(jsonObject.getInt("account_id"));
      driver.setIsActive(jsonObject.getBoolean("is_active"));
      driver.setIsAvailable(jsonObject.getBoolean("is_available"));
      driver.setLocationLatitude(jsonObject.getDouble("location_latitude"));
      driver.setLocationLongitude(jsonObject.getDouble("location_longitude"));

    } catch (JSONException e) {
    }
    return driver;
  }
}
