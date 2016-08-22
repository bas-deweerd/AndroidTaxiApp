package com.taxiapp.thetaxicompany.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by merve on 21.03.2016.
 */
public class Customer {

    private int accountId;
    private int phoneNumber;
    private String creditCard;

    public Customer(int accountId) {
        this.accountId = accountId;
    }

    public Customer() {
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public static Customer toCustomer(JSONObject jsonObject){
        Customer customer = new Customer();
        if(jsonObject == null){
            System.out.println("JSONObject : NullPointerException");
            return null;
        }
        try {
            customer.setAccountId(jsonObject.getInt("account_id"));
            customer.setPhoneNumber(jsonObject.getInt("phone_number"));
            customer.setCreditCard(jsonObject.getString("credit_card"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return customer;
    }

}



