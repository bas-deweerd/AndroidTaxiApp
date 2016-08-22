package com.taxiapp.thetaxicompany.push;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;

import com.taxiapp.thetaxicompany.R;
import com.taxiapp.thetaxicompany.json.JSONUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Checks whether the registration id differs from the one in the database, if so it updates the changes
 * Created by merve on 08.05.2016.
 */
public class RegistrationIntentService extends IntentService {

  private final static String TAG = "RegistrationIntentService";
  private SharedPreferences sharedPreferences;

  public RegistrationIntentService() {
    super(TAG);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    sharedPreferences = getSharedPreferences("User", 0);
    try {
      InstanceID instanceID = InstanceID.getInstance(this);
      String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

      int id = sharedPreferences.getInt("id", 0);
      String url= "http://cabtogo.eu-gb.mybluemix.net/api/accounts";
      JSONObject jsonObject = getJson(url, id);

      jsonObject.put("notification_key", token);
      sendJson(url, jsonObject.toString());

    } catch (IOException e) {
    } catch (JSONException e) {
    }
  }

  private JSONObject getJson(String url, int id){
    String idUrl = url.concat("/").concat(""+id);
    JSONObject obj;
    URL u;
    try {
      u = new URL(idUrl);
      BufferedReader reader = new BufferedReader(new InputStreamReader(u.openStream()));
      StringBuilder sb = new StringBuilder();
      String line;
      while((line = reader.readLine()) != null){
        sb.append(line + " ");
      }
      u.openStream().close();
      obj = new JSONObject(sb.toString());
      return obj;
    } catch (MalformedURLException | JSONException e) {
    } catch (IOException e) {
    }
   return null;
  }

  private void sendJson(String url, String json){
    try {
      HttpClient client = new DefaultHttpClient();
      HttpPost post = new HttpPost(url);
      HttpResponse response;

      StringEntity entity = new StringEntity(json);
      entity.setContentType("application/json");
      post.setEntity(entity);

      response = client.execute(post);
      System.out.println("json updated with new registration id");
      client.getConnectionManager().shutdown();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
