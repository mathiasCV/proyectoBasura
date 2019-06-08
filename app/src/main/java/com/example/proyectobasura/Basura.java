package com.example.proyectobasura;

import com.google.android.gms.maps.model.LatLng;

public class Basura {
    private LatLng punto;
    private String tipo;

    public Basura (LatLng punto, String tipo){
        this.punto = punto;
        this.tipo = tipo;
    }

    public LatLng getPunto() {
        return punto;
    }

    public String getTipo() {
        return tipo;
    }

}