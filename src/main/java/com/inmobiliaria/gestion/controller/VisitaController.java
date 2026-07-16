package com.inmobiliaria.gestion.controller;

import com.inmobiliaria.gestion.model.Publicacion;
import com.inmobiliaria.gestion.model.Visita;
import com.inmobiliaria.gestion.model.enums.EstadoVisita;
import com.inmobiliaria.gestion.repository.PublicacionRepository;
import com.inmobiliaria.gestion.service.VisitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/visitas")
public class VisitaController {

    @Autowired
    private VisitaService visitaService;

    @Autowired
    private PublicacionRepository publicacionRepository;

    // Listado
    @GetMapping
    public String listar(
            @RequestParam(required = false) Long publicacionId,
            @RequestParam(required = false) EstadoVisita estado,
            Model model) {

        List<Visita> visitas;
        boolean hayFiltros = publicacionId != null || estado != null;

        if (hayFiltros) {
            visitas = visitaService.filtrar(publicacionId, estado);
        } else {
            visitas = visitaService.listarTodas();
        }

        model.addAttribute("visitas", visitas);
        model.addAttribute("publicaciones", publicacionRepository.findByEliminadaFalse());
        model.addAttribute("estados", EstadoVisita.values());
        model.addAttribute("filtroPublicacionId", publicacionId);
        model.addAttribute("filtroEstado", estado);

        return "visita/listado";
    }

    // Alta
    @GetMapping("/nueva")
    public String formularioAlta(Model model) {
        model.addAttribute("visita", new Visita());
        cargarDatosFormulario(model);
        model.addAttribute("accion", "alta");
        return "visita/formulario";
    }

    @PostMapping("/nueva")
    public String guardarAlta(
            @ModelAttribute Visita visita,
            @RequestParam Long publicacionId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHora,
            RedirectAttributes redirectAttributes) {
        try {
            Publicacion publicacion = publicacionRepository.findById(publicacionId)
                    .orElseThrow(() -> new IllegalArgumentException("Publicacion no encontrada."));
            visita.setPublicacion(publicacion);
            visita.setFechaHora(fechaHora);
            visitaService.crear(visita);
            redirectAttributes.addFlashAttribute("mensajeExito", "Visita registrada exitosamente.");
            return "redirect:/visitas";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/visitas/nueva";
        }
    }

    // Edicion
    @GetMapping("/{id}/editar")
    public String formularioEdicion(@PathVariable Long id, Model model,
                                     RedirectAttributes redirectAttributes) {
        try {
            Visita visita = visitaService.buscarPorId(id);
            model.addAttribute("visita", visita);
            cargarDatosFormulario(model);
            model.addAttribute("accion", "edicion");
            return "visita/formulario";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/visitas";
        }
    }

    @PostMapping("/{id}/editar")
    public String guardarEdicion(
            @PathVariable Long id,
            @ModelAttribute Visita visita,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHora,
            RedirectAttributes redirectAttributes) {
        try {
            visita.setFechaHora(fechaHora);
            visitaService.modificar(id, visita);
            redirectAttributes.addFlashAttribute("mensajeExito", "Visita modificada exitosamente.");
            return "redirect:/visitas";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/visitas/" + id + "/editar";
        }
    }

    // Eliminacion
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            visitaService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Visita eliminada correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/visitas";
    }

    private void cargarDatosFormulario(Model model) {
        model.addAttribute("publicaciones", publicacionRepository.findByEliminadaFalse());
        model.addAttribute("estados", EstadoVisita.values());
    }
}
