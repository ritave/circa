package com.circa.hackzurich.circa;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.location.LocationServices.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;


public class MainActivity extends Activity implements UpdatePins {
    private final Integer interval = 10 * 1000;
    private AlarmManager downAlarm, pushAlarm;
    private PendingIntent downPintent, pushPintent;
    private GoogleMap mMap;
    private static Context applicationContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stopServices();

        // erasing db containing visited places
        LocalDB db = new LocalDB(getApplicationContext());
        db.removeAllUsed();
        db.close();

        PushService.pin = this;
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        centerMapOnMyLocation();
    }

    private void centerMapOnMyLocation() {

        mMap.setMyLocationEnabled(true);
        applicationContext = getApplicationContext();

        GPSTracker gps = new GPSTracker(applicationContext);
        LatLng myLocation = new LatLng(41.8169925,-71.421168);

        if (gps.canGetLocation()) {
            myLocation = new LatLng(gps.getLatitude(),
                    gps.getLongitude());
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void startServices() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 2);

        Intent downloadIntent = new Intent(this, DownloadService.class);
        downPintent = PendingIntent.getService(this, 0, downloadIntent, 0);
        downAlarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        downAlarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                100*interval, downPintent);

        Intent pushIntent = new Intent(this, PushService.class);
        pushPintent = PendingIntent.getService(this, 1, pushIntent, 0);
        pushAlarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        pushAlarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                interval, pushPintent);
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

    @Override
    public void Updated() {
        mMap.clear();
        LocalDB db = new LocalDB(getApplicationContext());
        Place[] places = db.returnAllPlaces();
        for (Place place : places) {
            addPoint(place.getLatitude(),place.getLongitude(), place.getKind());
        }
    }

    public void addPoint(double lat, double lon, int kind) {
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .title(DescConstants.IDToEventName(kind))
                .icon(BitmapDescriptorFactory.defaultMarker(DescConstants.IDToColor(kind))));
    }
}
