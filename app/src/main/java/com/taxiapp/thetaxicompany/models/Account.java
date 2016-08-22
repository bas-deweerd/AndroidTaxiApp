package com.taxiapp.thetaxicompany.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by merve on 21.03.2016.
 */
public class Account {

    private int id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public Account() {
    }

    public Account(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Converts a JSONObject into an Account.
     * @param jsonObject
     * @return
     */
    public static Account toAccount(JSONObject jsonObject){
       Account account = new Account();
        if(jsonObject == null){
            System.out.println("JSONObject : NullPointerException");
            return null;
        }
        try {
            account.setId(jsonObject.getInt("ID"));
            account.setEmail(jsonObject.getString("Email"));
            account.setPassword(jsonObject.getString("Password"));
            account.setFirstName(jsonObject.getString("FirstName"));
            account.setLastName(jsonObject.getString("LastName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return account;
    }
}


