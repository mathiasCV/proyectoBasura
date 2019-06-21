package com.example.proyectobasura;

import com.google.android.gms.maps.model.LatLng;

public class Basura {
    private LatLng punto;
    private String tipo;
    private float color;
    private boolean flag;
    private int peso;

    public Basura (String tipo, LatLng punto, int peso){
        this.punto = punto;
        this.tipo = tipo;
        setColor();
        flag=true;
        this. peso = peso;

    }
    private void setColor(){
        if(this.tipo.equals("plastico")) this.color = 50;
        if(this.tipo.equals("metal")) this.color = 300;
        if(this.tipo.equals("carton")) this.color = 150;
        if(this.tipo.equals("papel")) this.color = 200;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public LatLng getPunto() {
        return punto;
    }
    public String getTipo() {
        return tipo;
    }
    public float getColor() {
        return color;
    }
    public boolean isFlag() {
        return flag;
    }
    public int getPeso() {
        return peso;
    }
}