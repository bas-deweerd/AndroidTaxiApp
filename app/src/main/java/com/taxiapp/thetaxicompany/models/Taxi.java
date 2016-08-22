package com.taxiapp.thetaxicompany.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by merve on 21.03.2016.
 */
public class Taxi {

    private String licensePlate;
    private String model;
    private boolean isOccupied;
    private int seats;

    public Taxi(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Taxi() {
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setIsOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public static Taxi toTaxi(JSONObject jsonObject){
        Taxi taxi = new Taxi();
        if(jsonObject == null){
            System.out.println("JSONObject : NullPointerException");
            return null;
        }
        try {
            taxi.setLicensePlate(jsonObject.getString("license_plate"));
            taxi.setIsOccupied(jsonObject.getBoolean("is_occupied"));
            taxi.setModel(jsonObject.getString("model"));
            taxi.setSeats(jsonObject.getInt("seats"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return taxi;
    }
}
