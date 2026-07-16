package com.inmobiliaria.gestion.repository;

import com.inmobiliaria.gestion.model.Publicacion;
import com.inmobiliaria.gestion.model.enums.EstadoPublicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {

    List<Publicacion> findByEliminadaFalse();

    boolean existsByPropiedadIdAndEstadoPublicacionAndEliminadaFalse(
            Long propiedadId, EstadoPublicacion estadoPublicacion);

    @Query("""
        SELECT p FROM Publicacion p
        WHERE p.eliminada = false
          AND (:propiedadId IS NULL OR p.propiedad.id = :propiedadId)
          AND (:ciudadId IS NULL OR p.propiedad.ciudad.id = :ciudadId)
          AND (:estado IS NULL OR p.estadoPublicacion = :estado)
          AND (:precioMin IS NULL OR p.precioMensual >= :precioMin)
          AND (:precioMax IS NULL OR p.precioMensual <= :precioMax)
        ORDER BY p.fechaPublicacion DESC
    """)
    List<Publicacion> filtrarPublicaciones(
            @Param("propiedadId") Long propiedadId,
            @Param("ciudadId") Long ciudadId,
            @Param("estado") EstadoPublicacion estado,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax
    );
}
