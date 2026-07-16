USE gestion_inmobiliaria;

INSERT INTO incidente (titulo, descripcion, categoria, prioridad, estado, fecha_alta, responsable_tecnico, contrato_id, eliminado) VALUES
('Perdida de agua en baño', 'Se detecta perdida de agua en la caneria del baño principal.', 'PLOMERIA', 'ALTA', 'ABIERTO', NOW(), 'Juan Perez', 1, false),
('Corte de luz en cocina', 'No funciona el tomacorriente de la cocina.', 'ELECTRICIDAD', 'MEDIA', 'EN_PROCESO', NOW(), 'Carlos Lopez', 1, false),
('Revision calefon', 'El calefon no enciende correctamente.', 'GAS', 'ALTA', 'RESUELTO', NOW(), 'Pedro Garcia', 1, false),
('Puerta trabada', 'La puerta de entrada no cierra bien.', 'GENERAL', 'BAJA', 'ABIERTO', NOW(), NULL, 1, false);

INSERT INTO historial_estado_incidente (incidente_id, estado, fecha_hora) VALUES
(1, 'ABIERTO', NOW()),
(2, 'ABIERTO', NOW()),
(2, 'EN_PROCESO', NOW()),
(3, 'ABIERTO', NOW()),
(3, 'EN_PROCESO', NOW()),
(3, 'RESUELTO', NOW()),
(4, 'ABIERTO', NOW());