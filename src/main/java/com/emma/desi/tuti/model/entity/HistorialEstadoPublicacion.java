package com.emma.desi.tuti.model.entity;

import jakarta.persistence.*;
import com.emma.desi.tuti.model.enums.EstadoPublicacion;
import java.time.LocalDateTime;

@Entity
@Table(name="historial_estado_publicaciones")
public class HistorialEstadoPublicacion {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private EstadoPublicacion estado;

	@Column(name="fecha_hora",nullable=false)
	private LocalDateTime fechaHora;

	@ManyToOne
	@JoinColumn(name="publicacion_id",nullable=false)
	private Publicacion publicacion;

	//Constructor
	public HistorialEstadoPublicacion() {}

	//Getters setters
	public Long getId() {return id;}
	public EstadoPublicacion getEstado() {return estado;}
	public LocalDateTime getFechaHora() {return fechaHora;}
	public Publicacion getPublicacion() {return publicacion;}
	public void setId(Long id) {this.id=id;}
	public void setEstado(EstadoPublicacion estado) {this.estado=estado;}
	public void setFechaHora(LocalDateTime fechaHora) {this.fechaHora=fechaHora;}
	public void setPublicacion(Publicacion publicacion) {this.publicacion=publicacion;}
}
