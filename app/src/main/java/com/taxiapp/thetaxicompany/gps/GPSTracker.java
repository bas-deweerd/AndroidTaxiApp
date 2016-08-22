package com.taxiapp.thetaxicompany.gps;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import com.taxiapp.thetaxicompany.json.JSONUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by merve on 01.05.2016.
 */
public class GPSTracker extends Service implements LocationListener {

    private final int userId;
    private final Context context;
    private boolean isGPSEnabled = false;
    private String userGroup;

    private Location location;
    private double longitude;
    private double latitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 100;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    protected LocationManager locationManager;

    /**
     * @param context Current Activity of the user
     * @param id      user id (from the database)
     */
    public GPSTracker(Context context, int id, String ug) {
        this.context = context;
        this.userId = id;
        userGroup = ug;
        getLocation();
    }

    public Location getLocation() {
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGPSEnabled) {
            showSettings();
        } else {
            if (location == null) {
                try {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("GPS Enabled", "GPS Enabled");
                    if (locationManager != null) {
                        Log.d("loc.man. not null", "loc.man. not null");
                        //location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        location = getLastKnownLocation();
                        if (location != null) {
                            Log.d("loc. not null", "loc. not null");
                            longitude = location.getLongitude();
                            latitude = location.getLatitude();
                        }
                    }
                } catch (SecurityException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }

            }
        }
        return location;
    }

    private Location getLastKnownLocation() {
        locationManager = (LocationManager)context.getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = null;
            try {
                l = locationManager.getLastKnownLocation(provider);


            }catch (SecurityException e){}
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    /**
     * Asks the user to enable GPS.
     */
    private void showSettings() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("GPS is not enabled");
        alert.setMessage("Do you want to change the settings?");
        alert.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
    }

    /**
     * Updates the location of a specific user in a certain interval.
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        if(userGroup.equals("driver")) {
            String taxiUrl = "http://cabtogo.eu-gb.mybluemix.net/api/taxi_drivers";
            try {
                JSONObject old = new JSONUtil().execute(taxiUrl, JSONUtil.Action.GetById.name(), "" + userId).get();
                JSONObject j = new JSONObject();
                if (old.getBoolean("is_active")) {
                    j.put("account_id", userId);
                    j.put("phone_number", old.getInt("phone_number"));
                    j.put("location_longitude", location.getLongitude());
                    j.put("location_latitude", location.getLatitude());
                    j.put("is_active", true);
                    new JSONUtil().execute(taxiUrl, JSONUtil.Action.Update.name(), j.toString());
                }
            } catch (JSONException e) {
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    /**
     * If GPS is disabled, prompts the user to enable it.
     *
     * @param provider
     */
    @Override
    public void onProviderDisabled(String provider) {
        showSettings();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
