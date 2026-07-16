package com.inmobiliaria.gestion.service;

import com.inmobiliaria.gestion.model.Visita;
import com.inmobiliaria.gestion.model.enums.EstadoVisita;
import java.util.List;

public interface VisitaService {
    Visita crear(Visita visita);
    Visita modificar(Long id, Visita visitaModificada);
    void eliminar(Long id);
    List<Visita> listarTodas();
    List<Visita> filtrar(Long publicacionId, EstadoVisita estado);
    Visita buscarPorId(Long id);
}
