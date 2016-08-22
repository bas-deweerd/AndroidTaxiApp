package com.taxiapp.thetaxicompany.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by merve on 21.03.2016.
 */
public class Ride {

    private int customerId;
    private int accountId;
    private String destination;
    private String startLocation;
    private String startTime;
    private String endTime;
    private String request_time;

    public Ride() {
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRequest_time() {
        return request_time;
    }

    public void setRequestTime(String request_time) {
        this.request_time = request_time;
    }

    public static Ride toRide(JSONObject jsonObject){
        Ride ride = new Ride();
        if(jsonObject == null){
            System.out.println("JSONObject : NullPointerException");
            return null;
        }
        try {
            ride.setAccountId(jsonObject.getInt("account_id"));
            ride.setCustomerId(jsonObject.getInt("customer_id"));
            ride.setDestination(jsonObject.getString("destination"));
            ride.setStartLocation(jsonObject.getString("start_location"));
            ride.setStartTime(jsonObject.getString("start_time"));
            ride.setEndTime(jsonObject.getString("end_time"));
            ride.setRequestTime(jsonObject.getString("request_time"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ride;
    }
}
