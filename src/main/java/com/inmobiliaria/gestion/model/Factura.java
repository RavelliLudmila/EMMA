package com.inmobiliaria.gestion.model;

import com.inmobiliaria.gestion.model.enums.EstadoFactura;
import com.inmobiliaria.gestion.model.enums.MedioPago;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "factura")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contrato contrato;

    @NotBlank(message = "El concepto facturado es requerido")
    @Column(nullable = false)
    private String conceptoFacturado;

    @NotNull(message = "La fecha de emisión es requerida")
    @Column(nullable = false)
    private LocalDate fechaEmision;

    @NotNull(message = "La fecha de vencimiento es requerida")
    @Column(nullable = false)
    private LocalDate fechaVencimiento;

    @NotNull(message = "El importe es requerido")
    @DecimalMin(value = "0.01", message = "El importe debe ser un número positivo")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal importe;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoFactura estado = EstadoFactura.PENDIENTE;

    @Column(nullable = false)
    private boolean eliminado = false;

    // --- Datos de pago (opcionales, solo cuando estado = PAGADA) ---

    @Column
    private LocalDate fechaPago;

    @Enumerated(EnumType.STRING)
    @Column
    private MedioPago medioPago;

    @Column(precision = 12, scale = 2)
    private BigDecimal importePagado;

    @Column(precision = 12, scale = 2)
    private BigDecimal interes;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialEstadoFactura> historialEstados = new ArrayList<>();

    // Constructors
    public Factura() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Contrato getContrato() { return contrato; }
    public void setContrato(Contrato contrato) { this.contrato = contrato; }

    public String getConceptoFacturado() { return conceptoFacturado; }
    public void setConceptoFacturado(String conceptoFacturado) { this.conceptoFacturado = conceptoFacturado; }

    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }

    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public BigDecimal getImporte() { return importe; }
    public void setImporte(BigDecimal importe) { this.importe = importe; }

    public EstadoFactura getEstado() { return estado; }
    public void setEstado(EstadoFactura estado) { this.estado = estado; }

    public boolean isEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }

    public LocalDate getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }

    public MedioPago getMedioPago() { return medioPago; }
    public void setMedioPago(MedioPago medioPago) { this.medioPago = medioPago; }

    public BigDecimal getImportePagado() { return importePagado; }
    public void setImportePagado(BigDecimal importePagado) { this.importePagado = importePagado; }

    public BigDecimal getInteres() { return interes; }
    public void setInteres(BigDecimal interes) { this.interes = interes; }

    public List<HistorialEstadoFactura> getHistorialEstados() { return historialEstados; }
    public void setHistorialEstados(List<HistorialEstadoFactura> historialEstados) { this.historialEstados = historialEstados; }

    // Helper para limpiar datos de pago
    public void limpiarDatosPago() {
        this.fechaPago = null;
        this.medioPago = null;
        this.importePagado = null;
        this.interes = null;
    }
}
