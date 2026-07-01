package com.inmobiliaria.gestion.service;

import com.inmobiliaria.gestion.model.Contrato;
import com.inmobiliaria.gestion.model.Factura;
import com.inmobiliaria.gestion.model.enums.EstadoFactura;
import java.time.LocalDate;
import java.util.List;

public interface FacturaService {

    Factura crear(Factura factura);

    Factura modificar(Long id, Factura datosNuevos);

    void eliminar(Long id);

    List<Factura> listarTodas();

    List<Factura> buscarConFiltros(Long contratoId, Long propiedadId,
                                    Long inquilinoId, EstadoFactura estado,
                                    LocalDate fechaDesde, LocalDate fechaHasta);

    Factura obtenerPorId(Long id);

    List<Contrato> listarContratosActivos();

    List<Contrato> listarTodosContratos();
}
