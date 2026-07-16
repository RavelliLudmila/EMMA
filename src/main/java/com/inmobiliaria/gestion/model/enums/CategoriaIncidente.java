package com.inmobiliaria.gestion.model.enums;

public enum CategoriaIncidente {
    PLOMERIA("Plomería"),
    ELECTRICIDAD("Electricidad"),
    GAS("Gas"),
    GENERAL("General");

    private final String descripcion;

    CategoriaIncidente(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
