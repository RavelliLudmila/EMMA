package com.emma.desi.tuti.model.enums;


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

    

    public String toJson() {
        return "{\"codigo\":\"" + this.name() + "\",\"descripcion\":\"" + descripcion + "\"}";
    }
}

