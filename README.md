# Sofka Banking Microservices - Documentación Final

Este proyecto implementa una arquitectura de microservicios para la gestión bancaria, incluyendo clientes, cuentas, movimientos y reportes. Está dockerizado y listo para desarrollo, pruebas y despliegue en cualquier entorno compatible con Docker y Java 11+.

## Estructura Detallada del Proyecto

```
📁 sofka-banking-microservices/
├── 📁 sofka-account-service/                         # Microservicio de cuentas, movimientos y reportes
│   ├── 📄 Dockerfile                                 # Configuración Docker del microservicio
│   ├── 📄 pom.xml                                    # Dependencias Maven del microservicio
│   └── 📁 src/main/java/com/sofka/banking/account/
│       ├── 📄 AccountServiceApplication.java         # Clase principal Spring Boot
│       ├── 📁 controller/                            # Controladores REST (endpoints de API)
│       │   ├── 📄 CuentaController.java              # CRUD de cuentas (/api/cuentas)
│       │   ├── 📄 MovimientoController.java          # CRUD de movimientos (/api/movimientos)
│       │   └── 📄 ReporteController.java             # Endpoints de reportes (/api/reportes)
│       ├── 📁 dto/                                   # Data Transfer Objects (objetos de respuesta/petición)
│       │   ├── 📄 CuentaDto.java                     # DTO para transferencia de datos de cuentas
│       │   ├── 📄 MovimientoDto.java                 # DTO para transferencia de datos de movimientos
│       │   └── 📄 ReporteDto.java                    # DTO para respuestas de reportes
│       ├── 📁 entity/                                # Entidades JPA (mapeo de tablas de BD)
│       │   ├── 📄 Cuenta.java                        # Entidad para tabla ba_cuentas
│       │   └── 📄 Movimiento.java                    # Entidad para tabla ba_movimientos
│       ├── 📁 exception/                             # Manejo de excepciones y errores personalizados
│       │   ├── 📄 GlobalExceptionHandler.java        # Controlador global de errores HTTP
│       │   ├── 📄 CuentaNotFoundException.java       # Error cuando no se encuentra cuenta
│       │   ├── 📄 CuentaAlreadyExistsException.java  # Error cuando cuenta ya existe
│       │   ├── 📄 SaldoInsuficienteException.java    # Error de saldo insuficiente
│       │   └── 📄 ClienteNotFoundException.java      # Error cuando cliente no existe
│       ├── 📁 repository/                            # Interfaces de acceso a datos (Spring Data JPA)
│       │   ├── 📄 CuentaRepository.java              # Consultas y operaciones CRUD de cuentas
│       │   └── 📄 MovimientoRepository.java          # Consultas y operaciones CRUD de movimientos
│       ├── 📁 service/                               # Lógica de negocio y reglas bancarias
│       │   ├── 📄 CuentaService.java                 # Lógica de negocio para cuentas
│       │   ├── 📄 MovimientoService.java             # Lógica de negocio para movimientos
│       │   └── 📄 ReporteService.java                # Lógica de generación de reportes
│       └── 📁 resources/                             # Archivos de configuración
│           ├── 📄 application.yml                    # Configuración general
│           ├── 📄 application-dev.yml                # Configuración para desarrollo
│           ├── 📄 application-prod.yml               # Configuración para producción
│           ├── 📄 schema.sql                         # Script de creación de tablas
│           └── 📄 data.sql                           # Script de datos de prueba
│
├── 📁 sofka-client-service/                          # Microservicio de gestión de clientes
│   ├── 📄 Dockerfile                                 # Configuración Docker del microservicio
│   ├── 📄 pom.xml                                    # Dependencias Maven del microservicio
│   └── 📁 src/main/java/com/sofka/banking/client/
│       ├── 📄 ClientServiceApplication.java          # Clase principal Spring Boot
│       ├── 📁 controller/                            # Controladores REST
│       │   └── 📄 ClienteController.java             # CRUD de clientes (/api/clientes)
│       ├── 📁 dto/                                   # Data Transfer Objects
│       │   └── 📄 ClienteDto.java                    # DTO para transferencia de datos de clientes
│       ├── 📁 entity/                                # Entidades JPA
│       │   ├── 📄 Cliente.java                       # Entidad para tabla ba_clientes
│       │   └── 📄 Persona.java                       # Entidad base para datos personales
│       ├── 📁 exception/                             # Manejo de excepciones
│       │   └── 📄 ClienteAlreadyExistsException.java # Error cuando cliente ya existe
│       ├── 📁 repository/                            # Interfaces de acceso a datos
│       │   └── 📄 ClienteRepository.java             # Consultas y operaciones CRUD de clientes
│       ├── 📁 service/                               # Lógica de negocio
│       │   └── 📄 ClienteService.java                # Lógica de negocio para clientes
│       └── 📁 resources/                             # Archivos de configuración
│           ├── 📄 application.yml                    # Configuración general
│           ├── 📄 application-dev.yml                # Configuración para desarrollo
│           ├── 📄 application-prod.yml               # Configuración para producción
│           ├── 📄 schema.sql                         # Script de creación de tablas
│           └── 📄 data.sql                           # Script de datos de prueba
│
├── 📁 BaseDatos/                                     # Scripts SQL para inicialización
│   ├── 📄 accountdb.sql                              # Script completo para base de datos de cuentas
│   └── 📄 clientdb.sql                               # Script completo para base de datos de clientes
│
├── 📁 postman/                                                     # Documentación y pruebas de API
│   ├── 📄 Banking API - Microservicios V2.postman_collection.json  # Colección completa
│   └── 📄 README.md                                                # Guía de uso de la colección Postman
└── 📄 pom.xml                                                      # POM padre para gestión de dependencias
```

### Descripción de Paquetes y Responsabilidades

#### 📁 controller/
- **Propósito**: Exponer endpoints REST y manejar peticiones HTTP
- **Responsabilidad**: Validar entrada, delegar a servicios y formatear respuestas
- **Patrones**: REST, HTTP status codes, validaciones con @Valid

#### 📁 dto/
- **Propósito**: Objetos para transferencia de datos entre capas y API
- **Responsabilidad**: Encapsular datos de entrada/salida sin exponer entidades internas
- **Patrones**: DTO Pattern, validaciones Bean Validation

#### 📁 entity/
- **Propósito**: Mapeo objeto-relacional de las tablas de base de datos
- **Responsabilidad**: Representar estructura de datos y relaciones JPA
- **Patrones**: JPA/Hibernate annotations, Entity mapping

#### 📁 exception/
- **Propósito**: Manejo centralizado de errores y excepciones
- **Responsabilidad**: Capturar errores, formatear respuestas HTTP y logging
- **Patrones**: Global Exception Handler, Custom Exceptions

#### 📁 repository/
- **Propósito**: Capa de acceso a datos y consultas a base de datos
- **Responsabilidad**: CRUD operations, consultas personalizadas
- **Patrones**: Repository Pattern, Spring Data JPA

#### 📁 service/
- **Propósito**: Lógica de negocio y reglas bancarias
- **Responsabilidad**: Implementar casos de uso, validaciones de negocio, transacciones
- **Patrones**: Service Layer, Transaction Management

#### 📁 resources/
- **Propósito**: Configuración de aplicación y scripts de base de datos
- **Responsabilidad**: Propiedades de conexión, perfiles de entorno, datos iniciales
- **Patrones**: Configuration by profiles, externalized configuration

## Microservicios

### 1. sofka-account-service
- **Funcionalidad:**
  - CRUD de cuentas bancarias.
  - Registro y gestión de movimientos (depósitos, retiros).
  - Reportes por cliente, cuenta y rango de fechas.
  - Validación de saldo y lógica de negocio robusta.
- **Puerto:** 8082
- **Base de datos:** `accountdb`
- **Configuración:**
  - Variables de entorno para conexión a MySQL.
  - Configuración en `application-prod.yml`.

### 2. sofka-client-service
- **Funcionalidad:**
  - CRUD de clientes.
  - Activación/desactivación y eliminación lógica/física.
- **Puerto:** 8081
- **Base de datos:** `clientdb`
- **Configuración:**
  - Variables de entorno para conexión a MySQL.
  - Configuración en `application-prod.yml`.

## Base de Datos
- **MySQL 8.0**
- Inicialización automática con scripts:
  - `BaseDatos/accountdb.sql`: Estructura y datos de cuentas y movimientos.
  - `BaseDatos/clientdb.sql`: Estructura y datos de clientes.
- Usuarios y contraseñas configurados en `docker-compose.yml`.

## Docker y Orquestación
- **docker-compose.yml**:
  - Levanta MySQL y ambos microservicios.
  - Expone puertos 8081 (clientes), 8082 (cuentas/movimientos/reportes), 3306 (MySQL).
  - Variables de entorno para cada servicio.
  - Inicializa la base de datos con los scripts SQL.

## Pruebas de API con Postman
- **Colección:** `postman/Banking API - Microservicios V2.postman_collection.json`
- **Variables:**
  - `baseURLCLI`: URL para microservicio de clientes (por defecto: `http://localhost:8081`)
  - `baseURLCU`: URL para microservicio de cuentas (por defecto: `http://localhost:8082`)
- **Endpoints cubiertos:**
  - CRUD de clientes, cuentas, movimientos.
  - Reportes por cliente/cuenta y rango de fechas.
  - Validaciones de negocio y ejemplos de errores.
- **Uso:**
  1. Importa la colección en Postman.
  2. Ajusta las variables si cambias puertos o entorno.
  3. Ejecuta los endpoints agrupados por servicio y funcionalidad.

## Modelo de Base de Datos y Relaciones

### Estructura de Bases de Datos

El proyecto utiliza **dos bases de datos separadas** para seguir el patrón de microservicios:

#### 🗄️ **clientdb** - Base de Datos de Clientes
```sql
ba_clientes
├── cl_id_cliente (INT, PK, AUTO_INCREMENT)     # ID interno único
├── cl_nombre (VARCHAR(100))                    # Nombre completo del cliente
├── cl_genero (VARCHAR(20))                     # Género (Masculino/Femenino)
├── cl_edad (INT)                               # Edad del cliente
├── cl_identificacion (VARCHAR(20), UNIQUE)     # Cédula o documento de identidad
├── cl_direccion (VARCHAR(255))                 # Dirección física
├── cl_telefono (VARCHAR(20))                   # Número de teléfono
├── cl_id_cliente_codigo (VARCHAR(20), UNIQUE)  # Código público del cliente (ej: CLI001)
├── cl_contrasena (VARCHAR(255))                # Contraseña encriptada
├── cl_estado (BOOLEAN)                         # Estado activo/inactivo
├── cl_fecha_creacion (TIMESTAMP)               # Fecha de creación del registro
└── cl_fecha_actualizacion (TIMESTAMP)          # Fecha de última actualización
```

#### 🗄️ **accountdb** - Base de Datos de Cuentas y Movimientos
```sql
ba_cuentas
├── cu_id_cuenta (INT, PK, AUTO_INCREMENT)      # ID interno único de la cuenta
├── cu_numero_cuenta (VARCHAR(20), UNIQUE)      # Número de cuenta público (ej: 478758)
├── cu_tipo_cuenta (VARCHAR(20))                # Tipo: "Ahorro" o "Corriente"
├── cu_saldo_inicial (DECIMAL(15,2))            # Saldo al momento de crear la cuenta
├── cu_saldo_actual (DECIMAL(15,2))             # Saldo actual de la cuenta
├── cu_estado (BOOLEAN)                         # Estado activo/inactivo
├── cu_id_cliente (VARCHAR(20))                 # FK hacia cl_id_cliente_codigo
├── cu_fecha_creacion (TIMESTAMP)               # Fecha de creación de la cuenta
└── cu_fecha_actualizacion (TIMESTAMP)          # Fecha de última actualización

ba_movimientos
├── mo_id_movimiento (INT, PK, AUTO_INCREMENT)  # ID interno único del movimiento
├── mo_fecha (TIMESTAMP)                        # Fecha y hora del movimiento
├── mo_tipo_movimiento (VARCHAR(20))            # Tipo: "DEPOSITO" o "RETIRO"
├── mo_valor (DECIMAL(15,2))                    # Valor del movimiento
├── mo_saldo (DECIMAL(15,2))                    # Saldo resultante después del movimiento
├── mo_descripcion (VARCHAR(255))               # Descripción opcional del movimiento
└── cu_id_cuenta (INT, FK)                      # FK hacia ba_cuentas.cu_id_cuenta
```

### 🔗 Relaciones y Claves Foráneas

#### **Relación Cliente ↔ Cuenta** (Inter-Microservicio)
```
ba_clientes.cl_id_cliente_codigo (clientdb) ←→ ba_cuentas.cu_id_cliente (accountdb)
```
- **Tipo**: Uno a Muchos (1:N)
- **Descripción**: Un cliente puede tener múltiples cuentas bancarias
- **Implementación**: Referencia por código público (no FK física por estar en bases separadas)
- **Validación**: Se valida existencia del cliente via HTTP entre microservicios

#### **Relación Cuenta ↔ Movimiento** (Intra-Microservicio)
```sql
ba_cuentas.cu_id_cuenta (accountdb) ←→ ba_movimientos.cu_id_cuenta (accountdb)
FOREIGN KEY (cu_id_cuenta) REFERENCES ba_cuentas(cu_id_cuenta)
```
- **Tipo**: Uno a Muchos (1:N)
- **Descripción**: Una cuenta puede tener múltiples movimientos (depósitos/retiros)
- **Implementación**: Clave foránea física en la base de datos
- **Restricción**: No se puede eliminar una cuenta que tenga movimientos asociados

### 📊 Diagrama de Relaciones

```
┌──────────────────┐         ┌─────────────────┐         ┌─────────────────┐
│   ba_clientes    │         │   ba_cuentas    │         │ ba_movimientos  │
│   (clientdb)     │         │   (accountdb)   │         │   (accountdb)   │
├──────────────────┤         ├─────────────────┤         ├─────────────────┤
│ cl_id_cliente    │PK       │ cu_id_cuenta    │PK       │ mo_id_movimiento│PK
│ cl_id_cliente_   │────────→│ cu_id_cliente   │         │ mo_fecha        │
│ codigo (UNIQUE)  │   1:N   │ cu_numero_cuenta│         │ mo_tipo_mov     │
│ cl_nombre        │         │ cu_tipo_cuenta  │         │ mo_valor        │
│ cl_identificacion│         │ cu_saldo_inicial│         │ mo_saldo        │
│ cl_telefono      │         │ cu_saldo_actual │         │ mo_descripcion  │
│ cl_estado        │         │ cu_estado       │         │ cu_id_cuenta    │FK
└──────────────────┘         └─────────────────┘         └─────────────────┘
      (Cliente)                   (Cuenta)        ←──1:N──    (Movimiento)
                                       │                           ↑
                                       └───────FK FÍSICA──────────┘
```

### 🎯 Estrategia de Identificadores

#### **Para la API Pública:**
- **Clientes**: Se identifican por `cl_id_cliente_codigo` (ej: "CLI001")
- **Cuentas**: Se identifican por `cu_numero_cuenta` (ej: "478758")
- **Movimientos**: Se referencian por `mo_id_movimiento` pero operan via número de cuenta

#### **Para Relaciones Internas:**
- **Base de datos**: Usa IDs internos (`cu_id_cuenta`, `mo_id_movimiento`) para optimización
- **JPA/Hibernate**: Mapea relaciones por IDs internos para integridad referencial
- **Microservicios**: Comunican usando identificadores públicos (códigos/números)

### 📝 Scripts SQL Detallados
- **accountdb.sql:**
  - Crea tablas `ba_cuentas`, `ba_movimientos` con claves foráneas
  - Inserta datos de prueba con relaciones por ID interno
  - Configura índices y restricciones para integridad referencial
- **clientdb.sql:**
  - Crea tabla `ba_clientes` con índices únicos
  - Inserta datos de prueba de clientes con códigos públicos
  - Configura validaciones de dominio para campos como género y estado

## Configuración y Variables
- **Variables de entorno en Docker Compose:**
  - `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD` para cada microservicio.
- **Configuración de puertos:**
  - Los puertos en `application-prod.yml` deben coincidir con los expuestos en Docker Compose.

## Ejecución
1. Clona el repositorio y navega al directorio raíz.
2. Ejecuta `docker compose up -d --build` para levantar todos los servicios.
3. Verifica que los contenedores estén corriendo y los puertos estén expuestos.
4. Importa la colección Postman y prueba los endpoints.

## Notas y Recomendaciones
- Si cambias puertos o credenciales, actualiza tanto los archivos de configuración como las variables en Postman.
- Los endpoints están documentados y agrupados por servicio en la colección Postman.
- La arquitectura permite escalar y modificar cada microservicio de forma independiente.

