package com.circa.hackzurich.circa;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.lang.Integer;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ServerService extends IntentService {
    public static void confirmInfo(Context context, int tipId)
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
    }

    public static void newAlert(int kindId)
    {
        Log.e("FUCK", "new alert not handled");
    }


    public ServerService() {
        super("ServerService");
    }

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

    /**
     * Handle action Confirm in the provided background thread with the provided
     * parameters.
     */
    private void handleActionConfirm(Integer tipId) {

        Log.d("Circa", "confirm " + tipId);
    }

    /**
     * Handle action Debunk in the provided background thread with the provided
     * parameters.
     */
    private void handleActionDebunk(Integer tipId) {

        Log.d("Circa", "debunk " + tipId);
    }
}