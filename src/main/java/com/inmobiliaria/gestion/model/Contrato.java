package com.inmobiliaria.gestion.model;

import com.inmobiliaria.gestion.model.enums.EstadoContrato;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contrato")
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "propiedad_id", nullable = false)
    private Propiedad propiedad;

    @ManyToOne
    @JoinColumn(name = "inquilino_id", nullable = false)
    private Persona inquilino;

    @NotNull(message = "La fecha de inicio es requerida")
    @Column(nullable = false)
    private LocalDate fechaInicio;

    @NotNull(message = "La duración en meses es requerida")
    @Positive(message = "La duración debe ser un número positivo")
    @Column(nullable = false)
    private Integer duracionMeses;

    @NotNull(message = "El importe mensual es requerido")
    @DecimalMin(value = "0.01", message = "El importe mensual debe ser un número positivo")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal importeMensual;

    @NotNull(message = "El día de vencimiento es requerido")
    @Min(value = 1, message = "El día de vencimiento debe ser entre 1 y 31")
    @Max(value = 31, message = "El día de vencimiento debe ser entre 1 y 31")
    @Column(nullable = false)
    private Integer diaVencimientoMensual;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoContrato estado = EstadoContrato.BORRADOR;

    @Column(nullable = false)
    private boolean eliminado = false;

    @OneToMany(mappedBy = "contrato", cascade = CascadeType.ALL)
    private List<HistorialEstadoContrato> historialEstados = new ArrayList<>();

    // Constructors
    public Contrato() {}

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

    public List<HistorialEstadoContrato> getHistorialEstados() { return historialEstados; }
    public void setHistorialEstados(List<HistorialEstadoContrato> historialEstados) { this.historialEstados = historialEstados; }

    public String getResumen() {
        if (propiedad != null && inquilino != null) {
            return "Contrato #" + id + " - " + propiedad.getResumen() + " / " + inquilino.getNombreCompleto();
        }
        return "Contrato #" + id;
    }
}
