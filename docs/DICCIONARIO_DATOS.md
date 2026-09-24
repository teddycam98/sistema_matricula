# Diccionario de Datos
## Sistema de Matrícula (12 Tablas)

A continuación se detalla la estructura formal de las 12 tablas de la base de datos `sistema_matricula`.

---

### 1. `carreras`
Almacena los programas académicos de pregrado y posgrado ofrecidos por la universidad.
| Campo | Tipo | Nulo | Clave | Descripción |
|---|---|---|---|---|
| `id` | BIGINT | NO | PK (Auto) | Identificador único de la carrera |
| `codigo` | VARCHAR(20) | NO | UNIQUE | Código identificador (ej. ING-SIS, PSI-GEN) |
| `nombre` | VARCHAR(150) | NO | | Nombre oficial del programa |
| `facultad` | VARCHAR(150) | NO | | Facultad a la que pertenece |
| `duracion_ciclos` | INT | NO | | Duración estándar en semestres (def: 10) |
| `estado` | VARCHAR(20) | NO | | Estado operativo (ACTIVO / INACTIVO) |

---

### 2. `planes_estudio`
Vigencia y estructura curricular de las carreras.
| Campo | Tipo | Nulo | Clave | Descripción |
|---|---|---|---|---|
| `id` | BIGINT | NO | PK (Auto) | Identificador único del plan de estudios |
| `carrera_id` | BIGINT | NO | FK | Referencia a la tabla `carreras(id)` |
| `codigo_plan` | VARCHAR(30) | NO | UNIQUE | Código del plan (ej. PLAN-SIS-2024) |
| `anio_vigencia` | INT | NO | | Año en que entró en vigor |
| `total_creditos`| INT | NO | | Créditos requeridos para graduación |
| `estado` | VARCHAR(20) | NO | | Estado del plan (ACTIVO / HISTORICO) |

---

### 3. `periodos_academicos`
Semestres lectivos para el proceso de matrícula y dictado.
| Campo | Tipo | Nulo | Clave | Descripción |
|---|---|---|---|---|
| `id` | BIGINT | NO | PK (Auto) | Identificador único del periodo |
| `codigo` | VARCHAR(20) | NO | UNIQUE | Denominación del semestre (ej. 2026-I) |
| `fecha_inicio` | DATE | NO | | Fecha de inicio de clases |
| `fecha_fin` | DATE | NO | | Fecha de culminación del periodo |
| `estado` | VARCHAR(20) | NO | | Estado (ACTIVO / PROXIMO / CERRADO) |

---

### 4. `cursos`
Catálogo de asignaturas teóricas y prácticas que componen la malla curricular.
| Campo | Tipo | Nulo | Clave | Descripción |
|---|---|---|---|---|
| `id` | BIGINT | NO | PK (Auto) | Identificador único de la asignatura |
| `plan_estudio_id`| BIGINT | NO | FK | Referencia a `planes_estudio(id)` |
| `codigo` | VARCHAR(20) | NO | UNIQUE | Código del curso (ej. SIS101, SIS102) |
| `nombre` | VARCHAR(150) | NO | | Nombre completo de la asignatura |
| `creditos` | INT | NO | | Valor académico en créditos |
| `horas_teoria` | INT | NO | | Horas teóricas semanales |
| `horas_practica`| INT | NO | | Horas prácticas o de laboratorio semanales |
| `ciclo` | INT | NO | | Ciclo regular correspondiente (1 al 10) |
| `costo` | DECIMAL(10,2) | NO | | Tarifa asociada al curso |
| `estado` | VARCHAR(20) | NO | | Estado (ACTIVO / OBSOLETO) |

---

### 5. `docentes`
Catedráticos e instructores de la universidad.
| Campo | Tipo | Nulo | Clave | Descripción |
|---|---|---|---|---|
| `id` | BIGINT | NO | PK (Auto) | Identificador del docente |
| `dni` | VARCHAR(15) | NO | UNIQUE | Documento nacional de identidad |
| `nombres` | VARCHAR(100) | NO | | Nombres del profesor |
| `apellidos` | VARCHAR(100) | NO | | Apellidos paterno y materno |
| `email` | VARCHAR(120) | NO | UNIQUE | Correo institucional |
| `telefono` | VARCHAR(20) | SI | | Teléfono o celular de contacto |
| `especialidad` | VARCHAR(120) | NO | | Rama o campo de especialidad docente |
| `grado_academico`| VARCHAR(50)| NO | | Máximo grado (Doctor, Magister, etc.) |
| `estado` | VARCHAR(20) | NO | | Estado laboral (ACTIVO / LICENCIA) |

---

### 6. `aulas`
Ambientes físicos y virtuales de enseñanza.
| Campo | Tipo | Nulo | Clave | Descripción |
|---|---|---|---|---|
| `id` | BIGINT | NO | PK (Auto) | Identificador del aula |
| `codigo` | VARCHAR(20) | NO | UNIQUE | Código del aula (ej. A-101, LAB-SIS1) |
| `pabellon` | VARCHAR(50) | NO | | Edificio o pabellón |
| `capacidad` | INT | NO | | Aforo máximo de alumnos |
| `tipo` | VARCHAR(30) | NO | | TEORIA / LABORATORIO / AUDITORIO |
| `estado` | VARCHAR(20) | NO | | Estado (ACTIVO / EN MANTENIMIENTO) |

---

### 7. `estudiantes`
Alumnos admitidos en los distintos programas de la institución.
| Campo | Tipo | Nulo | Clave | Descripción |
|---|---|---|---|---|
| `id` | BIGINT | NO | PK (Auto) | Identificador del estudiante |
| `carrera_id` | BIGINT | NO | FK | Referencia a `carreras(id)` |
| `dni` | VARCHAR(15) | NO | UNIQUE | Documento de identidad |
| `codigo_estudiante`| VARCHAR(20)| NO | UNIQUE | Carnet universitario / Matrícula |
| `nombres` | VARCHAR(100) | NO | | Nombres de pila |
| `apellidos` | VARCHAR(100) | NO | | Apellidos completos |
| `email` | VARCHAR(120) | NO | UNIQUE | Correo institucional del alumno |
| `telefono` | VARCHAR(20) | SI | | Teléfono o celular |
| `fecha_nacimiento`| DATE | SI | | Fecha de nacimiento |
| `direccion` | VARCHAR(255)| SI | | Domicilio actual |
| `estado` | VARCHAR(20) | NO | | Condición (ACTIVO / EGRESADO) |

---

### 8. `secciones`
Apertura de grupos para cada curso dentro de un periodo académico específico.
| Campo | Tipo | Nulo | Clave | Descripción |
|---|---|---|---|---|
| `id` | BIGINT | NO | PK (Auto) | Identificador de la sección |
| `curso_id` | BIGINT | NO | FK | Referencia a `cursos(id)` |
| `periodo_id` | BIGINT | NO | FK | Referencia a `periodos_academicos(id)` |
| `docente_id` | BIGINT | NO | FK | Docente a cargo de la sección |
| `codigo_seccion` | VARCHAR(10) | NO | | Grupo (ej. SEC-A, SEC-B) |
| `vacantes` | INT | NO | | Cupo total asignado |
| `matriculados` | INT | NO | | Cantidad de alumnos inscritos |
| `turno` | VARCHAR(20) | NO | | MAÑANA / TARDE / NOCHE |
| `estado` | VARCHAR(20) | NO | | Estado (ACTIVO / CERRADO) |

---

### 9. `horarios`
Franjas horarias y asignación de aulas por sección.
| Campo | Tipo | Nulo | Clave | Descripción |
|---|---|---|---|---|
| `id` | BIGINT | NO | PK (Auto) | Identificador del horario |
| `seccion_id` | BIGINT | NO | FK | Referencia a `secciones(id)` |
| `aula_id` | BIGINT | NO | FK | Referencia a `aulas(id)` |
| `dia_semana` | VARCHAR(20) | NO | | LUNES, MARTES, MIERCOLES, etc. |
| `hora_inicio` | TIME | NO | | Hora de inicio de la sesión |
| `hora_fin` | TIME | NO | | Hora de finalización de la sesión |

---

### 10. `matriculas`
Cabecera de la inscripción del estudiante en el periodo académico.
| Campo | Tipo | Nulo | Clave | Descripción |
|---|---|---|---|---|
| `id` | BIGINT | NO | PK (Auto) | Identificador de la matrícula |
| `codigo_matricula` | VARCHAR(30) | NO | UNIQUE | Código de ficha (ej. MAT-2026-0001) |
| `estudiante_id` | BIGINT | NO | FK | Referencia a `estudiantes(id)` |
| `periodo_id` | BIGINT | NO | FK | Referencia a `periodos_academicos(id)` |
| `fecha_matricula` | DATETIME | NO | | Fecha y hora de procesamiento |
| `total_creditos` | INT | NO | | Suma de créditos matriculados |
| `costo_total` | DECIMAL(10,2) | NO | | Monto monetario total |
| `estado` | VARCHAR(20) | NO | | REGISTRADA / CONFIRMADA / PAGADA / ANULADA |

---

### 11. `detalles_matricula`
Renglones con cada sección en la que el estudiante quedó inscrito.
| Campo | Tipo | Nulo | Clave | Descripción |
|---|---|---|---|---|
| `id` | BIGINT | NO | PK (Auto) | Identificador del detalle |
| `matricula_id` | BIGINT | NO | FK | Referencia a `matriculas(id)` |
| `seccion_id` | BIGINT | NO | FK | Referencia a `secciones(id)` |
| `costo_curso` | DECIMAL(10,2) | NO | | Tarifa cobrada por la materia |
| `promedio_final` | DECIMAL(4,2) | SI | | Calificación final (0.00 a 20.00) |
| `estado_curso` | VARCHAR(20) | NO | | CURSANDO / APROBADO / DESAPROBADO / RETIRADO |

---

### 12. `pagos`
Control de caja y pagos vinculados a las matrículas.
| Campo | Tipo | Nulo | Clave | Descripción |
|---|---|---|---|---|
| `id` | BIGINT | NO | PK (Auto) | Identificador del pago |
| `matricula_id` | BIGINT | NO | FK | Referencia a `matriculas(id)` |
| `numero_operacion`| VARCHAR(50)| NO | UNIQUE | Número de comprobante / operación |
| `monto` | DECIMAL(10,2) | NO | | Monto amortizado |
| `metodo_pago` | VARCHAR(50) | NO | | EFECTIVO / TARJETA / YAPE / TRANSFERENCIA |
| `fecha_pago` | DATETIME | NO | | Fecha y hora de transacción |
| `estado` | VARCHAR(20) | NO | | PAGADO / PENDIENTE / ANULADO |
