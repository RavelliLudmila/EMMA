package com.inmobiliaria.gestion.model;

import com.inmobiliaria.gestion.model.enums.EstadoIncidente;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_estado_incidente")
public class HistorialEstadoIncidente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incidente_id", nullable = false)
    private Incidente incidente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoIncidente estado;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @PrePersist
    void prePersist() {
        if (fechaHora == null) {
            fechaHora = LocalDateTime.now();
        }
    }

    public HistorialEstadoIncidente() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Incidente getIncidente() { return incidente; }
    public void setIncidente(Incidente incidente) { this.incidente = incidente; }

    public EstadoIncidente getEstado() { return estado; }
    public void setEstado(EstadoIncidente estado) { this.estado = estado; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
}
