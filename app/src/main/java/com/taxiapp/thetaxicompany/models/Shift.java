package com.taxiapp.thetaxicompany.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by merve on 21.03.2016.
 */
public class Shift {

    private int accountId;
    private int licensePlate;
    private String start;
    private String end;

    public Shift(int accountId) {
        this.accountId = accountId;
    }

    public Shift() {
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(int licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public static Shift toShift(JSONObject jsonObject){
        Shift shift = new Shift();
        if(jsonObject == null){
            System.out.println("JSONObject : NullPointerException");
            return null;
        }
        try {
            shift.setAccountId(jsonObject.getInt("account_id"));
            shift.setLicensePlate(jsonObject.getInt("license_plate"));
            shift.setStart(jsonObject.getString("start"));
            shift.setEnd(jsonObject.getString("end"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return shift;
    }

}

