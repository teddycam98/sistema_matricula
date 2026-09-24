-- =====================================================
-- SISTEMA DE MATRICULA - BASE DE DATOS
-- Script DDL: Creación de Esquema y Tablas (12 tablas)
-- =====================================================

DROP DATABASE IF EXISTS sistema_matricula;
CREATE DATABASE sistema_matricula CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sistema_matricula;

-- 1. TABLA CARRERAS (Programas Académicos)
CREATE TABLE carreras (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(150) NOT NULL,
    facultad VARCHAR(150) NOT NULL,
    duracion_ciclos INT NOT NULL DEFAULT 10,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO'
) ENGINE=InnoDB;

-- 2. TABLA PLANES DE ESTUDIO
CREATE TABLE planes_estudio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    carrera_id BIGINT NOT NULL,
    codigo_plan VARCHAR(30) NOT NULL UNIQUE,
    anio_vigencia INT NOT NULL,
    total_creditos INT NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    CONSTRAINT fk_plan_carrera FOREIGN KEY (carrera_id) REFERENCES carreras(id) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 3. TABLA PERIODOS ACADÉMICOS (Semestres)
CREATE TABLE periodos_academicos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO'
) ENGINE=InnoDB;

-- 4. TABLA CURSOS (Asignaturas)
CREATE TABLE cursos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    plan_estudio_id BIGINT NOT NULL,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(150) NOT NULL,
    creditos INT NOT NULL,
    horas_teoria INT NOT NULL DEFAULT 2,
    horas_practica INT NOT NULL DEFAULT 2,
    ciclo INT NOT NULL DEFAULT 1,
    costo DECIMAL(10,2) NOT NULL DEFAULT 200.00,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    CONSTRAINT fk_curso_plan FOREIGN KEY (plan_estudio_id) REFERENCES planes_estudio(id) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 5. TABLA DOCENTES (Profesores)
CREATE TABLE docentes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(15) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    especialidad VARCHAR(120) NOT NULL,
    grado_academico VARCHAR(50) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO'
) ENGINE=InnoDB;

-- 6. TABLA AULAS
CREATE TABLE aulas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    pabellon VARCHAR(50) NOT NULL,
    capacidad INT NOT NULL,
    tipo VARCHAR(30) NOT NULL DEFAULT 'TEORIA',
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO'
) ENGINE=InnoDB;

-- 7. TABLA ESTUDIANTES (Alumnos)
CREATE TABLE estudiantes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    carrera_id BIGINT NOT NULL,
    dni VARCHAR(15) NOT NULL UNIQUE,
    codigo_estudiante VARCHAR(20) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    fecha_nacimiento DATE,
    direccion VARCHAR(255),
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    CONSTRAINT fk_estudiante_carrera FOREIGN KEY (carrera_id) REFERENCES carreras(id) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 8. TABLA SECCIONES (Grupos de cursos ofertados por periodo)
CREATE TABLE secciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    curso_id BIGINT NOT NULL,
    periodo_id BIGINT NOT NULL,
    docente_id BIGINT NOT NULL,
    codigo_seccion VARCHAR(10) NOT NULL,
    vacantes INT NOT NULL DEFAULT 35,
    matriculados INT NOT NULL DEFAULT 0,
    turno VARCHAR(20) NOT NULL DEFAULT 'MAÑANA',
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    CONSTRAINT uq_curso_periodo_seccion UNIQUE (curso_id, periodo_id, codigo_seccion),
    CONSTRAINT fk_seccion_curso FOREIGN KEY (curso_id) REFERENCES cursos(id) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_seccion_periodo FOREIGN KEY (periodo_id) REFERENCES periodos_academicos(id) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_seccion_docente FOREIGN KEY (docente_id) REFERENCES docentes(id) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 9. TABLA HORARIOS
CREATE TABLE horarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seccion_id BIGINT NOT NULL,
    aula_id BIGINT NOT NULL,
    dia_semana VARCHAR(20) NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    CONSTRAINT fk_horario_seccion FOREIGN KEY (seccion_id) REFERENCES secciones(id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_horario_aula FOREIGN KEY (aula_id) REFERENCES aulas(id) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 10. TABLA MATRICULAS
CREATE TABLE matriculas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo_matricula VARCHAR(30) NOT NULL UNIQUE,
    estudiante_id BIGINT NOT NULL,
    periodo_id BIGINT NOT NULL,
    fecha_matricula DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_creditos INT NOT NULL DEFAULT 0,
    costo_total DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    estado VARCHAR(20) NOT NULL DEFAULT 'REGISTRADA',
    CONSTRAINT uq_estudiante_periodo UNIQUE (estudiante_id, periodo_id),
    CONSTRAINT fk_matricula_estudiante FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_matricula_periodo FOREIGN KEY (periodo_id) REFERENCES periodos_academicos(id) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 11. TABLA DETALLES DE MATRICULA (Cursos matriculados)
CREATE TABLE detalles_matricula (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    matricula_id BIGINT NOT NULL,
    seccion_id BIGINT NOT NULL,
    costo_curso DECIMAL(10,2) NOT NULL,
    promedio_final DECIMAL(4,2) NULL,
    estado_curso VARCHAR(20) NOT NULL DEFAULT 'CURSANDO',
    CONSTRAINT uq_matricula_seccion UNIQUE (matricula_id, seccion_id),
    CONSTRAINT fk_detalle_matricula FOREIGN KEY (matricula_id) REFERENCES matriculas(id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_detalle_seccion FOREIGN KEY (seccion_id) REFERENCES secciones(id) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 12. TABLA PAGOS
CREATE TABLE pagos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    matricula_id BIGINT NOT NULL,
    numero_operacion VARCHAR(50) NOT NULL UNIQUE,
    monto DECIMAL(10,2) NOT NULL,
    metodo_pago VARCHAR(50) NOT NULL,
    fecha_pago DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(20) NOT NULL DEFAULT 'PAGADO',
    CONSTRAINT fk_pago_matricula FOREIGN KEY (matricula_id) REFERENCES matriculas(id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;
