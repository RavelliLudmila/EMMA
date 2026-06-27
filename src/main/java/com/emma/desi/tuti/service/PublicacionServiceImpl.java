package com.emma.desi.tuti.service;

import com.emma.desi.tuti.model.entity.Publicacion;
import com.emma.desi.tuti.model.enums.EstadoDisponibilidad;
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
		if (propiedad.getEstadoDisponibilidad() != EstadoDisponibilidad.DISPONIBLE) {
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
		Publicacion publicacion = buscarPorId(id);

		// Solo se pueden eliminar publicaciones ACTIVAS
		if (publicacion.getEstadoPublicacion() != EstadoPublicacion.ACTIVA) {
			throw new ReglaNegocioException("Solo se pueden eliminar publicaciones en estado ACTIVA.");
		}

		// Baja logica
		publicacion.setEliminada(true);
		publicacionRepository.save(publicacion);
	}

	// HU 2.3
	@Transactional
	public Publicacion actualizar(Long id, Publicacion publicacionModificada) {
		Publicacion existente = buscarPorId(id);

		// No se puede modificar una publicacion finalizada
		if (existente.getEstadoPublicacion() == EstadoPublicacion.FINALIZADA) {
			throw new ReglaNegocioException("No se puede modificar una publicacion finalizada.");
		}

		// Validar precio positivo
		if (publicacionModificada.getPrecioMensual() == null || publicacionModificada.getPrecioMensual().compareTo(BigDecimal.ZERO) <= 0) {
			throw new ReglaNegocioException("El precio mensual debe ser un numero positivo.");
		}

		EstadoPublicacion estadoNuevo = publicacionModificada.getEstadoPublicacion();
		EstadoPublicacion estadoActual = existente.getEstadoPublicacion();

		// Validar transicion de estado si cambia
		if (estadoNuevo != estadoActual) {
			// No se puede reactivar desde FINALIZADA (ya bloqueado arriba, pero lo dejamos explicito)
			if (estadoActual == EstadoPublicacion.FINALIZADA) {
				throw new ReglaNegocioException("No se puede cambiar el estado de una publicacion finalizada.");
			}
			// Para activar desde PAUSADA: verificar propiedad disponible y sin otra publicacion activa
			if (estadoNuevo == EstadoPublicacion.ACTIVA) {
				var propiedad = propiedadRepository.findById(existente.getPropiedad().getId())
					.orElseThrow(() -> new ReglaNegocioException("La propiedad no existe."));
				if (propiedad.getEstadoDisponibilidad() != EstadoDisponibilidad.DISPONIBLE) {
					throw new ReglaNegocioException("Solo se puede activar una publicacion si la propiedad esta en estado DISPONIBLE.");
				}
				if (publicacionRepository.existsByPropiedadIdAndEstadoPublicacionAndEliminadaFalse(
						existente.getPropiedad().getId(), EstadoPublicacion.ACTIVA)) {
					throw new ReglaNegocioException("Ya existe una publicacion activa para esta propiedad.");
				}
			}
			// Registrar cambio de estado en historial
			existente.cambiarEstado(estadoNuevo);
		}

		// Actualizar campos (propiedad es de solo lectura, no se modifica)
		existente.setPrecioMensual(publicacionModificada.getPrecioMensual());
		existente.setCondiciones(publicacionModificada.getCondiciones());
		existente.setDescripcion(publicacionModificada.getDescripcion());
		existente.setFechaPublicacion(publicacionModificada.getFechaPublicacion());

		return publicacionRepository.save(existente);
	}

	// HU 2.4
	public List<Publicacion> listarActivas() {
		return publicacionRepository.findByEliminadaFalse();
	}

	public List<Publicacion> filtrar(Long propiedadId, Long ciudadId, EstadoPublicacion estadoPublicacion, BigDecimal precioMin, BigDecimal precioMax) {
		return publicacionRepository.filtrarPublicaciones(propiedadId, ciudadId, estadoPublicacion, precioMin, precioMax);
	}

	public Publicacion buscarPorId(Long id) {
		return publicacionRepository.findById(id)
			.orElseThrow(() -> new ReglaNegocioException("La publicacion no existe."));
	}
}
