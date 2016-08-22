package com.taxiapp.thetaxicompany.push;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.taxiapp.thetaxicompany.R;
import com.taxiapp.thetaxicompany.RequestActivity;
import com.taxiapp.thetaxicompany.TaxiDriverActivity;
import com.taxiapp.thetaxicompany.models.Notification;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A class that handles notifications.
 * Created by merve on 02.05.2016.
 */
public class MessageService extends GcmListenerService {

  private SharedPreferences sp;//accessible over the entire application

  @Override
  public void onMessageReceived(String from, Bundle data) {
    int message = Integer.parseInt( data.getString("message_id"));
    System.out.println("in message service .. message id : "+message);
    sendNotification(getNotification(message));
  }

  private Notification getNotification(int id) {
    String idUrl = "http://cabtogo.eu-gb.mybluemix.net/api/notifications/" + id;
    JSONObject obj;
    URL u;
    try {
      u = new URL(idUrl);
      BufferedReader reader = new BufferedReader(new InputStreamReader(u.openStream()));
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line + " ");
      }
      u.openStream().close();
      obj = new JSONObject(sb.toString());

      return Notification.toNotification(obj);
    } catch (MalformedURLException e) {
    } catch (IOException e) {
    } catch (JSONException e) {
    }
    return null;
  }

  private void sendNotification(Notification n) {

    sp = getSharedPreferences("Notification", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sp.edit();
    editor.putString("title", n.getTitle());
    editor.putString("message", n.getMessage(this));
    editor.putFloat("location_latitude", (float) n.getLocationLatitude());
    editor.putFloat("location_longitude", (float) n.getLocationLongitude());
    editor.putInt("message_id", n.getId());
    editor.apply();

    System.out.println("message in messageserivce : "+sp.getString("message", "no message"));

    Bundle info = new Bundle();
    info.putString("title", n.getTitle());
    info.putString("message", n.getMessage(this));
    info.putString("from", n.getFromDevice());
    info.putString("destination", n.getDestination());
    info.putDouble("location_latitude", n.getLocationLatitude());
    info.putDouble("location_longitude", n.getLocationLongitude());


    Intent sendRequestIntent = new Intent(this, RequestActivity.class);
    sendRequestIntent.putExtra("info", info);

    NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
    notification.setContentTitle(n.getTitle());
    notification.setContentText(n.getMessage(this));
    notification.setTicker("New Request!");
    notification.setSmallIcon(R.drawable.cast_ic_notification_0);   //set taxi icon here

    PendingIntent pi = PendingIntent.getActivity(this, 1000, sendRequestIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    notification.setContentIntent(pi);
    notification.setAutoCancel(true);

    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    nm.notify(0, notification.build());

  }
}
