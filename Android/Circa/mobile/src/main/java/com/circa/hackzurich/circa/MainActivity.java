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
import java.util.HashSet;


public class MainActivity extends Activity {

    // 1 minute
    //private final Integer interval = 1 * 60 * 1000;
    private final Integer interval = 10 * 1000;
    private AlarmManager downAlarm, pushAlarm;
    private PendingIntent downPintent, pushPintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PushService.places = new ArrayList<Place>();
        PushService.usedPlaces = new HashSet<Integer>();
        PushService.radius = 10;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void startServices() {
        Intent downloadIntent = new Intent(this, DownloadService.class);
        downPintent = PendingIntent.getService(this, 0, downloadIntent, 0);
        downAlarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        downAlarm.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                5 * 1000, 6*interval, downPintent);
                //5 * 1000, AlarmManager.INTERVAL_HOUR, downPintent);

        Intent pushIntent = new Intent(this, PushService.class);
        pushPintent = PendingIntent.getService(this, 0, pushIntent, 0);
        pushAlarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        pushAlarm.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                30 * 1000, interval, pushPintent);
    }

    private void stopServices() {
        if (downAlarm != null) {
            downAlarm.cancel(downPintent);
        }
        if (pushAlarm != null) {
            pushAlarm.cancel(pushPintent);
        }
    }


    public void daemonButtonHandle(View v)
    {
        Button b = (Button) this.findViewById(R.id.daemon);
        if (b.getText().equals(getString(R.string.daemon_start_button))) {
            startServices();
            b.setText(getString(R.string.daemon_stop_button));
        } else {
            stopServices();
            b.setText(getString(R.string.daemon_start_button));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
