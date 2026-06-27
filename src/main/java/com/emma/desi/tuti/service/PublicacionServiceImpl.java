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
		throw new UnsupportedOperationException("HU 2.1 pendiente de implementacion");
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
