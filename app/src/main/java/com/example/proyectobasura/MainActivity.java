package com.example.proyectobasura;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private boolean permisoUbicacion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Verificar permisos de posición
        obtenerPermisosUbicacion();
    }

    protected void onClick(View view){
        Intent intento = null;
        final LocationManager locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        switch (view.getId()){
            case R.id.btnRecolector:
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    alertaGpsDesactivado();
                }else {
                    intento = new Intent(MainActivity.this,MapsActivity.class);
                }
                break;

            case R.id.btnCiudadano:
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    alertaGpsDesactivado();
                }else {
                    intento = new Intent(MainActivity.this, CiudadanoActivity.class);
                }break;

        }
        try{
            startActivity(intento);
        }
        catch (Exception e) {

        }
    }

    /*
    Verifica si el GPS se encuentra activado o no
    si no está activado le pregunta al usuario si desea activarlo
     */
    private void alertaGpsDesactivado() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Tenemos un problema...\nAl parecer el GPS está deactivado\n¿Deseas activarlo ahora?")
                .setCancelable(false)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
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
                permisoUbicacion = false;
                ActivityCompat.requestPermissions(this, permissions, 1234);
            }
        } else {
            permisoUbicacion = false;
            ActivityCompat.requestPermissions(this, permissions, 1234);
        }
    }
}
