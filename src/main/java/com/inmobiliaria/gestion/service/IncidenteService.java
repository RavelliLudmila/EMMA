package com.inmobiliaria.gestion.service;

import com.inmobiliaria.gestion.model.Incidente;
import com.inmobiliaria.gestion.model.enums.CategoriaIncidente;
import com.inmobiliaria.gestion.model.enums.EstadoIncidente;
import com.inmobiliaria.gestion.model.enums.PrioridadIncidente;
import java.util.List;

public interface IncidenteService {
    Incidente crear(Incidente incidente);
    Incidente modificar(Long id, Incidente incidenteModificado);
    void eliminar(Long id);
    List<Incidente> listarTodos();
    List<Incidente> filtrar(Long contratoId, EstadoIncidente estado,
                             CategoriaIncidente categoria, PrioridadIncidente prioridad);
    Incidente buscarPorId(Long id);
}
