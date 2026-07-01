package com.inmobiliaria.gestion.service;

import com.inmobiliaria.gestion.model.Publicacion;
import com.inmobiliaria.gestion.model.enums.EstadoPublicacion;
import java.math.BigDecimal;
import java.util.List;

public interface PublicacionService {
    Publicacion guardar(Publicacion publicacion);
    void eliminar(Long id);
    Publicacion actualizar(Long id, Publicacion publicacionModificada);
    List<Publicacion> listarActivas();
    List<Publicacion> filtrar(Long propiedadId, Long ciudadId, EstadoPublicacion estado,
                               BigDecimal precioMin, BigDecimal precioMax);
    Publicacion buscarPorId(Long id);
}
