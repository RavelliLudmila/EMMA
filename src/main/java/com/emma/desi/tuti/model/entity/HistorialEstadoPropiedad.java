package com.emma.desi.tuti.model.entity;
import jakarta.persistence.*;
import com.emma.desi.tuti.model.enums.EstadoDisponibilidad;
import java.time.LocalDateTime;
@Entity
@Table(name="historial_estado_propiedades")
public class HistorialEstadoPropiedad {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private EstadoDisponibilidad estado;
	@Column(name="fecha_hora",nullable=false)
	private LocalDateTime fechaHora;
	@ManyToOne
	@JoinColumn(name="propiedad_id",nullable=false)
	private Propiedad propiedad;
	//Constructor
	public HistorialEstadoPropiedad() {}
	//Getters setters
	public Long getId() {return id;}
	public EstadoDisponibilidad getEstado() {return estado;}
	public LocalDateTime getFechaHora() {return fechaHora;}
	public Propiedad getPropiedad() {return propiedad;}
	public void setId(Long id) {this.id = id;}
	public void setEstado(EstadoDisponibilidad estado) {this.estado=estado;}
	public void setFechaHora(LocalDateTime fechaHora) {this.fechaHora=fechaHora;}
	public void setPropiedad(Propiedad propiedad) {this.propiedad=propiedad;}
}
