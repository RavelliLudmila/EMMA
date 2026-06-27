package com.emma.desi.tuti.service;
import com.emma.desi.tuti.model.entity.Propiedad;
import com.emma.desi.tuti.model.enums.*;
import java.util.List;
public interface PropiedadService {
	// HU 1.1 Guardar una propiedad
	Propiedad guardar(Propiedad propiedad);
	
	// HU 1.2 Eliminar propiedad
	void eliminar(Long id);
	
	// HU 1.3 Actualizacion de propiedad
	Propiedad actualizar(Long id,Propiedad propiedadModificada);
	
	// HU 1.4 Listar propiedades y filtrarlas
	List<Propiedad> listarActivas();       
    List<Propiedad> filtrar(String direccion, Long ciudadId, TipoPropiedad tipo, EstadoDisponibilidad estado); // HU 1.4
    Propiedad buscarPorId(Long id);
	
	
	
	
	
	
}
