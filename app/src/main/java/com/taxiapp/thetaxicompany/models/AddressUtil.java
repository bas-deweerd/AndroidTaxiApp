package com.taxiapp.thetaxicompany.models;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * A utility class for getting a string representation of an address and convertin an address into a location.
 * Created by merve on 08.05.2016.
 */
public class AddressUtil {

  public static String getCity(Context context, Location location) {
    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
    try {
      List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
      Address adr = addresses.get(0);
      return adr.getLocality();
    } catch (IOException e) {
    }
    return null;
  }

  public static String getZip(Context context, Location location) {
    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
    try {
      List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
      Address adr = addresses.get(0);
      return adr.getPostalCode();
    } catch (IOException e) {
    }
    return null;
  }

  public static String getStreet(Context context, Location location) {
    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
    try {
      List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
      Address adr = addresses.get(0);
      return adr.getThoroughfare();
    } catch (IOException e) {
    }
    return null;
  }

  public static String getCountry(Context context, Location location) {
    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
    try {
      List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
      Address adr = addresses.get(0);
      return adr.getCountryName();
    } catch (IOException e) {
    }
    return null;
  }

  /**
   * Converts a string destination into a Location
   *
   * @param destination
   * @return
   */
  public static Location fromStringToLocation(Context context, String destination, Location current) {
    try {
      Geocoder geocoder = new Geocoder(context, Locale.getDefault());
      List<Address> addresses = geocoder.getFromLocationName(destination, 10);
      Address adr = null;
      Location loc  = new Location(LocationManager.GPS_PROVIDER);
      loc.setLatitude(addresses.get(0).getLatitude());
      loc.setLongitude(addresses.get(0).getLongitude());

      Location lo = loc;
      float min = loc.distanceTo(current);

      for(Address a : addresses) {
        Location l = new Location(LocationManager.GPS_PROVIDER);
        l.setLatitude(a.getLatitude());
        l.setLongitude(a.getLongitude());
        float dist = l.distanceTo(current);
        if (dist < min) {
          min = dist;
          lo = l;
        }
      }
        return lo;
    } catch (IOException e) {
    }
    return null;
  }

}
