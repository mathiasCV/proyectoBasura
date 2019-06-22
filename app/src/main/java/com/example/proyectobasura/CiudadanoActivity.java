package com.example.proyectobasura;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import dalvik.system.PathClassLoader;

public class CiudadanoActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mMap;

    private RadioButton rPlastico, rMetal, rPapel, rCarton;
    private SeekBar seekbar;
    private TextView textoSeekbar, comprobar;
    private LatLng posicionActual;
    private Button btnMarcarPosicion, btnEnviar;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private static final float ZOOM = 17f;
    private static final String NOMBRE_ARCHIVO = "infoCiudadano.txt";

    private boolean permisoUbicacion = false;
    private boolean marcaHecha = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ciudadano);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Intancia de botones
        rPlastico = findViewById(R.id.idRadioPlastico);
        rMetal = findViewById(R.id.idRadioMetal);
        rPapel = findViewById(R.id.idRadioPapel);
        rCarton = findViewById(R.id.idRadioCarton);
        btnMarcarPosicion = findViewById(R.id.btnMarcarPosicion);
        btnEnviar = findViewById(R.id.btnCiudadanoEnviar);

        //Verificar permisos de posición
        obtenerPermisosUbicacion();

        //listener boton marcan posición
        btnMarcarPosicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (permisoUbicacion) {
                        obtenerPosicionDispositivo();
                        marcarPosicion();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "No fue posible marcar la posición", Toast.LENGTH_SHORT).show();
                }
            }

        });

        // implementación de la seekbar para setear el peso
        seekbar = findViewById(R.id.seekbar);
        textoSeekbar = findViewById(R.id.textoSeekbar);
        seekbar.setMax(10);
        seekbar.setProgress(5);
        //listener de la seekbar para ver cambios
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int cambioValor = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cambioValor = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String texto = cambioValor + " kg";
                if (cambioValor == 0 || cambioValor == 10)
                    texto = cambioValor == 0 ? "Menos que 1 kg" : "10 kg o más";
                textoSeekbar.setText(texto);
            }

        });
        /*
        Listener botón enviar.
        Crea un archivo con los datos necesarios para crear un objeto basura en MapActivity
        Si el usuario marcó todos los campos lo crea, sino muestra mensaje de error.
         */
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verificar()){
                    Basura nBasura = nuevaBasura();
                    guardarInformacion(nBasura);
                    Toast.makeText(CiudadanoActivity.this, "¡Enviado!", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(CiudadanoActivity.this, "Primero debes marcar todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*
    Obtener permisos de ubicacion
    Verifica si los permisos para acceder al GPS están concedidos
    sino los pide.
    */
    private void obtenerPermisosUbicacion() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                permisoUbicacion = true;
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1234);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, 1234);
        }
    }

    /*
    Obtiene la ubicación actual del dispositivo
     */
    private void obtenerPosicionDispositivo() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (permisoUbicacion) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location ubicacionActual = (Location) task.getResult();
                            posicionActual = new LatLng(ubicacionActual.getLatitude(), ubicacionActual.getLongitude());
                            moverCamara(posicionActual, ZOOM);

                        } else {
                            Toast.makeText(getApplicationContext(), "Imposible obtener Ubicación. Verifique si GPS está activado o revise los permisos.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Si se dieron los permisos de ubicacion, marca la posición
        if (permisoUbicacion) {
            obtenerPosicionDispositivo();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
        }
    }

    /*
    Pone una marca en el mapa con la posición actual
    si no puede muestra mensaje de error.
     */
    public void marcarPosicion() {
        mMap.clear();
        try {
            mMap.addMarker(new MarkerOptions().position(posicionActual).title("Tu posición"));
            moverCamara(posicionActual, ZOOM);
            marcaHecha = true;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Imposible obtener ubicación. Verifique opciones de ubicación.", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    Mueve la cámara a la posición actual del dispositivo y deja el mapa con un zoom espeficco

    Parámetros
     latLag: Las coordenadas de la posición actual del dispositivo
     zoom: El zoom que llevará el mapa
     */
    private void moverCamara(LatLng latLng, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    /*
    Instancia un objeto basura con los datos dados
    Retorna: objeto Basura.
     */
    private Basura nuevaBasura() {
        String tipo = new String();
        int peso = seekbar.getProgress();

        if (rPlastico.isChecked()) { tipo = "plastico"; }
        if (rMetal.isChecked())    { tipo = "metal"; }
        if (rPapel.isChecked())    { tipo = "papel"; }
        if (rCarton.isChecked())   { tipo = "carton"; }
        return new Basura(tipo, posicionActual, peso);
    }
    /*
    Verifica que el usuario haya seleccionado todos los datos antes de instanciar un objeto Basura.
     */
    private boolean verificar(){
        boolean verif = false;
        if(rPlastico.isChecked() || rCarton.isChecked() || rPapel.isChecked() || rMetal.isChecked()){
            if (!textoSeekbar.getText().equals("Desliza para indicar el peso")){
                if (marcaHecha){ verif = true; }
            }
        }
        return verif;
    }

    /*
    Guarda la información del usuario en un archivo.
    Crea archivo para guardar la información que el usuario marcó.
    Si ya existe reemplaza el contenido viejo con el nuevo
     */
    private void guardarInformacion(Basura b){
        String texto;
        FileOutputStream fileOutputStream = null;

        texto = b.getTipo() + "\n" + b.getPunto().latitude + "\n" + b.getPunto().longitude + "\n" + b.getPeso();
        try{
            fileOutputStream = openFileOutput(NOMBRE_ARCHIVO,MODE_PRIVATE);
            fileOutputStream.write(texto.getBytes());
            Toast.makeText(getApplicationContext(), "Guardado en "+ getFilesDir() + "/" + NOMBRE_ARCHIVO, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "No se puede abrir el archivo", Toast.LENGTH_SHORT).show();
        }finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                }catch (IOException e){

                }
            }
        }
    }
}
