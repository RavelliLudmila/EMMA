package com.emma.desi.tuti.repository;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.emma.desi.tuti.model.entity.Contrato;
import com.emma.desi.tuti.model.enums.EstadoContrato;

import org.springframework.data.jpa.repository.JpaRepository;



import java.util.List;
import java.util.Optional;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    boolean existsByPropiedadIdAndEstado(Long propiedadId, EstadoContrato estado);
    
    boolean existsByPropiedadIdAndEstadoAndEliminadoFalse(Long propiedadId,EstadoContrato estado);
    
    boolean existsByPropiedadIdAndEstadoAndEliminadoFalseAndIdNot(
            Long propiedadId,
            EstadoContrato estado,
            Long id);

    Optional<Contrato> findByIdAndEliminadoFalse(Long id);
    
    List<Contrato> findByEliminadoFalseOrderByIdDesc();
    
    
  
    
    @Query("""
        
        
            select c
            from Contrato c
            join fetch c.propiedad p
            join fetch c.inquilino i
            left join fetch p.propietario propietario
            where c.eliminado = false
              and (:propiedadId is null or p.id = :propiedadId)
              and (:inquilinoId is null or i.id = :inquilinoId)
              and (:estado is null or c.estado = :estado)
              and (:fechaInicio is null or c.fechaInicio = :fechaInicio)
            order by c.id desc
            """)
    				
    List<Contrato> buscarListado(
    		@Param("propiedadId") Long propiedadId,
    		@Param("inquilinoId") Long inquilinoId,
    		@Param("estado") EstadoContrato estado,
    		@Param("fechaInicio") LocalDate fechaInicio);
    		
}


