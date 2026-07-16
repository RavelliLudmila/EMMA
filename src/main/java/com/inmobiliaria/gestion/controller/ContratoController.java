package com.inmobiliaria.gestion.controller;

import com.inmobiliaria.gestion.model.Contrato;
import com.inmobiliaria.gestion.model.Persona;
import com.inmobiliaria.gestion.model.Propiedad;
import com.inmobiliaria.gestion.model.enums.EstadoContrato;
import com.inmobiliaria.gestion.repository.PersonaRepository;
import com.inmobiliaria.gestion.repository.PropiedadRepository;
import com.inmobiliaria.gestion.service.ContratoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/contratos")
public class ContratoController {

    @Autowired
    private ContratoService contratoService;

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Autowired
    private PersonaRepository personaRepository;

    // HU 3.4 Listado
    @GetMapping
    public String listar(
            @RequestParam(required = false) Long propiedadId,
            @RequestParam(required = false) Long inquilinoId,
            @RequestParam(required = false) EstadoContrato estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            Model model) {

        List<Contrato> contratos;
        boolean hayFiltros = propiedadId != null || inquilinoId != null
                || estado != null || fechaInicio != null;

        if (hayFiltros) {
            contratos = contratoService.buscarConFiltros(
                    propiedadId, inquilinoId, estado, fechaInicio);
        } else {
            contratos = contratoService.listarTodos();
        }

        model.addAttribute("contratos", contratos);
        model.addAttribute("propiedades", contratoService.listarPropiedadesNoEliminadas());
        model.addAttribute("personas", contratoService.listarPersonasNoEliminadas());
        model.addAttribute("estados", EstadoContrato.values());
        model.addAttribute("filtroPropiedadId", propiedadId);
        model.addAttribute("filtroInquilinoId", inquilinoId);
        model.addAttribute("filtroEstado", estado);
        model.addAttribute("filtroFechaInicio", fechaInicio);

        return "contrato/listado";
    }

    // HU 3.1 Alta
    @GetMapping("/nuevo")
    public String formularioAlta(Model model) {
        model.addAttribute("contrato", new Contrato());
        cargarDatosFormulario(model);
        model.addAttribute("accion", "alta");
        return "contrato/formulario";
    }

    @PostMapping("/nuevo")
    public String guardarAlta(
            @ModelAttribute Contrato contrato,
            @RequestParam Long propiedadId,
            @RequestParam Long inquilinoId,
            RedirectAttributes redirectAttributes) {
        try {
            Propiedad propiedad = propiedadRepository.findById(propiedadId)
                    .orElseThrow(() -> new IllegalArgumentException("Propiedad no encontrada."));
            Persona inquilino = personaRepository.findById(inquilinoId)
                    .orElseThrow(() -> new IllegalArgumentException("Inquilino no encontrado."));
            contrato.setPropiedad(propiedad);
            contrato.setInquilino(inquilino);
            contratoService.crear(contrato);
            redirectAttributes.addFlashAttribute("mensajeExito", "Contrato creado exitosamente.");
            return "redirect:/contratos";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/contratos/nuevo";
        }
    }

    // HU 3.3 Modificacion
    @GetMapping("/{id}/editar")
    public String formularioEdicion(@PathVariable Long id, Model model,
                                     RedirectAttributes redirectAttributes) {
        try {
            Contrato contrato = contratoService.buscarPorId(id);
            model.addAttribute("contrato", contrato);
            cargarDatosFormulario(model);
            model.addAttribute("accion", "edicion");
            return "contrato/formulario";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/contratos";
        }
    }

    @PostMapping("/{id}/editar")
    public String guardarEdicion(
            @PathVariable Long id,
            @ModelAttribute Contrato contrato,
            @RequestParam Long propiedadId,
            @RequestParam Long inquilinoId,
            RedirectAttributes redirectAttributes) {
        try {
            Propiedad propiedad = propiedadRepository.findById(propiedadId)
                    .orElseThrow(() -> new IllegalArgumentException("Propiedad no encontrada."));
            Persona inquilino = personaRepository.findById(inquilinoId)
                    .orElseThrow(() -> new IllegalArgumentException("Inquilino no encontrado."));
            contrato.setPropiedad(propiedad);
            contrato.setInquilino(inquilino);
            contratoService.modificar(id, contrato);
            redirectAttributes.addFlashAttribute("mensajeExito", "Contrato modificado exitosamente.");
            return "redirect:/contratos";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/contratos/" + id + "/editar";
        }
    }

    // HU 3.2 Eliminacion
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            contratoService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Contrato eliminado correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/contratos";
    }

    private void cargarDatosFormulario(Model model) {
        model.addAttribute("propiedades", contratoService.listarPropiedadesNoEliminadas());
        model.addAttribute("personas", contratoService.listarPersonasNoEliminadas());
        model.addAttribute("estados", EstadoContrato.values());
    }
}
