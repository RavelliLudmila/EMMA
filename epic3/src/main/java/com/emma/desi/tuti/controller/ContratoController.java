
package com.emma.desi.tuti.controller;

import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RestController;

import com.emma.desi.tuti.dto.AltaContratoDTO;
import com.emma.desi.tuti.dto.ContratoListadoDTO;
import com.emma.desi.tuti.dto.ModificarContratoDTO;
import com.emma.desi.tuti.model.entity.Contrato;
import com.emma.desi.tuti.model.entity.Persona;
import com.emma.desi.tuti.model.entity.Propiedad;
import com.emma.desi.tuti.model.enums.EstadoContrato;
import com.emma.desi.tuti.exceptions.BusinessException;
import com.emma.desi.tuti.service.ContratoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/contratos")
public class ContratoController {

    private final ContratoService contratoService;

    public ContratoController(ContratoService contratoService) {
        this.contratoService = contratoService;
    }

    @PostMapping
    public ResponseEntity<Contrato> crearContrato(@Valid @RequestBody AltaContratoDTO dto) {
        Contrato contratoCreado = contratoService.crearContrato(dto);
        return ResponseEntity
                .created(URI.create("/api/contratos/" + contratoCreado.getId()))
                .body(contratoCreado);
    }
    
    @GetMapping
    public ResponseEntity<List<ContratoListadoDTO>> listarContratos(
    		@RequestParam(required = false) Long propiedadId,
    		@RequestParam(required = false) Long inquilinoId,
    		@RequestParam(required = false) String estado,
    		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio){
    	EstadoContrato estadoContrato = parseEstadoContrato(estado);
    	return ResponseEntity.ok(contratoService.listarContratos(propiedadId, inquilinoId, estadoContrato, fechaInicio));
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<ContratoListadoDTO> modificarContrato(
            @PathVariable Long id,
            @Valid @RequestBody ModificarContratoDTO dto) {
        return ResponseEntity.ok(contratoService.modificarContratoDTO(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarContrato(@PathVariable Long id) {
        contratoService.eliminarContrato(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/opciones/propiedades")
    public ResponseEntity<List<Map<String, Object>>> listarPropiedades() {
        List<Map<String, Object>> opciones = contratoService.listarPropiedadesNoEliminadas()
                .stream()
                .map(this::mapearPropiedad)
                .toList();
        return ResponseEntity.ok(opciones);
    }

    @GetMapping("/opciones/inquilinos")
    public ResponseEntity<List<Map<String, Object>>> listarInquilinos() {
        List<Map<String, Object>> opciones = contratoService.listarPersonasNoEliminadas()
                .stream()
                .map(this::mapearPersona)
                .toList();
        return ResponseEntity.ok(opciones);
    }

    @GetMapping("/opciones/estados")
    public ResponseEntity<List<String>> listarEstados() {
        List<String> estados = Arrays.stream(EstadoContrato.values())
                .map(EstadoContrato::toJson)
                .toList();
        return ResponseEntity.ok(estados);
    }

    private Map<String, Object> mapearPropiedad(Propiedad propiedad) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("id", propiedad.getId());
        item.put("direccion", propiedad.getDireccion());
        item.put("estado", propiedad.getEstado());
        return item;
    }

    private Map<String, Object> mapearPersona(Persona persona) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("id", persona.getId());
        item.put("nombreCompleto", persona.getApellido() + ", " + persona.getNombre());
        item.put("documento", persona.getDocumento());
        return item;
    }
    private EstadoContrato parseEstadoContrato(String estado) {
    	if(estado == null || estado.isBlank()){
    		return null;
        }
        try {
        return EstadoContrato.valueOf(estado.trim().toUpperCase());
     } catch (IllegalArgumentException exception) {
        throw new BusinessException("Estado de contrato invalido para el filtro: " + estado);
     }
    }

    @GetMapping("/form")
    public String form(){
        return "form";
    }
    
    
}




