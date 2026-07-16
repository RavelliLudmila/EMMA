package com.inmobiliaria.gestion.repository;

import com.inmobiliaria.gestion.model.Visita;
import com.inmobiliaria.gestion.model.enums.EstadoVisita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VisitaRepository extends JpaRepository<Visita, Long> {

    List<Visita> findByEliminadaFalse();

    @Query("""
        SELECT v FROM Visita v
        WHERE v.eliminada = false
          AND (:publicacionId IS NULL OR v.publicacion.id = :publicacionId)
          AND (:estado IS NULL OR v.estado = :estado)
        ORDER BY v.fechaHora DESC
    """)
    List<Visita> filtrarVisitas(
            @Param("publicacionId") Long publicacionId,
            @Param("estado") EstadoVisita estado
    );
}
