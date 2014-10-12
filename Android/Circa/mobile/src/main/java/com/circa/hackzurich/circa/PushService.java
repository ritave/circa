package com.circa.hackzurich.circa;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

public class PushService extends Service {
    public PushService() {
    }

    public static ArrayList<Place> places;
    public static HashSet<Integer> usedPlaces;
    public static Integer radius;
    public static UpdatePins pin;
    private GPSTracker gps;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.d("Circa", "push_service_on");
        gps = new GPSTracker(PushService.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            //Log.d("Circa", "lat: " + latitude);
            //Log.d("Circa", "long: " + longitude);

            // try match best place
            ArrayList<Place> bestPlaces = new ArrayList<Place>();

            for (Place place : places) {
                float[] results = new float[1];
                Location.distanceBetween(latitude, longitude, place.getLatitude(), place.getLongitude(), results);
                float distanceInMeters = results[0];
                if (distanceInMeters < radius && !usedPlaces.contains(place.getId())) {
                    bestPlaces.add(place);
                }
            }

            if (pin != null)
                pin.Updated();

            //Log.d("Circa", "Found " + bestPlaces.size() + " best places");
            // send notification (if exist appropriate place)
            for (Place place : bestPlaces) {
                // mark used places
                usedPlaces.add(place.getId());
                WearNotification.send(this, place.getId(), place.getKind(), true);
            }

        } else {
            Log.e("Circa", "GPS or Network is not enabled");
            // Ask user to enable GPS/network in settings
            //gps.showSettingsAlert();
        }

        return Service.START_NOT_STICKY;
    }
}
