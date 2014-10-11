package com.circa.hackzurich.circa;

import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class MessageService extends WearableListenerService {
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(DescConstants.MESSAGE_ID)) {
            int kindId = messageEvent.getData()[0];
            ServerService.newAlert(kindId);
        } else
            super.onMessageReceived(messageEvent);
    }
}
