package com.marcosoft.dexgeo.actividades.conductor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
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
import com.google.firebase.database.DatabaseError;
import com.marcosoft.dexgeo.R;
import com.marcosoft.dexgeo.actividades.MainActivity;
import com.marcosoft.dexgeo.includes.miToolbar;
import com.marcosoft.dexgeo.providers.AuthProvider;
import com.marcosoft.dexgeo.providers.GeofireProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MapaDeConductor extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private AuthProvider mAuthProvider;
    private GeofireProvider mGeofireProvider;

    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocation;

    private final static int LOCATION_REQUEST_CODE = 1;
    private final static int SETTINGS_REQUEST_CODE = 2;

    private Marker mMarker;
    private Boolean mEsPrimeraVez = true;
    private Button btnConeccion;
    private boolean mEstaConectado = false;
    private List<Marker> mMarkerConductores = new ArrayList<>();
    private LatLng mCurrentLatLng;

    private void actualizarUbicacion() {
        if (mAuthProvider.existeciaSesion() && mCurrentLatLng !=null ){
           // Toast.makeText(this,"Deberia andar",Toast.LENGTH_SHORT).show();
            //mGeofireProvider.crearUbi(mAuthProvider.getId(),mCurrentLatLng);
            mGeofireProvider.guardarUbicacion(mAuthProvider.getId(),mCurrentLatLng);
        }
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {
                    mCurrentLatLng = new LatLng(location.getLatitude(),location.getLongitude());
                    //Obtener location en  tienpo real
                    //REMOVEMOS SI YA TIENE UNA DIRECCION PARA QUE NO ME COPIE VARIOS ICNONS
                    if (mMarker !=null){
                        mMarker.remove();
                    }

                    mMarker=mMap.addMarker(new MarkerOptions().position(
                            new LatLng(location.getLatitude(),location.getLongitude())
                            ).title("Tu posicion")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.camion))
                    );
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .zoom(16f)
                                    .build()
                    ));
                    if (mEsPrimeraVez){
                        mEsPrimeraVez = false;
                        getBasuralesActivos();
                    }
                }
            }
        }
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_de_conductor);
        btnConeccion = findViewById(R.id.btnConexion);
        miToolbar.show(this, "Conductor", false);
        mAuthProvider = new AuthProvider();
        mGeofireProvider = new GeofireProvider();


        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapaConductor);
        mMapFragment.getMapAsync(this);

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST_CODE && gpsActivado()) {
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
            desautenticar();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getBasuralesActivos(){
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                   if (gpsActivado()){
                       mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                     //  mMap.setMyLocationEnabled(true);
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
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), SETTINGS_REQUEST_CODE);
                    }
                }).create().show();
    }



    private void startLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (gpsActivado()) {
                    btnConeccion.setText("DESCONECTAR");
                    mEstaConectado = true;
                    mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                   // mMap.setMyLocationEnabled(true);
                } else {
                    mostrarAlertaActivacion();
                }
            } else {
                chequearAceptacionPermisos();
            }
        } else {
            if (gpsActivado()) {
                mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
               // mMap.setMyLocationEnabled(true);
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
                                ActivityCompat.requestPermissions(MapaDeConductor.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                            }
                        }).create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(MapaDeConductor.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }
    private boolean gpsActivado() {
        boolean estaActivo = false;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            estaActivo = true;
        }
        return estaActivo;
    }

    private void desconectar(){
        if(mFusedLocation != null){
            btnConeccion.setText("CONECTAR");
            mEstaConectado=false;
            mFusedLocation.removeLocationUpdates(mLocationCallback);
            if (mAuthProvider.existeciaSesion()){
                mGeofireProvider.eliminarUbicacion(mAuthProvider.getId());
            }

        }else{
            Toast.makeText(this,"no se puede desconectar",Toast.LENGTH_SHORT).show();
        }
    }
    //
    void desautenticar() {
        desconectar();
        mAuthProvider.cerrarSesion();
        Intent intent = new Intent(MapaDeConductor.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    //Cuando se hace click en conectarse///
    public void clickConectarse(View view) {
        if (mEstaConectado) {
            desconectar();
        }else{
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startLocation();
        }
    }
}