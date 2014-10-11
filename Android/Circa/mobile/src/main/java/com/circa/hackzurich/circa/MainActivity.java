package com.circa.hackzurich.circa;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;


public class MainActivity extends Activity {

    // 1 minute
    //private final Integer interval = 1 * 60 * 1000;
    private final Integer interval = 10 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PushService.places = new ArrayList<Place>();
        PushService.radius = 10;

        Intent downloadIntent = new Intent(this, DownloadService.class);
        PendingIntent downPintent = PendingIntent.getService(this, 0, downloadIntent, 0);
        AlarmManager downAlarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        downAlarm.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                5 * 1000, AlarmManager.INTERVAL_HOUR, downPintent);

        Intent pushIntent = new Intent(this, PushService.class);
        PendingIntent pushPintent = PendingIntent.getService(this, 0, pushIntent, 0);
        AlarmManager pushAlarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        pushAlarm.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                30 * 1000, interval, pushPintent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    public void daemonButtonHandle(View v)
    {
        Button b = (Button)v;
        WearNotification.send(this, 666, "Free Wi-Fi nearby", true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
