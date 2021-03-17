package com.marcosoft.dexgeo.providers;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class GeofireProvider {
    DatabaseReference mDatabase;
    private GeoFire mGeofire;


    public GeofireProvider() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Basurales_activos");
        mGeofire = new GeoFire(mDatabase);
    }
    public void guardarUbicacion (String id,LatLng mlatLng){
        mGeofire.setLocation(id,new GeoLocation(mlatLng.latitude,mlatLng.longitude));
    }

    public void eliminarUbicacion(String id) {
        mGeofire.removeLocation(id);
    }

    public GeoQuery getConductoresActivos (LatLng latLng){
        GeoQuery geoQuery = mGeofire.queryAtLocation(new GeoLocation(latLng.latitude,latLng.longitude), 5 );
        geoQuery.removeAllListeners();
        return geoQuery;
    }
    public GeoQuery getBasurerosActivos (LatLng latLng){
        GeoQuery geoQuery = mGeofire.queryAtLocation(new GeoLocation(latLng.latitude,latLng.longitude), 5 );
        geoQuery.removeAllListeners();
        return geoQuery;
    }
}

