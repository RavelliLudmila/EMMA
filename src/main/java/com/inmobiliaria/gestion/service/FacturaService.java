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
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private ContratoRepository contratoRepository;


    // ─────────────────────────────────────────────
    // 4.1 ALTA
    // ─────────────────────────────────────────────

    @Transactional
    public Factura crear(Factura factura) {
        validarAlta(factura);

        factura.setEstado(EstadoFactura.PENDIENTE);
        factura.limpiarDatosPago();

        Factura guardada = facturaRepository.save(factura);

        // Registrar historial
        guardada.getHistorialEstados().add(new HistorialEstadoFactura(guardada, EstadoFactura.PENDIENTE));
        return facturaRepository.save(guardada);
    }

    private void validarAlta(Factura factura) {
        if (factura.getContrato() == null) {
            throw new IllegalArgumentException("Debe seleccionar un contrato.");
        }

        Contrato contrato = factura.getContrato();

        // No puede ser contrato finalizado, rescindido, eliminado o borrador
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

        // Validar fechas
        if (factura.getFechaEmision() == null) {
            throw new IllegalArgumentException("La fecha de emisión es requerida.");
        }
        if (factura.getFechaVencimiento() == null) {
            throw new IllegalArgumentException("La fecha de vencimiento es requerida.");
        }
        if (factura.getFechaVencimiento().isBefore(factura.getFechaEmision())) {
            throw new IllegalArgumentException("La fecha de vencimiento debe ser igual o posterior a la fecha de emisión.");
        }

        // Validar importe
        if (factura.getImporte() == null || factura.getImporte().signum() <= 0) {
            throw new IllegalArgumentException("El importe debe ser un número positivo.");
        }
    }

    // ─────────────────────────────────────────────
    // 4.2 MODIFICACIÓN
    // ─────────────────────────────────────────────

    @Transactional
    public Factura modificar(Long id, Factura datosNuevos) {
        Factura factura = obtenerPorId(id);

        // No se puede modificar anulada ni pagada
        if (factura.getEstado() == EstadoFactura.ANULADA) {
            throw new IllegalArgumentException("No se puede modificar una factura anulada.");
        }
        if (factura.getEstado() == EstadoFactura.PAGADA) {
            throw new IllegalArgumentException("No se puede modificar una factura pagada.");
        }

        // Validar transición de estado
        EstadoFactura estadoActual = factura.getEstado();
        EstadoFactura estadoNuevo = datosNuevos.getEstado();
        if (estadoNuevo != null && estadoNuevo != estadoActual) {
            validarTransicionEstado(estadoActual, estadoNuevo);
        }

        // Validar fecha vencimiento
        LocalDate emision = datosNuevos.getFechaEmision() != null
                ? datosNuevos.getFechaEmision() : factura.getFechaEmision();
        LocalDate vencimiento = datosNuevos.getFechaVencimiento() != null
                ? datosNuevos.getFechaVencimiento() : factura.getFechaVencimiento();
        if (vencimiento.isBefore(emision)) {
            throw new IllegalArgumentException("La fecha de vencimiento debe ser igual o posterior a la fecha de emisión.");
        }

        // Validar importe
        if (datosNuevos.getImporte() != null && datosNuevos.getImporte().signum() <= 0) {
            throw new IllegalArgumentException("El importe debe ser un número positivo.");
        }

        // Actualizar campos editables
        factura.setConceptoFacturado(datosNuevos.getConceptoFacturado());
        factura.setFechaEmision(emision);
        factura.setFechaVencimiento(vencimiento);

        if (datosNuevos.getImporte() != null) {
            factura.setImporte(datosNuevos.getImporte());
        }

        // Cambio de estado
        if (estadoNuevo != null && estadoNuevo != estadoActual) {
            // Si pasa a PAGADA, requiere datos de pago
            if (estadoNuevo == EstadoFactura.PAGADA) {
                if (datosNuevos.getFechaPago() == null) {
                    throw new IllegalArgumentException("Debe ingresar la fecha de pago.");
                }
                if (datosNuevos.getMedioPago() == null) {
                    throw new IllegalArgumentException("Debe seleccionar el medio de pago.");
                }
                if (datosNuevos.getImportePagado() == null || datosNuevos.getImportePagado().signum() <= 0) {
                    throw new IllegalArgumentException("El importe pagado debe ser un número positivo.");
                }
                factura.setFechaPago(datosNuevos.getFechaPago());
                factura.setMedioPago(datosNuevos.getMedioPago());
                factura.setImportePagado(datosNuevos.getImportePagado());
                factura.setInteres(datosNuevos.getInteres());
            } else {
                // Si no es PAGADA, limpiar datos de pago
                factura.limpiarDatosPago();
            }

            // Registrar historial
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
                "Transición de estado inválida: no se puede pasar de " +
                actual.getDescripcion() + " a " + nuevo.getDescripcion() + ".");
        }
    }

    // ─────────────────────────────────────────────
    // 4.3 ELIMINACIÓN LÓGICA
    // ─────────────────────────────────────────────

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
    // 4.4 LISTADO Y BÚSQUEDA
    // ─────────────────────────────────────────────

    public List<Factura> listarTodas() {
        return facturaRepository.findByEliminadoFalse();
    }

    public List<Factura> buscarConFiltros(Long contratoId, Long propiedadId,
                                          Long inquilinoId, EstadoFactura estado,
                                          LocalDate fechaDesde, LocalDate fechaHasta) {
        return facturaRepository.buscarConFiltros(
                contratoId, propiedadId, inquilinoId, estado, fechaDesde, fechaHasta);
    }

    public Factura obtenerPorId(Long id) {
        return facturaRepository.findById(id)
                .filter(f -> !f.isEliminado())
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada con id: " + id));
    }

    // ─────────────────────────────────────────────
    // AUXILIARES
    // ─────────────────────────────────────────────

    public List<Contrato> listarContratosActivos() {
        return contratoRepository.findByEstadoAndEliminadoFalse(EstadoContrato.ACTIVO);
    }

    public List<Contrato> listarTodosContratos() {
        return contratoRepository.findByEliminadoFalse();
    }
}
