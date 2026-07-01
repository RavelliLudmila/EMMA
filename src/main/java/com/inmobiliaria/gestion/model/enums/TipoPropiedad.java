package com.inmobiliaria.gestion.model.enums;

public enum TipoPropiedad {
    CASA("Casa"),
    DEPARTAMENTO("Departamento"),
    LOCAL("Local"),
    OTRO("Otro");

    private final String descripcion;

    TipoPropiedad(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
