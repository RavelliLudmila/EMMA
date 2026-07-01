package com.inmobiliaria.gestion.service;

import com.inmobiliaria.gestion.model.Contrato;
import com.inmobiliaria.gestion.model.HistorialEstadoContrato;
import com.inmobiliaria.gestion.model.Persona;
import com.inmobiliaria.gestion.model.Propiedad;
import com.inmobiliaria.gestion.model.enums.EstadoContrato;
import com.inmobiliaria.gestion.model.enums.EstadoDisponibilidad;
import com.inmobiliaria.gestion.repository.ContratoRepository;
import com.inmobiliaria.gestion.repository.HistorialEstadoContratoRepository;
import com.inmobiliaria.gestion.repository.PersonaRepository;
import com.inmobiliaria.gestion.repository.PropiedadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContratoServiceImpl implements ContratoService {

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Autowired
    private HistorialEstadoContratoRepository historialRepository;

    // HU 3.1 Alta
    @Transactional
    public Contrato crear(Contrato contrato) {
        Propiedad propiedad = contrato.getPropiedad();

        // Si se crea como ACTIVO, validar que la propiedad esté disponible
        if (contrato.getEstado() == EstadoContrato.ACTIVO) {
            if (propiedad.getEstadoDisponibilidad() != EstadoDisponibilidad.DISPONIBLE) {
                throw new IllegalArgumentException(
                        "No se puede crear un contrato activo si la propiedad no esta disponible.");
            }
            if (contratoRepository.existsByPropiedadIdAndEstadoAndEliminadoFalse(
                    propiedad.getId(), EstadoContrato.ACTIVO)) {
                throw new IllegalArgumentException(
                        "La propiedad ya tiene un contrato activo.");
            }
        }

        if (contrato.getEstado() == null) {
            contrato.setEstado(EstadoContrato.BORRADOR);
        }

        Contrato guardado = contratoRepository.save(contrato);
        registrarHistorial(guardado);

        // Si es ACTIVO, marcar propiedad como ALQUILADA
        if (guardado.getEstado() == EstadoContrato.ACTIVO) {
            propiedad.setEstadoDisponibilidad(EstadoDisponibilidad.ALQUILADA);
            propiedadRepository.save(propiedad);
        }

        return guardado;
    }

    // HU 3.3 Modificacion
    @Transactional
    public Contrato modificar(Long id, Contrato contratoModificado) {
        Contrato existente = buscarPorId(id);
        EstadoContrato estadoAnterior = existente.getEstado();
        Propiedad propiedadAnterior = existente.getPropiedad();

        // Validar transicion de estado
        validarTransicionEstado(estadoAnterior, contratoModificado.getEstado());

        // Validar si se activa
        if (contratoModificado.getEstado() == EstadoContrato.ACTIVO) {
            Propiedad propiedadNueva = contratoModificado.getPropiedad();
            boolean mismaPropiedad = propiedadAnterior.getId().equals(propiedadNueva.getId());
            boolean yaEstabaActivo = estadoAnterior == EstadoContrato.ACTIVO && mismaPropiedad;

            if (!yaEstabaActivo) {
                if (propiedadNueva.getEstadoDisponibilidad() != EstadoDisponibilidad.DISPONIBLE) {
                    throw new IllegalArgumentException(
                            "No se puede activar el contrato si la propiedad no esta disponible.");
                }
                if (contratoRepository.existsByPropiedadIdAndEstadoAndEliminadoFalseAndIdNot(
                        propiedadNueva.getId(), EstadoContrato.ACTIVO, id)) {
                    throw new IllegalArgumentException(
                            "La propiedad ya tiene otro contrato activo.");
                }
            }
        }

        // Actualizar campos
        existente.setPropiedad(contratoModificado.getPropiedad());
        existente.setInquilino(contratoModificado.getInquilino());
        existente.setFechaInicio(contratoModificado.getFechaInicio());
        existente.setDuracionMeses(contratoModificado.getDuracionMeses());
        existente.setImporteMensual(contratoModificado.getImporteMensual());
        existente.setDiaVencimientoMensual(contratoModificado.getDiaVencimientoMensual());
        existente.setDescripcion(contratoModificado.getDescripcion());

        // Cambio de estado
        if (estadoAnterior != contratoModificado.getEstado()) {
            existente.setEstado(contratoModificado.getEstado());
            registrarHistorial(existente);
        }

        Contrato guardado = contratoRepository.save(existente);

        // Sincronizar estado de propiedades
        sincronizarEstadoPropiedades(guardado, estadoAnterior, propiedadAnterior);

        return guardado;
    }

    // HU 3.2 Eliminacion logica
    @Transactional
    public void eliminar(Long id) {
        Contrato contrato = buscarPorId(id);
        if (contrato.getEstado() != EstadoContrato.BORRADOR) {
            throw new IllegalArgumentException(
                    "Solo pueden eliminarse contratos en estado BORRADOR.");
        }
        contrato.setEliminado(true);
        contrato.setFechaEliminacion(LocalDateTime.now());
        contratoRepository.save(contrato);
    }

    // HU 3.4 Listado
    public List<Contrato> listarTodos() {
        return contratoRepository.findByEliminadoFalse();
    }

    public List<Contrato> buscarConFiltros(Long propiedadId, Long inquilinoId,
                                            EstadoContrato estado, LocalDate fechaInicio) {
        return contratoRepository.buscarListado(propiedadId, inquilinoId, estado, fechaInicio);
    }

    public Contrato buscarPorId(Long id) {
        return contratoRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Contrato no encontrado con id: " + id));
    }

    public List<Propiedad> listarPropiedadesNoEliminadas() {
        return propiedadRepository.findByEliminadaFalse();
    }

    public List<Persona> listarPersonasNoEliminadas() {
        return personaRepository.findByEliminadoFalse();
    }

    // Auxiliares
    private void registrarHistorial(Contrato contrato) {
        HistorialEstadoContrato historial = new HistorialEstadoContrato();
        historial.setContrato(contrato);
        historial.setEstado(contrato.getEstado());
        historial.setFechaCambio(LocalDateTime.now());
        historialRepository.save(historial);
    }

    private void validarTransicionEstado(EstadoContrato actual, EstadoContrato nuevo) {
        boolean valida = switch (actual) {
            case BORRADOR -> nuevo == EstadoContrato.BORRADOR || nuevo == EstadoContrato.ACTIVO;
            case ACTIVO -> nuevo == EstadoContrato.ACTIVO
                    || nuevo == EstadoContrato.FINALIZADO
                    || nuevo == EstadoContrato.RESCINDIDO;
            case FINALIZADO -> nuevo == EstadoContrato.FINALIZADO;
            case RESCINDIDO -> nuevo == EstadoContrato.RESCINDIDO;
        };
        if (!valida) {
            throw new IllegalArgumentException(
                    "Transicion de estado no permitida: "
                    + actual.getDescripcion() + " -> " + nuevo.getDescripcion());
        }
    }

    private void sincronizarEstadoPropiedades(Contrato contrato,
                                               EstadoContrato estadoAnterior,
                                               Propiedad propiedadAnterior) {
        Propiedad propiedadActual = contrato.getPropiedad();
        boolean cambioPropiedad = !propiedadAnterior.getId().equals(propiedadActual.getId());

        if (estadoAnterior == EstadoContrato.ACTIVO
                && (contrato.getEstado() != EstadoContrato.ACTIVO || cambioPropiedad)) {
            propiedadAnterior.setEstadoDisponibilidad(EstadoDisponibilidad.DISPONIBLE);
            propiedadRepository.save(propiedadAnterior);
        }

        if (contrato.getEstado() == EstadoContrato.ACTIVO) {
            propiedadActual.setEstadoDisponibilidad(EstadoDisponibilidad.ALQUILADA);
            propiedadRepository.save(propiedadActual);
        }

        if (contrato.getEstado() == EstadoContrato.FINALIZADO
                || contrato.getEstado() == EstadoContrato.RESCINDIDO) {
            propiedadActual.setEstadoDisponibilidad(EstadoDisponibilidad.DISPONIBLE);
            propiedadRepository.save(propiedadActual);
        }
    }
}
