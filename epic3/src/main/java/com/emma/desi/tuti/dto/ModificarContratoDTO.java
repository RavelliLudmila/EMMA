package com.emma.desi.tuti.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.emma.desi.tuti.model.enums.EstadoContrato;

public class ModificarContratoDTO {

    @NotNull(message = "La propiedad es requerida")
    private Long propiedadId;

    @NotNull(message = "El inquilino es requerido")
    private Long inquilinoId;

    @NotNull(message = "La fecha de inicio es requerida")
    private LocalDate fechaInicio;

    @NotNull(message = "La duracion en meses es requerida")
    @Positive(message = "La duracion en meses debe ser un numero positivo")
    private Integer duracionMeses;

    @NotNull(message = "El importe mensual es requerido")
    @DecimalMin(value = "0.01", message = "El importe mensual debe ser un numero positivo")
    private BigDecimal importeMensual;

    @NotNull(message = "El dia de vencimiento mensual es requerido")
    @Min(value = 1, message = "El dia de vencimiento mensual debe estar entre 1 y 31")
    @Max(value = 31, message = "El dia de vencimiento mensual debe estar entre 1 y 31")
    private Integer diaVencimientoMensual;

    @NotBlank(message = "La descripcion es requerida")
    private String descripcion;

    @NotNull(message = "El estado del contrato es requerido")
    private EstadoContrato estado;

    public Long getPropiedadId() {
        return propiedadId;
    }

    public void setPropiedadId(Long propiedadId) {
        this.propiedadId = propiedadId;
    }

    public Long getInquilinoId() {
        return inquilinoId;
    }

    public void setInquilinoId(Long inquilinoId) {
        this.inquilinoId = inquilinoId;
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
}