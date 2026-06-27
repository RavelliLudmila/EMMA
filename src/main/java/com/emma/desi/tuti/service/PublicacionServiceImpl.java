package com.emma.desi.tuti.service;

import com.emma.desi.tuti.model.entity.Publicacion;
import com.emma.desi.tuti.model.enums.EstadoPublicacion;
import com.emma.desi.tuti.repository.PublicacionRepository;
import com.emma.desi.tuti.repository.PropiedadRepository;
import com.emma.desi.tuti.service.exception.ReglaNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class PublicacionServiceImpl implements PublicacionService {

	@Autowired
	private PublicacionRepository publicacionRepository;

	@Autowired
	private PropiedadRepository propiedadRepository;

	// HU 2.1
	@Transactional
	public Publicacion guardar(Publicacion publicacion) {
		// Validar que la propiedad exista y no este eliminada
		var propiedad = propiedadRepository.findById(publicacion.getPropiedad().getId())
			.orElseThrow(() -> new ReglaNegocioException("La propiedad no existe."));
		if (propiedad.isEliminada()) {
			throw new ReglaNegocioException("No se puede publicar una propiedad eliminada.");
		}

		// Validar que la propiedad este disponible
		if (propiedad.getEstadoDisponibilidad() != com.emma.desi.tuti.model.enums.EstadoDisponibilidad.DISPONIBLE) {
			throw new ReglaNegocioException("Solo se pueden publicar propiedades en estado DISPONIBLE.");
		}

		// Validar que no exista ya una publicacion activa para esa propiedad
		if (publicacionRepository.existsByPropiedadIdAndEstadoPublicacionAndEliminadaFalse(
				propiedad.getId(), EstadoPublicacion.ACTIVA)) {
			throw new ReglaNegocioException("Ya existe una publicacion activa para esta propiedad.");
		}

		// Validar precio positivo
		if (publicacion.getPrecioMensual() == null || publicacion.getPrecioMensual().compareTo(BigDecimal.ZERO) <= 0) {
			throw new ReglaNegocioException("El precio mensual debe ser un numero positivo.");
		}

		// Estado por defecto: ACTIVA
		if (publicacion.getEstadoPublicacion() == null) {
			publicacion.setEstadoPublicacion(EstadoPublicacion.ACTIVA);
		}

		// Registrar en historial de estados
		publicacion.cambiarEstado(publicacion.getEstadoPublicacion());

		return publicacionRepository.save(publicacion);
	}

	// HU 2.2
	@Transactional
	public void eliminar(Long id) {
		throw new UnsupportedOperationException("HU 2.2 pendiente de implementacion");
	}

	// HU 2.3
	@Transactional
	public Publicacion actualizar(Long id, Publicacion publicacionModificada) {
		throw new UnsupportedOperationException("HU 2.3 pendiente de implementacion");
	}

	// HU 2.4
	public List<Publicacion> listarActivas() {
		throw new UnsupportedOperationException("HU 2.4 pendiente de implementacion");
	}

	public List<Publicacion> filtrar(Long propiedadId, Long ciudadId, EstadoPublicacion estadoPublicacion, BigDecimal precioMin, BigDecimal precioMax) {
		throw new UnsupportedOperationException("HU 2.4 pendiente de implementacion");
	}

	public Publicacion buscarPorId(Long id) {
		return publicacionRepository.findById(id)
			.orElseThrow(() -> new ReglaNegocioException("La publicacion no existe."));
	}
}
