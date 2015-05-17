package it.bz.its.angelhack.threedudes.hikingbuddy.models;

import com.google.android.gms.maps.model.LatLng;

public class RoutePoint {
    String latitude;
    String longitude;

    public LatLng getGoogleMapCoord() {
        double lat = Double.parseDouble(this.latitude);
        double lon = Double.parseDouble(this.longitude);

        return new LatLng(lat, lon);
    }
}
