# OpenAPI Quick Reference

## Swagger UI URLs

### Cliente Persona Service
```
http://localhost:8081/swagger-ui.html
```

### Cuenta Movimiento Service
```
http://localhost:8082/swagger-ui.html
```

## OpenAPI JSON URLs

### Cliente Persona Service
```
http://localhost:8081/v3/api-docs
```

### Cuenta Movimiento Service
```
http://localhost:8082/v3/api-docs
```

## API Endpoints Summary

### Cliente Persona Service (Port 8081)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /clientes | Crear un nuevo cliente |
| GET | /clientes | Obtener todos los clientes |
| GET | /clientes/{id} | Obtener cliente por ID |
| GET | /clientes/by-cliente-id/{clienteId} | Obtener cliente por clienteId |
| PUT | /clientes/{id} | Actualizar un cliente |
| DELETE | /clientes/{id} | Eliminar un cliente |

### Cuenta Movimiento Service (Port 8082)

#### Cuentas
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /cuentas | Crear una nueva cuenta |
| GET | /cuentas | Obtener todas las cuentas |
| GET | /cuentas?clienteId={id} | Filtrar cuentas por cliente |
| GET | /cuentas/{id} | Obtener cuenta por ID |
| PUT | /cuentas/{id} | Actualizar una cuenta |

#### Movimientos
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /movimientos | Registrar un nuevo movimiento |
| GET | /movimientos | Obtener todos los movimientos |
| GET | /movimientos?cuentaId={id} | Filtrar movimientos por cuenta |
| GET | /movimientos/{id} | Obtener movimiento por ID |

#### Reportes
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /reportes?fecha={fecha}&cliente={id} | Generar reporte de estado de cuenta |

**Formato de fecha para reportes:**
- Fecha única: `01/02/2022`
- Rango de fechas: `01/02/2022-10/02/2022`

## Pruebas con Swagger UI

1. Navegar a la URL de Swagger UI
2. Click en un endpoint para expandirlo
3. Click "Try it out" button
4. Llenar los parámetros requeridos
5. Click "Execute" para enviar la solicitud
6. Ver la respuesta debajo

## Ejemplos de Cuerpos de Solicitud

### Crear Cliente
```json
{
  "nombre": "Juan Pérez",
  "genero": "M",
  "edad": 30,
  "identificacion": "1234567890",
  "direccion": "Calle Principal 123",
  "telefono": "+593987654321",
  "clienteId": "CLI001",
  "contrasena": "password123",
  "estado": true
}
```

### Crear Cuenta
```json
{
  "numeroCuenta": "478758",
  "tipoCuenta": "Ahorro",
  "saldoInicial": 1000.00,
  "estado": true,
  "clienteId": 1
}
```

### Crear Movimiento
```json
{
  "cuentaId": 1,
  "tipoMovimiento": "Deposito",
  "valor": 100.00,
  "descripcion": "Depósito en efectivo"
}
```

## Códigos de respuesta comunes

| Code | Description |
|------|-------------|
| 200 | OK - Request successful |
| 201 | Created - Resource created successfully |
| 204 | No Content - Resource deleted successfully |
| 400 | Bad Request - Invalid input data |
| 404 | Not Found - Resource not found |
| 500 | Internal Server Error - Server error |

## Tips

- Todos los campos requeridos están marcados con un asterisco (*) en Swagger UI
- Los errores de validación devolverán 400 con detalles sobre lo que está mal
- Use las secciones "Schemas" al final para ver todos los modelos de datos
- El botón "Authorize" puede ser utilizado si se agrega autenticación más adelante
- Exportar el JSON OpenAPI para importar en Postman o otros herramientas