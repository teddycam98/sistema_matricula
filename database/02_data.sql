-- =====================================================
-- SISTEMA DE MATRICULA - BASE DE DATOS
-- Script DML: Datos iniciales de prueba (Seed Data)
-- =====================================================

USE sistema_matricula;

-- 1. CARRERAS
INSERT INTO carreras (codigo, nombre, facultad, duracion_ciclos, estado) VALUES
('ING-SIS', 'Ingenieria de Sistemas e Informatica', 'Facultad de Ingenieria', 10, 'ACTIVO'),
('ING-IND', 'Ingenieria Industrial', 'Facultad de Ingenieria', 10, 'ACTIVO'),
('ADM-EMP', 'Administracion de Empresas', 'Facultad de Ciencias Empresariales', 10, 'ACTIVO'),
('CON-PUB', 'Contabilidad y Finanzas', 'Facultad de Ciencias Empresariales', 10, 'ACTIVO'),
('PSI-GEN', 'Psicologia General', 'Facultad de Ciencias de la Salud', 10, 'ACTIVO');

-- 2. PLANES DE ESTUDIO
INSERT INTO planes_estudio (carrera_id, codigo_plan, anio_vigencia, total_creditos, estado) VALUES
(1, 'PLAN-SIS-2024', 2024, 210, 'ACTIVO'),
(2, 'PLAN-IND-2024', 2024, 205, 'ACTIVO'),
(3, 'PLAN-ADM-2024', 2024, 200, 'ACTIVO'),
(4, 'PLAN-CON-2024', 2024, 200, 'ACTIVO'),
(5, 'PLAN-PSI-2024', 2024, 200, 'ACTIVO');

-- 3. PERIODOS ACADEMICOS
INSERT INTO periodos_academicos (codigo, fecha_inicio, fecha_fin, estado) VALUES
('2026-I', '2026-03-15', '2026-07-20', 'ACTIVO'),
('2026-II', '2026-08-15', '2026-12-20', 'PROXIMO'),
('2025-II', '2025-08-15', '2025-12-20', 'CERRADO');

-- 4. CURSOS
INSERT INTO cursos (plan_estudio_id, codigo, nombre, creditos, horas_teoria, horas_practica, ciclo, costo, estado) VALUES
(1, 'SIS101', 'Algoritmos y Estructura de Datos', 4, 3, 2, 2, 280.00, 'ACTIVO'),
(1, 'SIS102', 'Base de Datos I', 4, 2, 4, 3, 300.00, 'ACTIVO'),
(1, 'SIS103', 'Desarrollo de Aplicaciones Web', 4, 2, 4, 5, 320.00, 'ACTIVO'),
(1, 'SIS104', 'Arquitectura de Software', 3, 2, 2, 6, 260.00, 'ACTIVO'),
(1, 'SIS105', 'Inteligencia Artificial', 4, 3, 2, 7, 350.00, 'ACTIVO'),
(2, 'IND101', 'Procesos Industriales', 4, 2, 4, 3, 290.00, 'ACTIVO'),
(2, 'IND102', 'Investigacion de Operaciones', 4, 3, 2, 4, 300.00, 'ACTIVO'),
(3, 'ADM101', 'Fundamentos de Gestion y Liderazgo', 3, 3, 0, 1, 240.00, 'ACTIVO'),
(3, 'ADM102', 'Marketing Estrategico', 3, 2, 2, 3, 250.00, 'ACTIVO'),
(4, 'CON101', 'Contabilidad General', 4, 3, 2, 1, 260.00, 'ACTIVO');

-- 5. DOCENTES
INSERT INTO docentes (dni, nombres, apellidos, email, telefono, especialidad, grado_academico, estado) VALUES
('08923411', 'Carlos Alberto', 'Mendoza Ramos', 'cmendoza@universidad.edu.pe', '987123456', 'Ingenieria de Software y Java', 'Magister', 'ACTIVO'),
('45127890', 'Ana Patricia', 'Flores Quispe', 'aflores@universidad.edu.pe', '954789123', 'Bases de Datos y Big Data', 'Doctora', 'ACTIVO'),
('10293847', 'Roberto David', 'Silva Chavez', 'rsilva@universidad.edu.pe', '961234567', 'Inteligencia Artificial', 'Magister', 'ACTIVO'),
('71829304', 'Laura Elena', 'Perez Morales', 'lperez@universidad.edu.pe', '932145678', 'Gestion de Operaciones', 'Magister', 'ACTIVO'),
('33445566', 'Miguel Angel', 'Torres Vargas', 'mtorres@universidad.edu.pe', '945678123', 'Finanzas Corporativas', 'Licenciado', 'ACTIVO');

-- 6. AULAS
INSERT INTO aulas (codigo, pabellon, capacidad, tipo, estado) VALUES
('A-101', 'Pabellon A', 40, 'TEORIA', 'ACTIVO'),
('A-102', 'Pabellon A', 40, 'TEORIA', 'ACTIVO'),
('LAB-SIS1', 'Pabellon B (TI)', 30, 'LABORATORIO', 'ACTIVO'),
('LAB-SIS2', 'Pabellon B (TI)', 30, 'LABORATORIO', 'ACTIVO'),
('AUD-01', 'Pabellon Central', 120, 'AUDITORIO', 'ACTIVO');

-- 7. ESTUDIANTES
INSERT INTO estudiantes (carrera_id, dni, codigo_estudiante, nombres, apellidos, email, telefono, fecha_nacimiento, direccion, estado) VALUES
(1, '72839102', '20231001', 'Juan Carlos', 'Gomez Perez', 'juan.gomez@alumno.edu.pe', '981122334', '2004-05-12', 'Av. Arequipa 1234, Lima', 'ACTIVO'),
(1, '73940182', '20231002', 'Maria Fernanda', 'Lopez Diaz', 'maria.lopez@alumno.edu.pe', '982233445', '2004-08-20', 'Calle Las Flores 456, San Isidro', 'ACTIVO'),
(1, '74829103', '20231003', 'Diego Alonso', 'Vargas Torres', 'diego.vargas@alumno.edu.pe', '983344556', '2003-11-15', 'Jr. Union 789, Miraflores', 'ACTIVO'),
(2, '75930281', '20231004', 'Lucia Belen', 'Ramos Castro', 'lucia.ramos@alumno.edu.pe', '984455667', '2004-02-28', 'Av. Javier Prado 2300, San Borja', 'ACTIVO'),
(3, '76829174', '20231005', 'Kevin Alexander', 'Chavez Romero', 'kevin.chavez@alumno.edu.pe', '985566778', '2003-09-05', 'Av. Brasil 3100, Magdalena', 'ACTIVO');

-- 8. SECCIONES
INSERT INTO secciones (curso_id, periodo_id, docente_id, codigo_seccion, vacantes, matriculados, turno, estado) VALUES
(1, 1, 1, 'SEC-A', 30, 2, 'MANANA', 'ACTIVO'),
(1, 1, 1, 'SEC-B', 30, 0, 'NOCHE', 'ACTIVO'),
(2, 1, 2, 'SEC-A', 25, 2, 'MANANA', 'ACTIVO'),
(3, 1, 1, 'SEC-A', 25, 1, 'TARDE', 'ACTIVO'),
(4, 1, 3, 'SEC-A', 30, 0, 'NOCHE', 'ACTIVO'),
(5, 1, 3, 'SEC-A', 25, 1, 'TARDE', 'ACTIVO'),
(6, 1, 4, 'SEC-A', 35, 1, 'MANANA', 'ACTIVO'),
(8, 1, 5, 'SEC-A', 40, 1, 'MANANA', 'ACTIVO');

-- 9. HORARIOS
INSERT INTO horarios (seccion_id, aula_id, dia_semana, hora_inicio, hora_fin) VALUES
(1, 3, 'LUNES', '08:00:00', '10:00:00'),
(1, 1, 'MIERCOLES', '08:00:00', '10:00:00'),
(2, 3, 'MARTES', '19:00:00', '22:00:00'),
(3, 4, 'MARTES', '08:00:00', '11:00:00'),
(3, 1, 'JUEVES', '08:00:00', '10:00:00'),
(4, 3, 'MIERCOLES', '14:00:00', '18:00:00'),
(5, 2, 'VIERNES', '18:00:00', '21:00:00'),
(6, 4, 'JUEVES', '14:00:00', '18:00:00'),
(7, 1, 'LUNES', '10:00:00', '13:00:00'),
(8, 2, 'VIERNES', '08:00:00', '11:00:00');

-- 10. MATRICULAS
INSERT INTO matriculas (codigo_matricula, estudiante_id, periodo_id, fecha_matricula, total_creditos, costo_total, estado) VALUES
('MAT-2026-0001', 1, 1, '2026-03-01 09:30:00', 8, 580.00, 'PAGADA'),
('MAT-2026-0002', 2, 1, '2026-03-02 11:15:00', 8, 580.00, 'CONFIRMADA'),
('MAT-2026-0003', 3, 1, '2026-03-03 14:00:00', 4, 320.00, 'REGISTRADA'),
('MAT-2026-0004', 4, 1, '2026-03-04 10:20:00', 4, 290.00, 'PAGADA'),
('MAT-2026-0005', 5, 1, '2026-03-05 16:45:00', 3, 240.00, 'PAGADA');

-- 11. DETALLES DE MATRICULA
INSERT INTO detalles_matricula (matricula_id, seccion_id, costo_curso, promedio_final, estado_curso) VALUES
(1, 1, 280.00, NULL, 'CURSANDO'),
(1, 3, 300.00, NULL, 'CURSANDO'),
(2, 1, 280.00, NULL, 'CURSANDO'),
(2, 3, 300.00, NULL, 'CURSANDO'),
(3, 4, 320.00, NULL, 'CURSANDO'),
(4, 7, 290.00, NULL, 'CURSANDO'),
(5, 8, 240.00, NULL, 'CURSANDO');

-- 12. PAGOS
INSERT INTO pagos (matricula_id, numero_operacion, monto, metodo_pago, fecha_pago, estado) VALUES
(1, 'OP-2026-78901', 580.00, 'TARJETA', '2026-03-01 10:00:00', 'PAGADO'),
(4, 'OP-2026-78902', 290.00, 'TRANSFERENCIA', '2026-03-04 11:00:00', 'PAGADO'),
(5, 'OP-2026-78903', 240.00, 'YAPE', '2026-03-05 17:00:00', 'PAGADO');
