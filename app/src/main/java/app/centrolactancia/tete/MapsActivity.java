package app.centrolactancia.tete;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tete.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int LOCATION_REQUEST_CODE = 1;

    ArrayList<String> Ubicacion = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        listUbicacion();
    }



    public Connection conexionBD(){
        Connection conexion=null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conexion = DriverManager.getConnection("jdbc:jtds:sqlserver://52.232.165.138;databaseName=Tete;user=Tete;password=tete;");

        }catch(Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return conexion;
    }

    public void listUbicacion() {
        int n;


        Integer numColumnas=5;


        try {

            PreparedStatement pst = conexionBD().prepareStatement("select TOP(5) Nombre, Lat1, Long1, (3959 * acos(cos(radians(37)) * cos(radians(Lat1)) * cos(radians(Long1)-radians(-79.916539)) + sin (radians(-2.187074))*sin(radians(Lat)))) AS distance FROM Tbl_Ubicacion ORDER BY distance asc");
            ResultSet rs =pst.executeQuery();
            //obtentodo los datos y obten un conteo de todas las colummnas


            while (rs.next()) {

                        Ubicacion.add(rs.getString("Nombre").toString());//rs.getObject(i).toString();
                       // Toast.makeText(getApplicationContext(), Ubicacion.get(i), Toast.LENGTH_SHORT).show();


            }
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

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

        // Add a marker in CLINICA MORAN CASSAGNE and move the camera
        LatLng centro1 = new LatLng(-2.1780359,-79.9191023);
        mMap.addMarker(new MarkerOptions().position(centro1).title("CLINICA MORAN CASSAGNE"));
        // Add a marker in Centro de salud Ferroviaria and move the camera
        LatLng centro2 = new LatLng(-2.1824151,-79.9092786);
        mMap.addMarker(new MarkerOptions().position(centro2).title("Centro de salud Ferroviaria"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centro1,12));

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


}
