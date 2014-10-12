package com.circa.hackzurich.circa;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.location.LocationServices.*;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,GooglePlayServicesClient.OnConnectionFailedListener {
    // 1 minute
    //private final Integer interval = 1 * 60 * 1000;
    private final Integer interval = 10 * 1000;
    private AlarmManager downAlarm, pushAlarm;
    private PendingIntent downPintent, pushPintent;
    private GoogleMap mMap;
    private LocationClient mLocationClient;
    private Location mCurrentLocation;
    private static String msg;
    private final static int  CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PushService.places = new ArrayList<Place>();
        PushService.usedPlaces = new HashSet<Integer>();
        PushService.radius = 10;
        mLocationClient = new LocationClient(this, this, this);
        mLocationClient.connect();
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(10, 10))
                .title("Hello world"));
        centerMapOnMyLocation();
    }

    private void centerMapOnMyLocation() {

        mMap.setMyLocationEnabled(true);

        mCurrentLocation = mLocationClient.getLastLocation();
        LatLng myLocation = new LatLng(35.0, -100.0);

        if (mCurrentLocation != null) {
            myLocation = new LatLng(mCurrentLocation.getLatitude(),
                    mCurrentLocation.getLongitude());
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 5));
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

    /* Called by Location Services when the request to connect the client finishes successfully. At this point, you can request the current location or start periodic updates */
    @Override
    public void onConnected(Bundle dataBundle) {
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
    }

    public static class ErrorDialogFragment extends DialogFragment {
        private Dialog mDialog;
        public ErrorDialogFragment() {
            super();
            mDialog = null;                                                 // Default constructor. Sets the dialog field to null
        }
        public void setDialog(Dialog dialog) {
            mDialog = dialog;                                        // Set the dialog to display
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;                                                              // Return a Dialog to the DialogFragment.
        }
    }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    /*
     * Google Play services can resolve some errors it detects.If the error has a resolution, try sending an Intent to start a Google Play services activity that can resolve
     * error. */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this,CONNECTION_FAILURE_RESOLUTION_REQUEST);


            /*
             * Thrown if Google Play services canceled the original
             * PendingIntent
             */
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();

            }
        } else {
        /*  * If no resolution is available, display a dialog to the user with the error. */
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    /* Called by Location Services if the connection to the location client drops because of an error.*/
    @Override
    public void onDisconnected() {
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();

    }

    private final class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            // Report to the UI that the location was updated
            mCurrentLocation =location;
            Context context = getApplicationContext();

            msg = Double.toString(location.getLatitude()) + "," +  Double.toString(location.getLongitude());

            Toast.makeText(context, msg,Toast.LENGTH_LONG).show();

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
    /* This is called when the GPS status alters */
            Context context = getApplicationContext();
            switch (status) {
                case LocationProvider.OUT_OF_SERVICE:
                    Log.v(provider, "Status Changed: Out of Service");
                    Toast.makeText(context, "Status Changed: Out of Service",
                            Toast.LENGTH_SHORT).show();
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.v(provider, "Status Changed: Temporarily Unavailable");
                    Toast.makeText(context, "Status Changed: Temporarily Unavailable",
                            Toast.LENGTH_SHORT).show();
                    break;
                case LocationProvider.AVAILABLE:
                    Log.v(provider, "Status Changed: Available");
                    Toast.makeText(context, "Status Changed: Available",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    }

    private boolean showErrorDialog(int errorCode) {
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status

            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode,this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);
            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment =  new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(getFragmentManager(),"Location Updates");

            } return false;
        }
    }
}
