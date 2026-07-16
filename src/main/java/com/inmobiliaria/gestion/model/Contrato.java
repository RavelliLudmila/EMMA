package com.inmobiliaria.gestion.model;

import com.inmobiliaria.gestion.model.enums.EstadoContrato;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contrato")
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

    @NotNull(message = "La fecha de inicio es requerida")
    @Column(nullable = false)
    private LocalDate fechaInicio;

    @NotNull(message = "La duracion en meses es requerida")
    @Positive(message = "La duracion en meses debe ser un numero positivo")
    @Column(nullable = false)
    private Integer duracionMeses;

    @NotNull(message = "El importe mensual es requerido")
    @DecimalMin(value = "0.01", message = "El importe mensual debe ser un numero positivo")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal importeMensual;

    @NotNull(message = "El dia de vencimiento es requerido")
    @Min(value = 1, message = "El dia de vencimiento debe estar entre 1 y 31")
    @Max(value = 31, message = "El dia de vencimiento debe estar entre 1 y 31")
    @Column(nullable = false)
    private Integer diaVencimientoMensual;

    @NotBlank(message = "La descripcion es requerida")
    @Column(nullable = false, length = 1000)
    private String descripcion;

    @NotNull(message = "El estado es requerido")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoContrato estado = EstadoContrato.BORRADOR;

    @Column(nullable = false)
    private boolean eliminado = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @Column(nullable = false)
    private LocalDateTime actualizadoEn;

    @Column
    private LocalDateTime fechaEliminacion;

    @OneToMany(mappedBy = "contrato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialEstadoContrato> historialEstados = new ArrayList<>();

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

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Propiedad getPropiedad() { return propiedad; }
    public void setPropiedad(Propiedad propiedad) { this.propiedad = propiedad; }

    public Persona getInquilino() { return inquilino; }
    public void setInquilino(Persona inquilino) { this.inquilino = inquilino; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public Integer getDuracionMeses() { return duracionMeses; }
    public void setDuracionMeses(Integer duracionMeses) { this.duracionMeses = duracionMeses; }

    public BigDecimal getImporteMensual() { return importeMensual; }
    public void setImporteMensual(BigDecimal importeMensual) { this.importeMensual = importeMensual; }

    public Integer getDiaVencimientoMensual() { return diaVencimientoMensual; }
    public void setDiaVencimientoMensual(Integer diaVencimientoMensual) { this.diaVencimientoMensual = diaVencimientoMensual; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public EstadoContrato getEstado() { return estado; }
    public void setEstado(EstadoContrato estado) { this.estado = estado; }

    public boolean isEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public LocalDateTime getActualizadoEn() { return actualizadoEn; }

    public LocalDateTime getFechaEliminacion() { return fechaEliminacion; }
    public void setFechaEliminacion(LocalDateTime fechaEliminacion) { this.fechaEliminacion = fechaEliminacion; }

    public List<HistorialEstadoContrato> getHistorialEstados() { return historialEstados; }
    public void setHistorialEstados(List<HistorialEstadoContrato> h) { this.historialEstados = h; }

    public String getResumen() {
        if (propiedad != null && inquilino != null) {
            return "Contrato #" + id + " - " + propiedad.getResumen()
                    + " / " + inquilino.getNombreCompleto();
        }
        return "Contrato #" + id;
    }
}
