package com.circa.hackzurich.circa;

import com.evernote.client.android.EvernoteSession;

public class DescConstants {
    static final String NOTIFICATION_GROUP= "info_group";
    static final String NOTIFICATION_ID = "notification_id";
    static final String TIP_ID = "tip_id";
    static final String NOTIFICATION_IS_CONFIRM = "notification_status";
    static final String ACTION_CONFIRM = "action_confirm";
    static final String ACTION_DEBUNK = "action_debunk";
    static final String MESSAGE_ID = "/send/new_alert";

    static final String CONSUMER_KEY = "ritave";
    static final String CONSUMER_SECRET = "29f7ac88f572d484";
    static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.PRODUCTION;
    static final String EVERNOTE_PREFERENCES = "pref_evernoteCheckbox";

    static final String SERVER_NAME = "http://students.mimuw.edu.pl:33380/notification/";

    // Kinds
    static final int INFO_WATER      = 0;
    static final int INFO_PICTURE    = 1;
    static final int INFO_DANGERZONE = 2;
    static final int INFO_WIFI       = 3;
    static final int INFO_OTHER      = 4;

    public static String IDToEventName(int eventId)
    {
        switch (eventId)
        {
            case INFO_WATER:
                return "Free water";
            case INFO_PICTURE:
                return "Picture opportunity";
            case INFO_DANGERZONE:
                return "Dangerous zone";
            case INFO_WIFI:
                return "Free Wi-Fi";
            case INFO_OTHER:
                return "Other event";
        }
        return "Unknown happening";
    }

    public static int IDToPicture(int eventId)
    {
        switch (eventId)
        {
            case INFO_WATER:
                return R.drawable.ic_action_network_wifi;
            case INFO_PICTURE:
                return R.drawable.ic_action_camera;
            case INFO_DANGERZONE:
                return R.drawable.ic_action_good;
            case INFO_WIFI:
                return R.drawable.ic_action_network_wifi;
            case INFO_OTHER:
                return R.drawable.ic_action_event;
        }
        throw new RuntimeException("IDToPicture crapped out");
    }
}
