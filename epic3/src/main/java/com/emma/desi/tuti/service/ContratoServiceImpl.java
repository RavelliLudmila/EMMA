package com.emma.desi.tuti.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emma.desi.tuti.mapper.ContratoMapper;
import com.emma.desi.tuti.validator.ContratoValidator;
import com.emma.desi.tuti.dto.AltaContratoDTO;
import com.emma.desi.tuti.dto.ContratoListadoDTO;
import com.emma.desi.tuti.dto.ModificarContratoDTO;
import com.emma.desi.tuti.model.entity.Contrato;
import com.emma.desi.tuti.model.entity.HistorialEstadoContrato;
import com.emma.desi.tuti.model.entity.Persona;
import com.emma.desi.tuti.model.entity.Propiedad;
import com.emma.desi.tuti.model.enums.EstadoContrato;
import com.emma.desi.tuti.model.enums.EstadoPropiedad;
import com.emma.desi.tuti.exceptions.BusinessException;
import com.emma.desi.tuti.repository.ContratoRepository;
import com.emma.desi.tuti.repository.HistorialEstadoContratoRepository;
import com.emma.desi.tuti.repository.PersonaRepository;
import com.emma.desi.tuti.repository.PropiedadRepository;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Service
public class ContratoServiceImpl implements ContratoService {

    private final ContratoRepository contratoRepository;
    private final PersonaRepository personaRepository;
    private final PropiedadRepository propiedadRepository;
    private final HistorialEstadoContratoRepository historialEstadoContratoRepository;
    private final ContratoValidator contratoValidator;
    private final ContratoMapper contratoMapper;

    public ContratoServiceImpl(
            ContratoRepository contratoRepository,
            PersonaRepository personaRepository,
            PropiedadRepository propiedadRepository,
            HistorialEstadoContratoRepository historialEstadoContratoRepository,
            ContratoValidator contratoValidator,
            ContratoMapper contratoMapper) {
        this.contratoRepository = contratoRepository;
        this.personaRepository = personaRepository;
        this.propiedadRepository = propiedadRepository;
        this.historialEstadoContratoRepository = historialEstadoContratoRepository;
        this.contratoValidator = contratoValidator;
        this.contratoMapper = contratoMapper;
    }

    @Override
    @Transactional
    public Contrato crearContrato(AltaContratoDTO dto) {
        Propiedad propiedad = propiedadRepository.findByIdAndEliminadoFalse(dto.getPropiedadId())
                .orElseThrow(() -> new BusinessException("La propiedad seleccionada no existe o fue eliminada"));
        Persona inquilino = personaRepository.findByIdAndEliminadoFalse(dto.getInquilinoId())
                .orElseThrow(() -> new BusinessException("El inquilino seleccionado no existe o fue eliminado"));

        contratoValidator.validarAlta(dto, propiedad, inquilino);

        Contrato contrato = contratoMapper.toEntity(dto, propiedad, inquilino);
        Contrato contratoGuardado = contratoRepository.save(contrato);

        registrarHistorialEstado(contratoGuardado);

        if (contratoGuardado.getEstado() == EstadoContrato.ACTIVO) {
            propiedad.setEstado(EstadoPropiedad.ALQUILADA);
            propiedadRepository.save(propiedad);
        }

        return contratoGuardado;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContratoListadoDTO> listarContratos(
    		Long propiedadId,
    		Long inquilinoId,
    		EstadoContrato estado,
    		LocalDate fechaInicio) {
        return contratoRepository.buscarListado(propiedadId, inquilinoId, estado, fechaInicio)
                .stream()
                .map(contratoMapper::toListadoDTO)
                .toList();
    }

    @Override
    @Transactional
    public ContratoListadoDTO modificarContratoDTO(Long id, ModificarContratoDTO dto) {
        Contrato contrato = contratoRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new BusinessException("El contrato seleccionado no existe o fue eliminado"));
        Propiedad propiedad = propiedadRepository.findByIdAndEliminadoFalse(dto.getPropiedadId())
                .orElseThrow(() -> new BusinessException("La propiedad seleccionada no existe o fue eliminada"));
        Persona inquilino = personaRepository.findByIdAndEliminadoFalse(dto.getInquilinoId())
                .orElseThrow(() -> new BusinessException("El inquilino seleccionado no existe o fue eliminado"));

        EstadoContrato estadoAnterior = contrato.getEstado();
        Propiedad propiedadAnterior = contrato.getPropiedad();

        contratoValidator.validarModificacion(contrato, dto, propiedad, inquilino);
        contratoMapper.updateEntity(contrato, dto, propiedad, inquilino);

        Contrato contratoGuardado = contratoRepository.save(contrato);

        if (estadoAnterior != contratoGuardado.getEstado()) {
            registrarHistorialEstado(contratoGuardado);
        }

        sincronizarEstadoPropiedades(contratoGuardado, estadoAnterior, propiedadAnterior);

        return contratoMapper.toListadoDTO(contratoGuardado);
    }

    @Override
    @Transactional
    public void eliminarContrato(Long id) {
        Contrato contrato = contratoRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new BusinessException("El contrato seleccionado no existe o ya fue eliminado"));

        contratoValidator.validarEliminacion(contrato);

        contrato.setEliminado(true);
        contrato.setFechaEliminacion(LocalDateTime.now());
        contratoRepository.save(contrato);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Propiedad> listarPropiedadesNoEliminadas() {
        return propiedadRepository.findByEliminadoFalseOrderByDireccionAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Persona> listarPersonasNoEliminadas() {
        return personaRepository.findByEliminadoFalseOrderByApellidoAscNombreAsc();
    }

    private void registrarHistorialEstado(Contrato contrato) {
        HistorialEstadoContrato historial = new HistorialEstadoContrato();
        historial.setContrato(contrato);
        historial.setEstado(contrato.getEstado());
        historial.setFechaCambio(LocalDateTime.now());
        historialEstadoContratoRepository.save(historial);
    }

    private void sincronizarEstadoPropiedades(
            Contrato contrato,
            EstadoContrato estadoAnterior,
            Propiedad propiedadAnterior) {
        Propiedad propiedadActual = contrato.getPropiedad();
        boolean cambioPropiedad = !propiedadAnterior.getId().equals(propiedadActual.getId());

        if (estadoAnterior == EstadoContrato.ACTIVO
                && (contrato.getEstado() != EstadoContrato.ACTIVO || cambioPropiedad)) {
            propiedadAnterior.setEstado(EstadoPropiedad.DISPONIBLE);
            propiedadRepository.save(propiedadAnterior);
        }

        if (contrato.getEstado() == EstadoContrato.ACTIVO) {
            propiedadActual.setEstado(EstadoPropiedad.ALQUILADA);
            propiedadRepository.save(propiedadActual);
        }
    }
}

