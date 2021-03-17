package com.marcosoft.dexgeo.actividades.cliente;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.firebase.database.DatabaseError;
import com.marcosoft.dexgeo.R;
import com.marcosoft.dexgeo.actividades.MainActivity;
import com.marcosoft.dexgeo.includes.miToolbar;
import com.marcosoft.dexgeo.providers.AuthProvider;
import com.marcosoft.dexgeo.providers.GeofireProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OpcionesDeVecino extends AppCompatActivity{
    private AuthProvider mAuthProvider;
    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;

    private final static int LOCATION_REQUEST_CODE = 1;
    private final static int SETTING_REQUEST_CODE = 2;

    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocation;
    private Marker mMarker;
    private GeofireProvider mGeofireProvider;
    private LatLng mCurrentLatLng;
    private List<Marker> mMarkerConductores = new ArrayList<>();
    private Boolean mEsPrimeraVez = true;
    private PlacesClient mPlaces;
    private AutocompleteSupportFragment mAutocomplete;
    private String mOrigin;
    private LatLng mOriginLatLng;

 /*   LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for(Location location: locationResult.getLocations()) {
                if (getApplicationContext() != null) {
                    // OBTENER LA LOCALIZACION DEL USUARIO EN TIEMPO REAL
                    //Obtener location en  tienpo real
                    //REMOVEMOS SI YA TIENE UNA DIRECCION PARA QUE NO ME COPIE VARIOS ICNONS
                    if (mMarker !=null){
                        mMarker.remove();
                    }
                    mCurrentLatLng = new LatLng(location.getLatitude(),location.getLongitude());

                    mMarker=mMap.addMarker(new MarkerOptions().position(
                            new LatLng(location.getLatitude(),location.getLongitude())
                            ).title("Tu posicion")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.residuos))
                    );
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .zoom(15f)
                                    .build()
                    ));
                    if (mEsPrimeraVez){
                        mEsPrimeraVez = false;
                        getConductoresActivos();
                    }
                }
            }
        }
    };
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_vecino);
        miToolbar.show(this, "VECINO", false);
   /*     mAuthProvider = new AuthProvider();
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        mGeofireProvider = new GeofireProvider();
        if (!Places.isInitialized()){
            Places.initialize(getApplicationContext(),getResources().getString(R.string.google_api_key));
        }*/
 /*       mPlaces = Places.createClient(this);
        mAutocomplete = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.placeAutocompleteOrigin);
        //assert mAutocomplete != null;
        mAutocomplete.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.LAT_LNG,Place.Field.NAME));
        mAutocomplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                mOrigin = place.getName();
                mOriginLatLng = place.getLatLng();
                Log.d("PLACE","NOMBRE "+mOrigin);
                Log.d("PLACE","LATITUD "+mOriginLatLng.latitude);
                Log.d("PLACE","LONGITUD "+mOriginLatLng.longitude);

            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.accion_desautenticar) {
            mAuthProvider.cerrarSesion();
            Intent intent = new Intent(com.marcosoft.dexgeo.actividades.cliente.OpcionesDeVecino.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    }
/*

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTING_REQUEST_CODE && gpsActivado()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        } else {
            mostrarAlertaActivacion();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_conductor, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.accion_desautenticar) {
            mAuthProvider.cerrarSesion();
            Intent intent = new Intent(OpcionesDeVecino.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getConductoresActivos(){
        mGeofireProvider.getConductoresActivos(mCurrentLatLng).addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                //añadiresmo los marcadores de los conductores que se conecten
                for (Marker marker: mMarkerConductores){
                    if (marker.getTag() != null) {
                        if (marker.getTag().equals(key)){
                            return;
                        }
                    }
                }
                LatLng conductorLatLng = new LatLng(location.latitude, location.longitude);
                Marker marker = mMap.addMarker(new MarkerOptions().position(conductorLatLng).title("conductor disponible").icon(BitmapDescriptorFactory.fromResource(R.drawable.residuos)));
                marker.setTag(key);
                mMarkerConductores.add(marker);
            }

            @Override
            public void onKeyExited(String key) {
                for (Marker marker: mMarkerConductores){
                    if (marker.getTag() != null) {
                        if (marker.getTag().equals(key)){
                            marker.remove();
                            mMarkerConductores.remove(marker);
                            return;
                        }
                    }
                }
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                for (Marker marker: mMarkerConductores){
                    if (marker.getTag() != null) {
                        if (marker.getTag().equals(key)){
                            marker.setPosition(new LatLng(location.latitude,location.longitude));
                        }
                    }
                }
            }

            @Override
            public void onGeoQueryReady() {

            }
            @Override
            public void onGeoQueryError(DatabaseError error) {
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(5);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
       // mMap.setMyLocationEnabled(true);
        startLocation();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (gpsActivado()){
                        mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    }else{
                        mostrarAlertaActivacion();
                    }
                } else {
                    chequearAceptacionPermisos();
                }
            } else {
                chequearAceptacionPermisos();
            }
        }
    }

    private void mostrarAlertaActivacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("ACTIVA LA UBICACION PARA CONTINUAR")
                .setPositiveButton("Configuraciones", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), SETTING_REQUEST_CODE);
                    }
                }).create().show();
    }

    private boolean gpsActivado() {
        boolean estaActivo = false;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            estaActivo = true;
        }
        return estaActivo;
    }


    private void startLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (gpsActivado()) {
                    mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                } else {
                    mostrarAlertaActivacion();
                }
            } else {
                chequearAceptacionPermisos();
            }
        } else {
            if (gpsActivado()) {
                mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            } else {
                mostrarAlertaActivacion();
            }
        }
    }

    private void chequearAceptacionPermisos() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("PROPORCIONE PERMISOS PARA CONTINUAR")
                        .setMessage("ESTA APLICACIONNECESITA PERMISOS DE UBICACION PARA CONTINUAR")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(OpcionesDeVecino.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                            }
                        }).create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(OpcionesDeVecino.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }
}*/
