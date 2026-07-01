package com.inmobiliaria.gestion.controller;

import com.inmobiliaria.gestion.model.Contrato;
import com.inmobiliaria.gestion.model.Factura;
import com.inmobiliaria.gestion.model.enums.EstadoFactura;
import com.inmobiliaria.gestion.model.enums.MedioPago;
import com.inmobiliaria.gestion.repository.ContratoRepository;
import com.inmobiliaria.gestion.repository.PersonaRepository;
import com.inmobiliaria.gestion.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private PersonaRepository personaRepository;

    // ─────────────────────────────────────────────
    // 4.4 LISTADO
    // ─────────────────────────────────────────────

    @GetMapping
    public String listar(
            @RequestParam(required = false) Long contratoId,
            @RequestParam(required = false) Long propiedadId,
            @RequestParam(required = false) Long inquilinoId,
            @RequestParam(required = false) EstadoFactura estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            Model model) {

        List<Factura> facturas;

        boolean hayFiltros = contratoId != null || propiedadId != null ||
                inquilinoId != null || estado != null ||
                fechaDesde != null || fechaHasta != null;

        if (hayFiltros) {
            facturas = facturaService.buscarConFiltros(
                    contratoId, propiedadId, inquilinoId, estado, fechaDesde, fechaHasta);
        } else {
            facturas = facturaService.listarTodas();
        }

        model.addAttribute("facturas", facturas);
        model.addAttribute("contratos", facturaService.listarTodosContratos());
        model.addAttribute("personas", personaRepository.findByEliminadoFalse());
        model.addAttribute("estados", EstadoFactura.values());

        // Para mantener filtros en el formulario
        model.addAttribute("filtroContratoId", contratoId);
        model.addAttribute("filtroPropiedadId", propiedadId);
        model.addAttribute("filtroInquilinoId", inquilinoId);
        model.addAttribute("filtroEstado", estado);
        model.addAttribute("filtroFechaDesde", fechaDesde);
        model.addAttribute("filtroFechaHasta", fechaHasta);

        return "factura/listado";
    }

    // ─────────────────────────────────────────────
    // 4.1 ALTA
    // ─────────────────────────────────────────────

    @GetMapping("/nueva")
    public String formularioAlta(Model model) {
        model.addAttribute("factura", new Factura());
        model.addAttribute("contratos", facturaService.listarContratosActivos());
        model.addAttribute("estados", EstadoFactura.values());
        model.addAttribute("mediosPago", MedioPago.values());
        model.addAttribute("accion", "alta");
        return "factura/formulario";
    }

    @PostMapping("/nueva")
    public String guardarAlta(@ModelAttribute Factura factura,
                               @RequestParam Long contratoId,
                               RedirectAttributes redirectAttributes) {
        try {
            Contrato contrato = contratoRepository.findById(contratoId)
                    .orElseThrow(() -> new IllegalArgumentException("Contrato no encontrado."));
            factura.setContrato(contrato);
            facturaService.crear(factura);
            redirectAttributes.addFlashAttribute("mensajeExito", "Factura creada exitosamente.");
            return "redirect:/facturas";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/facturas/nueva";
        }
    }

    // ─────────────────────────────────────────────
    // 4.2 MODIFICACIÓN
    // ─────────────────────────────────────────────

    @GetMapping("/{id}/editar")
    public String formularioEdicion(@PathVariable Long id, Model model,
                                     RedirectAttributes redirectAttributes) {
        try {
            Factura factura = facturaService.obtenerPorId(id);
            model.addAttribute("factura", factura);
            model.addAttribute("contratos", facturaService.listarContratosActivos());
            model.addAttribute("estados", EstadoFactura.values());
            model.addAttribute("mediosPago", MedioPago.values());
            model.addAttribute("accion", "edicion");
            return "factura/formulario";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/facturas";
        }
    }

    @PostMapping("/{id}/editar")
    public String guardarEdicion(@PathVariable Long id,
                                  @ModelAttribute Factura factura,
                                  RedirectAttributes redirectAttributes) {
        try {
            facturaService.modificar(id, factura);
            redirectAttributes.addFlashAttribute("mensajeExito", "Factura modificada exitosamente.");
            return "redirect:/facturas";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/facturas/" + id + "/editar";
        }
    }

    // ─────────────────────────────────────────────
    // 4.3 ELIMINACIÓN
    // ─────────────────────────────────────────────

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            facturaService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Factura eliminada correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/facturas";
    }
}
