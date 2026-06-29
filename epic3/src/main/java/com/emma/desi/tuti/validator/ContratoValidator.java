
package com.emma.desi.tuti.validator;

import org.springframework.stereotype.Component;

import com.emma.desi.tuti.dto.AltaContratoDTO;
import com.emma.desi.tuti.dto.ModificarContratoDTO;
import com.emma.desi.tuti.model.entity.Contrato;
import com.emma.desi.tuti.model.entity.Persona;
import com.emma.desi.tuti.model.entity.Propiedad;
import com.emma.desi.tuti.model.enums.EstadoContrato;
import com.emma.desi.tuti.model.enums.EstadoPropiedad;
import com.emma.desi.tuti.exceptions.BusinessException;
import com.emma.desi.tuti.repository.ContratoRepository;








@Component
public class ContratoValidator {

    private final ContratoRepository contratoRepository;

    public ContratoValidator(ContratoRepository contratoRepository) {
        this.contratoRepository = contratoRepository;
    }

    public void validarAlta(AltaContratoDTO dto, Propiedad propiedad, Persona inquilino) {
        if (propiedad == null) {
            throw new BusinessException("La propiedad seleccionada no existe o fue eliminada");
        }
        if (inquilino == null) {
            throw new BusinessException("El inquilino seleccionado no existe o fue eliminado");
        }
        if (dto.getEstado() == EstadoContrato.ACTIVO) {
            validarAltaActiva(propiedad);
        }
    }

    public void validarEliminacion(Contrato contrato) {
        if (contrato.getEstado() != EstadoContrato.BORRADOR) {
            throw new BusinessException("Solo pueden eliminarse contratos en estado borrador");
        }
    }

    public void validarModificacion(Contrato contrato, ModificarContratoDTO dto, Propiedad propiedad, Persona inquilino) {
        if (propiedad == null) {
            throw new BusinessException("La propiedad seleccionada no existe o fue eliminada");
        }
        if (inquilino == null) {
            throw new BusinessException("El inquilino seleccionado no existe o fue eliminado");
        }

        validarCambioEstado(contrato.getEstado(), dto.getEstado());

        if (dto.getEstado() == EstadoContrato.ACTIVO) {
            validarContratoActivoEnModificacion(contrato, propiedad);
        }
    }

    private void validarAltaActiva(Propiedad propiedad) {
        if (propiedad.getEstado() != EstadoPropiedad.DISPONIBLE) {
            throw new BusinessException("No se puede crear un contrato activo si la propiedad no esta disponible");
        }

        boolean tieneContratoActivo = contratoRepository.existsByPropiedadIdAndEstadoAndEliminadoFalse(propiedad.getId(), EstadoContrato.ACTIVO);
        if (tieneContratoActivo) {
            throw new BusinessException("La propiedad ya tiene un contrato activo");
        }
    }

    private void validarCambioEstado(EstadoContrato estadoActual, EstadoContrato estadoNuevo) {
        boolean cambioPermitido = switch (estadoActual) {
            case BORRADOR -> estadoNuevo == EstadoContrato.BORRADOR || estadoNuevo == EstadoContrato.ACTIVO;
            case ACTIVO -> estadoNuevo == EstadoContrato.ACTIVO
                    || estadoNuevo == EstadoContrato.FINALIZADO
                    || estadoNuevo == EstadoContrato.RESCINDIDO;
            case FINALIZADO -> estadoNuevo == EstadoContrato.FINALIZADO;
            case RESCINDIDO -> estadoNuevo == EstadoContrato.RESCINDIDO;
        };

        if (!cambioPermitido) {
            throw new BusinessException("Cambio de estado no permitido: " + estadoActual.toJson()
                    + " -> " + estadoNuevo.toJson());
        }
    }

    private void validarContratoActivoEnModificacion(Contrato contrato, Propiedad propiedad) {
        boolean existeOtroActivo = contratoRepository.existsByPropiedadIdAndEstadoAndEliminadoFalseAndIdNot(
                propiedad.getId(),
                EstadoContrato.ACTIVO,
                contrato.getId());
        if (existeOtroActivo) {
            throw new BusinessException("No se puede activar el contrato porque la propiedad ya tiene otro contrato activo");
        }

        boolean mismaPropiedad = contrato.getPropiedad().getId().equals(propiedad.getId());
        boolean yaEstabaActivoEnMismaPropiedad = contrato.getEstado() == EstadoContrato.ACTIVO && mismaPropiedad;
        if (!yaEstabaActivoEnMismaPropiedad && propiedad.getEstado() != EstadoPropiedad.DISPONIBLE) {
            throw new BusinessException("No se puede activar el contrato si la propiedad no esta disponible");
        }
    }
}