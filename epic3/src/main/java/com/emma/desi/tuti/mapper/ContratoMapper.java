package com.emma.desi.tuti.mapper;

import org.springframework.stereotype.Component;

import com.emma.desi.tuti.dto.AltaContratoDTO;
import com.emma.desi.tuti.dto.ContratoListadoDTO;
import com.emma.desi.tuti.dto.ModificarContratoDTO;
import com.emma.desi.tuti.model.entity.Contrato;
import com.emma.desi.tuti.model.entity.Persona;
import com.emma.desi.tuti.model.entity.Propiedad;
import com.emma.desi.tuti.model.enums.EstadoContrato;


@Component
public class ContratoMapper {

    public Contrato toEntity(AltaContratoDTO dto, Propiedad propiedad, Persona inquilino) {
        Contrato contrato = new Contrato();
        contrato.setPropiedad(propiedad);
        contrato.setInquilino(inquilino);
        contrato.setFechaInicio(dto.getFechaInicio());
        contrato.setDuracionMeses(dto.getDuracionMeses());
        contrato.setImporteMensual(dto.getImporteMensual());
        contrato.setDiaVencimientoMensual(dto.getDiaVencimientoMensual());
        contrato.setDescripcion(dto.getDescripcion());
        contrato.setEstado(dto.getEstado());
        return contrato;
    }

    public void updateEntity(Contrato contrato, ModificarContratoDTO dto, Propiedad propiedad, Persona inquilino) {
        contrato.setPropiedad(propiedad);
        contrato.setInquilino(inquilino);
        contrato.setFechaInicio(dto.getFechaInicio());
        contrato.setDuracionMeses(dto.getDuracionMeses());
        contrato.setImporteMensual(dto.getImporteMensual());
        contrato.setDiaVencimientoMensual(dto.getDiaVencimientoMensual());
        contrato.setDescripcion(dto.getDescripcion());
        contrato.setEstado(dto.getEstado());
    }

    public ContratoListadoDTO toListadoDTO(Contrato contrato) {
        ContratoListadoDTO dto = new ContratoListadoDTO();
        dto.setId(contrato.getId());
        dto.setPropiedadId(contrato.getPropiedad().getId());
        dto.setPropiedadDireccion(contrato.getPropiedad().getDireccion());
        dto.setPropietarioId(idPersona(contrato.getPropiedad().getPropietario()));
        dto.setPropietarioNombreCompleto(nombreCompleto(contrato.getPropiedad().getPropietario()));
        dto.setInquilinoId(contrato.getInquilino().getId());
        dto.setInquilinoNombreCompleto(nombreCompleto(contrato.getInquilino()));
        dto.setFechaInicio(contrato.getFechaInicio());
        dto.setDuracionMeses(contrato.getDuracionMeses());
        dto.setImporteMensual(contrato.getImporteMensual());
        dto.setDiaVencimientoMensual(contrato.getDiaVencimientoMensual());
        dto.setDescripcion(contrato.getDescripcion());
        dto.setEstado(contrato.getEstado());
        dto.setPuedeModificar(true);
        dto.setPuedeEliminar(contrato.getEstado() == EstadoContrato.BORRADOR);
        dto.setUrlModificacion("/api/contratos/" + contrato.getId());
        dto.setUrlEliminacion("/api/contratos/" + contrato.getId());
        return dto;
    }

    private Long idPersona(Persona persona) {
        return persona == null ? null : persona.getId();
    }

    private String nombreCompleto(Persona persona) {
        if (persona == null) {
            return null;
        }
        return persona.getApellido() + ", " + persona.getNombre();
    }
}




