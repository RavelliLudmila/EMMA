package com.emma.desi.tuti.model.entity;
import jakarta.persistence.*;
import com.emma.desi.tuti.model.enums.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name="propiedades")
public class Propiedad {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
	@Column(nullable=false)
    private String direccion;
	@Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private TipoPropiedad tipo;
	@Column(name="cantidad_ambientes",nullable=false)
    private Integer cantidadAmbientes;
	@Column(name="metros_cuadrados",nullable=false)
    private Float metrosCuadrados;
	private String descripcion;
	private String comodidades;
	@Enumerated(EnumType.STRING)
    @Column(name="estado_disponibilidad",nullable=false)
    private EstadoDisponibilidad estadoDisponibilidad=EstadoDisponibilidad.DISPONIBLE;
	@Column(nullable=false)
    private Boolean eliminada=false;
	@ManyToOne
    @JoinColumn(name="propietario_id",nullable=false)
    private Persona propietario;
	@ManyToOne
    @JoinColumn(name="ciudad_id",nullable=false)
    private Ciudad city;
	@OneToMany(mappedBy="propiedad",cascade=CascadeType.ALL,orphanRemoval=true)
    private List<HistorialEstadoPropiedad> historialEstados=new ArrayList<>();
	//contructor
	public Propiedad() {}
	//getter setter, atentis que son mil
	public Long getId() {return id;}
	public String getDireccion() {return direccion;}
	public TipoPropiedad getTipo() {return tipo;}
	public Integer getCantidadAmbientes() {return cantidadAmbientes;}
	public Float getMetrosCuadrados() {return metrosCuadrados;}
	public String getDescripcion() {return descripcion;}
	public String getComodidades() {return comodidades;}
	public EstadoDisponibilidad getEstadoDisponibilidad() {return estadoDisponibilidad;}
	public Boolean isEliminada() {return eliminada;}
	public Persona getPropietario() {return propietario;}
	public Ciudad getCiudad() {return city;}
	public List<HistorialEstadoPropiedad> getHistorialEstados() {return historialEstados;}
	public void setId(Long id) {this.id=id;}
	public void setDireccion(String direccion) {this.direccion=direccion;}
	public void setTipo(TipoPropiedad tipo) {this.tipo=tipo;}
	public void setCantidadAmbientes(Integer cantidadAmbientes) {this.cantidadAmbientes=cantidadAmbientes;}
	public void setMetrosCuadrados(Float metrosCuadrados) {this.metrosCuadrados=metrosCuadrados;}
	public void setDescripcion(String descripcion) {this.descripcion=descripcion;}
	public void setComodidades(String comodidades) {this.comodidades=comodidades;}
	public void setEstadoDisponibilidad(EstadoDisponibilidad estadoDisponibilidad) {this.estadoDisponibilidad=estadoDisponibilidad;}
	public void setEliminada(boolean eliminada) {this.eliminada=eliminada;}
	public void setPropietario(Persona propietario) {this.propietario=propietario;}
	public void setCiudad(Ciudad ciudad) {this.city=ciudad;}
	public void setHistorialEstados(List<HistorialEstadoPropiedad> historialEstados) {this.historialEstados=historialEstados;}
	
	
	
	/*
	 *  Aca vamos a crear una nueva instancia de estado para la lista de historial de
	 *  estados de la propiedad. Entonces cada que le cambioamos el estado a una
	 *  propiedad, automaticamente como se crea, se guarda en la lista.
	 * 
	 * */
	public void cambiarEstado(EstadoDisponibilidad nuevoEstado) {
		this.estadoDisponibilidad=nuevoEstado;
		HistorialEstadoPropiedad registro=new HistorialEstadoPropiedad();
		registro.setEstado(nuevoEstado);
		registro.setFechaHora(LocalDateTime.now());
		registro.setPropiedad(this);
		this.historialEstados.add(registro);
	}
}
