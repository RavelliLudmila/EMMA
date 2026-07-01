package com.inmobiliaria.gestion.repository;

import com.inmobiliaria.gestion.model.Contrato;
import com.inmobiliaria.gestion.model.enums.EstadoContrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    List<Contrato> findByEliminadoFalse();

    List<Contrato> findByEstadoAndEliminadoFalse(EstadoContrato estado);

    Optional<Contrato> findByIdAndEliminadoFalse(Long id);

    boolean existsByPropiedadIdAndEstadoAndEliminadoFalse(Long propiedadId, EstadoContrato estado);

    boolean existsByPropiedadIdAndEstadoAndEliminadoFalseAndIdNot(
            Long propiedadId, EstadoContrato estado, Long id);

    @Query("""
        SELECT c FROM Contrato c
        JOIN FETCH c.propiedad p
        JOIN FETCH c.inquilino i
        WHERE c.eliminado = false
          AND (:propiedadId IS NULL OR p.id = :propiedadId)
          AND (:inquilinoId IS NULL OR i.id = :inquilinoId)
          AND (:estado IS NULL OR c.estado = :estado)
          AND (:fechaInicio IS NULL OR c.fechaInicio = :fechaInicio)
        ORDER BY c.id DESC
    """)
    List<Contrato> buscarListado(
            @Param("propiedadId") Long propiedadId,
            @Param("inquilinoId") Long inquilinoId,
            @Param("estado") EstadoContrato estado,
            @Param("fechaInicio") LocalDate fechaInicio);
}
