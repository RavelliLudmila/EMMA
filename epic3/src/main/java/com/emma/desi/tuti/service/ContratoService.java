package com.emma.desi.tuti.service;


import java.time.LocalDate;
import java.util.List;

import com.emma.desi.tuti.dto.AltaContratoDTO;
import com.emma.desi.tuti.dto.ContratoListadoDTO;
import com.emma.desi.tuti.dto.ModificarContratoDTO;
import com.emma.desi.tuti.model.entity.Contrato;
import com.emma.desi.tuti.model.entity.Persona;
import com.emma.desi.tuti.model.entity.Propiedad;
import com.emma.desi.tuti.model.enums.EstadoContrato;

public interface ContratoService {

    Contrato crearContrato(AltaContratoDTO dto);
    
    ContratoListadoDTO modificarContratoDTO (Long id, ModificarContratoDTO dto);
    
    
    List<ContratoListadoDTO> listarContratos(
    		Long propiedadId,
    		Long inquilinoId,
    		EstadoContrato estado,
    		LocalDate fechaInicio);
    
    void eliminarContrato(Long id);
    
     List<Propiedad> listarPropiedadesNoEliminadas();

    //List<Propiedad> obtenerPropiedadesSeleccionables();

    //List<Persona> obtenerInquilinosSeleccionables();
    
    List<Persona> listarPersonasNoEliminadas();
    
    
}
