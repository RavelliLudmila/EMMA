package com.inmobiliaria.gestion.service;

import com.inmobiliaria.gestion.model.Incidente;
import com.inmobiliaria.gestion.model.enums.CategoriaIncidente;
import com.inmobiliaria.gestion.model.enums.EstadoIncidente;
import com.inmobiliaria.gestion.model.enums.PrioridadIncidente;
import com.inmobiliaria.gestion.repository.ContratoRepository;
import com.inmobiliaria.gestion.repository.IncidenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class IncidenteServiceImpl implements IncidenteService {

    @Autowired
    private IncidenteRepository incidenteRepository;

    @Autowired
    private ContratoRepository contratoRepository;

    // Alta
    @Override
    @Transactional
    public Incidente crear(Incidente incidente) {
        validarCamposObligatorios(incidente);

        if (incidente.getEstado() == null) {
            incidente.setEstado(EstadoIncidente.ABIERTO);
        }

        incidente.cambiarEstado(incidente.getEstado());
        return incidenteRepository.save(incidente);
    }

    // Modificacion
    @Override
    @Transactional
    public Incidente modificar(Long id, Incidente incidenteModificado) {
        Incidente existente = buscarPorId(id);

        if (existente.getEstado() == EstadoIncidente.CANCELADO) {
            throw new IllegalArgumentException("No se puede modificar un incidente cancelado.");
        }

        validarCamposObligatorios(incidenteModificado);

        EstadoIncidente estadoAnterior = existente.getEstado();
        EstadoIncidente estadoNuevo = incidenteModificado.getEstado();

        existente.setTitulo(incidenteModificado.getTitulo());
        existente.setDescripcion(incidenteModificado.getDescripcion());
        existente.setCategoria(incidenteModificado.getCategoria());
        existente.setPrioridad(incidenteModificado.getPrioridad());
        existente.setResponsableTecnico(incidenteModificado.getResponsableTecnico());
        existente.setObservacionesResolucion(incidenteModificado.getObservacionesResolucion());
        existente.setCostoResolucion(incidenteModificado.getCostoResolucion());

        if (estadoNuevo != null && estadoNuevo != estadoAnterior) {
            // Si pasa a RESUELTO, registrar fecha resolucion
            if (estadoNuevo == EstadoIncidente.RESUELTO) {
                existente.setFechaResolucion(LocalDateTime.now());
            }
            existente.cambiarEstado(estadoNuevo);
        }

        return incidenteRepository.save(existente);
    }

    // Eliminacion logica
    @Override
    @Transactional
    public void eliminar(Long id) {
        Incidente incidente = buscarPorId(id);
        if (incidente.getEstado() == EstadoIncidente.EN_PROCESO) {
            throw new IllegalArgumentException(
                    "No se puede eliminar un incidente en proceso.");
        }
        incidente.setEliminado(true);
        incidenteRepository.save(incidente);
    }

    // Listado
    @Override
    public List<Incidente> listarTodos() {
        return incidenteRepository.findByEliminadoFalse();
    }

    @Override
    public List<Incidente> filtrar(Long contratoId, EstadoIncidente estado,
                                    CategoriaIncidente categoria, PrioridadIncidente prioridad) {
        return incidenteRepository.filtrarIncidentes(contratoId, estado, categoria, prioridad);
    }

    @Override
    public Incidente buscarPorId(Long id) {
        return incidenteRepository.findById(id)
                .filter(i -> !i.isEliminado())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Incidente no encontrado con id: " + id));
    }

    private void validarCamposObligatorios(Incidente incidente) {
        if (incidente.getTitulo() == null || incidente.getTitulo().isBlank()) {
            throw new IllegalArgumentException("El titulo es requerido.");
        }
        if (incidente.getDescripcion() == null || incidente.getDescripcion().isBlank()) {
            throw new IllegalArgumentException("La descripcion es requerida.");
        }
        if (incidente.getCategoria() == null) {
            throw new IllegalArgumentException("La categoria es requerida.");
        }
        if (incidente.getPrioridad() == null) {
            throw new IllegalArgumentException("La prioridad es requerida.");
        }
        if (incidente.getContrato() == null) {
            throw new IllegalArgumentException("El contrato es requerido.");
        }
    }
}
