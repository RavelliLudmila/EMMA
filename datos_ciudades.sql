-- ═══════════════════════════════════════════════════════════
-- Datos de prueba para Provincias y Ciudades
-- Ejecutar DESPUÉS de que Hibernate genere las tablas
-- ═══════════════════════════════════════════════════════════

USE gestion_inmobiliaria;

INSERT INTO provincia (nombre) VALUES
('Santa Fe'),
('Buenos Aires'),
('Cordoba');

INSERT INTO ciudad (nombre, provincia_id) VALUES
('Santa Fe', 1),
('Santo Tome', 1),
('Rosario', 1),
('Buenos Aires', 2),
('Cordoba', 3);
