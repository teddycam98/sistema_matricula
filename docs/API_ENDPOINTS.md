# Documentación de la API REST
## Sistema de Matrícula Universitaria

Todos los endpoints retornan una estructura estándar JSON mediante la clase `ApiResponse<T>`:
```json
{
  "success": true,
  "message": "Mensaje informativo",
  "data": { ... },
  "timestamp": "2026-09-24T14:50:00"
}
```

---

### 1. Dashboard
| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/dashboard/stats` | Retorna los KPIs totales: estudiantes, cursos, docentes, matriculas, recaudación |

---

### 2. Estudiantes (`/api/estudiantes`)
| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/estudiantes` | Lista todos los estudiantes (filtro opcional `?carreraId=1`) |
| `GET` | `/api/estudiantes/{id}` | Obtiene un estudiante por su ID |
| `GET` | `/api/estudiantes/dni/{dni}` | Busca un estudiante por su DNI |
| `GET` | `/api/estudiantes/codigo/{codigo}` | Busca por código universitario |
| `POST`| `/api/estudiantes` | Registra un nuevo estudiante |
| `PUT` | `/api/estudiantes/{id}` | Actualiza datos del estudiante |
| `DELETE`| `/api/estudiantes/{id}` | Elimina el registro del estudiante |

**Ejemplo Body POST `/api/estudiantes`:**
```json
{
  "dni": "78912345",
  "nombres": "Rodrigo Alonso",
  "apellidos": "Salas Gutiérrez",
  "email": "rodrigo.salas@alumno.edu.pe",
  "telefono": "987654321",
  "carrera": { "id": 1 },
  "direccion": "Av. La Marina 200, San Miguel",
  "estado": "ACTIVO"
}
```

---

### 3. Matrículas (`/api/matriculas`)
| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/matriculas` | Lista todas las matrículas registradas (`?estudianteId=1` opcional) |
| `GET` | `/api/matriculas/{id}` | Obtiene el detalle completo de la matrícula y sus asignaturas |
| `GET` | `/api/matriculas/codigo/{codigo}` | Busca la matrícula por código (ej. MAT-2026I-0001) |
| `POST`| `/api/matriculas` | Procesa y registra una matrícula con descuento de vacantes |
| `PATCH`| `/api/matriculas/{id}/anular` | Anula una matrícula y devuelve las vacantes a las secciones |

**Ejemplo Body POST `/api/matriculas`:**
```json
{
  "estudianteId": 1,
  "periodoId": 1,
  "seccionIds": [1, 3],
  "metodoPago": "TARJETA"
}
```

---

### 4. Cursos (`/api/cursos`)
| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/cursos` | Lista cursos (`?planEstudioId=1`, `?ciclo=2`) |
| `GET` | `/api/cursos/{id}` | Obtiene curso por ID |
| `POST`| `/api/cursos` | Registra nuevo curso en la malla curricular |
| `PUT` | `/api/cursos/{id}` | Actualiza datos de un curso |
| `DELETE`| `/api/cursos/{id}` | Elimina una asignatura |

---

### 5. Secciones (`/api/secciones`)
| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/secciones` | Lista secciones (`?periodoId=1`) |
| `GET` | `/api/secciones/disponibles?periodoId=1` | Lista únicamente secciones con vacantes disponibles |
| `GET` | `/api/secciones/{id}` | Obtiene sección por ID |
| `POST`| `/api/secciones` | Crea una nueva sección académica |
| `PUT` | `/api/secciones/{id}` | Actualiza sección |
| `DELETE`| `/api/secciones/{id}` | Elimina sección |

---

### 6. Docentes (`/api/docentes`)
| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/docentes` | Lista la plana docente |
| `GET` | `/api/docentes/{id}` | Obtiene docente por ID |
| `GET` | `/api/docentes/dni/{dni}` | Busca docente por DNI |
| `POST`| `/api/docentes` | Registra un nuevo profesor |
| `PUT` | `/api/docentes/{id}` | Actualiza docente |
| `DELETE`| `/api/docentes/{id}` | Elimina docente |

---

### 7. Pagos (`/api/pagos`)
| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/pagos` | Lista pagos (`?matriculaId=1`) |
| `GET` | `/api/pagos/{id}` | Obtiene pago por ID |
| `POST`| `/api/pagos/matricula/{matriculaId}` | Registra pago de una matrícula pendiente |

**Ejemplo Body POST `/api/pagos/matricula/1`:**
```json
{
  "metodoPago": "YAPE"
}
```

---

### 8. Horarios (`/api/horarios`)
| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/horarios` | Lista todos los horarios (`?seccionId=1`, `?aulaId=2`) |
| `GET` | `/api/horarios/{id}` | Obtiene horario por ID |
| `POST`| `/api/horarios` | Asigna nuevo horario a una sección y aula |
| `PUT` | `/api/horarios/{id}` | Actualiza horario |
| `DELETE`| `/api/horarios/{id}` | Elimina horario |

---

### 9. Carreras, Periodos, Planes y Aulas
- `/api/carreras`: CRUD de programas académicos.
- `/api/periodos`: CRUD de semestres lectivos (2026-I, etc.).
- `/api/aulas`: CRUD de ambientes físicos y laboratorios.
- `/api/planes-estudio`: CRUD de planes curriculares.

---

### 10. Reportes Oficiales en PDF (JasperReports)
| Método | Endpoint | Retorno | Descripción |
|---|---|---|---|
| `GET` | `/api/reportes/matricula/{id}/pdf` | `application/pdf` | Genera y descarga la Ficha Oficial de Matrícula en PDF para el estudiante especificado. |
| `GET` | `/api/reportes/matriculas/pdf` | `application/pdf` | Reporte ejecutivo consolidado de todas las matrículas registradas. |
| `GET` | `/api/reportes/estudiantes/pdf` | `application/pdf` | Padrón oficial de estudiantes por carrera y correo institucional. |
