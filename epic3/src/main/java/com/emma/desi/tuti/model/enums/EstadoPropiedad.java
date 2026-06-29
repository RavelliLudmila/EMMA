package com.emma.desi.tuti.model.enums;


public enum EstadoPropiedad {
    DISPONIBLE("Disponible"),
    ALQUILADA("Alquilada"),
    NO_DISPONIBLE("No disponible");

    private final String descripcion;

    EstadoPropiedad(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
