package com.inmobiliaria.gestion.controller;

import com.inmobiliaria.gestion.model.Contrato;
import com.inmobiliaria.gestion.model.Incidente;
import com.inmobiliaria.gestion.model.enums.CategoriaIncidente;
import com.inmobiliaria.gestion.model.enums.EstadoIncidente;
import com.inmobiliaria.gestion.model.enums.PrioridadIncidente;
import com.inmobiliaria.gestion.repository.ContratoRepository;
import com.inmobiliaria.gestion.service.IncidenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/incidentes")
public class IncidenteController {

    @Autowired
    private IncidenteService incidenteService;

    @Autowired
    private ContratoRepository contratoRepository;

    // Listado
    @GetMapping
    public String listar(
            @RequestParam(required = false) Long contratoId,
            @RequestParam(required = false) EstadoIncidente estado,
            @RequestParam(required = false) CategoriaIncidente categoria,
            @RequestParam(required = false) PrioridadIncidente prioridad,
            Model model) {

        List<Incidente> incidentes;
        boolean hayFiltros = contratoId != null || estado != null
                || categoria != null || prioridad != null;

        if (hayFiltros) {
            incidentes = incidenteService.filtrar(contratoId, estado, categoria, prioridad);
        } else {
            incidentes = incidenteService.listarTodos();
        }

        model.addAttribute("incidentes", incidentes);
        model.addAttribute("contratos", contratoRepository.findByEliminadoFalse());
        model.addAttribute("estados", EstadoIncidente.values());
        model.addAttribute("categorias", CategoriaIncidente.values());
        model.addAttribute("prioridades", PrioridadIncidente.values());
        model.addAttribute("filtroContratoId", contratoId);
        model.addAttribute("filtroEstado", estado);
        model.addAttribute("filtroCategoria", categoria);
        model.addAttribute("filtroPrioridad", prioridad);

        return "incidente/listado";
    }

    // Alta
    @GetMapping("/nuevo")
    public String formularioAlta(Model model) {
        model.addAttribute("incidente", new Incidente());
        cargarDatosFormulario(model);
        model.addAttribute("accion", "alta");
        return "incidente/formulario";
    }

    @PostMapping("/nuevo")
    public String guardarAlta(
            @ModelAttribute Incidente incidente,
            @RequestParam Long contratoId,
            RedirectAttributes redirectAttributes) {
        try {
            Contrato contrato = contratoRepository.findById(contratoId)
                    .orElseThrow(() -> new IllegalArgumentException("Contrato no encontrado."));
            incidente.setContrato(contrato);
            incidenteService.crear(incidente);
            redirectAttributes.addFlashAttribute("mensajeExito", "Incidente registrado exitosamente.");
            return "redirect:/incidentes";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/incidentes/nuevo";
        }
    }

    // Edicion
    @GetMapping("/{id}/editar")
    public String formularioEdicion(@PathVariable Long id, Model model,
                                     RedirectAttributes redirectAttributes) {
        try {
            Incidente incidente = incidenteService.buscarPorId(id);
            model.addAttribute("incidente", incidente);
            cargarDatosFormulario(model);
            model.addAttribute("accion", "edicion");
            return "incidente/formulario";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/incidentes";
        }
    }

    @PostMapping("/{id}/editar")
    public String guardarEdicion(
            @PathVariable Long id,
            @ModelAttribute Incidente incidente,
            @RequestParam Long contratoId,
            RedirectAttributes redirectAttributes) {
        try {
            Contrato contrato = contratoRepository.findById(contratoId)
                    .orElseThrow(() -> new IllegalArgumentException("Contrato no encontrado."));
            incidente.setContrato(contrato);
            incidenteService.modificar(id, incidente);
            redirectAttributes.addFlashAttribute("mensajeExito", "Incidente modificado exitosamente.");
            return "redirect:/incidentes";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/incidentes/" + id + "/editar";
        }
    }

    // Eliminacion
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            incidenteService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Incidente eliminado correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/incidentes";
    }

    private void cargarDatosFormulario(Model model) {
        model.addAttribute("contratos", contratoRepository.findByEliminadoFalse());
        model.addAttribute("estados", EstadoIncidente.values());
        model.addAttribute("categorias", CategoriaIncidente.values());
        model.addAttribute("prioridades", PrioridadIncidente.values());
    }
}
