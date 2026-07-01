package com.inmobiliaria.gestion.service;

import com.inmobiliaria.gestion.model.Propiedad;
import com.inmobiliaria.gestion.model.Publicacion;
import com.inmobiliaria.gestion.model.enums.EstadoDisponibilidad;
import com.inmobiliaria.gestion.model.enums.EstadoPublicacion;
import com.inmobiliaria.gestion.repository.PropiedadRepository;
import com.inmobiliaria.gestion.repository.PublicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PublicacionServiceImpl implements PublicacionService {

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    private PropiedadRepository propiedadRepository;

    // HU 2.1 Alta
    @Transactional
    public Publicacion guardar(Publicacion publicacion) {
        Propiedad propiedad = propiedadRepository.findById(publicacion.getPropiedad().getId())
                .orElseThrow(() -> new IllegalArgumentException("La propiedad no existe."));

        if (propiedad.isEliminada()) {
            throw new IllegalArgumentException("No se puede publicar una propiedad eliminada.");
        }
        if (propiedad.getEstadoDisponibilidad() != EstadoDisponibilidad.DISPONIBLE) {
            throw new IllegalArgumentException("Solo se pueden publicar propiedades en estado DISPONIBLE.");
        }
        if (publicacionRepository.existsByPropiedadIdAndEstadoPublicacionAndEliminadaFalse(
                propiedad.getId(), EstadoPublicacion.ACTIVA)) {
            throw new IllegalArgumentException("Ya existe una publicacion activa para esta propiedad.");
        }
        if (publicacion.getPrecioMensual() == null ||
                publicacion.getPrecioMensual().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio mensual debe ser un numero positivo.");
        }
        if (publicacion.getEstadoPublicacion() == null) {
            publicacion.setEstadoPublicacion(EstadoPublicacion.ACTIVA);
        }

        publicacion.setFechaPublicacion(LocalDate.now());
        publicacion.cambiarEstado(publicacion.getEstadoPublicacion());
        return publicacionRepository.save(publicacion);
    }

    // HU 2.2 Eliminacion logica
    @Transactional
    public void eliminar(Long id) {
        Publicacion publicacion = buscarPorId(id);
        if (publicacion.getEstadoPublicacion() != EstadoPublicacion.ACTIVA) {
            throw new IllegalArgumentException("Solo se pueden eliminar publicaciones en estado ACTIVA.");
        }
        publicacion.setEliminada(true);
        publicacionRepository.save(publicacion);
    }

    // HU 2.3 Modificacion
    @Transactional
    public Publicacion actualizar(Long id, Publicacion publicacionModificada) {
        Publicacion existente = buscarPorId(id);

        if (existente.getEstadoPublicacion() == EstadoPublicacion.FINALIZADA) {
            throw new IllegalArgumentException("No se puede modificar una publicacion finalizada.");
        }
        if (publicacionModificada.getPrecioMensual() == null ||
                publicacionModificada.getPrecioMensual().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio mensual debe ser un numero positivo.");
        }

        EstadoPublicacion estadoNuevo = publicacionModificada.getEstadoPublicacion();
        EstadoPublicacion estadoActual = existente.getEstadoPublicacion();

        if (estadoNuevo != null && estadoNuevo != estadoActual) {
            if (estadoNuevo == EstadoPublicacion.ACTIVA) {
                Propiedad propiedad = propiedadRepository.findById(existente.getPropiedad().getId())
                        .orElseThrow(() -> new IllegalArgumentException("La propiedad no existe."));
                if (propiedad.getEstadoDisponibilidad() != EstadoDisponibilidad.DISPONIBLE) {
                    throw new IllegalArgumentException(
                            "Solo se puede activar una publicacion si la propiedad esta en estado DISPONIBLE.");
                }
                if (publicacionRepository.existsByPropiedadIdAndEstadoPublicacionAndEliminadaFalse(
                        existente.getPropiedad().getId(), EstadoPublicacion.ACTIVA)) {
                    throw new IllegalArgumentException("Ya existe una publicacion activa para esta propiedad.");
                }
            }
            existente.cambiarEstado(estadoNuevo);
        }

        existente.setPrecioMensual(publicacionModificada.getPrecioMensual());
        existente.setCondiciones(publicacionModificada.getCondiciones());
        existente.setDescripcion(publicacionModificada.getDescripcion());

        return publicacionRepository.save(existente);
    }

    // HU 2.4 Listado
    public List<Publicacion> listarActivas() {
        return publicacionRepository.findByEliminadaFalse();
    }

    public List<Publicacion> filtrar(Long propiedadId, Long ciudadId,
                                      EstadoPublicacion estado,
                                      BigDecimal precioMin, BigDecimal precioMax) {
        return publicacionRepository.filtrarPublicaciones(
                propiedadId, ciudadId, estado, precioMin, precioMax);
    }

    public Publicacion buscarPorId(Long id) {
        return publicacionRepository.findById(id)
                .filter(p -> !p.isEliminada())
                .orElseThrow(() -> new IllegalArgumentException("La publicacion no existe."));
    }
}
