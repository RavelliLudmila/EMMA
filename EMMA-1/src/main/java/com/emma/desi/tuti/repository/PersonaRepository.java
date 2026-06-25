package com.emma.desi.tuti.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.emma.desi.tuti.model.entity.Persona;
public interface PersonaRepository extends JpaRepository<Persona,Long> {

}
