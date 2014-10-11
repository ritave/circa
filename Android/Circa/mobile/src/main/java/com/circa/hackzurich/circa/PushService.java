package com.circa.hackzurich.circa;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class PushService extends Service {
    public PushService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Circa", "service");

        return Service.START_NOT_STICKY;
    }
}
