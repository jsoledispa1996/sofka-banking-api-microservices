
# 📋 Documentación Completa de API - Banking Microservices

Esta carpeta contiene el archivo único `Banking API - Microservicios V2.postman_collection.json` para importar en Postman y probar todos los endpoints del API de Sofka Banking Microservices.

## 📁 Estructura
- `Banking API - Microservicios V2.postman_collection.json`: Colección única con todos los endpoints agrupados por servicio:
  - 🏦 **Client Service** (8081)
  - 💰 **Account Service - Cuentas** (8082)
  - 💸 **Account Service - Movimientos** (8082)
  - 📊 **Account Service - Reportes** (8082)

## 🔗 Variables de Entorno en Postman
La colección utiliza variables configurables para facilitar el cambio de entorno:

| Variable     | Valor por Defecto       | Descripción                                                    |
|--------------|-------------------------|----------------------------------------------------------------|
| `baseURLCLI` | `http://localhost:8081` | URL base para el microservicio de clientes                     |
| `baseURLCU`  | `http://localhost:8082` | URL base para el microservicio de cuentas/movimientos/reportes |

**📝 Configuración de Variables:**
1. En Postman, ve a la pestaña **Variables** de la colección
2. Modifica los valores según tu entorno (Docker, desarrollo, producción)
3. Para Docker: mantén `localhost` y los puertos por defecto
4. Para nube: cambia a la URL del servidor correspondiente

## 📊 Endpoints Detallados por Microservicio

### 🏦 **Client Service** (Puerto 8081)

| Método    | Endpoint                              | Descripción                        | Ejemplo de Uso                   |
|-----------|---------------------------------------|------------------------------------|----------------------------------|
| `GET`     | `/api/clientes/health`                | Verificar estado del microservicio | Monitoreo y health checks        |
| `POST`    | `/api/clientes`                       | Crear un nuevo cliente             | Registro de nuevos usuarios      |
| `PUT`     | `/api/clientes/{id}`                  | Actualizar cliente por ID interno  | Modificar datos completos        |
| `PATCH`   | `/api/clientes/{id}/estado`           | Activar/desactivar cliente         | Gestión de estados               |
| `DELETE`  | `/api/clientes/{id}`                  | Eliminación lógica (soft delete)   | Desactivar cliente               |
| `DELETE`  | `/api/clientes/{id}/fisico`           | Eliminación física (hard delete)   | Remover cliente completamente    |
| `GET`     | `/api/clientes/clienteId/{clienteId}` | Buscar por código público          | Búsqueda por CLI001, CLI002, etc.|
| `GET`     | `/api/clientes`                       | Listar todos los clientes          | Administración y reportes        |

**📝 Ejemplo de Payload para Crear Cliente:**
```json
{
  "nombre": "José Soledispa",
  "genero": "Masculino",
  "edad": 30,
  "identificacion": "2400036498",
  "direccion": "Calle 123",
  "telefono": "0960696292",
  "clienteId": "CLI105",
  "contrasena": "prueba123"
}
```

### 💰 **Account Service - Cuentas** (Puerto 8082)

| Método    | Endpoint                              | Descripción                         | Ejemplo de Uso                        |
|-----------|---------------------------------------|-------------------------------------|---------------------------------------|
| `GET`     | `/api/cuentas/health`                 | Verificar estado del microservicio  | Monitoreo y health checks             |
| `POST`    | `/api/cuentas`                        | Crear nueva cuenta bancaria         | Apertura de cuentas                   |
| `PUT`     | `/api/cuentas/{id}`                   | Actualizar cuenta por ID interno    | Modificar datos por ID                |
| `PUT`     | `/api/cuentas/numero/{numeroCuenta}`  | Actualizar cuenta por número        | Modificar por número público          |
| `DELETE`  | `/api/cuentas/{id}`                   | Eliminación lógica de cuenta        | Desactivar cuenta                     |
| `DELETE`  | `/api/cuentas/{id}/fisico`            | Eliminación física de cuenta        | Remover cuenta (solo sin movimientos) |
| `GET`     | `/api/cuentas/numero/{numeroCuenta}`  | Buscar cuenta por número            | Consulta por 478758, 225487, etc.     |
| `GET`     | `/api/cuentas/cliente/{clienteId}`    | Listar cuentas de un cliente        | Consultar cuentas de CLI001           |
| `GET`     | `/api/cuentas`                        | Listar todas las cuentas            | Administración y reportes             |
| `PATCH`   | `/api/cuentas/{id}/estado`            | Activar/desactivar cuenta           | Gestión de estados                    |

**📝 Ejemplo de Payload para Crear Cuenta:**
```json
{
  "numeroCuenta": "2400040",
  "tipoCuenta": "Ahorro",
  "saldoInicial": 10000.00,
  "saldoActual": 10000.00,
  "estado": true,
  "clienteId": "CLI100"
}
```

### 💸 **Account Service - Movimientos** (Puerto 8082)

| Método    | Endpoint                                  | Descripción                                   | Ejemplo de Uso                  |
|-----------|-------------------------------------------|-----------------------------------------------|---------------------------------|
| `GET`     | `/api/movimientos/health`                 | Verificar estado del microservicio            | Monitoreo y health checks       |
| `POST`    | `/api/movimientos`                        | Registrar movimiento (depósito/retiro)        | Transacciones bancarias         |
| `PATCH`   | `/api/movimientos/{id}`                   | Actualizar descripción del movimiento         | Corregir descripciones          |
| `DELETE`  | `/api/movimientos/{id}`                   | Eliminar movimiento y ajustar saldo           | Reversar transacciones          |
| `GET`     | `/api/movimientos`                        | Listar todos los movimientos                  | Administración y auditoría      |
| `GET`     | `/api/movimientos/cuenta/{numeroCuenta}`  | Movimientos de una cuenta específica          | Historial de cuenta             |
| `GET`     | `/api/movimientos/cliente/{clienteId}`    | Movimientos de todas las cuentas del cliente  | Historial completo del cliente  |

**📝 Ejemplo de Payload para Depósito:**
```json
{
  "tipoMovimiento": "DEPOSITO",
  "valor": 6000.00,
  "numeroCuenta": "2400036",
  "descripcion": "Depósito en efectivo"
}
```

**📝 Ejemplo de Payload para Retiro:**
```json
{
  "tipoMovimiento": "RETIRO",
  "valor": 1000.00,
  "numeroCuenta": "2400036",
  "descripcion": "Retiro en cajero"
}
```

### 📊 **Account Service - Reportes** (Puerto 8082)

| Método | Endpoint                                           | Descripción                               | Parámetros                |
|--------|----------------------------------------------------|-------------------------------------------|---------------------------|
| `GET` | `/api/reportes/health`                              | Verificar estado del microservicio        | -                         |
| `GET` | `/api/reportes/cliente/{clienteId}`                 | Reporte completo por cliente y fechas     | `fechaInicio`, `fechaFin` |
| `GET` | `/api/reportes/cuenta/{numeroCuenta}`               | Reporte de cuenta específica por fechas   | `fechaInicio`, `fechaFin` |
| `GET` | `/api/reportes/cliente/{clienteId}/recientes`       | Movimientos recientes del cliente         | `dias` (ej: 30)           |
| `GET` | `/api/reportes/cuenta/{numeroCuenta}/estadisticas`  | Estadísticas de cuenta por fechas         | `fechaInicio`, `fechaFin` |
| `GET` | `/api/reportes/cliente/{clienteId}/hoy`             | Movimientos del día actual                | -                         |
| `GET` | `/api/reportes/cliente/{clienteId}/semana`          | Movimientos de la semana actual           | -                         |
| `GET` | `/api/reportes/cliente/{clienteId}/mes`             | Movimientos del mes actual                | -                         |

**📝 Ejemplo de URL con Parámetros:**
```
GET {{baseURLCU}}/api/reportes/cliente/CLI100?fechaInicio=2025-01-01T00:00:00&fechaFin=2025-12-31T23:59:59
```

**📝 Ejemplo de Respuesta de Reporte:**
```json
{
  "clienteId": "CLI001",
  "cuentas": [{
    "numeroCuenta": "478758",
    "saldoActual": 1500.00,
    "movimientos": [{
      "tipoMovimiento": "DEPOSITO",
      "valor": 100.00,
      "fecha": "2025-08-28T10:30:00",
      "descripcion": "Depósito en efectivo"
    }]
  }]
}
```

## 🚨 Manejo de Errores y Códigos de Respuesta

### 📋 Códigos HTTP Utilizados:
- **200 OK**: Operación exitosa
- **201 Created**: Recurso creado exitosamente
- **400 Bad Request**: Datos de entrada inválidos
- **404 Not Found**: Recurso no encontrado
- **409 Conflict**: Conflicto (ej: saldo insuficiente, recurso ya existe)
- **500 Internal Server Error**: Error interno del servidor

### 🔥 Ejemplo de Error - Saldo Insuficiente:
```json
{
  "error": "Saldo no disponible",
  "timestamp": "2025-08-29T10:30:00",
  "status": 409,
  "message": "Saldo insuficiente para realizar el retiro"
}
```

## 🔄 Flujo de Uso Recomendado

### 1. **⚙️ Configuración Inicial:**
```bash
# Levantar servicios con Docker
docker compose up -d --build

# Verificar que los servicios estén corriendo
curl http://localhost:8081/api/clientes/health
curl http://localhost:8082/api/cuentas/health
```

### 2. **📋 Flujo de Creación Completo:**
1. **Crear Cliente** → `POST /api/clientes`
2. **Crear Cuenta** → `POST /api/cuentas` (usar clienteId del paso 1)
3. **Realizar Movimientos** → `POST /api/movimientos` (usar numeroCuenta del paso 2)
4. **Generar Reportes** → `GET /api/reportes/cliente/{clienteId}`

### 3. **🧪 Pruebas en Postman:**
1. Importar `Banking API - Microservicios V2.postman_collection.json`
2. Configurar variables `baseURLCLI` y `baseURLCU` según tu entorno
3. Ejecutar requests en el orden: Clientes → Cuentas → Movimientos → Reportes
4. Verificar respuestas y códigos de estado HTTP

## 📋 Formato de reportes y respuestas
- Todas las respuestas son en formato JSON.
- Los reportes incluyen cuentas asociadas, saldos, detalle de movimientos y estadísticas según el endpoint.
- Ejemplos de respuesta y errores están documentados en la colección.

## 🚀 Ejemplo de uso paso a paso
1. Importa el archivo `Banking API - Microservicios V2.postman_collection.json` en Postman.
2. Revisa y ajusta las variables `baseURLCLI` y `baseURLCU` si usas puertos o entornos diferentes.
3. Ejecuta los endpoints agrupados por servicio y funcionalidad. Cada request tiene ejemplos y documentación en la descripción.
4. Para pruebas en Docker, asegúrate que los puertos 8081 y 8082 estén expuestos y los servicios estén corriendo.

## 📌 Notas adicionales
- Si usas Docker, los puertos por defecto son 8081 (clientes) y 8082 (cuentas/movimientos/reportes).
- Si cambias los puertos en el `docker-compose.yml` o en los archivos de configuración, actualiza las variables en Postman.
- La colección cubre todos los casos de negocio, errores y ejemplos de respuesta para facilitar pruebas y desarrollo.
- Cada endpoint tiene ejemplos de request/response documentados en Postman.
- Los endpoints de health permiten verificar el estado de cada microservicio.

---

**📧 Soporte:** Para consultas o problemas con la API, consulta la documentación principal del proyecto.
**🔧 Configuración avanzada:** Revisa el archivo `docker-compose.yml` y `application-prod.yml` para personalizaciones.

