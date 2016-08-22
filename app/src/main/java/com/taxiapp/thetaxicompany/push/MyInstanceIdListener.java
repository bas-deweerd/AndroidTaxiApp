package com.taxiapp.thetaxicompany.push;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by merve on 08.05.2016.
 */
public class MyInstanceIdListener extends InstanceIDListenerService {

  @Override
  public void onTokenRefresh() {
    Intent intent = new Intent(this, RegistrationIntentService.class);
    startService(intent);
  }
}
