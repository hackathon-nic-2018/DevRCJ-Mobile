package com.hackcathon.nica.granestadia;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Marcadores
        LatLng arrendador1 = new LatLng(12.108565, -86.314491);
        LatLng arrendador2 = new LatLng(12.125058, -86.281388);
        mMap.addMarker(new MarkerOptions().position(arrendador1).title("Arrendador 1"));
        mMap.addMarker(new MarkerOptions().position(arrendador2).title("Arrendador 2"));

        //Posicion inicial en mapa
        LatLng startPositionLatLng = arrendador1;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startPositionLatLng));

        float zoomLevel = 10.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arrendador1, zoomLevel));
    }
}
