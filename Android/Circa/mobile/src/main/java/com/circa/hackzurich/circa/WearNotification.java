package com.circa.hackzurich.circa;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.Gravity;

public class WearNotification extends Intent{
    static int notificationId = 1;

    public static void send(Context context, int tipId, int kindId, boolean is_tip)
    {
        Intent yesIntent = new Intent(context, ResponseActivity.class);
        yesIntent.putExtra(DescConstants.NOTIFICATION_ID, notificationId);
        yesIntent.putExtra(DescConstants.TIP_ID, tipId);
        yesIntent.putExtra(DescConstants.NOTIFICATION_IS_CONFIRM, true);

        Intent noIntent = new Intent(context, ResponseActivity.class);
        noIntent.putExtra(DescConstants.NOTIFICATION_ID, notificationId);
        noIntent.putExtra(DescConstants.TIP_ID, tipId);
        noIntent.putExtra(DescConstants.NOTIFICATION_IS_CONFIRM, false);

        PendingIntent yesPendingIntent = PendingIntent.getActivity(context, notificationId,
                yesIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent noPendingIntent = PendingIntent.getActivity(context, notificationId + 1,
                noIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
                .setContentIcon(DescConstants.IDToPicture(kindId))
                .setContentIconGravity(Gravity.START)
                .setHintHideIcon(false);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(DescConstants.IDToPicture(kindId))
                        .setContentText(DescConstants.IDToEventName(kindId))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .addAction(
                                R.drawable.ic_action_accept,
                                context.getString(R.string.confirm_info_notification),
                                yesPendingIntent)
                        .addAction(
                                R.drawable.ic_action_cancel,
                                context.getString(R.string.debunk_info_notification),
                                noPendingIntent)
                        .extend(wearableExtender);
        if (is_tip)
            notificationBuilder.setContentTitle("Tip");
        else
            notificationBuilder.setContentTitle("Warning");

        NotificationManagerCompat notificationManagerCompat =
                NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(notificationId, notificationBuilder.build());
        notificationId += 2;
    }

    public static void cancel(Context context, int notificationId)
    {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.cancel(notificationId);
    }
}
