package com.circa.hackzurich.circa;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.OnClientCallback;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteAttributes;

/**
 * Created by Ritave on 11-Oct-14.
 */
public class CircaApplication extends Application {
    public static EvernoteSession evernoteSession;
    private static Context applicationContext;
    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        evernoteSession = EvernoteSession.getInstance(
                this,
                DescConstants.CONSUMER_KEY,
                DescConstants.CONSUMER_SECRET,
                DescConstants.EVERNOTE_SERVICE,
                false);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean sendToEvernote = sharedPreferences.getBoolean(DescConstants.EVERNOTE_PREFERENCES, false);
        if (sendToEvernote && !evernoteSession.isLoggedIn())
            evernoteSession.authenticate(this);
    }

    public static void sentEvernoteNote(String title, String text) {
        GPSTracker gps = new GPSTracker(applicationContext);
        if (evernoteSession.isLoggedIn() && gps.canGetLocation()) {
            Note note = new Note();
            note.setTitle(title);
            note.setContent(EvernoteUtil.NOTE_PREFIX + text + EvernoteUtil.NOTE_SUFFIX);
            NoteAttributes attributes = new NoteAttributes();
            attributes.setLatitude(gps.getLatitude());
            attributes.setLongitude(gps.getLongitude());
            note.setAttributes(attributes);
            try {
                evernoteSession.getClientFactory().createNoteStoreClient().createNote(note, new OnClientCallback<Note>() {
                    @Override
                    public void onSuccess(final Note data) {
                        Toast.makeText(applicationContext, data.getTitle() + " has been created", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onException(Exception exception) {
                        Log.e("FUCK", "Error creating note", exception);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
