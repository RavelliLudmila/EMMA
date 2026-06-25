package com.emma.desi.tuti.model.entity;
import jakarta.persistence.*;
@Entity
@Table(name="personas")
public class Persona {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(nullable=false)
	private String nombre;
	@Column(nullable=false)
	private String apellido;
	@Column(name="dni_cuit",nullable=false,unique=true)
	private String dniCuit; //Ponemos como unico el DNI
	private String domicilio;
	private String email;
	private String telefono;
	public Persona() {}
	//Getter setters
	public Long getId() {return id;}
	public String getNombre() {return nombre; }
	public String getApellido() {return apellido;}
	public String getDniCuit() {return dniCuit;}
	public String getDomicilio() {return domicilio;}
	public String getEmail() {return email;}
	public String getTelefono() {return telefono;}
	public void setId(Long id) {this.id = id;}  
	public void setNombre(String nombre) {this.nombre = nombre;}
	public void setApellido(String apellido) {this.apellido = apellido;}
	public void setDniCuit(String dniCuit) {this.dniCuit = dniCuit;}
	public void setDomicilio(String domicilio) {this.domicilio = domicilio;}
	public void setEmail(String email) {this.email = email;}
	public void setTelefono(String telefono) {this.telefono = telefono;}
	
	
	
	
}
