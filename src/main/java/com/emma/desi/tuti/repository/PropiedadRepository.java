package com.emma.desi.tuti.repository;
import com.emma.desi.tuti.model.entity.Propiedad;
import com.emma.desi.tuti.model.enums.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface PropiedadRepository extends JpaRepository<Propiedad,Long> {
	
	List<Propiedad> findByEliminadaFalse(); //Lsitamos las propiedades que no estan "eliminadas"
	
	boolean existDireccion(String direccion, Long idCiudad); //Chequeamos quie no exista una propiedad con la misma direccion
	
	@Query("SELECT p FROM Propiedad p WHERE p.eliminada = false " +
	           "AND (:direccion IS NULL OR p.direccion LIKE %:direccion%) " +
	           "AND (:ciudadId IS NULL OR p.ciudad.id = :ciudadId) " +
	           "AND (:tipo IS NULL OR p.tipo = :tipo) " +
	           "AND (:estado IS NULL OR p.estadoDisponibilidad = :estado)")
	    List<Propiedad> filtrarPropiedades(
	        @Param("direccion") String direccion,
	        @Param("ciudadId") Long ciudadId,
	        @Param("tipo") TipoPropiedad tipo,
	        @Param("estado") EstadoDisponibilidad estado
	    );
}
