-- Script de Base de Datos para Sistema Bancario
-- Microservicios: Cliente-Persona y Cuenta-Movimiento

-- Crear base de datos (si no existe)
-- CREATE DATABASE banking_db;

-- ================================
-- TABLAS DEL MICROSERVICIO CLIENTE-PERSONA
-- ================================

-- Tabla Persona
CREATE TABLE IF NOT EXISTS persona (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(1) CHECK (genero IN ('M', 'F', 'O')),
    edad INTEGER CHECK (edad >= 0 AND edad <= 150),
    identificacion VARCHAR(20) UNIQUE NOT NULL,
    direccion VARCHAR(255),
    telefono VARCHAR(20)
);

-- Tabla Cliente
CREATE TABLE IF NOT EXISTS cliente (
    id BIGSERIAL PRIMARY KEY,
    persona_id BIGINT UNIQUE NOT NULL REFERENCES persona(id) ON DELETE CASCADE,
    cliente_id VARCHAR(50) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    estado BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla Cuenta
CREATE TABLE IF NOT EXISTS cuenta (
    id BIGSERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(20) UNIQUE NOT NULL,
    tipo_cuenta VARCHAR(20) NOT NULL CHECK (tipo_cuenta IN ('Ahorro', 'Corriente')),
    saldo_inicial DECIMAL(15, 2) NOT NULL DEFAULT 0,
    saldo_actual DECIMAL(15, 2) NOT NULL DEFAULT 0,
    estado BOOLEAN DEFAULT true,
    cliente_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla Movimiento
CREATE TABLE IF NOT EXISTS movimiento (
    id BIGSERIAL PRIMARY KEY,
    cuenta_id BIGINT NOT NULL REFERENCES cuenta(id) ON DELETE CASCADE,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo_movimiento VARCHAR(50) NOT NULL,
    valor DECIMAL(15, 2) NOT NULL,
    saldo_anterior DECIMAL(15, 2) NOT NULL,
    saldo_nuevo DECIMAL(15, 2) NOT NULL,
    descripcion VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ================================
-- ÍNDICES PARA MEJORAR PERFORMANCE
-- ================================

CREATE INDEX IF NOT EXISTS idx_cliente_persona_id ON cliente(persona_id);
CREATE INDEX IF NOT EXISTS idx_cliente_cliente_id ON cliente(cliente_id);
CREATE INDEX IF NOT EXISTS idx_cuenta_cliente_id ON cuenta(cliente_id);
CREATE INDEX IF NOT EXISTS idx_cuenta_numero ON cuenta(numero_cuenta);
CREATE INDEX IF NOT EXISTS idx_movimiento_cuenta_id ON movimiento(cuenta_id);
CREATE INDEX IF NOT EXISTS idx_movimiento_fecha ON movimiento(fecha);

-- ================================
-- DATOS DE PRUEBA
-- ================================

-- Insertar Personas
INSERT INTO persona (nombre, genero, edad, identificacion, direccion, telefono) VALUES
('Jose Lema', 'M', 35, '098254785', 'Otavalo sn y principal', '098254785'),
('Marianela Montalvo', 'F', 28, '097548965', 'Amazonas y NNUU', '097548965'),
('Juan Osorio', 'M', 42, '098874587', '13 junio y Equinoccial', '098874587')
ON CONFLICT (identificacion) DO NOTHING;

-- Insertar Clientes
INSERT INTO cliente (persona_id, cliente_id, contrasena, estado) VALUES
(1, '1234', '$2a$10$rO5nPz3fQKCxH9K7KQz8/.SqGzJxKxKxKxKxKxKxKxKxKxKxKx', true),
(2, '5678', '$2a$10$rO5nPz3fQKCxH9K7KQz8/.SqGzJxKxKxKxKxKxKxKxKxKxKxKx', true),
(3, '1245', '$2a$10$rO5nPz3fQKCxH9K7KQz8/.SqGzJxKxKxKxKxKxKxKxKxKxKxKx', true)
ON CONFLICT (cliente_id) DO NOTHING;

-- Insertar Cuentas
INSERT INTO cuenta (numero_cuenta, tipo_cuenta, saldo_inicial, saldo_actual, estado, cliente_id) VALUES
('478758', 'Ahorro', 2000.00, 2000.00, true, 1),
('225487', 'Corriente', 100.00, 100.00, true, 2),
('495878', 'Ahorro', 0.00, 0.00, true, 3),
('496825', 'Ahorro', 540.00, 540.00, true, 2),
('585545', 'Corriente', 1000.00, 1000.00, true, 1)
ON CONFLICT (numero_cuenta) DO NOTHING;

-- Insertar Movimientos de ejemplo
INSERT INTO movimiento (cuenta_id, fecha, tipo_movimiento, valor, saldo_anterior, saldo_nuevo, descripcion) VALUES
(1, '2022-02-08 00:00:00', 'Retiro', -575.00, 2000.00, 1425.00, 'Retiro de 575'),
(2, '2022-02-10 00:00:00', 'Deposito', 600.00, 100.00, 700.00, 'Deposito de 600'),
(3, '2022-02-08 00:00:00', 'Deposito', 150.00, 0.00, 150.00, 'Deposito de 150'),
(4, '2022-02-08 00:00:00', 'Retiro', -540.00, 540.00, 0.00, 'Retiro de 540');

-- ================================
-- FUNCIONES Y TRIGGERS
-- ================================

-- Función para actualizar timestamp de updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Trigger para Cliente
DROP TRIGGER IF EXISTS update_cliente_updated_at ON cliente;
CREATE TRIGGER update_cliente_updated_at
    BEFORE UPDATE ON cliente
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Trigger para Cuenta
DROP TRIGGER IF EXISTS update_cuenta_updated_at ON cuenta;
CREATE TRIGGER update_cuenta_updated_at
    BEFORE UPDATE ON cuenta
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

/*================================
 VISTAS ÚTILES
 ================================*/
-- Vista: Clientes con información completa
CREATE OR REPLACE VIEW vista_clientes_completa AS
SELECT 
    c.id as cliente_db_id,
    c.cliente_id,
    c.estado as cliente_estado,
    p.nombre,
    p.genero,
    p.edad,
    p.identificacion,
    p.direccion,
    p.telefono,
    c.created_at,
    c.updated_at
FROM cliente c
INNER JOIN persona p ON c.persona_id = p.id;

/* Vista: Resumen de cuentas por cliente*/
CREATE OR REPLACE VIEW vista_resumen_cuentas AS
SELECT 
    c.cliente_id,
    COUNT(cu.id) as total_cuentas,
    SUM(cu.saldo_actual) as saldo_total,
    SUM(CASE WHEN cu.tipo_cuenta = 'Ahorro' THEN cu.saldo_actual ELSE 0 END) as saldo_ahorro,
    SUM(CASE WHEN cu.tipo_cuenta = 'Corriente' THEN cu.saldo_actual ELSE 0 END) as saldo_corriente
FROM cliente c
LEFT JOIN cuenta cu ON c.id = cu.cliente_id AND cu.estado = true
GROUP BY c.cliente_id;

/*================================
 PERMISOS (opcional, según necesidad)
 ================================*/

-- GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO postgres;
-- GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO postgres;

/*================================
 COMENTARIOS EN TABLAS
 ================================*/

COMMENT ON TABLE persona IS 'Tabla base para almacenar información de personas';
COMMENT ON TABLE cliente IS 'Tabla de clientes que extiende Persona';
COMMENT ON TABLE cuenta IS 'Tabla de cuentas bancarias';
COMMENT ON TABLE movimiento IS 'Tabla de movimientos/transacciones bancarias';

-- ================================
-- VERIFICACIÓN
-- ================================

SELECT 'Base de datos inicializada correctamente' as status;
