# Sistema de Matrícula Universitaria - Arquitectura MVC

Sistema integral de gestión de matrículas académicas desarrollado con el patrón arquitectónico **Modelo - Vista - Controlador (MVC)**, implementado en **Java 21**, **Spring Boot 4**, **Spring Data JPA**, **MySQL 8.0** y una interfaz interactiva de usuario **Single Page Application (SPA)** con TailwindCSS y JavaScript Fetch API.

---

## Características Principales

- **Mínimo 10 Tablas Relacionales (12 tablas implementadas)**: Totalmente normalizadas (3FN) con llaves primarias, foráneas, índices de unicidad y cascadas de integridad.
- **Estructura MVC Estricta**:
  - **Entity**: Modelos del dominio con mapeo ORM JPA (`@Entity`, `@Table`, `@ManyToOne`, `@OneToMany`).
  - **Repository**: Capa de persistencia con Spring Data JPA y consultas personalizadas JPQL.
  - **Service**: Capa de lógica de negocio transaccional (`@Transactional`), control de vacantes, suma de créditos y liquidación financiera.
  - **Controller**: Controladores REST (`@RestController`) con validaciones y manejo global de excepciones (`@RestControllerAdvice`).
  - **View (Frontend)**: Interfaz web responsiva conectada en tiempo real al backend.
- **Dashboard Estadístico**: Métricas en vivo (alumnos, cursos activos, secciones, matrículas y total recaudado en soles).
- **Proceso de Matrícula Inteligente**:
  - Selección dinámica de alumno y semestre lectivo.
  - Filtrado de secciones con cupos disponibles.
  - Validación de incompatibilidad de horarios y no duplicidad de asignaturas.
  - Cálculo instantáneo de créditos y tarifa de matrícula.
  - Descuento automático de vacantes e integración con caja de pagos.
  - Opción de anulación con restitución de vacantes.

---

## Modelo de Datos (12 Tablas)

El sistema cuenta con 12 tablas en base de datos `sistema_matricula`:

1. `carreras` - Carreras y programas académicos.
2. `planes_estudio` - Mallas curriculares vigentes.
3. `periodos_academicos` - Semestres lectivos (2026-I, 2026-II, etc.).
4. `cursos` - Asignaturas con créditos, horas y ciclo.
5. `docentes` - Catedráticos con grado académico y especialidad.
6. `aulas` - Ambientes de clases y laboratorios de cómputo.
7. `estudiantes` - Alumnos registrados con DNI y código de carnet.
8. `secciones` - Grupos de dictado por curso y periodo con vacantes limitadas.
9. `horarios` - Días, turnos y horas asignadas a cada sección y aula.
10. `matriculas` - Fichas de matrícula generadas por estudiante y periodo.
11. `detalles_matricula` - Asignaturas y secciones inscritas en la matrícula.
12. `pagos` - Comprobantes de pago y amortizaciones (efectivo, tarjeta, Yape).

Para ver el diagrama relacional completo en formato Mermaid, consulte [docs/DIAGRAMA_ER.md](docs/DIAGRAMA_ER.md).

---

## Estructura del Proyecto

```
sistema_matricula/
├── backend/                                <- Proyecto Spring Boot Maven
│   ├── src/main/java/com/example/backend/
│   │   ├── config/                         <- CORS y configuraciones
│   │   ├── controller/                     <- Controladores REST (MVC)
│   │   ├── dto/                            <- Objetos de transferencia y respuestas
│   │   ├── entity/                         <- Entidades JPA (12 tablas)
│   │   ├── exception/                      <- Manejo global de errores
│   │   ├── repository/                     <- Repositorios Spring Data JPA
│   │   └── service/                        <- Lógica de negocio e interfaces
│   ├── src/main/resources/
│   │   ├── application.properties          <- Conexión MySQL y JPA
│   │   └── static/                         <- Frontend integrado servido por Spring Boot
│   ├── pom.xml                             <- Dependencias Maven
│   └── mvnw.cmd                            <- Maven Wrapper ejecutable
├── database/                               <- Scripts SQL
│   ├── 01_schema.sql                       <- DDL: Creación de BD y 12 tablas
│   └── 02_data.sql                         <- DML: Datos iniciales de prueba (Seed)
├── docs/                                   <- Documentación Técnica
│   ├── DIAGRAMA_ER.md                      <- Diagrama Entidad-Relación Mermaid
│   ├── ARQUITECTURA_MVC.md                 <- Documentación técnica del patrón MVC
│   ├── DICCIONARIO_DATOS.md                <- Diccionario de 12 tablas y campos
│   └── API_ENDPOINTS.md                    <- Catálogo completo de endpoints REST
├── frontend/                               <- Interfaz Web SPA Standalone
│   ├── index.html                          <- Estructura HTML con TailwindCSS
│   ├── app.js                              <- Lógica de interacción y llamadas AJAX
│   └── styles.css                          <- Estilos complementarios
└── README.md                               <- Documentación principal
```

---

## Requisitos de Instalación

- **Java JDK**: Versión 21 o superior.
- **MySQL Server**: Versión 8.0 o superior (puerto default 3306).
- **Maven**: Incluido mediante `mvnw.cmd` (no requiere instalación externa).

---

##  Instrucciones de Ejecución

### 1. Inicializar la Base de Datos en MySQL
Ejecute los scripts de la carpeta `database/` en su consola o MySQL Workbench:

```powershell
# En consola de comandos o PowerShell:
mysql -u root -p < database/01_schema.sql
mysql -u root -p < database/02_data.sql
```
*Las credenciales configuradas por defecto en `application.properties` son: usuario `root`, contraseña `root`.*

### 2. Iniciar el Servidor Backend (Spring Boot)
Abra una terminal en la carpeta `backend` y ejecute:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

El servidor iniciará en:
 `http://localhost:8080`

### 3. Acceder a la Aplicación Web
Puede usar el sistema de dos formas:
1. **Directamente desde el navegador**: Al ingresar a `http://localhost:8080`, Spring Boot cargará la interfaz gráfica automáticamente desde la carpeta estática.
2. **Abriendo el archivo frontend**: También puede hacer doble clic en `frontend/index.html` en su navegador preferido.

---

## 📡 Endpoints de la API REST

| Módulo | Endpoint Principal | Métodos Soportados |
|---|---|---|
| **Dashboard** | `/api/dashboard/stats` | `GET` |
| **Matrículas** | `/api/matriculas` | `GET`, `POST`, `PATCH` (`/anular`) |
| **Estudiantes** | `/api/estudiantes` | `GET`, `POST`, `PUT`, `DELETE` |
| **Cursos** | `/api/cursos` | `GET`, `POST`, `PUT`, `DELETE` |
| **Secciones** | `/api/secciones` | `GET`, `POST`, `PUT`, `DELETE` |
| **Docentes** | `/api/docentes` | `GET`, `POST`, `PUT`, `DELETE` |
| **Pagos** | `/api/pagos` | `GET`, `POST` (`/matricula/{id}`) |
| **Carreras** | `/api/carreras` | `GET`, `POST`, `PUT`, `DELETE` |
| **Periodos** | `/api/periodos` | `GET`, `POST`, `PUT`, `DELETE` |
| **Aulas** | `/api/aulas` | `GET`, `POST`, `PUT`, `DELETE` |

---

## Datos de Prueba Listos para Demostración

- **Periodo Académico**: `2026-I` (Activo)
- **Estudiantes de ejemplo**:
  - `72839102` - Juan Carlos Gómez Pérez (Ing. Sistemas)
  - `73940182` - María Fernanda López Díaz (Ing. Sistemas)
  - `74829103` - Diego Alonso Vargas Torres (Ing. Sistemas)
  - `75930281` - Lucía Belén Ramos Castro (Ing. Industrial)
  - `76829174` - Kevin Alexander Chávez Romero (Administración)
- **Cursos Disponibles**: Algoritmos, Base de Datos, Desarrollo Web, Arquitectura de Software, Inteligencia Artificial, etc.
