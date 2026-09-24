# Diagrama Entidad - Relación (12 Tablas)
## Sistema de Matrícula Académica Universitaria

El sistema de matrícula está modelado con **12 tablas relacionales normalizadas** en 3ra Forma Normal (3FN), garantizando integridad referencial, consistencia de datos y rendimiento óptimo.

```mermaid
erDiagram
    carreras ||--o{ planes_estudio : "1 carrera tiene N planes"
    carreras ||--o{ estudiantes : "1 carrera tiene N estudiantes"
    planes_estudio ||--o{ cursos : "1 plan contiene N cursos"
    periodos_academicos ||--o{ secciones : "1 periodo oferta N secciones"
    cursos ||--o{ secciones : "1 curso se divide en N secciones"
    docentes ||--o{ secciones : "1 docente dicta N secciones"
    secciones ||--o{ horarios : "1 seccion tiene N horarios"
    aulas ||--o{ horarios : "1 aula alberga N horarios"
    estudiantes ||--o{ matriculas : "1 estudiante realiza N matriculas"
    periodos_academicos ||--o{ matriculas : "1 periodo registra N matriculas"
    matriculas ||--o{ detalles_matricula : "1 matricula contiene N cursos"
    secciones ||--o{ detalles_matricula : "1 seccion es inscrita en N detalles"
    matriculas ||--o{ pagos : "1 matricula genera 1..N pagos"

    carreras {
        bigint id PK
        varchar codigo UK
        varchar nombre
        varchar facultad
        int duracion_ciclos
        varchar estado
    }

    planes_estudio {
        bigint id PK
        bigint carrera_id FK
        varchar codigo_plan UK
        int anio_vigencia
        int total_creditos
        varchar estado
    }

    periodos_academicos {
        bigint id PK
        varchar codigo UK
        date fecha_inicio
        date fecha_fin
        varchar estado
    }

    cursos {
        bigint id PK
        bigint plan_estudio_id FK
        varchar codigo UK
        varchar nombre
        int creditos
        int horas_teoria
        int horas_practica
        int ciclo
        decimal costo
        varchar estado
    }

    docentes {
        bigint id PK
        varchar dni UK
        varchar nombres
        varchar apellidos
        varchar email UK
        varchar telefono
        varchar especialidad
        varchar grado_academico
        varchar estado
    }

    aulas {
        bigint id PK
        varchar codigo UK
        varchar pabellon
        int capacidad
        varchar tipo
        varchar estado
    }

    estudiantes {
        bigint id PK
        bigint carrera_id FK
        varchar dni UK
        varchar codigo_estudiante UK
        varchar nombres
        varchar apellidos
        varchar email UK
        varchar telefono
        date fecha_nacimiento
        varchar direccion
        varchar estado
    }

    secciones {
        bigint id PK
        bigint curso_id FK
        bigint periodo_id FK
        bigint docente_id FK
        varchar codigo_seccion
        int vacantes
        int matriculados
        varchar turno
        varchar estado
    }

    horarios {
        bigint id PK
        bigint seccion_id FK
        bigint aula_id FK
        varchar dia_semana
        time hora_inicio
        time hora_fin
    }

    matriculas {
        bigint id PK
        varchar codigo_matricula UK
        bigint estudiante_id FK
        bigint periodo_id FK
        datetime fecha_matricula
        int total_creditos
        decimal costo_total
        varchar estado
    }

    detalles_matricula {
        bigint id PK
        bigint matricula_id FK
        bigint seccion_id FK
        decimal costo_curso
        decimal promedio_final
        varchar estado_curso
    }

    pagos {
        bigint id PK
        bigint matricula_id FK
        varchar numero_operacion UK
        decimal monto
        varchar metodo_pago
        datetime fecha_pago
        varchar estado
    }
```

---

### Resumen de Cardinalidades e Integridad
1. **Carreras & Planes de Estudio**: Una carrera profesional ofrece uno o varios planes de estudio a lo largo del tiempo.
2. **Planes de Estudio & Cursos**: Un plan de estudios define la malla curricular de cursos por ciclo.
3. **Cursos & Secciones**: Para cada periodo lectivo, una asignatura puede abrir múltiples secciones (A, B, C...) con límites de vacantes.
4. **Secciones & Horarios / Aulas**: Cada sección posee horarios en días específicos asignados a aulas físicas o laboratorios.
5. **Estudiantes & Matrículas**: Un estudiante se matricula una única vez por cada periodo académico (semestre).
6. **Matrículas & Detalles**: Una matrícula asocia al alumno con múltiples secciones de asignaturas elegidas, calculando créditos acumulados y costo total.
7. **Matrículas & Pagos**: La matrícula genera un registro financiero o comprobante de pago con métodos como tarjeta, efectivo, transferencia o Yape.
