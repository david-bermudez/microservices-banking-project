# Sistema Bancario - Microservicios

Sistema de gestión bancaria implementado con arquitectura de microservicios usando Spring Boot.

## Arquitectura

- **cliente-persona-service**: Gestión de clientes y personas
- **cuenta-movimiento-service**: Gestión de cuentas y movimientos bancarios
- Comunicación asíncrona mediante RabbitMQ
- Base de datos relacional (PostgreSQL/MySQL)

## Tecnologías

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- Spring AMQP (RabbitMQ)
- PostgreSQL
- Docker & Docker Compose
- Maven 3.8+

## Funcionalidades Implementadas

### CRUD 
- Cliente: Create, Read, Update, Delete
- Cuenta: Create, Read, Update
- Movimiento: Create, Read, Update

### Registro de Movimientos
- Movimientos con valores positivos y negativos
- Actualización automática de saldo
- Registro de transacciones

### Validación de Saldo
- Mensaje "Saldo no disponible" cuando el saldo es insuficiente

###  Reporte de Estado de Cuenta
- Endpoint: `/reportes?fecha=rango fechas & cliente={clienteId}`
- Retorna JSON con cuentas y movimientos

### Pruebas Unitarias
- Pruebas unitarias para entidad Cliente

### Despliegue en Docker
- Docker Compose con todos los servicios

## Estructura del Proyecto

```
microservices-banking/
├── cliente-persona-service/
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── cuenta-movimiento-service/
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── docker-compose.yml
├── BaseDatos.sql
├── Postman_Collection.json
├── README.md
└── OPENAPI_README.md
```

## Requisitos Previos

- Java 17+
- Maven 3.8+
- Docker & Docker Compose
- PostgreSQL (o usar el contenedor de Docker)

## Configuración y Ejecución

### Es necesario tener Docker (Recomendado)

```bash
# Levantar todos los servicios
docker-compose up -d
# o
docker-compose up --build --force-recreate
```

Los servicios estarán disponibles en:
- Cliente-Persona Service: http://localhost:8081
- Cuenta-Movimiento Service: http://localhost:8082
- RabbitMQ Management: http://localhost:15672 (usuario: guest, password: guest)

## Endpoints Principales

### Cliente-Persona Service (Puerto 8081)

```
POST   /clientes              - Crear cliente
GET    /clientes              - Listar clientes
GET    /clientes/{id}         - Obtener cliente
PUT    /clientes/{id}         - Actualizar cliente
DELETE /clientes/{id}         - Eliminar cliente
```

### Cuenta-Movimiento Service (Puerto 8082)

```
POST   /cuentas               - Crear cuenta
GET    /cuentas               - Listar cuentas
GET    /cuentas/{id}          - Obtener cuenta
PUT    /cuentas/{id}          - Actualizar cuenta

POST   /movimientos           - Registrar movimiento
GET    /movimientos           - Listar movimientos
GET    /movimientos/{id}      - Obtener movimiento

GET    /reportes?fecha=rango fechas&cliente={id}  - Reporte de estado de cuenta
```

## Ejemplos de Uso

### 1. Crear Usuario (Persona)

```bash
curl -X POST http://localhost:8081/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Jose Lema",
    "genero": "M",
    "edad": 35,
    "identificacion": "098254785",
    "direccion": "Otavalo 8 de noviembre y principal",
    "telefono": "098254785",
    "clienteId": "1234",
    "contrasena": "1234",
    "estado": true
  }'
```

### 2. Crear Cuenta

```bash
curl -X POST http://localhost:8082/cuentas \
  -H "Content-Type: application/json" \
  -d '{
    "numeroCuenta": "478758",
    "tipoCuenta": "Ahorro",
    "saldoInicial": 2000,
    "estado": true,
    "clienteId": 1
  }'
```

### 3. Registrar Movimiento

```bash
curl -X POST http://localhost:8082/movimientos \
  -H "Content-Type: application/json" \
  -d '{
    "cuentaId": 1,
    "tipoMovimiento": "Retiro",
    "valor": -575
  }'
```

### 4. Consultar Reporte

```bash
curl "http://localhost:8082/reportes?fecha=01/02/2022&cliente=1"
```

## Base de Datos

El script `BaseDatos.sql` incluye:
- Esquema de tablas
- Pequeña cantidad de datos de prueba

## Pruebas

```bash
# Ejecutar pruebas unitarias
mvn test

```

## Validación con Postman

Importar el archivo `Postman_Collection.json` en Postman para probar todos los endpoints.

## Autor
Desarrollado por **David Bermudez Sabagh**.
