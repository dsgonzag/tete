package app.centrolactancia.tete;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tete.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Member;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import app.centrolactancia.tete.Database.clsDatabase;
import app.centrolactancia.tete.Entidades.clsGeolocalizacion;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private static final int LOCATION_REQUEST_CODE = 1;
    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    ArrayList<String> Ubicacion = new ArrayList<>();
    private clsDatabase loDatabase;
    private SQLiteDatabase loExecute;
    List<clsGeolocalizacion> liDistanciasCalculadas = new ArrayList<>();
    private TextView loTxtMostrarDistanciaMinima;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        loDatabase= new clsDatabase(this);
       // int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        loTxtMostrarDistanciaMinima = findViewById(R.id.txtLugarCercano);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
      //  if ( status == ConnectionResult.SUCCESS){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

       // }else{
         //   Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status,(Activity)getApplicationContext(),10);
           // dialog.show();
        //}
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Add a marker in CLINICA MORAN CASSAGNE and move the camera
        LatLng centro1 = new LatLng(-2.278339,-79.895838);
        mMap.addMarker(new MarkerOptions().position(centro1)
                .title("Hospital General Guasmo Sur")
                .snippet("Avenida Cacique Tomalá")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        // Add a marker in Centro de salud Ferroviaria and move the camera
        LatLng centro2 = new LatLng(-2.096141,-79.946781);
        mMap.addMarker(new MarkerOptions().position(centro2)
                .title("Hospital Universitario")
                .snippet("Avenida 43 NO, BODEGAS FERCONSA")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        // Add a marker in CLINICA MORAN CASSAGNE and move the camera
        LatLng centro3 = new LatLng(-2.2032727,-79.8953945);
        mMap.addMarker(new MarkerOptions().position(centro3)
                .title("Hospital del Niño, Dr Fransisco de Icaza Bustamante")
                .snippet("Avenida Quito")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));


        // Add a marker in Centro de salud Ferroviaria and move the camera
        LatLng centro4 = new LatLng(-2.1762186,-79.9407821);
        mMap.addMarker(new MarkerOptions().position(centro4)
                .title("Hospital del IESS Los Ceibos")
                .snippet("Avenida del Bombero")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        // Add a marker in Centro de salud Ferroviaria and move the camera
        LatLng centro5 = new LatLng(-2.1778518,-79.8850402);
        mMap.addMarker(new MarkerOptions().position(centro5)
                .title("Hospital de Niños Dr. Roberto Gilbert E.")
                .snippet("Avenida Roberto Gilbert y, Nicasio Safadi")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        // Add a marker in Centro de salud Ferroviaria and move the camera
        LatLng centro6 = new LatLng(-2.1289721,-79.9645355);
        mMap.addMarker(new MarkerOptions().position(centro6)
                .title("Hospital De Niños Leon Becerra")
                .snippet("Eloy Alfaro y, Bolivia")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centro4,10));

    //    mMap.getUiSettings().setZoomControlsEnabled(true);

        // Controles UI
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Mostrar diálogo explicativo
            } else {
                // Solicitar permiso
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
            }
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Marcadores
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)));


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            // ¿Permisos asignados?
            if (permissions.length > 0 &&
                    permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                Toast.makeText(this, "Error de permisos", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }


    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
        //    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
            if(currentLatitude !=0 && currentLongitude !=0)
            {
                clsGeolocalizacion poDistanciaMinima = lCalcularDistancia();
                loTxtMostrarDistanciaMinima.setText("Distancia minima calculada: "+poDistanciaMinima.getLsNombreLugar() + ", Distancia metros: "+poDistanciaMinima.getLiDistancia());

            }
        }
    }


    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /**
     * If locationChanges change lat and long
     *
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        Toast.makeText(this, currentLatitude + " WORKS1 " + currentLongitude + "", Toast.LENGTH_LONG).show();
        if(currentLatitude !=0 && currentLongitude !=0)
        {
            clsGeolocalizacion poDistanciaMinima = lCalcularDistancia();
            loTxtMostrarDistanciaMinima.setText("Distancia minima calculada: "+poDistanciaMinima.getLsNombreLugar() + ", Distancia metros: "+poDistanciaMinima.getLiDistancia());

        }
    }


    private clsGeolocalizacion lCalcularDistancia()
    {

        clsGeolocalizacion poDistanciaMinima= new clsGeolocalizacion();
        try{
            loExecute = loDatabase.getWritableDatabase();
            String sql="select * from tbListaLocales";
            Cursor cursor = loExecute.rawQuery(sql,null);
            double piLongitud=0;
            double piLatitud=0;
            int piIndice=0;
            if(cursor.moveToFirst()) {
                do {
                    clsGeolocalizacion poData = new clsGeolocalizacion();

                    piLatitud = cursor.getDouble(cursor.getColumnIndex("latitud"));
                    piLongitud = cursor.getDouble(cursor.getColumnIndex("longitud"));

                    Location locationA = new Location("punto A");
                    locationA.setLatitude(piLatitud);
                    locationA.setLongitude(piLongitud);

                    Location locationB = new Location("punto B");

                    locationB.setLatitude(currentLatitude);
                    locationB.setLongitude(currentLongitude);

                    double distance = locationA.distanceTo(locationB);

                    poData.setLsNombreLugar(cursor.getString(cursor.getColumnIndex("nombre_establecimiento")));
                    poData.setLiDistancia(distance);
                    liDistanciasCalculadas.add(poData);

                } while (cursor.moveToNext());
            }

            if(liDistanciasCalculadas.size() >0)
            {

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    poDistanciaMinima= liDistanciasCalculadas.stream().min((first, second) -> Double.compare(first.getLiDistancia(), second.getLiDistancia())).get();
                }
            }

        }catch (Exception ex)
        {
            Toast.makeText(MapsActivity.this,"Error al momento de calcular distancias en el metodo lCalcularDistancia()",Toast.LENGTH_LONG).show();
        }
        return poDistanciaMinima;
    }




}
