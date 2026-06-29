package com.emma.desi.tuti.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.emma.desi.tuti.model.entity.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Long> {

    List<Persona> findByEliminadoFalseOrderByApellidoAscNombreAsc();

    Optional<Persona> findByIdAndEliminadoFalse(Long id);
}

