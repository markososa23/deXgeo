package com.marcosoft.dexgeo.modelos;

public class Conductor {
    String id;
    String nombre;
    String correo;
    String dni;

    public Conductor(String id, String nombre, String correo, String dni) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.dni = dni;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }


    public String getDni() {
        return dni;
    }

    public void setDni(String placaVehiculo) {
        this.dni = dni;
    }
}
