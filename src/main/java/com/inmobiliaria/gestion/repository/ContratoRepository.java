package com.inmobiliaria.gestion.repository;

import com.inmobiliaria.gestion.model.Contrato;
import com.inmobiliaria.gestion.model.enums.EstadoContrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    List<Contrato> findByEliminadoFalse();

    List<Contrato> findByEstadoAndEliminadoFalse(EstadoContrato estado);
}
