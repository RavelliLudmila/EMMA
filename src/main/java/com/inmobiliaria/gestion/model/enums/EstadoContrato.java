package com.inmobiliaria.gestion.model.enums;

public enum EstadoContrato {
    BORRADOR("Borrador"),
    ACTIVO("Activo"),
    FINALIZADO("Finalizado"),
    RESCINDIDO("Rescindido");

    private final String descripcion;

    EstadoContrato(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
