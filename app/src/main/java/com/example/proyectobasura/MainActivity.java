package com.example.proyectobasura;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    protected void onClick(View view){
        Intent intento = null;

        switch (view.getId()){
            case R.id.btnRecolector:
                intento = new Intent(MainActivity.this,MapsActivity.class);
                break;

            case R.id.btnCiudadano:
                final LocationManager locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    alertaGpsDesactivado();
                }else {
                    intento = new Intent(MainActivity.this, CiudadanoActivity.class);
                }
                break;

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
}
