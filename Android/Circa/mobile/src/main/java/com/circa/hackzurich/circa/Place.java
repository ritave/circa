package com.circa.hackzurich.circa;

public class Place {
    private Integer id;
    private String created_at;
    private Double latitude;
    private Double longitude;
    private Integer kind;
    private String text_note;
    private Integer confirmed;
    private Integer debunk;

    public Place() {
        this.created_at = "";
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.kind = 0;
        this.text_note = "";
        this.confirmed = 0;
        this.debunk = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getKind() {
        return kind;
    }

    public void setKind(Integer kind) {
        this.kind = kind;
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