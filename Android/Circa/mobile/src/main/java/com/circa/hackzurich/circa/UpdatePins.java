package com.circa.hackzurich.circa;

/**
 * Created by samuelbrebner on 10/12/14.
 */
public interface UpdatePins {
    void Updated();
    void addPoint(double lat, double lon, int kind);
}
