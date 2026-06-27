package com.emma.desi.tuti.service;

import com.emma.desi.tuti.model.entity.Publicacion;
import com.emma.desi.tuti.model.enums.EstadoPublicacion;
import java.math.BigDecimal;
import java.util.List;

public interface PublicacionService {

	// HU 2.1 — Alta de una publicacion
	Publicacion guardar(Publicacion publicacion);

	// HU 2.2 — Eliminacion logica de una publicacion
	void eliminar(Long id);

	// HU 2.3 — Modificacion de una publicacion
	Publicacion actualizar(Long id, Publicacion publicacionModificada);

	// HU 2.4 — Listar publicaciones no eliminadas
	List<Publicacion> listarActivas();

	// HU 2.4 — Filtrar publicaciones
	List<Publicacion> filtrar(Long propiedadId, Long ciudadId, EstadoPublicacion estadoPublicacion, BigDecimal precioMin, BigDecimal precioMax);

	// Buscar por id (helper compartido)
	Publicacion buscarPorId(Long id);
}
