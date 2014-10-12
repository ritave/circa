package com.circa.hackzurich.circa;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class DownloadService extends Service {
    public DownloadService() {
    }

    public static UpdatePins pin;

    private GPSTracker gps;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.d("Circa", "download_service_on");
        gps = new GPSTracker(DownloadService.this);


        // check if GPS enabled
        if (gps.canGetLocation()) {
            final double latitude = gps.getLatitude();
            final double longitude = gps.getLongitude();
            //Log.d("Circa", "lat: " + latitude);
            //Log.d("Circa", "long: " + longitude);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpClient client = new DefaultHttpClient();
                    try {
                        String url = DescConstants.SERVER_NAME +
                                        + latitude + "/" + longitude + "/";
                        String gsonResult = "";

                        // Create Request to server and get response
                        HttpGet httpget = new HttpGet(url);
                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
                        gsonResult = client.execute(httpget, responseHandler);
                        Gson gson = new Gson();
                        PushService.places.clear();
                        PushService.places = new ArrayList<Place>(Arrays.asList(gson.fromJson(gsonResult, Place[].class)));
                        //Log.d("Circa", "Result: " + gsonResult);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

            if (pin != null)
                pin.Updated();

        } else {
            Log.e("Circa", "GPS or Network is not enabled");
        }

        return Service.START_NOT_STICKY;
    }
}
