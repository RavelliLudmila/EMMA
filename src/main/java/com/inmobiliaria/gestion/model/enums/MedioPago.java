package com.inmobiliaria.gestion.model.enums;

public enum MedioPago {
    TRANSFERENCIA("Transferencia"),
    EFECTIVO("Efectivo"),
    DEBITO("Débito"),
    CREDITO("Crédito");

    private final String descripcion;

    MedioPago(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
