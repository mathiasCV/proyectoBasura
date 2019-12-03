package com.example.proyectobasura;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class Pruebas {

    /*
        Test: Ciudadano marcó todos los campos antes de envíar una basura.
        En: CiudadanoActivity.verificar()

     */
    @Test
    public void test_verificar_verdadero() {
        CiudadanoActivity ciudadano = new CiudadanoActivity();
        assertEquals(true, ciudadano.verificar(true, true, true));
    }

    @Test
    public void test_verificar_falso() {
        CiudadanoActivity ciudadano = new CiudadanoActivity();
        assertEquals(false, ciudadano.verificar(false, true, true));
    }

    /*
        Test: Verificar que el filtro por tipo de basura sea correcto
        En: MapsActivity
     */
    @Test
    public void test_filtrado_metal(){
        MapsActivity map = new MapsActivity();
        assertEquals(5, map.listaFiltrada(map.validar(false,true,false,false)));
    }

    @Test
    public void test_filtrado_papel(){
        MapsActivity map = new MapsActivity();
        assertEquals(5, map.listaFiltrada(map.validar(false,false,true,false)));
    }

    @Test
    public void test_filtrado_plastico(){
        MapsActivity map = new MapsActivity();
        assertEquals(6,map.listaFiltrada(map.validar(true,false,false,false)));
    }

    @Test
    public void test_filtrado_carton() {
        MapsActivity map = new MapsActivity();
        assertEquals(5,map.listaFiltrada(map.validar(false,false,false,true)));
    }
    @Test
    public void test_filtrado_empty(){
        MapsActivity map = new MapsActivity();
        assertEquals("Elige al menos una categoría para filtar.", map.listaVacia(map.validar(false,false,false,false)) );
    }

    /*
        Test: Verificar que el peso ingresado sea el correcto
        En: CiudadanoActivity
     */
    @Test
    public void test_peso(){
        CiudadanoActivity ciudadano = new CiudadanoActivity();
        assertEquals(10, ciudadano.getPesoSeekbar(10));
    }

    /*
        Test: Verificar que el color sea correcto
        En: Basura
     */
    @Test
    public void test_color_plastico(){
        Basura b = new Basura("plastico", new LatLng(-33.035580, -71.626953),10);
        assertEquals(50, (int)(b.getColor()));
    }

    @Test
    public void test_color_carton(){
        Basura b = new Basura("carton", new LatLng(-33.035580, -71.626953),10);
        assertEquals(150, (int)(b.getColor()));
    }
    @Test
    public void test_color_papel(){
        Basura b = new Basura("papel", new LatLng(-33.035580, -71.626953),10);
        assertEquals(200, (int)(b.getColor()));
    }
    @Test
    public void test_color_metal(){
        Basura b = new Basura("metal", new LatLng(-33.035580, -71.626953),10);
        assertEquals(300, (int)(b.getColor()));
    }
}