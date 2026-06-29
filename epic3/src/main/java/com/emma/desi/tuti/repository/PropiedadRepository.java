package com.emma.desi.tuti.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.emma.desi.tuti.model.entity.Propiedad;

public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {

    List<Propiedad> findByEliminadoFalseOrderByDireccionAsc();

    Optional<Propiedad> findByIdAndEliminadoFalse(Long id);
}

