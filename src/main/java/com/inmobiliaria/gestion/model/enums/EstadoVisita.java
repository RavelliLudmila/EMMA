package com.inmobiliaria.gestion.model.enums;

public enum EstadoVisita {
    PENDIENTE("Pendiente"),
    REALIZADA("Realizada"),
    CANCELADA("Cancelada");

    private final String descripcion;

    EstadoVisita(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
