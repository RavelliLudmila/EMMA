package com.inmobiliaria.gestion.controller;

import com.inmobiliaria.gestion.model.Propiedad;
import com.inmobiliaria.gestion.model.Ciudad;
import com.inmobiliaria.gestion.model.Persona;
import com.inmobiliaria.gestion.model.enums.EstadoDisponibilidad;
import com.inmobiliaria.gestion.model.enums.TipoPropiedad;
import com.inmobiliaria.gestion.repository.CiudadRepository;
import com.inmobiliaria.gestion.repository.PersonaRepository;
import com.inmobiliaria.gestion.service.PropiedadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/propiedades")
public class PropiedadController {

    @Autowired
    private PropiedadService propiedadService;

    @Autowired
    private CiudadRepository ciudadRepository;

    @Autowired
    private PersonaRepository personaRepository;

    // ─────────────────────────────────────────────
    // H1.4 LISTADO
    // ─────────────────────────────────────────────

    @GetMapping
    public String listar(
            @RequestParam(required = false) String direccion,
            @RequestParam(required = false) Long ciudadId,
            @RequestParam(required = false) TipoPropiedad tipo,
            @RequestParam(required = false) EstadoDisponibilidad estado,
            Model model) {

        List<Propiedad> propiedades;

        boolean hayFiltros = (direccion != null && !direccion.isBlank())
                || ciudadId != null || tipo != null || estado != null;

        if (hayFiltros) {
            propiedades = propiedadService.filtrar(
                    direccion != null && direccion.isBlank() ? null : direccion,
                    ciudadId, tipo, estado);
        } else {
            propiedades = propiedadService.listarActivas();
        }

        model.addAttribute("propiedades", propiedades);
        model.addAttribute("ciudades", ciudadRepository.findAllByOrderByNombreAsc());
        model.addAttribute("tipos", TipoPropiedad.values());
        model.addAttribute("estados", EstadoDisponibilidad.values());
        model.addAttribute("filtroDireccion", direccion);
        model.addAttribute("filtroCiudadId", ciudadId);
        model.addAttribute("filtroTipo", tipo);
        model.addAttribute("filtroEstado", estado);

        return "propiedad/listado";
    }

    // ─────────────────────────────────────────────
    // H1.1 ALTA
    // ─────────────────────────────────────────────

    @GetMapping("/nueva")
    public String formularioAlta(Model model) {
        model.addAttribute("propiedad", new Propiedad());
        cargarDatosFormulario(model);
        model.addAttribute("accion", "alta");
        return "propiedad/formulario";
    }

    @PostMapping("/nueva")
    public String guardarAlta(
            @ModelAttribute Propiedad propiedad,
            @RequestParam Long ciudadId,
            @RequestParam Long propietarioId,
            RedirectAttributes redirectAttributes) {
        try {
            Ciudad ciudad = ciudadRepository.findById(ciudadId)
                    .orElseThrow(() -> new IllegalArgumentException("Ciudad no encontrada."));
            Persona propietario = personaRepository.findById(propietarioId)
                    .orElseThrow(() -> new IllegalArgumentException("Propietario no encontrado."));
            propiedad.setCiudad(ciudad);
            propiedad.setPropietario(propietario);
            propiedadService.guardar(propiedad);
            redirectAttributes.addFlashAttribute("mensajeExito", "Propiedad creada exitosamente.");
            return "redirect:/propiedades";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/propiedades/nueva";
        }
    }

    // ─────────────────────────────────────────────
    // H1.3 MODIFICACIÓN
    // ─────────────────────────────────────────────

    @GetMapping("/{id}/editar")
    public String formularioEdicion(@PathVariable Long id, Model model,
                                     RedirectAttributes redirectAttributes) {
        try {
            Propiedad propiedad = propiedadService.buscarPorId(id);
            model.addAttribute("propiedad", propiedad);
            cargarDatosFormulario(model);
            model.addAttribute("accion", "edicion");
            return "propiedad/formulario";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/propiedades";
        }
    }

    @PostMapping("/{id}/editar")
    public String guardarEdicion(
            @PathVariable Long id,
            @ModelAttribute Propiedad propiedad,
            @RequestParam Long ciudadId,
            @RequestParam Long propietarioId,
            RedirectAttributes redirectAttributes) {
        try {
            Ciudad ciudad = ciudadRepository.findById(ciudadId)
                    .orElseThrow(() -> new IllegalArgumentException("Ciudad no encontrada."));
            Persona propietario = personaRepository.findById(propietarioId)
                    .orElseThrow(() -> new IllegalArgumentException("Propietario no encontrado."));
            propiedad.setCiudad(ciudad);
            propiedad.setPropietario(propietario);
            propiedadService.actualizar(id, propiedad);
            redirectAttributes.addFlashAttribute("mensajeExito", "Propiedad modificada exitosamente.");
            return "redirect:/propiedades";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/propiedades/" + id + "/editar";
        }
    }

    // ─────────────────────────────────────────────
    // H1.2 ELIMINACIÓN
    // ─────────────────────────────────────────────

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            propiedadService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Propiedad eliminada correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/propiedades";
    }

    // ─────────────────────────────────────────────
    // AUXILIAR
    // ─────────────────────────────────────────────

    private void cargarDatosFormulario(Model model) {
        model.addAttribute("ciudades", ciudadRepository.findAllByOrderByNombreAsc());
        model.addAttribute("personas", personaRepository.findByEliminadoFalse());
        model.addAttribute("tipos", TipoPropiedad.values());
        model.addAttribute("estados", EstadoDisponibilidad.values());
    }
}
