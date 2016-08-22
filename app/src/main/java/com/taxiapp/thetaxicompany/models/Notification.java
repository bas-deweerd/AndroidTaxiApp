package com.taxiapp.thetaxicompany.models;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by merve on 08.05.2016.
 */
public class Notification {

  private int id;
  private String fromDevice;
  private String destination;
  private double locationLatitude;
  private double locationLongitude;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getFromDevice() {
    return fromDevice;
  }

  public void setFromDevice(String fromDevice) {
    this.fromDevice = fromDevice;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
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

  public Location getLocation() {
    Location loc = new Location(LocationManager.GPS_PROVIDER);
    loc.setLongitude(locationLongitude);
    loc.setLatitude(locationLatitude);
    return loc;
  }

  public Location getDestinationLocation(Context context){
    return AddressUtil.fromStringToLocation(context, destination, getLocation());
  }

  public final String getTitle(){
    return "new request!";
  }

  public final String getMessage(Context context){
    double distance = getLocation().distanceTo(AddressUtil.fromStringToLocation(context, destination, getLocation()));
    String message =  "Customer location : \n "+AddressUtil.getStreet(context, getLocation())+", "+AddressUtil.getZip(context, getLocation())
      +", "+AddressUtil.getCity(context, getLocation())+", "+AddressUtil.getCountry(context, getLocation())+"\n\n"
      +"Destination : \n "+destination+"\n\nDistance : \n "+distance+" km";
    return message;
  }

  public static Notification toNotification(JSONObject jsonObject){
    Notification notification = new Notification();
    try {
      notification.setLocationLongitude(Double.parseDouble(jsonObject.getString("location_longitude")));
      notification.setLocationLatitude(Double.parseDouble(jsonObject.getString("location_latitude")));
      notification.setDestination(jsonObject.getString("destination"));
      notification.setFromDevice(jsonObject.getString("from_user"));
      notification.setId(jsonObject.getInt("id"));
    } catch (JSONException e) {
    }
    return notification;
  }
}
