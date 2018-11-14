package ca.kelownakangaroos.psycle;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final float DEFAULT_ZOOM = 14;
    private GoogleMap mMap;
    private ArrayList<ArrayList<Location>> listOfLocations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setTitle((String) getIntent().getExtras().get("Title"));
        listOfLocations = (ArrayList<ArrayList<Location>>) getIntent().getSerializableExtra("locations");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near New Westminister, Canada.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;

        // Add a marker in New West and move the camera
        LatLng newWest = new LatLng(49.203, -122.919);
        mMap.addMarker(new MarkerOptions().position(newWest).title("Marker in New West"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newWest, DEFAULT_ZOOM));


        for(ArrayList<Location> list : listOfLocations){
            for(Location loc : list){
                mMap.addMarker(new MarkerOptions().position(loc.getLatLng()).title(loc.getName()));
            }
        }


    }
}
