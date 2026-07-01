package com.inmobiliaria.gestion.model;

import com.inmobiliaria.gestion.model.enums.EstadoDisponibilidad;
import com.inmobiliaria.gestion.model.enums.TipoPropiedad;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "propiedad")
public class Propiedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La dirección es requerida")
    @Column(nullable = false)
    private String direccion;

    @NotNull(message = "El tipo de propiedad es requerido")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPropiedad tipo;

    @NotNull(message = "La cantidad de ambientes es requerida")
    @Positive(message = "La cantidad de ambientes debe ser un número positivo")
    @Column(name = "cantidad_ambientes", nullable = false)
    private Integer cantidadAmbientes;

    @NotNull(message = "Los metros cuadrados son requeridos")
    @Positive(message = "Los metros cuadrados deben ser un número positivo")
    @Column(name = "metros_cuadrados", nullable = false)
    private Double metrosCuadrados;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(columnDefinition = "TEXT")
    private String comodidades;

    @NotNull(message = "El estado de disponibilidad es requerido")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_disponibilidad", nullable = false)
    private EstadoDisponibilidad estadoDisponibilidad = EstadoDisponibilidad.DISPONIBLE;

    @Column(nullable = false)
    private Boolean eliminada = false;

    @ManyToOne
    @JoinColumn(name = "propietario_id", nullable = false)
    private Persona propietario;

    @ManyToOne
    @JoinColumn(name = "ciudad_id", nullable = false)
    private Ciudad ciudad;

    @OneToMany(mappedBy = "propiedad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialEstadoPropiedad> historialEstados = new ArrayList<>();

    public Propiedad() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public TipoPropiedad getTipo() { return tipo; }
    public void setTipo(TipoPropiedad tipo) { this.tipo = tipo; }

    public Integer getCantidadAmbientes() { return cantidadAmbientes; }
    public void setCantidadAmbientes(Integer cantidadAmbientes) { this.cantidadAmbientes = cantidadAmbientes; }

    public Double getMetrosCuadrados() { return metrosCuadrados; }
    public void setMetrosCuadrados(Double metrosCuadrados) { this.metrosCuadrados = metrosCuadrados; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getComodidades() { return comodidades; }
    public void setComodidades(String comodidades) { this.comodidades = comodidades; }

    public EstadoDisponibilidad getEstadoDisponibilidad() { return estadoDisponibilidad; }
    public void setEstadoDisponibilidad(EstadoDisponibilidad estado) { this.estadoDisponibilidad = estado; }

    public Boolean isEliminada() { return eliminada; }
    public void setEliminada(boolean eliminada) { this.eliminada = eliminada; }

    public Persona getPropietario() { return propietario; }
    public void setPropietario(Persona propietario) { this.propietario = propietario; }

    public Ciudad getCiudad() { return ciudad; }
    public void setCiudad(Ciudad ciudad) { this.ciudad = ciudad; }

    public List<HistorialEstadoPropiedad> getHistorialEstados() { return historialEstados; }
    public void setHistorialEstados(List<HistorialEstadoPropiedad> h) { this.historialEstados = h; }

    public void cambiarEstado(EstadoDisponibilidad nuevoEstado) {
        this.estadoDisponibilidad = nuevoEstado;
        HistorialEstadoPropiedad registro = new HistorialEstadoPropiedad();
        registro.setEstado(nuevoEstado);
        registro.setFechaHora(LocalDateTime.now());
        registro.setPropiedad(this);
        this.historialEstados.add(registro);
    }

    public String getResumen() {
        String ciudad = this.ciudad != null ? this.ciudad.getNombre() : "";
        return direccion + " - " + ciudad;
    }
}