package com.inmobiliaria.gestion.repository;

import com.inmobiliaria.gestion.model.Propiedad;
import com.inmobiliaria.gestion.model.enums.EstadoDisponibilidad;
import com.inmobiliaria.gestion.model.enums.TipoPropiedad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {

    List<Propiedad> findByEliminadaFalse();

    // Verifica si existe propiedad activa con misma dirección y ciudad (excluyendo un id)
    @Query("""
        SELECT COUNT(p) > 0 FROM Propiedad p
        WHERE p.eliminada = false
          AND LOWER(p.direccion) = LOWER(:direccion)
          AND p.ciudad.id = :ciudadId
          AND (:excludeId IS NULL OR p.id <> :excludeId)
    """)
    boolean existeDireccionDuplicada(
        @Param("direccion") String direccion,
        @Param("ciudadId") Long ciudadId,
        @Param("excludeId") Long excludeId
    );

    @Query("""
        SELECT p FROM Propiedad p WHERE p.eliminada = false
          AND (:direccion IS NULL OR p.direccion LIKE %:direccion%)
          AND (:ciudadId IS NULL OR p.ciudad.id = :ciudadId)
          AND (:tipo IS NULL OR p.tipo = :tipo)
          AND (:estado IS NULL OR p.estadoDisponibilidad = :estado)
        ORDER BY p.id DESC
    """)
    List<Propiedad> filtrarPropiedades(
        @Param("direccion") String direccion,
        @Param("ciudadId") Long ciudadId,
        @Param("tipo") TipoPropiedad tipo,
        @Param("estado") EstadoDisponibilidad estado
    );
}