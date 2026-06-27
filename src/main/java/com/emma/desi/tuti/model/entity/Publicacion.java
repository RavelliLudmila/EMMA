package com.emma.desi.tuti.model.entity;

import jakarta.persistence.*;
import com.emma.desi.tuti.model.enums.EstadoPublicacion;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="publicaciones")
public class Publicacion {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name="propiedad_id",nullable=false)
	private Propiedad propiedad;

	@Column(name="precio_mensual",nullable=false)
	private BigDecimal precioMensual;

	@Column(name="condiciones")
	private String condiciones;

	@Column(name="fecha_publicacion",nullable=false)
	private LocalDate fechaPublicacion;

	@Enumerated(EnumType.STRING)
	@Column(name="estado_publicacion",nullable=false)
	private EstadoPublicacion estadoPublicacion=EstadoPublicacion.ACTIVA;

	@Column(nullable=false)
	private Boolean eliminada=false;

	@Column(name="descripcion")
	private String descripcion;

	@OneToMany(mappedBy="publicacion",cascade=CascadeType.ALL,orphanRemoval=true)
	private List<HistorialEstadoPublicacion> historialEstados=new ArrayList<>();

	@OneToMany(mappedBy="publicacion",cascade=CascadeType.ALL,orphanRemoval=true)
	private List<Visita> visitas=new ArrayList<>();

	//constructor
	public Publicacion() {}

	//getters
	public Long getId() {return id;}
	public Propiedad getPropiedad() {return propiedad;}
	public BigDecimal getPrecioMensual() {return precioMensual;}
	public String getCondiciones() {return condiciones;}
	public LocalDate getFechaPublicacion() {return fechaPublicacion;}
	public EstadoPublicacion getEstadoPublicacion() {return estadoPublicacion;}
	public Boolean isEliminada() {return eliminada;}
	public String getDescripcion() {return descripcion;}
	public List<HistorialEstadoPublicacion> getHistorialEstados() {return historialEstados;}
	public List<Visita> getVisitas() {return visitas;}

	//setters
	public void setId(Long id) {this.id=id;}
	public void setPropiedad(Propiedad propiedad) {this.propiedad=propiedad;}
	public void setPrecioMensual(BigDecimal precioMensual) {this.precioMensual=precioMensual;}
	public void setCondiciones(String condiciones) {this.condiciones=condiciones;}
	public void setFechaPublicacion(LocalDate fechaPublicacion) {this.fechaPublicacion=fechaPublicacion;}
	public void setEstadoPublicacion(EstadoPublicacion estadoPublicacion) {this.estadoPublicacion=estadoPublicacion;}
	public void setEliminada(boolean eliminada) {this.eliminada=eliminada;}
	public void setDescripcion(String descripcion) {this.descripcion=descripcion;}
	public void setHistorialEstados(List<HistorialEstadoPublicacion> historialEstados) {this.historialEstados=historialEstados;}
	public void setVisitas(List<Visita> visitas) {this.visitas=visitas;}

	// Registra el cambio de estado en el historial de la publicacion. Se invoca cada vez que el estado cambia (al crear o al modificar).
	public void cambiarEstado(EstadoPublicacion nuevoEstado) {
		this.estadoPublicacion=nuevoEstado;
		HistorialEstadoPublicacion registro=new HistorialEstadoPublicacion();
		registro.setEstado(nuevoEstado);
		registro.setFechaHora(LocalDateTime.now());
		registro.setPublicacion(this);
		this.historialEstados.add(registro);
	}
}
