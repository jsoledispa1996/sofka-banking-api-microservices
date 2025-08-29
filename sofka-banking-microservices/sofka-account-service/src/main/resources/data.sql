-- Insertar datos de prueba para Cuentas
INSERT INTO ba_cuentas (
cu_numero_cuenta,   cu_tipo_cuenta, cu_saldo_inicial, cu_saldo_actual,  cu_estado,  cu_id_cliente,  cu_fecha_creacion) VALUES
('478758',          'Ahorro',       2000.00,            2000.00,        true,       'CLI001',       '2025-08-28 10:00:00'),
('225487',          'Corriente',    100.00,             100.00,         true,       'CLI002',       '2025-08-28 10:30:00'),
('495878',          'Ahorro',       0.00,               0.00,           true,       'CLI003',       '2025-08-28 11:00:00'),
('496825',          'Ahorro',       540.00,             540.00,         true,       'CLI004',       '2025-08-28 11:30:00'),
('585545',          'Corriente',    1000.00,            1000.00,        true,       'CLI001',       '2025-08-28 12:00:00');

-- Insertar datos de prueba para Movimientos
INSERT INTO ba_movimientos (
mo_fecha,                   mo_tipo_movimiento,     mo_valor,   mo_saldo,   mo_descripcion,             mo_id_cuenta) VALUES
-- Movimientos para cuenta 478758 (CLI001)
('2025-08-28 10:15:00',     'RETIRO',               575.00,     1425.00,    'Retiro cajero automático', 1),
('2025-08-28 10:30:00',     'DEPOSITO',             100.00,     1525.00,    'Depósito en efectivo',     1),

-- Movimientos para cuenta 225487 (CLI002)
('2025-08-28 11:00:00',     'DEPOSITO',             600.00,     700.00,     'Depósito transferencia',   2),
('2025-08-28 11:15:00',     'RETIRO',               150.00,     550.00,     'Pago servicios',           2),

-- Movimientos para cuenta 496825 (CLI004)
('2025-08-28 12:00:00',     'RETIRO',               540.00,     0.00,       'Retiro total',             4),

-- Movimientos para cuenta 585545 (CLI001)
('2025-08-28 12:30:00',     'DEPOSITO',             500.00,     1500.00,    'Depósito nómina',          5);

-- Actualizar saldos actuales de las cuentas después de los movimientos
UPDATE ba_cuentas SET cu_saldo_actual = 1525.00 WHERE cu_numero_cuenta = '478758';
UPDATE ba_cuentas SET cu_saldo_actual = 550.00  WHERE cu_numero_cuenta = '225487';
UPDATE ba_cuentas SET cu_saldo_actual = 0.00    WHERE cu_numero_cuenta = '495878';
UPDATE ba_cuentas SET cu_saldo_actual = 0.00    WHERE cu_numero_cuenta = '496825';
UPDATE ba_cuentas SET cu_saldo_actual = 1500.00 WHERE cu_numero_cuenta = '585545';