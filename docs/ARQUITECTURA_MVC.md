# Arquitectura MVC y Patrones de Diseño
## Sistema de Matrícula Universitaria

Este proyecto implementa el patrón arquitectónico **Modelo - Vista - Controlador (MVC)** en capas mediante el ecosistema **Spring Boot 4 / Java 21** y base de datos relacional **MySQL 8.0**.

---

### 1. Diagrama de Capas del Sistema

```
  +--------------------------------------------------------------+
  |                   VISTA (Presentation Layer)                 |
  |  - Single Page Application (HTML5, TailwindCSS, JavaScript)  |
  |  - Consumo asíncrono con Fetch API y formato JSON            |
  |  - Servida estáticamente en Spring Boot (:8080)              |
  +--------------------------------------------------------------+
                                |
                                v (HTTP REST / JSON)
  +--------------------------------------------------------------+
  |               CONTROLADOR (Controller Layer)                 |
  |  - @RestController y @RequestMapping                         |
  |  - Enrutamiento de peticiones GET, POST, PUT, DELETE, PATCH  |
  |  - Conversión a DTO y Respuestas estandarizadas ApiResponse  |
  |  - Manejo global de excepciones (@RestControllerAdvice)      |
  +--------------------------------------------------------------+
                                |
                                v (Inyección de Dependencias)
  +--------------------------------------------------------------+
  |                 SERVICIO (Business Service Layer)            |
  |  - Interfaces (ICarreraService, IMatriculaService, etc.)     |
  |  - Implementaciones @Service con @Transactional             |
  |  - Reglas de negocio:                                        |
  |    * Control de cupos y decremento de vacantes               |
  |    * Suma de créditos y validación de doble inscripción      |
  |    * Verificación de estado de alumno y periodo lectivo      |
  |    * Emisión de código de matrícula y registro de pago       |
  +--------------------------------------------------------------+
                                |
                                v
  +--------------------------------------------------------------+
  |              REPOSITORIO (Data Access Layer - DAO)           |
  |  - Interfaces Spring Data JPA extends JpaRepository<T, ID>   |
  |  - Consultas personalizadas JPQL (@Query)                    |
  |  - Métodos automáticos por convención (findByDni, etc.)       |
  +--------------------------------------------------------------+
                                |
                                v (JPA / Hibernate ORM)
  +--------------------------------------------------------------+
  |                  MODELO (Entity / Domain Layer)              |
  |  - Clases @Entity JPA con llaves primarias y foráneas        |
  |  - Relaciones @ManyToOne, @OneToMany, @JoinColumn            |
  |  - Restricciones de unicidad y auditoría de estado           |
  +--------------------------------------------------------------+
                                |
                                v (JDBC Driver)
  +--------------------------------------------------------------+
  |                 BASE DE DATOS (MySQL 8.0)                    |
  |  - 12 Tablas normalizadas en InnoDB con UTF-8mb4             |
  +--------------------------------------------------------------+
```

---

### 2. Estructura de Paquetes en Java
```
com.example.backend
├── config
│   └── CorsConfig.java                     <- Configuración CORS para accesos Web
├── controller
│   ├── AulaController.java                 <- Endpoints para Aulas
│   ├── CarreraController.java              <- Endpoints para Carreras
│   ├── CursoController.java                <- Endpoints para Cursos
│   ├── DashboardController.java            <- Métricas en tiempo real
│   ├── DocenteController.java              <- Endpoints para Profesores
│   ├── EstudianteController.java           <- CRUD de Alumnos
│   ├── MatriculaController.java            <- Transacciones de Matrícula
│   ├── PagoController.java                 <- Pagos de Matrícula
│   ├── PeriodoAcademicoController.java     <- Semestres académicos
│   ├── PlanEstudioController.java          <- Mallas curriculares
│   └── SeccionController.java              <- Secciones y vacantes
├── dto
│   ├── ApiResponse.java                    <- Envoltorio uniforme de respuestas
│   ├── DashboardStatsDTO.java              <- KPIs del sistema
│   ├── DetalleMatriculaDTO.java            <- Detalle plano de curso
│   ├── MatriculaRequestDTO.java            <- Payload de creación
│   └── MatriculaResponseDTO.java           <- Salida formateada
├── entity
│   ├── Aula.java
│   ├── Carrera.java
│   ├── Curso.java
│   ├── DetalleMatricula.java
│   ├── Docente.java
│   ├── Estudiante.java
│   ├── Horario.java
│   ├── Matricula.java
│   ├── Pago.java
│   ├── PeriodoAcademico.java
│   ├── PlanEstudio.java
│   └── Seccion.java
├── exception
│   ├── BadRequestException.java
│   ├── GlobalExceptionHandler.java         <- Captura centralizada de errores
│   └── ResourceNotFoundException.java
├── repository
│   ├── AulaRepository.java
│   ├── CarreraRepository.java
│   ├── CursoRepository.java
│   ├── DetalleMatriculaRepository.java
│   ├── DocenteRepository.java
│   ├── EstudianteRepository.java
│   ├── HorarioRepository.java
│   ├── MatriculaRepository.java
│   ├── PagoRepository.java
│   ├── PeriodoAcademicoRepository.java
│   ├── PlanEstudioRepository.java
│   └── SeccionRepository.java
└── service
    ├── IAulaService.java / impl
    ├── ICarreraService.java / impl
    ├── ICursoService.java / impl
    ├── IDashboardService.java / impl
    ├── IDocenteService.java / impl
    ├── IEstudianteService.java / impl
    ├── IMatriculaService.java / impl
    ├── IPagoService.java / impl
    ├── IPeriodoAcademicoService.java / impl
    ├── IPlanEstudioService.java / impl
    └── ISeccionService.java / impl
```

---

### 3. Principios y Buenas Prácticas Aplicadas
- **Single Responsibility Principle (SRP)**: Cada controlador se encarga únicamente de enrutar HTTP; cada servicio de la lógica de negocio; cada repositorio de la persistencia.
- **Inversión de Control (IoC) e Inyección de Dependencias (DI)**: Inyección por constructor mediante Spring, facilitando el desacoplamiento y pruebas unitarias.
- **Transaccionalidad `@Transactional`**: Al registrar una matrícula, si ocurre una excepción (falta de vacantes o fallo en base de datos), se realiza Rollback automático protegiendo la integridad de los datos.
- **DTO Pattern**: Evita exponer la base de datos de manera directa y previene bucles de serialización infinita JSON típicos de relaciones bidireccionales JPA.
