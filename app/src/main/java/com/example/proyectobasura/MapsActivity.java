package com.example.proyectobasura;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private CheckBox checkPlastico, checkMetal, checkPapel, checkCarton;
    private Button btnFiltrar, btnGenerarRuta;
    private ArrayList<Basura> listaPuntoBasura = new ArrayList<Basura>();

    private static final String NOMBRE_ARCHIVO = "infoCiudadano.txt";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Instancia de botones
        checkPlastico = findViewById(R.id.opcionPlastico);
        checkCarton = findViewById(R.id.opcionCarton);
        checkMetal = findViewById(R.id.opcionMetal);
        checkPapel = findViewById(R.id.opcionPapel);
        btnFiltrar = findViewById(R.id.btnFiltrarMapa);
        btnGenerarRuta = findViewById(R.id.btnGenerarRuta);
        llenarLista();

        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkCarton.isChecked() && !checkMetal.isChecked() && !checkPapel.isChecked() && !checkPlastico.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Elige al menos una categoría para filtar.", Toast.LENGTH_SHORT).show();
                }
                ponerMarcadores(validar(checkPlastico.isChecked(), checkMetal.isChecked(), checkPapel.isChecked(), checkCarton.isChecked()));
            }
        });

        btnGenerarRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapsActivity.this, "No disponible... :(", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ponerMarcadores(validar(checkPlastico.isChecked(), checkMetal.isChecked(), checkPapel.isChecked(), checkCarton.isChecked()));
        Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_SHORT).show();
    }

    /*
    Recorre el array con listaPuntoBasura y marca los puntos en el mapa
     */
    public void ponerMarcadores(ArrayList<Basura> basuraFiltrada){
        mMap.clear();
        cargarInformacion();

        for (Basura lista: basuraFiltrada) {
                String title = null;
                if (lista.getPeso()>0 && lista.getPeso() < 10){
                    title = lista.getTipo()+" "+lista.getPeso()+" kg";
                }else{
                    title = lista.getPeso() < 1 ? lista.getTipo()+" 1 kg o menos" : lista.getTipo() + " 10 kg o más";
                }
                mMap.addMarker(new MarkerOptions().position(lista.getPunto()).title(title).icon(BitmapDescriptorFactory.defaultMarker(lista.getColor())));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(lista.getPunto()));
                mMap.setMinZoomPreference(14);
            }
        System.out.println(listaFiltrada(validar(checkPlastico.isChecked(), checkMetal.isChecked(), checkPapel.isChecked(), checkCarton.isChecked())));
    }
    /*
    Verifica cuales items estan marcados en el checkBox
    Si está marcado cambia el flag a true
     */
    public int listaFiltrada(ArrayList<Basura> basuraFiltrada){
        return basuraFiltrada.size();
    }
    public String listaVacia(ArrayList<Basura> basuraFiltrada){
        if(basuraFiltrada.size() == 0) return "Elige al menos una categoría para filtar.";
        return "fail";
    }

    public ArrayList<Basura> validar(boolean plastico, boolean metal, boolean papel, boolean carton){
       /* boolean plastico, metal, papel, carton;

        plastico = checkPlastico.isChecked();
        metal = checkMetal.isChecked();
        papel = checkPapel.isChecked();
        carton = checkCarton.isChecked(); */
        //cargarInformacion();
        llenarLista();
        ArrayList<Basura> basurasFiltradas = new ArrayList<Basura>();

        /*
            filtrado por tipo de basura
            Se marca el flag para saber si esta marcado o no
         */
        for (Basura lista: listaPuntoBasura){
            if(lista.getTipo().equals("plastico")) lista.setFlag(plastico);
            if(lista.getTipo().equals("metal")) lista.setFlag(metal);
            if(lista.getTipo().equals("papel")) lista.setFlag(papel);
            if(lista.getTipo().equals("carton")) lista.setFlag(carton);
        }

        for (Basura lista: listaPuntoBasura){
            if (lista.isFlag()){
                basurasFiltradas.add(lista);
            }
        }
        return basurasFiltradas;
    }

    /*
    Lee el archivo y obtiene los datos para crear un objeto Basura
    y lo agrega a la lista
     */
    private void cargarInformacion(){
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = openFileInput(NOMBRE_ARCHIVO);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String texto;

            while ((texto = bufferedReader.readLine()) != null) {
                stringBuilder.append(texto).append("\n");
            }
            String array[] = stringBuilder.toString().split("\n");
            if (!array[0].equals("\n")){
                listaPuntoBasura.add(new Basura(array[0], new LatLng(Double.parseDouble(array[1]), Double.parseDouble(array[2])), Integer.parseInt(array[3])));
            }
        }catch(Exception e){ }
        finally {
            if (fileInputStream != null){
                try {
                    fileInputStream.close();
                }catch (Exception e){
                }
            }
        }
    }
    

    /*
       Llena la lista de objeto Basura, que son los puntos que muestra el mapa
       para simular una base de datos
    */
    private void llenarLista(){
        listaPuntoBasura.add(new Basura("plastico", new LatLng(-33.035580, -71.626953),10));
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
