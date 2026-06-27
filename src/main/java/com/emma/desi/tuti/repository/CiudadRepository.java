package com.emma.desi.tuti.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.emma.desi.tuti.model.entity.Ciudad;

@Repository
public interface CiudadRepository extends JpaRepository<Ciudad,Long> {

}
