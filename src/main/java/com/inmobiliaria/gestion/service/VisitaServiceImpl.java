package com.inmobiliaria.gestion.service;

import com.inmobiliaria.gestion.model.Visita;
import com.inmobiliaria.gestion.model.enums.EstadoVisita;
import com.inmobiliaria.gestion.model.enums.EstadoPublicacion;
import com.inmobiliaria.gestion.repository.PublicacionRepository;
import com.inmobiliaria.gestion.repository.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class VisitaServiceImpl implements VisitaService {

    @Autowired
    private VisitaRepository visitaRepository;

    @Autowired
    private PublicacionRepository publicacionRepository;

    // Alta
    @Override
    @Transactional
    public Visita crear(Visita visita) {
        validarCamposObligatorios(visita);

        // Solo se puede crear visita para publicaciones activas
        if (visita.getPublicacion().getEstadoPublicacion() != EstadoPublicacion.ACTIVA) {
            throw new IllegalArgumentException(
                    "Solo se pueden registrar visitas para publicaciones activas.");
        }

        if (visita.getFechaHora() == null) {
            throw new IllegalArgumentException("La fecha y hora son requeridas.");
        }

        visita.setEstado(EstadoVisita.PENDIENTE);
        return visitaRepository.save(visita);
    }

    // Modificacion
    @Override
    @Transactional
    public Visita modificar(Long id, Visita visitaModificada) {
        Visita existente = buscarPorId(id);

        if (existente.getEstado() == EstadoVisita.CANCELADA) {
            throw new IllegalArgumentException("No se puede modificar una visita cancelada.");
        }
        if (existente.getEstado() == EstadoVisita.REALIZADA) {
            throw new IllegalArgumentException("No se puede modificar una visita ya realizada.");
        }

        if (visitaModificada.getFechaHora() == null) {
            throw new IllegalArgumentException("La fecha y hora son requeridas.");
        }

        existente.setFechaHora(visitaModificada.getFechaHora());

        if (visitaModificada.getEstado() != null &&
                visitaModificada.getEstado() != existente.getEstado()) {
            existente.setEstado(visitaModificada.getEstado());
        }

        return visitaRepository.save(existente);
    }

    // Eliminacion logica
    @Override
    @Transactional
    public void eliminar(Long id) {
        Visita visita = buscarPorId(id);
        if (visita.getEstado() == EstadoVisita.REALIZADA) {
            throw new IllegalArgumentException("No se puede eliminar una visita ya realizada.");
        }
        visita.setEliminada(true);
        visitaRepository.save(visita);
    }

    // Listado
    @Override
    public List<Visita> listarTodas() {
        return visitaRepository.findByEliminadaFalse();
    }

    @Override
    public List<Visita> filtrar(Long publicacionId, EstadoVisita estado) {
        return visitaRepository.filtrarVisitas(publicacionId, estado);
    }

    @Override
    public Visita buscarPorId(Long id) {
        return visitaRepository.findById(id)
                .filter(v -> !v.isEliminada())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Visita no encontrada con id: " + id));
    }

    private void validarCamposObligatorios(Visita visita) {
        if (visita.getPublicacion() == null) {
            throw new IllegalArgumentException("La publicacion es requerida.");
        }
        if (visita.getFechaHora() == null) {
            throw new IllegalArgumentException("La fecha y hora son requeridas.");
        }
    }
}
