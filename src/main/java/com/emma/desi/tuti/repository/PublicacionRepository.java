package com.emma.desi.tuti.repository;

import com.emma.desi.tuti.model.entity.Publicacion;
import com.emma.desi.tuti.model.enums.EstadoPublicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion,Long> {

	// HU 2.4 — listar publicaciones no eliminadas
	List<Publicacion> findByEliminadaFalse();

	// HU 2.1 / 2.3 — verificar si existe una publicacion activa para una propiedad (excluye eliminadas)
	boolean existsByPropiedadIdAndEstadoPublicacionAndEliminadaFalse(Long propiedadId, EstadoPublicacion estadoPublicacion);

	// HU 2.4 — filtros combinados (propiedad, ciudad, estado, rango de precio)
	@Query("SELECT p FROM Publicacion p WHERE p.eliminada = false " +
		   "AND (:propiedadId IS NULL OR p.propiedad.id = :propiedadId) " +
		   "AND (:ciudadId IS NULL OR p.propiedad.city.id = :ciudadId) " +
		   "AND (:estadoPublicacion IS NULL OR p.estadoPublicacion = :estadoPublicacion) " +
		   "AND (:precioMin IS NULL OR p.precioMensual >= :precioMin) " +
		   "AND (:precioMax IS NULL OR p.precioMensual <= :precioMax)")
	List<Publicacion> filtrarPublicaciones(
		@Param("propiedadId") Long propiedadId,
		@Param("ciudadId") Long ciudadId,
		@Param("estadoPublicacion") EstadoPublicacion estadoPublicacion,
		@Param("precioMin") BigDecimal precioMin,
		@Param("precioMax") BigDecimal precioMax
	);
}
