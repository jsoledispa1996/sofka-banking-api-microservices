-- Insertar datos de prueba para Personas/Clientes
INSERT INTO ba_personas (
pe_nombre,          pe_genero,      pe_edad,    pe_identificacion,  pe_direccion,                       pe_telefono) VALUES
('Juan Pérez',      'Masculino',    30,         '1234567890',       'Calle 123 #45-67, Bogotá',         '3001234567'),
('María García',    'Femenino',     25,         '0987654321',       'Carrera 456 #78-90, Medellín',     '3109876543'),
('Carlos López',    'Masculino',    35,         '1122334455',       'Avenida 789 #12-34, Cali',         '3201122334'),
('Ana Martínez',    'Femenino',     28,         '5566778899',       'Diagonal 321 #56-78, Barranquilla','3155566778');

-- Insertar datos de prueba para Clientes
INSERT INTO ba_clientes (
cl_id_persona,  cl_id_cliente,  cl_contrasena,  cl_estado) VALUES
(1,             'CLI001',       'password123',  true),
(2,             'CLI002',       'maria456',     true),
(3,             'CLI003',       'carlos789',    false),
(4,             'CLI004',       'ana321',       true);