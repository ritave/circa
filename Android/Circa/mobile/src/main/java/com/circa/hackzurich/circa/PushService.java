package com.circa.hackzurich.circa;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class PushService extends Service {
    public PushService() {
    }

    public static ArrayList<Place> places;
    public static Integer radius;
    private GPSTracker gps;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Circa", "push_service_on");
        gps = new GPSTracker(PushService.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            Log.d("Circa", "lat: " + latitude);
            Log.d("Circa", "long: " + longitude);

            // try match best place
            Place bestPlace = null;
            int rank = -3;

            for (Place place : places) {
                float[] results = new float[1];
                Location.distanceBetween(latitude, longitude, place.getLatitude(), place.getLongitude(), results);
                float distanceInMeters = results[0];
                int newRank = place.getConfirmed() - place.getDebunk();
                if (distanceInMeters < radius && newRank > rank) {
                    bestPlace = place;
                }
            }
            // send notification (if exist appropriate place)
            if (bestPlace != null) {
                // TO DO - a few different kinds of notification
                WearNotification.send(this, bestPlace.getId(), "Free Wi-Fi nearby", true);
            }

        } else {
            Log.d("Circa", "GPS or Network is not enabled");
            // Ask user to enable GPS/network in settings
            //gps.showSettingsAlert();
        }

        return Service.START_NOT_STICKY;
    }
}
