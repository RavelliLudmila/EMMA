package com.inmobiliaria.gestion.service;

import com.inmobiliaria.gestion.model.Contrato;
import com.inmobiliaria.gestion.model.Persona;
import com.inmobiliaria.gestion.model.Propiedad;
import com.inmobiliaria.gestion.model.enums.EstadoContrato;
import java.time.LocalDate;
import java.util.List;

public interface ContratoService {
    Contrato crear(Contrato contrato);
    Contrato modificar(Long id, Contrato contratoModificado);
    void eliminar(Long id);
    List<Contrato> listarTodos();
    List<Contrato> buscarConFiltros(Long propiedadId, Long inquilinoId,
                                     EstadoContrato estado, LocalDate fechaInicio);
    Contrato buscarPorId(Long id);
    List<Propiedad> listarPropiedadesNoEliminadas();
    List<Persona> listarPersonasNoEliminadas();
}
