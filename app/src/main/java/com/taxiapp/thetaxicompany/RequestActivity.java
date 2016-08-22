package com.taxiapp.thetaxicompany;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.taxiapp.thetaxicompany.models.AddressUtil;

public class RequestActivity  extends AppCompatActivity implements View.OnClickListener {

  private TextView message;
  private Button accept;

  private SharedPreferences sharedPreferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_request);

    sharedPreferences = getSharedPreferences("Notification", Context.MODE_PRIVATE);

    message = (TextView ) findViewById(R.id.message_txt);
    accept = (Button) findViewById(R.id.accept_btn);
    accept.setOnClickListener(this);

    String msg = sharedPreferences.getString("message", "no message");
    float loc_lat = sharedPreferences.getFloat("location_latitude", 0);
    float loc_long = sharedPreferences.getFloat("location_longitude", 0);

    Location loc = new Location(LocationManager.GPS_PROVIDER);
    loc.setLatitude(loc_lat);
    loc.setLongitude(loc_long);

    message.setText(msg);
}

  @Override
  public void onClick(View v) {
    //TODO

  }
}
