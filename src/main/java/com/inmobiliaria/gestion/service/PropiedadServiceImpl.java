package com.inmobiliaria.gestion.service;

import com.inmobiliaria.gestion.model.Propiedad;
import com.inmobiliaria.gestion.model.enums.EstadoContrato;
import com.inmobiliaria.gestion.model.enums.EstadoDisponibilidad;
import com.inmobiliaria.gestion.model.enums.TipoPropiedad;
import com.inmobiliaria.gestion.repository.ContratoRepository;
import com.inmobiliaria.gestion.repository.PropiedadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PropiedadServiceImpl implements PropiedadService {

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Autowired
    private ContratoRepository contratoRepository;

    // Verifica si la propiedad tiene un contrato activo
    private boolean tieneContratoActivo(Long propiedadId) {
        return contratoRepository.findByEstadoAndEliminadoFalse(EstadoContrato.ACTIVO)
                .stream()
                .anyMatch(c -> c.getPropiedad().getId().equals(propiedadId));
    }

    // H1.1 Alta
    @Transactional
    public Propiedad guardar(Propiedad propiedad) {
        if (propiedad.getCantidadAmbientes() <= 0) {
            throw new IllegalArgumentException("Los ambientes deben ser enteros positivos.");
        }
        if (propiedad.getMetrosCuadrados() <= 0) {
            throw new IllegalArgumentException("Los metros cuadrados deben ser positivos.");
        }
        if (propiedadRepository.existeDireccionDuplicada(
                propiedad.getDireccion(), propiedad.getCiudad().getId(), null)) {
            throw new IllegalArgumentException("Ya existe una propiedad activa en esta dirección y ciudad.");
        }
        if (propiedad.getEstadoDisponibilidad() == null) {
            propiedad.setEstadoDisponibilidad(EstadoDisponibilidad.DISPONIBLE);
        }
        propiedad.cambiarEstado(propiedad.getEstadoDisponibilidad());
        return propiedadRepository.save(propiedad);
    }

    // H1.2 Eliminación lógica
    @Transactional
    public void eliminar(Long id) {
        Propiedad propiedad = buscarPorId(id);
        if (tieneContratoActivo(id)) {
            throw new IllegalArgumentException("No se puede eliminar. La propiedad tiene un contrato vigente.");
        }
        propiedad.setEliminada(true);
        propiedadRepository.save(propiedad);
    }

    // H1.3 Modificación
    @Transactional
    public Propiedad actualizar(Long id, Propiedad propiedadModificada) {
        Propiedad propiedadExistente = buscarPorId(id);

        if (propiedadRepository.existeDireccionDuplicada(
                propiedadModificada.getDireccion(),
                propiedadModificada.getCiudad().getId(), id)) {
            throw new IllegalArgumentException("Otra propiedad activa ya tiene esa dirección y ciudad.");
        }

        if (tieneContratoActivo(id)) {
            EstadoDisponibilidad nuevoEst = propiedadModificada.getEstadoDisponibilidad();
            if (nuevoEst == EstadoDisponibilidad.DISPONIBLE) {
                throw new IllegalArgumentException("La propiedad tiene un contrato activo. No puede pasar a Disponible.");
            }
            if (nuevoEst == EstadoDisponibilidad.INACTIVA) {
                throw new IllegalArgumentException("La propiedad tiene un contrato activo. No puede pasar a Inactiva.");
            }
        }

        if (!propiedadExistente.getEstadoDisponibilidad()
                .equals(propiedadModificada.getEstadoDisponibilidad())) {
            propiedadExistente.cambiarEstado(propiedadModificada.getEstadoDisponibilidad());
        }

        propiedadExistente.setCiudad(propiedadModificada.getCiudad());
        propiedadExistente.setPropietario(propiedadModificada.getPropietario());
        propiedadExistente.setDescripcion(propiedadModificada.getDescripcion());
        propiedadExistente.setDireccion(propiedadModificada.getDireccion());
        propiedadExistente.setTipo(propiedadModificada.getTipo());
        propiedadExistente.setCantidadAmbientes(propiedadModificada.getCantidadAmbientes());
        propiedadExistente.setMetrosCuadrados(propiedadModificada.getMetrosCuadrados());
        propiedadExistente.setComodidades(propiedadModificada.getComodidades());

        return propiedadRepository.save(propiedadExistente);
    }

    // H1.4 Listado
    public List<Propiedad> listarActivas() {
        return propiedadRepository.findByEliminadaFalse();
    }

    public List<Propiedad> filtrar(String direccion, Long ciudadId,
                                    TipoPropiedad tipo, EstadoDisponibilidad estado) {
        return propiedadRepository.filtrarPropiedades(direccion, ciudadId, tipo, estado);
    }

    public Propiedad buscarPorId(Long id) {
        return propiedadRepository.findById(id)
                .filter(p -> !p.isEliminada())
                .orElseThrow(() -> new IllegalArgumentException("Propiedad no encontrada con id: " + id));
    }
}