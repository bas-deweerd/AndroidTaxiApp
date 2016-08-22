package com.taxiapp.thetaxicompany;

import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.taxiapp.thetaxicompany.gps.GPSTracker;
import com.taxiapp.thetaxicompany.json.JSONUtil;
import com.taxiapp.thetaxicompany.json.ModelUrl;
import com.taxiapp.thetaxicompany.models.Notification;
import com.taxiapp.thetaxicompany.push.PushRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class CustomerActivity extends AppCompatActivity implements View.OnClickListener {

  private EditText destination;
  private Button request;
  private final static String to = "APA91bFeEJc7W52ZTvwbS75Y5mFCiMd34b83dkylbtzEWyKsnQN9CjrsGxvIyi2kD9Mz7bRJlyjhw5K-pU0BH_Jxamt9z0TW0Y_FsDW7LdJKApWutmTiwl2b6gWgFCqxvG9L6n_gg9Pq";
  private final static String key = "AIzaSyBt4ojaXsQMC36ktY7BaUmlBR0duOUNUaM"; //API-Key
  private GoogleCloudMessaging gcm;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_customer);

    gcm = GoogleCloudMessaging.getInstance(this);
    destination = (EditText) findViewById(R.id.destination_txt);
    request = (Button) findViewById(R.id.location_button);
    request.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    try {
      GPSTracker gps = new GPSTracker(this, 0, "customer");
      Location loc = gps.getLocation();
      if (loc != null) {
        String dest = destination.getText().toString();
        String from = PushRequest.getRegistrationId(this);

        System.out.println("registration id : " + from);

        //saving everything in the database
        String sendUrl = "http://cabtogo.eu-gb.mybluemix.net/api/notifications";
        String idUrl = "http://cabtogo.eu-gb.mybluemix.net/api/notifications?filter[order]=id%20DESC&filter[limit]=1";
        new JSONUtil().execute(sendUrl, JSONUtil.Action.Register.name(), idUrl, "destination", dest, "from_user", from, "location_latitude", loc.getLatitude()+"", "location_longitude", loc.getLongitude()+"");

        //getting the Notification
        String lastIdUrl = "http://cabtogo.eu-gb.mybluemix.net/api/notifications?filter[order]=id%20DESC&filter[limit]=1&filter[where][from_user]=" + from;
        Notification notification = getNotification(lastIdUrl);
        //sending the notification to the 5 nearest drivers
        System.out.println("sending request");
        new JSONUtil().execute(ModelUrl.ACCOUNT.getUrl(), JSONUtil.Action.Remove.name(), ""+4);
        PushRequest.sendNotification(notification, 5, loc);

      } else System.out.println("couldn't proceed because location is null!");

    } catch (SecurityException e) {
    }
  }

  private void postRequest(String title, String message, String to) {
    JSONObject jo = new JSONObject();
    try {
      jo.put("to", to);
      JSONObject jo2 = new JSONObject();
      jo2.put("title", title);
      jo2.put("message", "this is a messsage");
      jo.put("data", jo2);

      final String jsonString = jo.toString();
      final String url = "https://gcm-http.googleapis.com/gcm/send";

      new AsyncTask<Void, Void, Void>() {

        @Override
        protected Void doInBackground(Void... params) {
          try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            HttpResponse response;
            StringEntity entity = null;

            post.setHeader("Content-type", "application/json");
            post.setHeader("Authorization", "key=" + key);
            entity = new StringEntity(jsonString);
            entity.setContentType("application/json");
            post.setEntity(entity);

            response = client.execute(post);
            System.out.println("response from gcm post : " + response.toString());
            client.getConnectionManager().shutdown();

          } catch (UnsupportedEncodingException e) {

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

  private Notification getNotification(String url) {
    try {
      JSONObject jsonObject = new JSONUtil().execute(url, JSONUtil.Action.Get.name()).get();
      return Notification.toNotification(jsonObject);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
    return null;
  }

}
