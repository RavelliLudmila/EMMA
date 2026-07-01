USE gestion_inmobiliaria;

-- Publicaciones (solo para propiedades DISPONIBLE)
INSERT INTO publicacion (propiedad_id, precio_mensual, condiciones, descripcion, fecha_publicacion, estado_publicacion, eliminada) VALUES
(2, 80000.00, 'Deposito: 1 mes. Garantia propietario. No mascotas.', 'Departamento luminoso en planta baja, cerca del centro.', '2025-06-01', 'ACTIVA', false),
(3, 120000.00, 'Deposito: 2 meses. Garantia bancaria. Apto comercial.', 'Local comercial ideal para negocio, amplio y bien ubicado.', '2025-05-15', 'PAUSADA', false);

-- Historial estados publicaciones
INSERT INTO historial_estado_publicacion (publicacion_id, estado, fecha_hora) VALUES
(1, 'ACTIVA', '2025-06-01 09:00:00'),
(2, 'ACTIVA', '2025-05-15 10:00:00'),
(2, 'PAUSADA', '2025-05-20 14:00:00');