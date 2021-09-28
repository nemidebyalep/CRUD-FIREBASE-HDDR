package com.example.crud_hddr_firebase.Models;

public class Persona {
    private  String idpersona;
    private  String nombres;
    private String  dni;
    private String correo;
    private String estado_civil;
    private  String telefono;
    private  String fecharegistro;
    private  long timestamp;


    public String getIdpersona() {return idpersona; }

    public void setIdpersona(String idpersona) { this.idpersona = idpersona;  }

    public String getNombres() { return nombres; }

    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getTelefono() { return telefono;  }

    public String getFecharegistro() {
        return fecharegistro;
    }

    public void setFecharegistro(String fecharegistro) {
        this.fecharegistro = fecharegistro;
    }

    public void setTelefono(String telefono) { this.telefono = telefono;  }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDni() { return dni; }

    public void setDni(String dni) { this.dni = dni;    }

    public String getCorreo() {return correo; }

    public void setCorreo(String correo) {  this.correo = correo; }

    public String getEstado_civil() { return estado_civil;  }

    public void setEstado_civil(String estado_civil) {
        this.estado_civil = estado_civil;
    }

    @Override
    public String toString() {
        return nombres;
    }

}
