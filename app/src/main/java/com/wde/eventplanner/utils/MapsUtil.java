package com.wde.eventplanner.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsUtil {
    public static String joinCity(String address, String city) {
        return address + ", " + city;
    }

    public static LatLng getLocationFromAddress(String address, String city, Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocationName(joinCity(address, city), 1);
            if (addressList != null && !addressList.isEmpty()) {
                Address location = addressList.get(0);
                return new LatLng(location.getLatitude(), location.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
