package com.emma.desi.tuti.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.emma.desi.tuti.model.entity.Provincia;

@Repository
public interface ProvinciaRepository extends JpaRepository<Provincia,Long> {

}
