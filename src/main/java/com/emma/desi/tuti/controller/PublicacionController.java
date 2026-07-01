package com.emma.desi.tuti.controller;

import com.emma.desi.tuti.model.entity.Publicacion;
import com.emma.desi.tuti.model.enums.EstadoPublicacion;
import com.emma.desi.tuti.repository.CiudadRepository;
import com.emma.desi.tuti.service.PropiedadService;
import com.emma.desi.tuti.service.PublicacionService;
import com.emma.desi.tuti.service.exception.ReglaNegocioException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;

@Controller
@RequestMapping("/publicaciones")
public class PublicacionController {

	private final PublicacionService publicacionService;

	private final PropiedadService propiedadService;

	private final CiudadRepository ciudadRepository;

	PublicacionController(PublicacionService publicacionService, PropiedadService propiedadService, CiudadRepository ciudadRepository) {
		this.publicacionService = publicacionService;
		this.propiedadService = propiedadService;
		this.ciudadRepository = ciudadRepository;
	}

	// HU 2.4 — Listado con filtros opcionales
	@GetMapping
	public String listar(Model model,
			@RequestParam(required=false) Long propiedadId,
			@RequestParam(required=false) Long ciudadId,
			@RequestParam(required=false) EstadoPublicacion estadoPublicacion,
			@RequestParam(required=false) BigDecimal precioMin,
			@RequestParam(required=false) BigDecimal precioMax) {

		boolean hayFiltros = propiedadId != null || ciudadId != null || estadoPublicacion != null
				|| precioMin != null || precioMax != null;

		if (hayFiltros) {
			model.addAttribute("publicaciones",
				publicacionService.filtrar(propiedadId, ciudadId, estadoPublicacion, precioMin, precioMax));
		} else {
			model.addAttribute("publicaciones", publicacionService.listarActivas());
		}

		model.addAttribute("propiedades", propiedadService.listarActivas());
		model.addAttribute("ciudades", ciudadRepository.findAll());
		model.addAttribute("estados", EstadoPublicacion.values());
		model.addAttribute("propiedadIdFiltro", propiedadId);
		model.addAttribute("ciudadIdFiltro", ciudadId);
		model.addAttribute("estadoFiltro", estadoPublicacion);
		model.addAttribute("precioMinFiltro", precioMin);
		model.addAttribute("precioMaxFiltro", precioMax);

		return "publicaciones/lista";
	}

	// HU 2.1 — Formulario nueva publicacion
	@GetMapping("/nueva")
	public String nuevaForm(Model model) {
		model.addAttribute("publicacion", new Publicacion());
		model.addAttribute("propiedades", propiedadService.listarActivas());
		model.addAttribute("estados", EstadoPublicacion.values());
		return "publicaciones/formulario";
	}

	// HU 2.1 — Guardar nueva publicacion
	@PostMapping("/guardar")
	public String guardar(@ModelAttribute Publicacion publicacion, Model model) {
		try {
			publicacionService.guardar(publicacion);
			return "redirect:/publicaciones";
		} catch (ReglaNegocioException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("publicacion", publicacion);
			model.addAttribute("propiedades", propiedadService.listarActivas());
			model.addAttribute("estados", EstadoPublicacion.values());
			return "publicaciones/formulario";
		}
	}

	// HU 2.3 — Formulario edicion
	@GetMapping("/editar/{id}")
	public String editarForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Publicacion publicacion = publicacionService.buscarPorId(id);
			model.addAttribute("publicacion", publicacion);
			model.addAttribute("estados", EstadoPublicacion.values());
			return "publicaciones/formulario";
		} catch (ReglaNegocioException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/publicaciones";
		}
	}

	// HU 2.3 — Actualizar publicacion
	@PostMapping("/actualizar/{id}")
	public String actualizar(@PathVariable Long id, @ModelAttribute Publicacion publicacion,
			Model model, RedirectAttributes redirectAttributes) {
		try {
			publicacionService.actualizar(id, publicacion);
			return "redirect:/publicaciones";
		} catch (ReglaNegocioException e) {
			model.addAttribute("error", e.getMessage());
			publicacion.setId(id);
			model.addAttribute("publicacion", publicacion);
			model.addAttribute("estados", EstadoPublicacion.values());
			return "publicaciones/formulario";
		}
	}

	// HU 2.2 — Eliminar publicacion
	@PostMapping("/eliminar/{id}")
	public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			publicacionService.eliminar(id);
		} catch (ReglaNegocioException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}
		return "redirect:/publicaciones";
	}
}
