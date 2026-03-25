package com.running_platform.util;

import com.google.maps.model.LatLng;

import java.util.List;

public class DistanceUtils {

    private static final double EARTH_RADIUS = 6371; // km

    public static double haversine(LatLng p1, LatLng p2) {
        double dLat = Math.toRadians(p2.lat - p1.lat);
        double dLng = Math.toRadians(p2.lng - p1.lng);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(p1.lat))
                * Math.cos(Math.toRadians(p2.lat))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);

        return 2 * EARTH_RADIUS * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    public static double calculateTotal(List<LatLng> points) {
        double total = 0;
        for (int i = 1; i < points.size(); i++) {
            total += haversine(points.get(i - 1), points.get(i));
        }
        return total;
    }

    public static double calculateDistanceFromPolyline(String polyline) {
        List<LatLng> points = PoLineUtils.decode(polyline);

        double total = DistanceUtils.calculateTotal(points);

        //example: 5.6789 -> 56.789 -> 57 -> 5.7
        return Math.round(total * 10.0) / 10.0;
    }
}