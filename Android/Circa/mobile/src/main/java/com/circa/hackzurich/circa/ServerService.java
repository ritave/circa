package com.circa.hackzurich.circa;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.lang.Integer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ServerService extends IntentService {
    public static void sendFeedback(final int tipId, final boolean positive) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient client = new DefaultHttpClient();
                try {
                    String url = DescConstants.SERVER_NAME +
                            +tipId + "/rate/" + (positive ? "positive" : "negative") + "/";
                    String gsonResult = "";

                    // Create Request to server and check response
                    HttpPost httppost = new HttpPost(url);
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    gsonResult = client.execute(httppost, responseHandler);
                    //Log.d("Circa", "Result: " + gsonResult);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static void newAlert(final int kindId, final double latitude, final double longitude) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient client = new DefaultHttpClient();
                try {
                    String gsonResult = "";
                    // Create Request to server and check response
                    HttpPost httppost = new HttpPost(DescConstants.SERVER_NAME);
                    List<NameValuePair> params = new ArrayList<NameValuePair>(2);
                    params.add(new BasicNameValuePair("latitude", "" + latitude));
                    params.add(new BasicNameValuePair("longitude", "" + longitude));
                    params.add(new BasicNameValuePair("kind", "" + kindId));
                    httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    gsonResult = client.execute(httppost, responseHandler);
                    Gson gson = new Gson();
                    Place res = gson.fromJson(gsonResult, Place.class);
                    //Log.d("Circa", "Result: " + gsonResult.toString() + ", id = " + res.getId());
                    PushService.usedPlaces.add(res.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


    public ServerService() {
        super("ServerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    }
}