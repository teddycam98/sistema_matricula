-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: sistema_matricula
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `aulas`
--

DROP TABLE IF EXISTS `aulas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aulas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `codigo` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `pabellon` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `capacidad` int NOT NULL,
  `tipo` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'TEORIA',
  `estado` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVO',
  PRIMARY KEY (`id`),
  UNIQUE KEY `codigo` (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aulas`
--

LOCK TABLES `aulas` WRITE;
/*!40000 ALTER TABLE `aulas` DISABLE KEYS */;
INSERT INTO `aulas` VALUES (1,'A-101','Pabellon A',40,'TEORIA','ACTIVO'),(2,'A-102','Pabellon A',40,'TEORIA','ACTIVO'),(3,'LAB-SIS1','Pabellon B (TI)',30,'LABORATORIO','ACTIVO'),(4,'LAB-SIS2','Pabellon B (TI)',30,'LABORATORIO','ACTIVO'),(5,'AUD-01','Pabellon Central',120,'AUDITORIO','ACTIVO');
/*!40000 ALTER TABLE `aulas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carreras`
--

DROP TABLE IF EXISTS `carreras`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carreras` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `codigo` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `facultad` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `duracion_ciclos` int NOT NULL DEFAULT '10',
  `estado` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVO',
  PRIMARY KEY (`id`),
  UNIQUE KEY `codigo` (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carreras`
--

LOCK TABLES `carreras` WRITE;
/*!40000 ALTER TABLE `carreras` DISABLE KEYS */;
INSERT INTO `carreras` VALUES (1,'ING-SIS','Ingenieria de Sistemas e Informatica','Facultad de Ingenieria',10,'ACTIVO'),(2,'ING-IND','Ingenieria Industrial','Facultad de Ingenieria',10,'ACTIVO'),(3,'ADM-EMP','Administracion de Empresas','Facultad de Ciencias Empresariales',10,'ACTIVO'),(4,'CON-PUB','Contabilidad y Finanzas','Facultad de Ciencias Empresariales',10,'ACTIVO'),(5,'PSI-GEN','Psicologia General','Facultad de Ciencias de la Salud',10,'ACTIVO');
/*!40000 ALTER TABLE `carreras` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cursos`
--

DROP TABLE IF EXISTS `cursos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cursos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `plan_estudio_id` bigint NOT NULL,
  `codigo` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `creditos` int NOT NULL,
  `horas_teoria` int NOT NULL DEFAULT '2',
  `horas_practica` int NOT NULL DEFAULT '2',
  `ciclo` int NOT NULL DEFAULT '1',
  `costo` decimal(10,2) NOT NULL DEFAULT '200.00',
  `estado` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVO',
  PRIMARY KEY (`id`),
  UNIQUE KEY `codigo` (`codigo`),
  KEY `fk_curso_plan` (`plan_estudio_id`),
  CONSTRAINT `fk_curso_plan` FOREIGN KEY (`plan_estudio_id`) REFERENCES `planes_estudio` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cursos`
--

LOCK TABLES `cursos` WRITE;
/*!40000 ALTER TABLE `cursos` DISABLE KEYS */;
INSERT INTO `cursos` VALUES (1,1,'SIS101','Algoritmos y Estructura de Datos',4,3,2,2,280.00,'ACTIVO'),(2,1,'SIS102','Base de Datos I',4,2,4,3,300.00,'ACTIVO'),(3,1,'SIS103','Desarrollo de Aplicaciones Web',4,2,4,5,320.00,'ACTIVO'),(4,1,'SIS104','Arquitectura de Software',3,2,2,6,260.00,'ACTIVO'),(5,1,'SIS105','Inteligencia Artificial',4,3,2,7,350.00,'ACTIVO'),(6,2,'IND101','Procesos Industriales',4,2,4,3,290.00,'ACTIVO'),(7,2,'IND102','Investigacion de Operaciones',4,3,2,4,300.00,'ACTIVO'),(8,3,'ADM101','Fundamentos de Gestion y Liderazgo',3,3,0,1,240.00,'ACTIVO'),(9,3,'ADM102','Marketing Estrategico',3,2,2,3,250.00,'ACTIVO'),(10,4,'CON101','Contabilidad General',4,3,2,1,260.00,'ACTIVO');
/*!40000 ALTER TABLE `cursos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalles_matricula`
--

DROP TABLE IF EXISTS `detalles_matricula`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalles_matricula` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `matricula_id` bigint NOT NULL,
  `seccion_id` bigint NOT NULL,
  `costo_curso` decimal(10,2) NOT NULL,
  `promedio_final` decimal(4,2) DEFAULT NULL,
  `estado_curso` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'CURSANDO',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_matricula_seccion` (`matricula_id`,`seccion_id`),
  UNIQUE KEY `UK21wnqipfs0yx48uu4thjency6` (`matricula_id`,`seccion_id`),
  KEY `fk_detalle_seccion` (`seccion_id`),
  CONSTRAINT `fk_detalle_matricula` FOREIGN KEY (`matricula_id`) REFERENCES `matriculas` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_detalle_seccion` FOREIGN KEY (`seccion_id`) REFERENCES `secciones` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalles_matricula`
--

LOCK TABLES `detalles_matricula` WRITE;
/*!40000 ALTER TABLE `detalles_matricula` DISABLE KEYS */;
INSERT INTO `detalles_matricula` VALUES (1,1,1,280.00,NULL,'CURSANDO'),(2,1,3,300.00,NULL,'CURSANDO'),(3,2,1,280.00,NULL,'CURSANDO'),(4,2,3,300.00,NULL,'CURSANDO'),(5,3,4,320.00,NULL,'CURSANDO'),(6,4,7,290.00,NULL,'CURSANDO'),(7,5,8,240.00,NULL,'CURSANDO'),(8,6,1,280.00,NULL,'CURSANDO'),(9,6,3,300.00,NULL,'CURSANDO');
/*!40000 ALTER TABLE `detalles_matricula` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `docentes`
--

DROP TABLE IF EXISTS `docentes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `docentes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dni` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombres` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellidos` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
  `telefono` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `especialidad` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
  `grado_academico` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `estado` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVO',
  PRIMARY KEY (`id`),
  UNIQUE KEY `dni` (`dni`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `docentes`
--

LOCK TABLES `docentes` WRITE;
/*!40000 ALTER TABLE `docentes` DISABLE KEYS */;
INSERT INTO `docentes` VALUES (1,'08923411','Carlos Alberto','Mendoza Ramos','cmendoza@universidad.edu.pe','987123456','Ingenieria de Software y Java','Magister','ACTIVO'),(2,'45127890','Ana Patricia','Flores Quispe','aflores@universidad.edu.pe','954789123','Bases de Datos y Big Data','Doctora','ACTIVO'),(3,'10293847','Roberto David','Silva Chavez','rsilva@universidad.edu.pe','961234567','Inteligencia Artificial','Magister','ACTIVO'),(4,'71829304','Laura Elena','Perez Morales','lperez@universidad.edu.pe','932145678','Gestion de Operaciones','Magister','ACTIVO'),(5,'33445566','Miguel Angel','Torres Vargas','mtorres@universidad.edu.pe','945678123','Finanzas Corporativas','Licenciado','ACTIVO');
/*!40000 ALTER TABLE `docentes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estudiantes`
--

DROP TABLE IF EXISTS `estudiantes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estudiantes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `carrera_id` bigint NOT NULL,
  `dni` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL,
  `codigo_estudiante` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombres` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellidos` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
  `telefono` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `direccion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `estado` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVO',
  PRIMARY KEY (`id`),
  UNIQUE KEY `dni` (`dni`),
  UNIQUE KEY `codigo_estudiante` (`codigo_estudiante`),
  UNIQUE KEY `email` (`email`),
  KEY `fk_estudiante_carrera` (`carrera_id`),
  CONSTRAINT `fk_estudiante_carrera` FOREIGN KEY (`carrera_id`) REFERENCES `carreras` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estudiantes`
--

LOCK TABLES `estudiantes` WRITE;
/*!40000 ALTER TABLE `estudiantes` DISABLE KEYS */;
INSERT INTO `estudiantes` VALUES (1,1,'72839102','20231001','Juan Carlos','Gomez Perez','juan.gomez@alumno.edu.pe','981122334','2004-05-12','Av. Arequipa 1234, Lima','ACTIVO'),(2,1,'73940182','20231002','Maria Fernanda','Lopez Diaz','maria.lopez@alumno.edu.pe','982233445','2004-08-20','Calle Las Flores 456, San Isidro','ACTIVO'),(3,1,'74829103','20231003','Diego Alonso','Vargas Torres','diego.vargas@alumno.edu.pe','983344556','2003-11-15','Jr. Union 789, Miraflores','ACTIVO'),(4,2,'75930281','20231004','Lucia Belen','Ramos Castro','lucia.ramos@alumno.edu.pe','984455667','2004-02-28','Av. Javier Prado 2300, San Borja','ACTIVO'),(5,3,'76829174','20231005','Kevin Alexander','Chavez Romero','kevin.chavez@alumno.edu.pe','985566778','2003-09-05','Av. Brasil 3100, Magdalena','ACTIVO'),(6,1,'71234567','20261099','Andres','Castro Diaz','andres@alumno.edu.pe','987654321',NULL,NULL,'ACTIVO');
/*!40000 ALTER TABLE `estudiantes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `horarios`
--

DROP TABLE IF EXISTS `horarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `horarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `seccion_id` bigint NOT NULL,
  `aula_id` bigint NOT NULL,
  `dia_semana` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `hora_inicio` time NOT NULL,
  `hora_fin` time NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_horario_seccion` (`seccion_id`),
  KEY `fk_horario_aula` (`aula_id`),
  CONSTRAINT `fk_horario_aula` FOREIGN KEY (`aula_id`) REFERENCES `aulas` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_horario_seccion` FOREIGN KEY (`seccion_id`) REFERENCES `secciones` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `horarios`
--

LOCK TABLES `horarios` WRITE;
/*!40000 ALTER TABLE `horarios` DISABLE KEYS */;
INSERT INTO `horarios` VALUES (1,1,3,'LUNES','08:00:00','10:00:00'),(2,1,1,'MIERCOLES','08:00:00','10:00:00'),(3,2,3,'MARTES','19:00:00','22:00:00'),(4,3,4,'MARTES','08:00:00','11:00:00'),(5,3,1,'JUEVES','08:00:00','10:00:00'),(6,4,3,'MIERCOLES','14:00:00','18:00:00'),(7,5,2,'VIERNES','18:00:00','21:00:00'),(8,6,4,'JUEVES','14:00:00','18:00:00'),(9,7,1,'LUNES','10:00:00','13:00:00'),(10,8,2,'VIERNES','08:00:00','11:00:00');
/*!40000 ALTER TABLE `horarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `matriculas`
--

DROP TABLE IF EXISTS `matriculas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `matriculas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `codigo_matricula` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `estudiante_id` bigint NOT NULL,
  `periodo_id` bigint NOT NULL,
  `fecha_matricula` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `total_creditos` int NOT NULL DEFAULT '0',
  `costo_total` decimal(10,2) NOT NULL DEFAULT '0.00',
  `estado` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'REGISTRADA',
  PRIMARY KEY (`id`),
  UNIQUE KEY `codigo_matricula` (`codigo_matricula`),
  UNIQUE KEY `uq_estudiante_periodo` (`estudiante_id`,`periodo_id`),
  UNIQUE KEY `UKrr6y9rf66100wj5oy1usfg8sg` (`estudiante_id`,`periodo_id`),
  KEY `fk_matricula_periodo` (`periodo_id`),
  CONSTRAINT `fk_matricula_estudiante` FOREIGN KEY (`estudiante_id`) REFERENCES `estudiantes` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_matricula_periodo` FOREIGN KEY (`periodo_id`) REFERENCES `periodos_academicos` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `matriculas`
--

LOCK TABLES `matriculas` WRITE;
/*!40000 ALTER TABLE `matriculas` DISABLE KEYS */;
INSERT INTO `matriculas` VALUES (1,'MAT-2026-0001',1,1,'2026-03-01 09:30:00',8,580.00,'PAGADA'),(2,'MAT-2026-0002',2,1,'2026-03-02 11:15:00',8,580.00,'CONFIRMADA'),(3,'MAT-2026-0003',3,1,'2026-03-03 14:00:00',4,320.00,'REGISTRADA'),(4,'MAT-2026-0004',4,1,'2026-03-04 10:20:00',4,290.00,'PAGADA'),(5,'MAT-2026-0005',5,1,'2026-03-05 16:45:00',3,240.00,'PAGADA'),(6,'MAT-2026I-0006',6,1,'2026-09-26 16:04:23',8,580.00,'PAGADA');
/*!40000 ALTER TABLE `matriculas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pagos`
--

DROP TABLE IF EXISTS `pagos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pagos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `matricula_id` bigint NOT NULL,
  `numero_operacion` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `monto` decimal(10,2) NOT NULL,
  `metodo_pago` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_pago` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `estado` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PAGADO',
  PRIMARY KEY (`id`),
  UNIQUE KEY `numero_operacion` (`numero_operacion`),
  KEY `fk_pago_matricula` (`matricula_id`),
  CONSTRAINT `fk_pago_matricula` FOREIGN KEY (`matricula_id`) REFERENCES `matriculas` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pagos`
--

LOCK TABLES `pagos` WRITE;
/*!40000 ALTER TABLE `pagos` DISABLE KEYS */;
INSERT INTO `pagos` VALUES (1,1,'OP-2026-78901',580.00,'TARJETA','2026-03-01 10:00:00','PAGADO'),(2,4,'OP-2026-78902',290.00,'TRANSFERENCIA','2026-03-04 11:00:00','PAGADO'),(3,5,'OP-2026-78903',240.00,'YAPE','2026-03-05 17:00:00','PAGADO'),(4,6,'OP-1790438663133',580.00,'YAPE','2026-09-26 16:04:23','PAGADO');
/*!40000 ALTER TABLE `pagos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `periodos_academicos`
--

DROP TABLE IF EXISTS `periodos_academicos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `periodos_academicos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `codigo` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date NOT NULL,
  `estado` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVO',
  PRIMARY KEY (`id`),
  UNIQUE KEY `codigo` (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `periodos_academicos`
--

LOCK TABLES `periodos_academicos` WRITE;
/*!40000 ALTER TABLE `periodos_academicos` DISABLE KEYS */;
INSERT INTO `periodos_academicos` VALUES (1,'2026-I','2026-03-15','2026-07-20','ACTIVO'),(2,'2026-II','2026-08-15','2026-12-20','PROXIMO'),(3,'2025-II','2025-08-15','2025-12-20','CERRADO');
/*!40000 ALTER TABLE `periodos_academicos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `planes_estudio`
--

DROP TABLE IF EXISTS `planes_estudio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `planes_estudio` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `carrera_id` bigint NOT NULL,
  `codigo_plan` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `anio_vigencia` int NOT NULL,
  `total_creditos` int NOT NULL,
  `estado` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVO',
  PRIMARY KEY (`id`),
  UNIQUE KEY `codigo_plan` (`codigo_plan`),
  KEY `fk_plan_carrera` (`carrera_id`),
  CONSTRAINT `fk_plan_carrera` FOREIGN KEY (`carrera_id`) REFERENCES `carreras` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `planes_estudio`
--

LOCK TABLES `planes_estudio` WRITE;
/*!40000 ALTER TABLE `planes_estudio` DISABLE KEYS */;
INSERT INTO `planes_estudio` VALUES (1,1,'PLAN-SIS-2024',2024,210,'ACTIVO'),(2,2,'PLAN-IND-2024',2024,205,'ACTIVO'),(3,3,'PLAN-ADM-2024',2024,200,'ACTIVO'),(4,4,'PLAN-CON-2024',2024,200,'ACTIVO'),(5,5,'PLAN-PSI-2024',2024,200,'ACTIVO');
/*!40000 ALTER TABLE `planes_estudio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `secciones`
--

DROP TABLE IF EXISTS `secciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `secciones` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `curso_id` bigint NOT NULL,
  `periodo_id` bigint NOT NULL,
  `docente_id` bigint NOT NULL,
  `codigo_seccion` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `vacantes` int NOT NULL DEFAULT '35',
  `matriculados` int NOT NULL DEFAULT '0',
  `turno` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'MAÑANA',
  `estado` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVO',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_curso_periodo_seccion` (`curso_id`,`periodo_id`,`codigo_seccion`),
  UNIQUE KEY `UK74g6h84bwiex1o6wbeavknds7` (`curso_id`,`periodo_id`,`codigo_seccion`),
  KEY `fk_seccion_periodo` (`periodo_id`),
  KEY `fk_seccion_docente` (`docente_id`),
  CONSTRAINT `fk_seccion_curso` FOREIGN KEY (`curso_id`) REFERENCES `cursos` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_seccion_docente` FOREIGN KEY (`docente_id`) REFERENCES `docentes` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_seccion_periodo` FOREIGN KEY (`periodo_id`) REFERENCES `periodos_academicos` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `secciones`
--

LOCK TABLES `secciones` WRITE;
/*!40000 ALTER TABLE `secciones` DISABLE KEYS */;
INSERT INTO `secciones` VALUES (1,1,1,1,'SEC-A',30,3,'MANANA','ACTIVO'),(2,1,1,1,'SEC-B',30,0,'NOCHE','ACTIVO'),(3,2,1,2,'SEC-A',25,3,'MANANA','ACTIVO'),(4,3,1,1,'SEC-A',25,1,'TARDE','ACTIVO'),(5,4,1,3,'SEC-A',30,0,'NOCHE','ACTIVO'),(6,5,1,3,'SEC-A',25,1,'TARDE','ACTIVO'),(7,6,1,4,'SEC-A',35,1,'MANANA','ACTIVO'),(8,8,1,5,'SEC-A',40,1,'MANANA','ACTIVO');
/*!40000 ALTER TABLE `secciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'sistema_matricula'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-27 23:59:23
