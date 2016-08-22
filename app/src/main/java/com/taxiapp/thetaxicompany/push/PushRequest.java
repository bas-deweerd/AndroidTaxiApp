package com.taxiapp.thetaxicompany.push;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import com.taxiapp.thetaxicompany.json.JSONUtil;
import com.taxiapp.thetaxicompany.models.Notification;
import com.taxiapp.thetaxicompany.models.TaxiDriver;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by merve on 01.05.2016.
 */
public class PushRequest {
  private final static String SENDER_ID = "881359213127";
  private static GoogleCloudMessaging gcm = null;
  private final static String key = "AIzaSyBt4ojaXsQMC36ktY7BaUmlBR0duOUNUaM";


  /**
   * Sends a notification to a group of devices.
   *
   * @param notification
   * @param maxRange the amount of drivers to receive the notification
   * @param location the current location of the customer making this request
   */
  public static void sendNotification(Notification notification, int maxRange, Location location) {
    System.out.println("invoked send notification...");
    List<TaxiDriver> drivers = getDrivers(location, maxRange);
    System.out.print("is drivers null ? ");
    System.out.print(drivers!=null);
    System.out.println("and sorted drivers size : "+drivers.size());

    for (TaxiDriver td : drivers) {
      String notificationKey = getNotificationKey(td.getId());
      sendNotification(notification, notificationKey);
    }
  }

  private static String getNotificationKey(int id) {
    String url = "http://cabtogo.eu-gb.mybluemix.net/api/accounts";
    try {
      String nk = new JSONUtil().execute(url, JSONUtil.Action.GetById.name(), id + "").get().getString("notification_key");
      return nk;
    } catch (JSONException e) {
    } catch (InterruptedException e) {
    } catch (ExecutionException e) {
    }
    return null;
  }

  ///// fetching and sorting of a list containing taxi drivers

  private static List<TaxiDriver> getDrivers(Location location, int range) {
    List<TaxiDriver> filtered = filterAvailableDrivers(getAllDrivers());
    List<TaxiDriver> sorted = sort(location, filtered);
    List<TaxiDriver> drivers = new ArrayList<>(range);

    System.out.println("range : "+ range);
    System.out.println("size of sorted list (should be 1) : "+sorted.size());
    for (int i = 0;  i < sorted.size(); i++) {
      drivers.add(sorted.get(i));
      if(i +1 == range)break;
    }
    return drivers;
  }

  private static List<TaxiDriver> sort(final Location location, List<TaxiDriver> drivers) {
    Comparator<TaxiDriver> comp = new Comparator<TaxiDriver>() {
      @Override
      public int compare(TaxiDriver l1, TaxiDriver l2) {
        if(location.distanceTo(l1.getLocation()) > location.distanceTo(l2.getLocation())) return 1;
        else if(location.distanceTo(l1.getLocation()) == location.distanceTo(l2.getLocation())) return 0;
        else return -1;
      }
    };

    Collections.sort(drivers, comp);

    return drivers;
  }

  private static List<TaxiDriver> getAllDrivers() {
    List<TaxiDriver> drivers = new ArrayList<>();
    String url = "http://cabtogo.eu-gb.mybluemix.net/api/taxi_drivers";
    try {
      JSONObject jo = new JSONUtil().execute(url, JSONUtil.Action.GetArray.name(), "taxi_drivers").get();
      JSONArray ja = jo.getJSONArray("taxi_drivers");

      for (int i = 0; i < ja.length(); i++) {
        drivers.add(TaxiDriver.toTaxiDriver(ja.getJSONObject(i)));
      }

    } catch (InterruptedException e) {
    } catch (ExecutionException e) {
    } catch (JSONException e) {
    }
    System.out.println("size all drivers"+ drivers.size());
    return drivers;
  }

  private static List<TaxiDriver> filterAvailableDrivers(List<TaxiDriver> drivers) {
    List<TaxiDriver> filtered = new ArrayList<>();

    for (TaxiDriver td : drivers) {
      if (td.isActive() && td.isAvailable()) filtered.add(td);
    }
    System.out.println("size of filtered list : "+filtered.size());
    return filtered;
  }

  ///// end of fetching and sorting

  /**
   * Sends a notification to a specific device.
   * @param notification
   * @param to
   */
  private static void sendNotification(Notification notification, String to) {
    JSONObject jo = new JSONObject();
    try {
      JSONObject jo2 = new JSONObject();
      System.out.println("notification id : "+notification.getId());
      jo2.put("message_id", notification.getId());
      jo.put("data", jo2);
      jo.put("to", to);

      final String jsonString = jo.toString();
      final String url = "https://gcm-http.googleapis.com/gcm/send";
      System.out.println(jsonString);
      System.out.println("notification json string : "+notification.getId()+", "+notification.getFromDevice());
      System.out.println("to device : "+to);


      new AsyncTask<Void, Void, Void>() {

        @Override
        protected Void doInBackground(Void... params) {
          try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            HttpResponse response;
            StringEntity entity = null;

            post.setHeader("Content-Type", "application/json");
            post.setHeader("Authorization", "key=" + key);
            entity = new StringEntity(jsonString);
            entity.setContentType("application/json");
            post.setEntity(entity);

            response = client.execute(post);
            System.out.println("response from gcm post : " + response.toString());
            client.getConnectionManager().shutdown();

          } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
          } catch (ClientProtocolException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          }
          return null;
        }
      }.execute(null, null, null);

    } catch (JSONException e) {
    }
  }

  /**
   * Gets a permanent registration id for each device.
   * Should only be executed when registering a new user.
   *
   * @param context
   * @return
   */
  public static String getRegistrationId(final Context context) {
   try {
      return new AsyncTask<Void, Void, String>() {
        String registrationId;
        String msg = "";

        @Override
        protected String doInBackground(Void... params) {
          if (gcm == null) gcm = GoogleCloudMessaging.getInstance(context);
          try {
            registrationId = gcm.register(SENDER_ID);
            msg = "Device registered : " + registrationId;

          } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR : could not register device!!");
          }
          return registrationId;
        }
      }.execute(null, null, null).get();

    } catch (InterruptedException e) {
     e.printStackTrace();
    } catch (ExecutionException e) {
     e.printStackTrace();
    }
    return null;
  }
}
