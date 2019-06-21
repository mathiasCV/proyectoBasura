package com.example.proyectobasura;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

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
    CheckBox checkPlastico, checkMetal, checkPapel, checkCarton;
    Button bRecolectar;
    ArrayList<Basura> listaPuntoBasura = new ArrayList<Basura>();
    boolean plastico = true, metal = true, papel = true, carton = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Recivir datos de CiudadanoActivity


        //Instancia de botones
        checkPlastico = findViewById(R.id.opcionPlastico);
        checkCarton = findViewById(R.id.opcionCarton);
        checkMetal = findViewById(R.id.opcionMetal);
        checkPapel = findViewById(R.id.opcionPapel);
        bRecolectar = findViewById(R.id.btnFiltrarMapa);
        llenarLista();
        bRecolectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkCarton.isChecked() && !checkMetal.isChecked() && !checkPapel.isChecked() && !checkPlastico.isChecked()){
                    Toast.makeText(getApplicationContext(), "Elige al menos una categoría para filtar.", Toast.LENGTH_SHORT).show();
                }
                validar();
                ponerMarcadores();
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("MANDANDO_BASURA"));
        ponerMarcadores();
        Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_SHORT).show();

    }

    public void ponerMarcadores(){
        mMap.clear();
        for (Basura lista: listaPuntoBasura) {
            if (lista.isFlag()) {
                mMap.addMarker(new MarkerOptions().position(lista.getPunto()).title(lista.getTipo()+" "+Integer.toString(lista.getPeso())+" Kg").icon(BitmapDescriptorFactory.defaultMarker(lista.getColor())));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(lista.getPunto()));
                mMap.setMinZoomPreference(14);
            }
        }
    }
    public void validar(){
        boolean plastico, metal, papel, carton;

        plastico = checkPlastico.isChecked();
        metal = checkMetal.isChecked();
        papel = checkPapel.isChecked();
        carton = checkCarton.isChecked();

        for (Basura lista: listaPuntoBasura){
            if(lista.getTipo().equals("plastico")) lista.setFlag(plastico);
            if(lista.getTipo().equals("metal")) lista.setFlag(metal);
            if(lista.getTipo().equals("papel")) lista.setFlag(papel);
            if(lista.getTipo().equals("carton")) lista.setFlag(carton);
        }
    }


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            LatLng punto = new LatLng(bundle.getDouble("lat") , bundle.getDouble("lng"));
            listaPuntoBasura.add(new Basura( bundle.getString("tipo"), punto, bundle.getInt("peso")));
        }
    };




    /*
        La función llena la lista de obj Basura, que son los puntos que muestra el mapa
       para simular una base de datos
       */
    public void llenarLista(){
       // listaPuntoBasura.add(new Basura("plastico", new LatLng(-33.035580, -71.626953),10));
        listaPuntoBasura.add(new Basura("metal", new LatLng(-33.044241, -71.620523),1));
        listaPuntoBasura.add(new Basura("papel", new LatLng(-33.043656, -71.622325),1));
        listaPuntoBasura.add(new Basura("carton",new LatLng(-33.046291, -71.621156),5));
        listaPuntoBasura.add(new Basura("plastico",new LatLng(-33.045473, -71.622218),10));
        listaPuntoBasura.add(new Basura("metal",new LatLng(-33.044843, -71.623055),3));
        listaPuntoBasura.add(new Basura("papel",new LatLng(-33.042604, -71.621178),4));
        listaPuntoBasura.add(new Basura("carton",new LatLng(-33.042379, -71.622669),2));
        listaPuntoBasura.add(new Basura("plastico",new LatLng(-33.042406, -71.623924),1));
        listaPuntoBasura.add(new Basura("papel",new LatLng(-33.043359, -71.623484),6));
        listaPuntoBasura.add(new Basura("carton",new LatLng(-33.041668, -71.623591),1));
        listaPuntoBasura.add(new Basura("plastico", new LatLng(-33.040742, -71.624943),2));
        listaPuntoBasura.add(new Basura("metal", new LatLng(-33.039870, -71.626252),7));
        listaPuntoBasura.add(new Basura("papel", new LatLng(-33.039717, -71.628419),1));
        listaPuntoBasura.add(new Basura("carton",new LatLng(-33.038728, -71.628247),3));
        listaPuntoBasura.add(new Basura("plastico", new LatLng(-33.038440, -71.630511),1));
        listaPuntoBasura.add(new Basura("metal", new LatLng(-33.037595, -71.629996),9));
        listaPuntoBasura.add(new Basura("papel", new LatLng(-33.036812, -71.628912),2));
        listaPuntoBasura.add(new Basura("carton",new LatLng(-33.040267, -71.625338),1));
        listaPuntoBasura.add(new Basura("plastico",new LatLng(-33.038603, -71.628986),8));
        listaPuntoBasura.add(new Basura("metal",new LatLng(-33.037218, -71.627355),5));
    }
}
