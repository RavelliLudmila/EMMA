package com.emma.desi.tuti.model.entity;

	import jakarta.persistence.Column;
	import jakarta.persistence.Entity;
	import jakarta.persistence.EnumType;
	import jakarta.persistence.Enumerated;
	import jakarta.persistence.FetchType;
	import jakarta.persistence.GeneratedValue;
	import jakarta.persistence.GenerationType;
	import jakarta.persistence.Id;
	import jakarta.persistence.JoinColumn;
	import jakarta.persistence.ManyToOne;
	import jakarta.persistence.PrePersist;
	import jakarta.persistence.PreUpdate;
	import jakarta.persistence.Table;
	import java.math.BigDecimal;
	import java.time.LocalDate;
	import java.time.LocalDateTime;

import com.emma.desi.tuti.model.enums.EstadoContrato;

	@Entity
	@Table(name = "contratos")
public class Contrato {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @ManyToOne(fetch = FetchType.LAZY, optional = false)
	    @JoinColumn(name = "propiedad_id", nullable = false)
	    private Propiedad propiedad;

	    @ManyToOne(fetch = FetchType.LAZY, optional = false)
	    @JoinColumn(name = "inquilino_id", nullable = false)
	    private Persona inquilino;

	    @Column(nullable = false)
	    private LocalDate fechaInicio;

	    @Column(nullable = false)
	    private Integer duracionMeses;

	    @Column(nullable = false, precision = 12, scale = 2)
	    private BigDecimal importeMensual;

	    @Column(nullable = false)
	    private Integer diaVencimientoMensual;

	    @Column(nullable = false, length = 1000)
	    private String descripcion;

	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false, length = 30)
	    private EstadoContrato estado;

	    @Column(nullable = false)
	    private boolean eliminado = false;

	    @Column(nullable = false, updatable = false)
	    private LocalDateTime creadoEn;

	    @Column(nullable = false)
	    private LocalDateTime actualizadoEn;

	    @PrePersist
	    void prePersist() {
	        LocalDateTime ahora = LocalDateTime.now();
	        creadoEn = ahora;
	        actualizadoEn = ahora;
	    }

	    @PreUpdate
	    void preUpdate() {
	        actualizadoEn = LocalDateTime.now();
	    }

	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public Propiedad getPropiedad() {
	        return propiedad;
	    }

	    public void setPropiedad(Propiedad propiedad) {
	        this.propiedad = propiedad;
	    }

	    public Persona getInquilino() {
	        return inquilino;
	    }

	    public void setInquilino(Persona inquilino) {
	        this.inquilino = inquilino;
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

	    public boolean isEliminado() {
	        return eliminado;
	    }

	    public void setEliminado(boolean eliminado) {
	        this.eliminado = eliminado;
	    }

	    public LocalDateTime getCreadoEn() {
	        return creadoEn;
	    }

	    public LocalDateTime getActualizadoEn() {
	        return actualizadoEn;
	    }
        private LocalDateTime fechaEliminacion;

        public LocalDateTime getFechaEliminacion() {
	        return fechaEliminacion;
	    }
	    public void setFechaEliminacion(LocalDateTime fechaEliminacion) {
	    	this.fechaEliminacion = fechaEliminacion;
	    }
	


}
