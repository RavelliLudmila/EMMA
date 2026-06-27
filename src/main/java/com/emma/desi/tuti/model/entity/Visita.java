package com.emma.desi.tuti.model.entity;

import jakarta.persistence.*;
import com.emma.desi.tuti.model.enums.EstadoVisita;
import java.time.LocalDateTime;

@Entity
@Table(name="visitas")
public class Visita {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="fecha_hora",nullable=false)
	private LocalDateTime fechaHora;

	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private EstadoVisita estado;

	@ManyToOne
	@JoinColumn(name="publicacion_id",nullable=false)
	private Publicacion publicacion;

	//Constructor
	public Visita() {}

	//Getters setters
	public Long getId() {return id;}
	public LocalDateTime getFechaHora() {return fechaHora;}
	public EstadoVisita getEstado() {return estado;}
	public Publicacion getPublicacion() {return publicacion;}
	public void setId(Long id) {this.id=id;}
	public void setFechaHora(LocalDateTime fechaHora) {this.fechaHora=fechaHora;}
	public void setEstado(EstadoVisita estado) {this.estado=estado;}
	public void setPublicacion(Publicacion publicacion) {this.publicacion=publicacion;}
}
