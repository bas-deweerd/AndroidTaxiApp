package com.taxiapp.thetaxicompany.json;

/**
 * Created by merve on 21.03.2016.
 */
public enum ModelUrl {
    ACCOUNT ("http://cabtogo.eu-gb.mybluemix.net/api/accounts"),
    ADMIN ("http://cabtogo.eu-gb.mybluemix.net/api/users"),
    TAXI_DRIVER ("http://cabtogo.eu-gb.mybluemix.net/api/taxi_drivers"),
    CUSTOMER ("http://cabtogo.eu-gb.mybluemix.net/api/customers"),
    RIDE ("http://cabtogo.eu-gb.mybluemix.net/api/rides"),
    SHIFT ("http://cabtogo.eu-gb.mybluemix.net/api/shifts"),
    TAXI ("http://cabtogo.eu-gb.mybluemix.net/api/taxis");

    private final String url;

    ModelUrl(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }
}
