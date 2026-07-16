package com.inmobiliaria.gestion.model;

import com.inmobiliaria.gestion.model.enums.CategoriaIncidente;
import com.inmobiliaria.gestion.model.enums.EstadoIncidente;
import com.inmobiliaria.gestion.model.enums.PrioridadIncidente;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "incidente")
public class Incidente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El titulo es requerido")
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "La descripcion es requerida")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @NotNull(message = "La categoria es requerida")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaIncidente categoria;

    @NotNull(message = "La prioridad es requerida")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrioridadIncidente prioridad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoIncidente estado = EstadoIncidente.ABIERTO;

    @Column(nullable = false)
    private LocalDateTime fechaAlta;

    @Column
    private LocalDateTime fechaResolucion;

    @Column(columnDefinition = "TEXT")
    private String observacionesResolucion;

    @Column(precision = 12, scale = 2)
    private BigDecimal costoResolucion;

    @Column
    private String responsableTecnico;

    @Column(nullable = false)
    private boolean eliminado = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contrato contrato;

    @OneToMany(mappedBy = "incidente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialEstadoIncidente> historialEstados = new ArrayList<>();

    @PrePersist
    void prePersist() {
        if (fechaAlta == null) {
            fechaAlta = LocalDateTime.now();
        }
    }

    public Incidente() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public CategoriaIncidente getCategoria() { return categoria; }
    public void setCategoria(CategoriaIncidente categoria) { this.categoria = categoria; }

    public PrioridadIncidente getPrioridad() { return prioridad; }
    public void setPrioridad(PrioridadIncidente prioridad) { this.prioridad = prioridad; }

    public EstadoIncidente getEstado() { return estado; }
    public void setEstado(EstadoIncidente estado) { this.estado = estado; }

    public LocalDateTime getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDateTime fechaAlta) { this.fechaAlta = fechaAlta; }

    public LocalDateTime getFechaResolucion() { return fechaResolucion; }
    public void setFechaResolucion(LocalDateTime fechaResolucion) { this.fechaResolucion = fechaResolucion; }

    public String getObservacionesResolucion() { return observacionesResolucion; }
    public void setObservacionesResolucion(String obs) { this.observacionesResolucion = obs; }

    public BigDecimal getCostoResolucion() { return costoResolucion; }
    public void setCostoResolucion(BigDecimal costoResolucion) { this.costoResolucion = costoResolucion; }

    public String getResponsableTecnico() { return responsableTecnico; }
    public void setResponsableTecnico(String responsableTecnico) { this.responsableTecnico = responsableTecnico; }

    public boolean isEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }

    public Contrato getContrato() { return contrato; }
    public void setContrato(Contrato contrato) { this.contrato = contrato; }

    public List<HistorialEstadoIncidente> getHistorialEstados() { return historialEstados; }
    public void setHistorialEstados(List<HistorialEstadoIncidente> h) { this.historialEstados = h; }

    public void cambiarEstado(EstadoIncidente nuevoEstado) {
        this.estado = nuevoEstado;
        HistorialEstadoIncidente registro = new HistorialEstadoIncidente();
        registro.setEstado(nuevoEstado);
        registro.setFechaHora(LocalDateTime.now());
        registro.setIncidente(this);
        this.historialEstados.add(registro);
    }
}
