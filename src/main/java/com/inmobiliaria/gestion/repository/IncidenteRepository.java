package com.inmobiliaria.gestion.repository;

import com.inmobiliaria.gestion.model.Incidente;
import com.inmobiliaria.gestion.model.enums.CategoriaIncidente;
import com.inmobiliaria.gestion.model.enums.EstadoIncidente;
import com.inmobiliaria.gestion.model.enums.PrioridadIncidente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IncidenteRepository extends JpaRepository<Incidente, Long> {

    List<Incidente> findByEliminadoFalse();

    @Query("""
        SELECT i FROM Incidente i
        WHERE i.eliminado = false
          AND (:contratoId IS NULL OR i.contrato.id = :contratoId)
          AND (:estado IS NULL OR i.estado = :estado)
          AND (:categoria IS NULL OR i.categoria = :categoria)
          AND (:prioridad IS NULL OR i.prioridad = :prioridad)
        ORDER BY i.fechaAlta DESC
    """)
    List<Incidente> filtrarIncidentes(
            @Param("contratoId") Long contratoId,
            @Param("estado") EstadoIncidente estado,
            @Param("categoria") CategoriaIncidente categoria,
            @Param("prioridad") PrioridadIncidente prioridad
    );
}
