package com.running_platform.util;

import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.LatLng;

import java.util.List;

public class PoLineUtils {

    public static List<LatLng> decode(String encodedPolyline) {
        return PolylineEncoding.decode(encodedPolyline);
    }

}
