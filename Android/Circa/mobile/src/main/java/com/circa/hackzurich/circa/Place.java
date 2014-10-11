package com.circa.hackzurich.circa;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Place {
    private Integer id;
    private String created_at;
    private Double latitude;
    private Double longitude;
    private Integer kind;
    private String text_note;
    private Integer confirmed;
    private Integer debunk;

    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.GERMANY);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public Place(Integer id, String created_at, Double latitude, Double longitude, Integer kind, Integer debunk, Integer confirmed) {
        this.id = id;
        this.created_at = created_at;
        this.latitude = latitude;
        this.longitude = longitude;
        this.kind = kind;
        this.debunk = debunk;
        this.confirmed = confirmed;
    }

    public Integer getId() {
        return id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getKind() {
        return kind;
    }

    public void setKind(Integer kind) {
        this.kind = kind;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getText_note() {
        return text_note;
    }

    public void setText_note(String text_note) {
        this.text_note = text_note;
    }

    public Integer getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Integer confirmed) {
        this.confirmed = confirmed;
    }

    public Integer getDebunk() {
        return debunk;
    }

    public void setDebunk(Integer debunk) {
        this.debunk = debunk;
    }
}