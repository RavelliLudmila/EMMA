package com.inmobiliaria.gestion.repository;

import com.inmobiliaria.gestion.model.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CiudadRepository extends JpaRepository<Ciudad, Long> {
    List<Ciudad> findAllByOrderByNombreAsc();
}
