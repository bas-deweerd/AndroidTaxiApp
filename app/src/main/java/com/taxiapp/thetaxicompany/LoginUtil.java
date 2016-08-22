package com.taxiapp.thetaxicompany;

import android.content.Context;
import android.content.Intent;

import com.taxiapp.thetaxicompany.CustomerActivity;
import com.taxiapp.thetaxicompany.TaxiDriverActivity;

import org.json.JSONException;
import org.json.JSONObject;
import com.taxiapp.thetaxicompany.json.JSONUtil;
import com.taxiapp.thetaxicompany.json.ModelUrl;

import java.util.concurrent.ExecutionException;

/**
 * Created by merve on 01.05.2016.
 */
public class LoginUtil {

    private static JSONObject jsonObject;

    public static boolean authenticate(final String email, final String password) {
        return passwordMatches(email, password);
    }

    private static boolean userExists(final String email) {
        try {
            String url = "http://cabtogo.eu-gb.mybluemix.net/api/accounts";
            jsonObject = new JSONUtil().execute(url, JSONUtil.Action.Filter.name(), "email", email).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return jsonObject != null;
    }

    private static boolean passwordMatches(final String email, final String password) {
        if (userExists(email)) {
            String pw = null;
            try {
                pw = jsonObject.getString("password");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return pw.equals(password);
        }
        return false;
    }

    private static String getUserGroup(final String email) {
        if (userExists(email)) {
            String driverUrl = "http://cabtogo.eu-gb.mybluemix.net/api/taxi_drivers";
            String customerUrl = "http://cabtogo.eu-gb.mybluemix.net/api/customers";
            try {
                int id = jsonObject.getInt("id");
                JSONObject jo = new JSONUtil().execute(driverUrl, JSONUtil.Action.GetById.name(), "" + id).get();
                JSONObject jo2 = new JSONUtil().execute(customerUrl, JSONUtil.Action.GetById.name(), "" + id).get();

                if (jo != null) return "taxi_driver";
                else if (jo2 != null) return "customer";

                System.out.println("user group not found");
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
            } catch (JSONException e) {
            }
        }
        return null;
    }

    /**
     * Identifies the usergroup and redirects to the correct view.
     *
     * @param email
     * @param password
     * @param context
     * @return
     */
    public static Intent redirectUser(final String email, final String password, final Context context) {
        String userGroup = getUserGroup(email);
        if (userGroup != null) {
            if (userGroup.equals("customer")) return new Intent(context, CustomerActivity.class);
            else if (userGroup.equals("taxi_driver")) {
                try {
                    int id = jsonObject.getInt("id");
                    JSONObject jo = new JSONUtil().execute(ModelUrl.TAXI_DRIVER.getUrl(), JSONUtil.Action.Filter.name(), "account_id", id + "").get();
                    jo.put("is_active", true);
                    jo.put("is_available", true);
                    new JSONUtil().execute(ModelUrl.TAXI_DRIVER.getUrl(), JSONUtil.Action.Update.name(), jo.toString());

                } catch (JSONException e) {
                } catch (InterruptedException e) {
                } catch (ExecutionException e) {
                }
                return new Intent(context, TaxiDriverActivity.class);
            }
        }
        return null;
    }

    public static JSONObject getJsonObject() {
        return jsonObject;
    }
}
