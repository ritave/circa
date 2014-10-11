package com.circa.hackzurich.circa;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

public class DownloadService extends Service {
    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Circa", "download_service_on");

        // TO DO - update PushService.places
        // example
        Place place = new Place(1, Place.getDateTime(), 1.0, 1.0, 0, 0, 1);
        PushService.places.clear();
        PushService.places.add(place);
        Log.d("Circa", "Num of places in cache: " + PushService.places.size());

        return Service.START_NOT_STICKY;
    }
}
