package com.emma.desi.tuti.model.entity;
import jakarta.persistence.*;
@Entity
@Table(name="ciudades")
public class Ciudad {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable=false)
	private String nombre;
	@ManyToOne
	@JoinColumn(name="provincia_id",nullable=false)
	private Provincia provincia;
	//Contructor
	public Ciudad() {}
	//Getter setter
	public Long getId() {return id;}
	public String getNombre() {return nombre;}
	public Provincia getProvincia() {return provincia;}
	public void setId(Long id) {this.id=id;}  
	public void setNombre(String nombre) {this.nombre=nombre;}
	public void setProvincia(Provincia provincia) {this.provincia=provincia;}
}
