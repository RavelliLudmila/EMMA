package com.inmobiliaria.gestion.service;

import com.inmobiliaria.gestion.model.Propiedad;
import com.inmobiliaria.gestion.model.enums.EstadoDisponibilidad;
import com.inmobiliaria.gestion.model.enums.TipoPropiedad;
import java.util.List;

public interface PropiedadService {
    Propiedad guardar(Propiedad propiedad);
    void eliminar(Long id);
    Propiedad actualizar(Long id, Propiedad propiedadModificada);
    List<Propiedad> listarActivas();
    List<Propiedad> filtrar(String direccion, Long ciudadId, TipoPropiedad tipo, EstadoDisponibilidad estado);
    Propiedad buscarPorId(Long id);
}
