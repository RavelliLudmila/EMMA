package com.emma.desi.tuti.dto;



import java.math.BigDecimal;
import java.time.LocalDate;

import com.emma.desi.tuti.model.enums.EstadoContrato;


public class ContratoListadoDTO {

    private Long id;
    private Long propiedadId;
    private String propiedadDireccion;
    private Long propietarioId;
    private String propietarioNombreCompleto;
    private Long inquilinoId;
    private String inquilinoNombreCompleto;
    private LocalDate fechaInicio;
    private Integer duracionMeses;
    private BigDecimal importeMensual;
    private Integer diaVencimientoMensual;
    private String descripcion;
    private EstadoContrato estado;
    private boolean puedeModificar;
    private boolean puedeEliminar;
    private String urlModificacion;
    private String urlEliminacion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPropiedadId() {
        return propiedadId;
    }

    public void setPropiedadId(Long propiedadId) {
        this.propiedadId = propiedadId;
    }

    public String getPropiedadDireccion() {
        return propiedadDireccion;
    }

    public void setPropiedadDireccion(String propiedadDireccion) {
        this.propiedadDireccion = propiedadDireccion;
    }

    public Long getPropietarioId() {
        return propietarioId;
    }

    public void setPropietarioId(Long propietarioId) {
        this.propietarioId = propietarioId;
    }

    public String getPropietarioNombreCompleto() {
        return propietarioNombreCompleto;
    }

    public void setPropietarioNombreCompleto(String propietarioNombreCompleto) {
        this.propietarioNombreCompleto = propietarioNombreCompleto;
    }

    public Long getInquilinoId() {
        return inquilinoId;
    }

    public void setInquilinoId(Long inquilinoId) {
        this.inquilinoId = inquilinoId;
    }

    public String getInquilinoNombreCompleto() {
        return inquilinoNombreCompleto;
    }

    public void setInquilinoNombreCompleto(String inquilinoNombreCompleto) {
        this.inquilinoNombreCompleto = inquilinoNombreCompleto;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Integer getDuracionMeses() {
        return duracionMeses;
    }

    public void setDuracionMeses(Integer duracionMeses) {
        this.duracionMeses = duracionMeses;
    }

    public BigDecimal getImporteMensual() {
        return importeMensual;
    }

    public void setImporteMensual(BigDecimal importeMensual) {
        this.importeMensual = importeMensual;
    }

    public Integer getDiaVencimientoMensual() {
        return diaVencimientoMensual;
    }

    public void setDiaVencimientoMensual(Integer diaVencimientoMensual) {
        this.diaVencimientoMensual = diaVencimientoMensual;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoContrato getEstado() {
        return estado;
    }

    public void setEstado(EstadoContrato estado) {
        this.estado = estado;
    }

    public boolean isPuedeModificar() {
        return puedeModificar;
    }

    public void setPuedeModificar(boolean puedeModificar) {
        this.puedeModificar = puedeModificar;
    }

    public boolean isPuedeEliminar() {
        return puedeEliminar;
    }

    public void setPuedeEliminar(boolean puedeEliminar) {
        this.puedeEliminar = puedeEliminar;
    }

    public String getUrlModificacion() {
        return urlModificacion;
    }

    public void setUrlModificacion(String urlModificacion) {
        this.urlModificacion = urlModificacion;
    }

    public String getUrlEliminacion() {
        return urlEliminacion;
    }

    public void setUrlEliminacion(String urlEliminacion) {
        this.urlEliminacion = urlEliminacion;
    }
}




