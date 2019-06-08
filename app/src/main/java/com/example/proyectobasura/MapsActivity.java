package com.example.proyectobasura;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

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

        ArrayList<Basura> listaPuntoBasura = new ArrayList<Basura>();
        listaPuntoBasura.add(new Basura(new LatLng(-33.035580, -71.626953),"plastico"));
        listaPuntoBasura.add(new Basura(new LatLng(-33.046187,-71.619874),"metal"));
       listaPuntoBasura.add(new Basura(new LatLng(-33.038699, -71.629042),"plastico"));

        for(Basura lista: listaPuntoBasura){
            float hue = (lista.getTipo().equals("plastico") == true) ? 150 : 50;
            mMap.addMarker(new MarkerOptions().position(lista.getPunto()).title(lista.getTipo()).icon(BitmapDescriptorFactory.defaultMarker(hue)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(lista.getPunto()));
            mMap.setMinZoomPreference(14);

        }
/*
        // Add a marker in Sydney and move the camera
        LatLng valpo = new LatLng();
        mMap.addMarker(new MarkerOptions().position(valpo).title("Marcador en Valparaiso"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(valpo));
        mMap.setMinZoomPreference(14);*/
    }
}
