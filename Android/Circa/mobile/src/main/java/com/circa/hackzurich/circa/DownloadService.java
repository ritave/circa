package com.circa.hackzurich.circa;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DownloadService extends Service {
    public DownloadService() {
    }

    private GPSTracker gps;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Circa", "download_service_on");
        gps = new GPSTracker(DownloadService.this);

        // TO DO - update PushService.places
        // example
        /*Place place = new Place(1, Place.getDateTime(), 1.0, 1.0, 0, 0, 1);
        PushService.places.clear();
        PushService.places.add(place);*/
        // check if GPS enabled
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            Log.d("Circa", "lat: " + latitude);
            Log.d("Circa", "long: " + longitude);

            new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... params) {
                    URL url = null;
                    HttpURLConnection connection = null;

                    try

                    {
                        url = new URL("http://students.mimuw.edu.pl:33380/notification/");
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setRequestProperty("Content-Type", "application/json");
                        connection.setDoInput(true);
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        Log.d("Circa", "connected5");
                        String notes = br.readLine();
                        Log.d("Circa", "connected6");
                        String buf = "";
                        while ((buf = br.readLine()) != null) {
                            Log.d("Circa", "connected7");
                            notes += buf;
                        }
                        Log.d("Circa", "Result: " + notes);
                    } catch (
                            Exception e
                            )

                    {
                        e.printStackTrace();
                        try {
                            Log.e("FUCK", "Error code: " + connection.getResponseCode());
                        } catch (Exception e2) {}
                    }

                    return null;
                }
            }.execute();



        } else {
            Log.d("Circa", "GPS or Network is not enabled");
        }
        Log.d("Circa", "Num of places in cache: " + PushService.places.size());

        return Service.START_NOT_STICKY;
    }
}
