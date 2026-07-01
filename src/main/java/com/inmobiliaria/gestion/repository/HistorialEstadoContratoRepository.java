package com.inmobiliaria.gestion.repository;

import com.inmobiliaria.gestion.model.HistorialEstadoContrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialEstadoContratoRepository extends JpaRepository<HistorialEstadoContrato, Long> {
}
