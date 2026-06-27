-- ═══════════════════════════════════════════════════════════
-- Script de datos de prueba – Sistema Inmobiliaria DESI 2026
-- Ejecutar DESPUÉS de que Hibernate genere las tablas (ddl-auto=update)
-- Ejecutar PRIMERO datos_ciudades.sql
-- ═══════════════════════════════════════════════════════════

USE gestion_inmobiliaria;

-- Personas (propietarios e inquilinos)
INSERT INTO persona (nombre, apellido, dni_cuit, telefono, email, domicilio, eliminado) VALUES
('Carlos', 'Rodriguez', '20-12345678-9', '3425001111', 'carlos.rodriguez@mail.com', 'Bv. Galvez 1200, Santa Fe', false),
('Maria', 'Lopez', '27-23456789-0', '3425002222', 'maria.lopez@mail.com', 'San Martin 450, Santa Fe', false),
('Roberto', 'Gomez', '20-34567890-1', '3425003333', 'roberto.gomez@mail.com', 'Rivadavia 800, Santo Tome', false),
('Ana', 'Fernandez', '27-45678901-2', '3425004444', 'ana.fernandez@mail.com', 'Urquiza 320, Santa Fe', false),
('Diego', 'Martinez', '20-56789012-3', '3425005555', 'diego.martinez@mail.com', '25 de Mayo 600, Santa Fe', false);

-- Propiedades (ciudad_id referencia la tabla ciudad)
-- ciudad_id 1 = Santa Fe, 2 = Santo Tome
INSERT INTO propiedad (direccion, ciudad_id, tipo, cantidad_ambientes, metros_cuadrados, descripcion, estado_disponibilidad, propietario_id, eliminada) VALUES
('Rivadavia 1500', 1, 'CASA', 4, 120.0, 'Casa con jardin y cochera.', 'ALQUILADA', 1, false),
('San Martin 200', 2, 'DEPARTAMENTO', 2, 55.0, 'Departamento 2 ambientes, 3er piso.', 'DISPONIBLE', 2, false),
('Belgrano 750', 1, 'LOCAL', 1, 80.0, 'Local comercial en planta baja.', 'DISPONIBLE', 1, false);

-- Historial estados propiedades
INSERT INTO historial_estado_propiedad (propiedad_id, estado, fecha_hora) VALUES
(1, 'ALQUILADA', '2025-01-01 10:00:00'),
(2, 'DISPONIBLE', '2025-01-01 10:00:00'),
(3, 'DISPONIBLE', '2025-01-01 10:00:00');

-- Contratos
INSERT INTO contrato (propiedad_id, inquilino_id, fecha_inicio, duracion_meses, importe_mensual, dia_vencimiento_mensual, descripcion, estado, eliminado) VALUES
(1, 3, '2025-01-01', 24, 150000.00, 5, 'Contrato de alquiler vivienda familiar.', 'ACTIVO', false),
(2, 4, '2025-06-01', 12, 80000.00, 10, 'Contrato de alquiler departamento.', 'BORRADOR', false);

-- Historial estados contratos
INSERT INTO historial_estado_contrato (contrato_id, estado, fecha_hora) VALUES
(1, 'BORRADOR', '2024-12-15 10:00:00'),
(1, 'ACTIVO', '2024-12-20 11:00:00'),
(2, 'BORRADOR', '2025-05-20 09:00:00');

-- Facturas
INSERT INTO factura (contrato_id, concepto_facturado, fecha_emision, fecha_vencimiento, importe, estado, eliminado) VALUES
(1, 'Alquiler Enero 2025', '2025-01-01', '2025-01-05', 150000.00, 'PAGADA', false),
(1, 'Alquiler Febrero 2025', '2025-02-01', '2025-02-05', 150000.00, 'PAGADA', false),
(1, 'Alquiler Marzo 2025', '2025-03-01', '2025-03-05', 150000.00, 'VENCIDA', false),
(1, 'Alquiler Abril 2025', '2025-04-01', '2025-04-05', 150000.00, 'PENDIENTE', false),
(1, 'Expensas Enero 2025', '2025-01-15', '2025-01-20', 12000.00, 'ANULADA', false);

-- Datos de pago para facturas pagadas
UPDATE factura SET fecha_pago='2025-01-03', medio_pago='TRANSFERENCIA', importe_pagado=150000.00, interes=0.00 WHERE id=1;
UPDATE factura SET fecha_pago='2025-02-04', medio_pago='TRANSFERENCIA', importe_pagado=150000.00, interes=0.00 WHERE id=2;

-- Historial estados facturas
INSERT INTO historial_estado_factura (factura_id, estado, fecha_hora) VALUES
(1, 'PENDIENTE', '2025-01-01 08:00:00'),
(1, 'PAGADA', '2025-01-03 15:30:00'),
(2, 'PENDIENTE', '2025-02-01 08:00:00'),
(2, 'PAGADA', '2025-02-04 12:00:00'),
(3, 'PENDIENTE', '2025-03-01 08:00:00'),
(3, 'VENCIDA', '2025-03-06 00:00:00'),
(4, 'PENDIENTE', '2025-04-01 08:00:00'),
(5, 'PENDIENTE', '2025-01-15 08:00:00'),
(5, 'ANULADA', '2025-01-16 10:00:00');
