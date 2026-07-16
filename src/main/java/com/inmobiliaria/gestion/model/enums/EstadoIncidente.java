package com.inmobiliaria.gestion.model.enums;

public enum EstadoIncidente {
    ABIERTO("Abierto"),
    EN_PROCESO("En proceso"),
    RESUELTO("Resuelto"),
    CANCELADO("Cancelado"),
    REABIERTO("Reabierto");

    private final String descripcion;

    EstadoIncidente(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
