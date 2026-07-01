package com.inmobiliaria.gestion.controller;

import com.inmobiliaria.gestion.model.Publicacion;
import com.inmobiliaria.gestion.model.enums.EstadoPublicacion;
import com.inmobiliaria.gestion.repository.CiudadRepository;
import com.inmobiliaria.gestion.service.PropiedadService;
import com.inmobiliaria.gestion.service.PublicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;

@Controller
@RequestMapping("/publicaciones")
public class PublicacionController {

    @Autowired
    private PublicacionService publicacionService;

    @Autowired
    private PropiedadService propiedadService;

    @Autowired
    private CiudadRepository ciudadRepository;

    // HU 2.4 Listado
    @GetMapping
    public String listar(Model model,
            @RequestParam(required = false) Long propiedadId,
            @RequestParam(required = false) Long ciudadId,
            @RequestParam(required = false) EstadoPublicacion estadoPublicacion,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax) {

        boolean hayFiltros = propiedadId != null || ciudadId != null ||
                estadoPublicacion != null || precioMin != null || precioMax != null;

        if (hayFiltros) {
            model.addAttribute("publicaciones",
                    publicacionService.filtrar(propiedadId, ciudadId, estadoPublicacion, precioMin, precioMax));
        } else {
            model.addAttribute("publicaciones", publicacionService.listarActivas());
        }

        model.addAttribute("propiedades", propiedadService.listarActivas());
        model.addAttribute("ciudades", ciudadRepository.findAllByOrderByNombreAsc());
        model.addAttribute("estados", EstadoPublicacion.values());
        model.addAttribute("propiedadIdFiltro", propiedadId);
        model.addAttribute("ciudadIdFiltro", ciudadId);
        model.addAttribute("estadoFiltro", estadoPublicacion);
        model.addAttribute("precioMinFiltro", precioMin);
        model.addAttribute("precioMaxFiltro", precioMax);

        return "publicacion/listado";
    }

    // HU 2.1 Alta
    @GetMapping("/nueva")
    public String nuevaForm(Model model) {
        model.addAttribute("publicacion", new Publicacion());
        model.addAttribute("propiedades", propiedadService.listarActivas());
        model.addAttribute("estados", EstadoPublicacion.values());
        model.addAttribute("accion", "alta");
        return "publicacion/formulario";
    }

    @PostMapping("/nueva")
    public String guardar(@ModelAttribute Publicacion publicacion,
                          @RequestParam Long propiedadId,
                          RedirectAttributes redirectAttributes) {
        try {
            com.inmobiliaria.gestion.model.Propiedad propiedad =
                    new com.inmobiliaria.gestion.model.Propiedad();
            propiedad.setId(propiedadId);
            publicacion.setPropiedad(propiedad);
            publicacionService.guardar(publicacion);
            redirectAttributes.addFlashAttribute("mensajeExito", "Publicacion creada exitosamente.");
            return "redirect:/publicaciones";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/publicaciones/nueva";
        }
    }

    // HU 2.3 Edicion
    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Long id, Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            Publicacion publicacion = publicacionService.buscarPorId(id);
            model.addAttribute("publicacion", publicacion);
            model.addAttribute("estados", EstadoPublicacion.values());
            model.addAttribute("accion", "edicion");
            return "publicacion/formulario";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/publicaciones";
        }
    }

    @PostMapping("/{id}/editar")
    public String actualizar(@PathVariable Long id,
                              @ModelAttribute Publicacion publicacion,
                              RedirectAttributes redirectAttributes) {
        try {
            publicacionService.actualizar(id, publicacion);
            redirectAttributes.addFlashAttribute("mensajeExito", "Publicacion modificada exitosamente.");
            return "redirect:/publicaciones";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/publicaciones/" + id + "/editar";
        }
    }

    // HU 2.2 Eliminacion
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            publicacionService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Publicacion eliminada correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/publicaciones";
    }
}
