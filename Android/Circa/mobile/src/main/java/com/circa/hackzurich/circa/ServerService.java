package com.circa.hackzurich.circa;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.lang.Integer;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ServerService extends IntentService {
    /*public static void confirmInfo(Context context, int tipId)
    {
        Intent intent = new Intent(context, ServerService.class);
        intent.setAction(DescConstants.ACTION_CONFIRM);
        intent.putExtra(DescConstants.TIP_ID, tipId);
        context.startService(intent);
    }

    public static void debunkInfo(Context context, int tipId)
    {
        Intent intent = new Intent(context, ServerService.class);
        intent.setAction(DescConstants.ACTION_DEBUNK);
        intent.putExtra(DescConstants.TIP_ID, tipId);
        context.startService(intent);
    }*/

    public static void sendFeedback(final int tipId, final boolean positive) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient client = new DefaultHttpClient();
                try {
                    String url = "http://students.mimuw.edu.pl:33380/notification/" +
                            + tipId + "/rate/" + (positive ? "positive" : "negative") + "/" ;
                    String gsonResult = "";

                    // Create Request to server and check response
                    HttpPost httppost = new HttpPost(url);
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    gsonResult = client.execute(httppost, responseHandler);
                    Log.d("Circa", "Result: " + gsonResult);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static void debunkInfo(int tipId) {
        Log.e("FUCK", "new alert not handled");
    }

    public static void newAlert(int kindId) {
        Log.e("FUCK", "new alert not handled");
    }


    public ServerService() {
        super("ServerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    }
/*
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (DescConstants.ACTION_CONFIRM.equals(action)) {
                final Integer tipId = intent.getIntExtra(DescConstants.TIP_ID, 0);
                handleActionConfirm(tipId);
            } else if (DescConstants.ACTION_DEBUNK.equals(action)) {
                final Integer tipId = intent.getIntExtra(DescConstants.TIP_ID, 0);
                handleActionDebunk(tipId);
            }
        }
    }

    private void handleActionConfirm(Integer tipId) {

        Log.d("Circa", "confirm " + tipId);
    }

    private void handleActionDebunk(Integer tipId) {

        Log.d("Circa", "debunk " + tipId);
    }*/
}