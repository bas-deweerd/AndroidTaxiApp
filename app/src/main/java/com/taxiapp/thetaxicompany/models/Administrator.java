package com.taxiapp.thetaxicompany.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by merve on 21.03.2016.
 */
public class Administrator {

    private int id;

    public Administrator(int id) {
        this.id = id;
    }

    public Administrator() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static Administrator toAdministrator(JSONObject jsonObject){
        Administrator admin = new Administrator();
        if(jsonObject == null){
            System.out.println("JSONObject : NullPointerException");
            return null;
        }
        try {
            admin.setId(jsonObject.getInt("Admin_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return admin;
    }
}
