package com.inmobiliaria.gestion.service;

import com.inmobiliaria.gestion.model.Contrato;
import com.inmobiliaria.gestion.model.Factura;
import com.inmobiliaria.gestion.model.HistorialEstadoFactura;
import com.inmobiliaria.gestion.model.enums.EstadoContrato;
import com.inmobiliaria.gestion.model.enums.EstadoFactura;
import com.inmobiliaria.gestion.repository.ContratoRepository;
import com.inmobiliaria.gestion.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private ContratoRepository contratoRepository;

    // ─────────────────────────────────────────────
    // 4.1 ALTA
    // ─────────────────────────────────────────────

    @Override
    @Transactional
    public Factura crear(Factura factura) {
        validarAlta(factura);

        factura.setEstado(EstadoFactura.PENDIENTE);
        factura.limpiarDatosPago();

        Factura guardada = facturaRepository.save(factura);
        guardada.getHistorialEstados().add(new HistorialEstadoFactura(guardada, EstadoFactura.PENDIENTE));
        return facturaRepository.save(guardada);
    }

    private void validarAlta(Factura factura) {
        if (factura.getContrato() == null) {
            throw new IllegalArgumentException("Debe seleccionar un contrato.");
        }

        Contrato contrato = factura.getContrato();

        if (contrato.isEliminado()) {
            throw new IllegalArgumentException("No se puede crear una factura para un contrato eliminado.");
        }

        EstadoContrato estadoContrato = contrato.getEstado();
        if (estadoContrato == EstadoContrato.FINALIZADO ||
            estadoContrato == EstadoContrato.RESCINDIDO ||
            estadoContrato == EstadoContrato.BORRADOR) {
            throw new IllegalArgumentException(
                "No se puede crear una factura para un contrato en estado " +
                estadoContrato.getDescripcion() + ".");
        }

        if (factura.getFechaEmision() == null) {
            throw new IllegalArgumentException("La fecha de emision es requerida.");
        }
        if (factura.getFechaVencimiento() == null) {
            throw new IllegalArgumentException("La fecha de vencimiento es requerida.");
        }
        if (factura.getFechaVencimiento().isBefore(factura.getFechaEmision())) {
            throw new IllegalArgumentException(
                "La fecha de vencimiento debe ser igual o posterior a la fecha de emision.");
        }
        if (factura.getImporte() == null || factura.getImporte().signum() <= 0) {
            throw new IllegalArgumentException("El importe debe ser un numero positivo.");
        }
    }

    // ─────────────────────────────────────────────
    // 4.2 MODIFICACION
    // ─────────────────────────────────────────────

    @Override
    @Transactional
    public Factura modificar(Long id, Factura datosNuevos) {
        Factura factura = obtenerPorId(id);

        if (factura.getEstado() == EstadoFactura.ANULADA) {
            throw new IllegalArgumentException("No se puede modificar una factura anulada.");
        }
        if (factura.getEstado() == EstadoFactura.PAGADA) {
            throw new IllegalArgumentException("No se puede modificar una factura pagada.");
        }

        EstadoFactura estadoActual = factura.getEstado();
        EstadoFactura estadoNuevo = datosNuevos.getEstado();
        if (estadoNuevo != null && estadoNuevo != estadoActual) {
            validarTransicionEstado(estadoActual, estadoNuevo);
        }

        LocalDate emision = datosNuevos.getFechaEmision() != null
                ? datosNuevos.getFechaEmision() : factura.getFechaEmision();
        LocalDate vencimiento = datosNuevos.getFechaVencimiento() != null
                ? datosNuevos.getFechaVencimiento() : factura.getFechaVencimiento();

        if (vencimiento.isBefore(emision)) {
            throw new IllegalArgumentException(
                "La fecha de vencimiento debe ser igual o posterior a la fecha de emision.");
        }
        if (datosNuevos.getImporte() != null && datosNuevos.getImporte().signum() <= 0) {
            throw new IllegalArgumentException("El importe debe ser un numero positivo.");
        }

        factura.setConceptoFacturado(datosNuevos.getConceptoFacturado());
        factura.setFechaEmision(emision);
        factura.setFechaVencimiento(vencimiento);

        if (datosNuevos.getImporte() != null) {
            factura.setImporte(datosNuevos.getImporte());
        }

        if (estadoNuevo != null && estadoNuevo != estadoActual) {
            if (estadoNuevo == EstadoFactura.PAGADA) {
                if (datosNuevos.getFechaPago() == null) {
                    throw new IllegalArgumentException("Debe ingresar la fecha de pago.");
                }
                if (datosNuevos.getMedioPago() == null) {
                    throw new IllegalArgumentException("Debe seleccionar el medio de pago.");
                }
                if (datosNuevos.getImportePagado() == null ||
                        datosNuevos.getImportePagado().signum() <= 0) {
                    throw new IllegalArgumentException(
                        "El importe pagado debe ser un numero positivo.");
                }
                factura.setFechaPago(datosNuevos.getFechaPago());
                factura.setMedioPago(datosNuevos.getMedioPago());
                factura.setImportePagado(datosNuevos.getImportePagado());
                factura.setInteres(datosNuevos.getInteres());
            } else {
                factura.limpiarDatosPago();
            }

            factura.setEstado(estadoNuevo);
            factura.getHistorialEstados().add(new HistorialEstadoFactura(factura, estadoNuevo));
        }

        return facturaRepository.save(factura);
    }

    private void validarTransicionEstado(EstadoFactura actual, EstadoFactura nuevo) {
        boolean valida = switch (actual) {
            case PENDIENTE -> nuevo == EstadoFactura.PAGADA ||
                              nuevo == EstadoFactura.VENCIDA ||
                              nuevo == EstadoFactura.ANULADA;
            case VENCIDA   -> nuevo == EstadoFactura.PAGADA;
            case PAGADA, ANULADA -> false;
        };
        if (!valida) {
            throw new IllegalArgumentException(
                "Transicion de estado invalida: no se puede pasar de " +
                actual.getDescripcion() + " a " + nuevo.getDescripcion() + ".");
        }
    }

    // ─────────────────────────────────────────────
    // 4.3 ELIMINACION
    // ─────────────────────────────────────────────

    @Override
    @Transactional
    public void eliminar(Long id) {
        Factura factura = obtenerPorId(id);
        if (factura.getEstado() == EstadoFactura.PAGADA) {
            throw new IllegalArgumentException("No se puede eliminar una factura pagada.");
        }
        factura.setEliminado(true);
        facturaRepository.save(factura);
    }

    // ─────────────────────────────────────────────
    // 4.4 LISTADO
    // ─────────────────────────────────────────────

    @Override
    public List<Factura> listarTodas() {
        return facturaRepository.findByEliminadoFalse();
    }

    @Override
    public List<Factura> buscarConFiltros(Long contratoId, Long propiedadId,
                                           Long inquilinoId, EstadoFactura estado,
                                           LocalDate fechaDesde, LocalDate fechaHasta) {
        return facturaRepository.buscarConFiltros(
                contratoId, propiedadId, inquilinoId, estado, fechaDesde, fechaHasta);
    }

    @Override
    public Factura obtenerPorId(Long id) {
        return facturaRepository.findById(id)
                .filter(f -> !f.isEliminado())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Factura no encontrada con id: " + id));
    }

    @Override
    public List<Contrato> listarContratosActivos() {
        return contratoRepository.findByEstadoAndEliminadoFalse(EstadoContrato.ACTIVO);
    }

    @Override
    public List<Contrato> listarTodosContratos() {
        return contratoRepository.findByEliminadoFalse();
    }
}
