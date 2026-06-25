package com.emma.desi.tuti.service;

import com.emma.desi.tuti.model.entity.Propiedad;
import com.emma.desi.tuti.model.enums.*;
import com.emma.desi.tuti.repository.PropiedadRepository;
import com.emma.desi.tuti.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PropiedadServiceImpl implements PropiedadService {
	
	@Autowired
	private PropiedadRepository propiedadRepository;
	
	//Acá iría la verificacion de si hay un contrato. Hardcodeo un false hasta que tenga el service de la epic.
	
	private boolean tieneContratoActivo(Long propiedadId) {return false;}
	
	//H1.1 
    @Transactional
    public Propiedad guardar(Propiedad propiedad) {
		//Verifica que los ambientes sean entero positivo
		if (propiedad.getCantidadAmbientes()<=0) {
			throw new ReglaNegocioException("Los ambientes deben ser enteros positivos.");
		}
		//Verificamos que los m2 sean positivos
		if (propiedad.getMetrosCuadrados()<=0) {
            throw new ReglaNegocioException("Los metros cuadrados deben ser positivos.");
        }
		
		//Chequea propiedads duplicadas 
		if (propiedadRepository.existDireccion(propiedad.getDireccion(),propiedad.getCiudad().getId())) {
			throw new ReglaNegocioException("Ya existe una propiedad en esta direccion.");
		}
		
		//Si no se especifica estado, es disponible
        if (propiedad.getEstadoDisponibilidad()==null) {
            propiedad.setEstadoDisponibilidad(EstadoDisponibilidad.DISPONIBLE);
        }
        
        //Guardar en el historial de estados
        propiedad.cambiarEstado(propiedad.getEstadoDisponibilidad());

        //termina
        return propiedadRepository.save(propiedad);
	}
	
	//H1.2
    @Transactional
    public void eliminar(Long id) {
        Propiedad propiedad=buscarPorId(id);

        //Chequea si hay un contrato vigente.
        if (tieneContratoActivo(id)) {
            throw new ReglaNegocioException("No se puede borrar. La propiedad tiene un contrato vigente."); 
        }

        //Eliminacion logica 
        propiedad.setEliminada(true); 
        propiedadRepository.save(propiedad);
    }
	
	//H1.3
    @Transactional
    public Propiedad actualizar(Long id,Propiedad propiedadModificada) {
        Propiedad propiedadExistente=buscarPorId(id);

        //No permitir duplicados excluyendo la propiedad actual
        if (propiedadRepository.existDireccion(propiedadModificada.getDireccion(),propiedadModificada.getCiudad().getId()) && id!=propiedadModificada.getId()) {
            throw new ReglaNegocioException("Otra propiedad ocupa la direccion."); 
        }

        //No puede cambiar a disponible o inactiva si hay un contrato vigente
        if (tieneContratoActivo(id)) {
            EstadoDisponibilidad nuevoEst=propiedadModificada.getEstadoDisponibilidad();
            if (nuevoEst==EstadoDisponibilidad.DISPONIBLE) {
                throw new ReglaNegocioException("La propiedad tiene un contrato ACTIVO. No es posible pasar a disponible.");
            } else if (nuevoEst==EstadoDisponibilidad.INACTIVA) {
                throw new ReglaNegocioException("La propiedad tiene un contrato ACTIVO. No es posible pasar a inactiva.");
            }
        }

        //Mantener registro histórico de cambios de estado si el estado cambió 
        if (!propiedadExistente.getEstadoDisponibilidad().equals(propiedadModificada.getEstadoDisponibilidad())) {
            propiedadExistente.cambiarEstado(propiedadModificada.getEstadoDisponibilidad()); 
        }

        //Actualiza datos
        propiedadExistente.setCiudad(propiedadModificada.getCiudad());
        propiedadExistente.setPropietario(propiedadModificada.getPropietario());
        propiedadExistente.setDescripcion(propiedadModificada.getDescripcion());
        propiedadExistente.setDireccion(propiedadModificada.getDireccion());
        propiedadExistente.setTipo(propiedadModificada.getTipo());
        propiedadExistente.setCantidadAmbientes(propiedadModificada.getCantidadAmbientes());
        propiedadExistente.setMetrosCuadrados(propiedadModificada.getMetrosCuadrados());
        propiedadExistente.setComodidades(propiedadModificada.getComodidades());
        
        //termina
        return propiedadRepository.save(propiedadExistente);
    }
	
	//H1.4
    public List<Propiedad> listarActivas() {
        //Traer solo las propiedades no eliminadas
        return propiedadRepository.findByEliminadaFalse();
    }
    
    public List<Propiedad> filtrar(String direccion, Long ciudadId, TipoPropiedad tipo, EstadoDisponibilidad estado) {
        //Devuelve por filtros
        return propiedadRepository.filtrarPropiedades(direccion, ciudadId, tipo, estado);
    }

    public Propiedad buscarPorId(Long id) {
    	java.util.Optional<Propiedad> resultado=propiedadRepository.findById(id);
        if (resultado.isEmpty()) { 
            throw new ReglaNegocioException("La propiedad no existe");
        } else {
            return resultado.get();
        }
    }
	
	
	
	
	
	
}
