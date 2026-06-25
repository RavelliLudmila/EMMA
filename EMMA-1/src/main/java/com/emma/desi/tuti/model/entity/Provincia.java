package com.emma.desi.tuti.model.entity;
import jakarta.persistence.*;
@Entity
@Table(name="provincias")

public class Provincia {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable=false)
	private String nombre;
	//Contructor
	public Provincia() {}
	//Getters setters
	public Long getId() {return id;}
	public String getNombre() {return nombre;}
	public void setId(Long id) {this.id=id;} 
	public void setNombre(String nombre) {this.nombre=nombre;}
}
