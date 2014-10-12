package com.circa.hackzurich.circa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Dunix on 2014-10-12.
 */
public class LocalDB extends SQLiteOpenHelper {

    private static final String DB_FILE = "database.db";
    private static final int DB_VERSION = 5;

    public LocalDB(Context context) {
        super(context, DB_FILE,  null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL( "CREATE TABLE places ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "online_id INTEGER NOT NULL,"+
                "created_at VARCHAR(128) NOT NULL,"+
                "latitude INTEGER NOT NULL,"+
                "longitude INTEGER NOT NULL,"+
                "text_note VARCHAR(256),"+
                "kind INTEGER NOT NULL,"+
                "debunk INTEGER NOT NULL," +
                "confirmed INTEGER NOT NULL);");
        db.execSQL( "CREATE TABLE used ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "used_id INTEGER NOT NULL);");

        Log.d("Circa", "Database creating...");
        Log.d("Circa", "TableS ver. " + DB_VERSION + " created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS places;");
        db.execSQL("DROP TABLE IF EXISTS used;");
        this.onCreate(db);
    }

    public void addPlace(Place place) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues vars = new ContentValues();
        vars.put("online_id", place.getId());
        vars.put("created_at", place.getCreated_at());
        vars.put("latitude", place.getLatitude());
        vars.put("longitude", place.getLongitude());
        vars.put("text_note", place.getText_note());
        vars.put("kind", place.getKind());
        vars.put("debunk", place.getDebunk());
        vars.put("confirmed", place.getConfirmed());
        db.insertOrThrow("places", null, vars);
        db.close();
    }

    public void addUsed(Integer used_id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues vars = new ContentValues();
        vars.put("used_id", used_id);
        db.insertOrThrow("used", null, vars);
        db.close();
    }

    public void removeAllPlaces()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("places", null, null);
        db.close();
    }

    public void removeAllUsed()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("used", null, null);
        db.close();
    }

    public Place[] returnAllPlaces() {

        SQLiteDatabase db = getReadableDatabase();
        String col[] = {"online_id", "created_at", "latitude", "longitude", "text_note", "kind", "debunk", "confirmed"};
        Cursor cur = db.query("places", col, null, null, null, null, "confirmed DESC");
        List<Place> list = new ArrayList<Place>();
        while(cur.moveToNext()) {
            Place place = new Place(cur.getInt(0), cur.getString(1), cur.getDouble(2), cur.getDouble(3),
                            cur.getString(4), cur.getInt(5), cur.getInt(6), cur.getInt(7));
            list.add(place);
        }
        cur.close();
        db.close();
        return list.toArray(new Place[list.size()]);
    }

    public HashSet<Integer> returnAllUsed() {
        SQLiteDatabase db = getReadableDatabase();
        String col[] = {"used_id"};
        Cursor cur = db.query("used", col, null, null, null, null, null);
        HashSet<Integer> usedPlaces = new HashSet<Integer>();
        while(cur.moveToNext()) {
            usedPlaces.add(cur.getInt(0));
        }
        cur.close();
        db.close();
        return usedPlaces;
    }

}