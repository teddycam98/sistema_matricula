package com.example.backend.service.impl;

import com.example.backend.dto.DetalleMatriculaDTO;
import com.example.backend.entity.DetalleMatricula;
import com.example.backend.entity.Estudiante;
import com.example.backend.entity.Matricula;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.EstudianteRepository;
import com.example.backend.repository.MatriculaRepository;
import com.example.backend.service.IReporteService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.type.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * =========================================================================
 * IMPLEMENTACIÓN DEL SERVICIO DE REPORTES CON JASPERREPORTS
 * =========================================================================
 * Esta clase se encarga de orquestar la generación de documentos oficiales
 * en formato PDF utilizando el motor JasperReports Library.
 * 
 * Flujo de Trabajo en JasperReports:
 * 1. Diseño / Plantilla: Archivo XML (.jrxml) o JasperDesign en memoria.
 * 2. Compilación: JasperCompileManager compila el diseño a un objeto JasperReport.
 * 3. Llenado (Fill): JasperFillManager combina el reporte compilado con los
 *    parámetros (Map) y los datos de la fuente (JRBeanCollectionDataSource).
 * 4. Exportación: JasperExportManager convierte el JasperPrint resultante en un PDF binario (byte[]).
 */
@Service
@Transactional(readOnly = true)
public class ReporteServiceImpl implements IReporteService {

    private final MatriculaRepository matriculaRepository;
    private final EstudianteRepository estudianteRepository;

    /**
     * Inyección de dependencias por constructor de los repositorios JPA.
     */
    public ReporteServiceImpl(MatriculaRepository matriculaRepository, EstudianteRepository estudianteRepository) {
        this.matriculaRepository = matriculaRepository;
        this.estudianteRepository = estudianteRepository;
    }

    /**
     * Genera la Constancia / Ficha de Matrícula en PDF para una matrícula específica.
     * Carga el archivo .jrxml de resources, inyecta los parámetros del alumno y sus cursos.
     */
    @Override
    public byte[] generarFichaMatriculaPdf(Long matriculaId) {
        try {
            // 1. Obtener la entidad de Matrícula desde la Base de Datos
            Matricula matricula = matriculaRepository.findById(matriculaId)
                    .orElseThrow(() -> new ResourceNotFoundException("No se encontró la matrícula con ID: " + matriculaId));

            // 2. Mapear los detalles de los cursos inscritos a una lista de DTOs para el DataSource
            List<DetalleMatriculaDTO> cursosDTO = new ArrayList<>();
            if (matricula.getDetalles() != null) {
                for (DetalleMatricula d : matricula.getDetalles()) {
                    DetalleMatriculaDTO dto = new DetalleMatriculaDTO();
                    dto.setDetalleId(d.getId());
                    if (d.getSeccion() != null) {
                        dto.setCodigoSeccion(d.getSeccion().getCodigoSeccion());
                        if (d.getSeccion().getCurso() != null) {
                            dto.setCursoCodigo(d.getSeccion().getCurso().getCodigo());
                            dto.setCursoNombre(d.getSeccion().getCurso().getNombre());
                            dto.setCreditos(d.getSeccion().getCurso().getCreditos());
                            dto.setCiclo(d.getSeccion().getCurso().getCiclo());
                        }
                        if (d.getSeccion().getDocente() != null) {
                            dto.setDocenteNombre(d.getSeccion().getDocente().getNombres() + " " + d.getSeccion().getDocente().getApellidos());
                        }
                    }
                    dto.setCostoCurso(d.getCostoCurso());
                    dto.setEstadoCurso(d.getEstadoCurso());
                    cursosDTO.add(dto);
                }
            }

            // 3. Crear el BeanCollectionDataSource de JasperReports con la lista de asignaturas
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(cursosDTO);

            // 4. Preparar el mapa de Parámetros generales de la matrícula
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("P_CODIGO_MATRICULA", matricula.getCodigoMatricula());
            parameters.put("P_ESTUDIANTE_NOMBRE", matricula.getEstudiante() != null ? 
                    (matricula.getEstudiante().getNombres() + " " + matricula.getEstudiante().getApellidos()) : "N/A");
            parameters.put("P_ESTUDIANTE_DNI", matricula.getEstudiante() != null ? matricula.getEstudiante().getDni() : "N/A");
            parameters.put("P_ESTUDIANTE_CODIGO", matricula.getEstudiante() != null ? matricula.getEstudiante().getCodigoEstudiante() : "N/A");
            parameters.put("P_CARRERA", (matricula.getEstudiante() != null && matricula.getEstudiante().getCarrera() != null) ? 
                    matricula.getEstudiante().getCarrera().getNombre() : "N/A");
            parameters.put("P_PERIODO", matricula.getPeriodo() != null ? matricula.getPeriodo().getCodigo() : "N/A");
            parameters.put("P_TOTAL_CREDITOS", matricula.getTotalCreditos() != null ? matricula.getTotalCreditos() : 0);
            parameters.put("P_COSTO_TOTAL", matricula.getCostoTotal() != null ? matricula.getCostoTotal() : BigDecimal.ZERO);
            parameters.put("P_ESTADO", matricula.getEstado() != null ? matricula.getEstado() : "REGISTRADA");
            parameters.put("P_FECHA_EMISION", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

            // 5. Cargar y compilar el diseño JRXML desde el classpath
            InputStream reportStream = new ClassPathResource("reports/ficha_matricula.jrxml").getInputStream();
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // 6. Llenar el reporte (Fill) con los datos y parámetros
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // 7. Exportar el reporte en memoria a un arreglo binario PDF (byte[])
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception ex) {
            throw new RuntimeException("Error al generar la constancia de matrícula con JasperReports: " + ex.getMessage(), ex);
        }
    }

    /**
     * Genera el Reporte General de Matrículas utilizando JasperDesign programático en Java.
     * Esto demuestra la capacidad de diseñar reportes dinámicos sin requerir XML estático.
     */
    @Override
    public byte[] generarReporteMatriculasPdf() {
        try {
            List<Matricula> matriculas = matriculaRepository.findAll();

            // Construir el diseño dinámico del reporte
            JasperDesign design = new JasperDesign();
            design.setName("ReporteGeneralMatriculas");
            design.setPageWidth(595);
            design.setPageHeight(842);
            design.setColumnWidth(555);
            design.setLeftMargin(20);
            design.setRightMargin(20);
            design.setTopMargin(20);
            design.setBottomMargin(20);

            // Definir campos para el DataSource
            JRDesignField fCodigo = new JRDesignField();
            fCodigo.setName("codigoMatricula");
            fCodigo.setValueClass(String.class);
            design.addField(fCodigo);

            JRDesignField fPeriodo = new JRDesignField();
            fPeriodo.setName("periodoCodigo");
            fPeriodo.setValueClass(String.class);
            design.addField(fPeriodo);

            JRDesignField fEstudiante = new JRDesignField();
            fEstudiante.setName("estudianteNombreCompleto");
            fEstudiante.setValueClass(String.class);
            design.addField(fEstudiante);

            JRDesignField fCreditos = new JRDesignField();
            fCreditos.setName("totalCreditos");
            fCreditos.setValueClass(String.class);
            design.addField(fCreditos);

            JRDesignField fCosto = new JRDesignField();
            fCosto.setName("costoTotal");
            fCosto.setValueClass(String.class);
            design.addField(fCosto);

            JRDesignField fEstado = new JRDesignField();
            fEstado.setName("estado");
            fEstado.setValueClass(String.class);
            design.addField(fEstado);

            // Banda de Título
            JRDesignBand titleBand = new JRDesignBand();
            titleBand.setHeight(60);

            JRDesignStaticText titleText = new JRDesignStaticText();
            titleText.setText("REPORTE GENERAL DE MATRÍCULAS ACADÉMICAS");
            titleText.setX(0);
            titleText.setY(10);
            titleText.setWidth(555);
            titleText.setHeight(25);
            titleText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            titleText.setFontSize(14f);
            titleText.setBold(true);
            titleBand.addElement(titleText);

            JRDesignStaticText subText = new JRDesignStaticText();
            subText.setText("Emitido por el Sistema de Matrícula MVC - Spring Boot 4 & MySQL");
            subText.setX(0);
            subText.setY(35);
            subText.setWidth(555);
            subText.setHeight(15);
            subText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            subText.setFontSize(9f);
            titleBand.addElement(subText);
            design.setTitle(titleBand);

            // Banda de Encabezado de Columnas
            JRDesignBand colHeader = new JRDesignBand();
            colHeader.setHeight(25);

            String[] titulos = {"Código", "Periodo", "Estudiante", "Créd.", "Costo", "Estado"};
            int[] xs = {5, 95, 170, 370, 420, 490};
            int[] widths = {85, 70, 195, 45, 65, 60};

            for (int i = 0; i < titulos.length; i++) {
                JRDesignStaticText st = new JRDesignStaticText();
                st.setText(titulos[i]);
                st.setX(xs[i]);
                st.setY(5);
                st.setWidth(widths[i]);
                st.setHeight(18);
                st.setBold(true);
                st.setFontSize(9f);
                colHeader.addElement(st);
            }
            design.setColumnHeader(colHeader);

            // Banda de Detalle
            JRDesignBand detail = new JRDesignBand();
            detail.setHeight(20);

            String[] exprs = {"$F{codigoMatricula}", "$F{periodoCodigo}", "$F{estudianteNombreCompleto}", 
                              "$F{totalCreditos}", "$F{costoTotal}", "$F{estado}"};

            for (int i = 0; i < exprs.length; i++) {
                JRDesignTextField tf = new JRDesignTextField();
                JRDesignExpression exp = new JRDesignExpression();
                exp.setText(exprs[i]);
                tf.setExpression(exp);
                tf.setX(xs[i]);
                tf.setY(2);
                tf.setWidth(widths[i]);
                tf.setHeight(16);
                tf.setFontSize(8.5f);
                detail.addElement(tf);
            }
            ((JRDesignSection) design.getDetailSection()).addBand(detail);

            // Compilar y Llenar
            JasperReport report = JasperCompileManager.compileReport(design);

            List<Map<String, Object>> dataList = new ArrayList<>();
            for (Matricula m : matriculas) {
                Map<String, Object> map = new HashMap<>();
                map.put("codigoMatricula", m.getCodigoMatricula());
                map.put("periodoCodigo", m.getPeriodo() != null ? m.getPeriodo().getCodigo() : "--");
                map.put("estudianteNombreCompleto", m.getEstudiante() != null ? 
                        (m.getEstudiante().getNombres() + " " + m.getEstudiante().getApellidos()) : "--");
                map.put("totalCreditos", (m.getTotalCreditos() != null ? m.getTotalCreditos() : 0) + " cred.");
                map.put("costoTotal", "S/ " + (m.getCostoTotal() != null ? m.getCostoTotal() : BigDecimal.ZERO));
                map.put("estado", m.getEstado());
                dataList.add(map);
            }

            JasperPrint print = JasperFillManager.fillReport(report, new HashMap<>(), new JRBeanCollectionDataSource(dataList));
            return JasperExportManager.exportReportToPdf(print);

        } catch (Exception ex) {
            throw new RuntimeException("Error al generar el reporte de matrículas: " + ex.getMessage(), ex);
        }
    }

    /**
     * Genera el Padrón Oficial de Estudiantes en PDF.
     */
    @Override
    public byte[] generarReporteEstudiantesPdf() {
        try {
            List<Estudiante> estudiantes = estudianteRepository.findAll();

            JasperDesign design = new JasperDesign();
            design.setName("PadronEstudiantes");
            design.setPageWidth(595);
            design.setPageHeight(842);
            design.setColumnWidth(555);
            design.setLeftMargin(20);
            design.setRightMargin(20);
            design.setTopMargin(20);
            design.setBottomMargin(20);

            // Campos
            JRDesignField fCod = new JRDesignField();
            fCod.setName("codigoEstudiante");
            fCod.setValueClass(String.class);
            design.addField(fCod);

            JRDesignField fDni = new JRDesignField();
            fDni.setName("dni");
            fDni.setValueClass(String.class);
            design.addField(fDni);

            JRDesignField fNombre = new JRDesignField();
            fNombre.setName("nombreCompleto");
            fNombre.setValueClass(String.class);
            design.addField(fNombre);

            JRDesignField fCarrera = new JRDesignField();
            fCarrera.setName("carreraNombre");
            fCarrera.setValueClass(String.class);
            design.addField(fCarrera);

            JRDesignField fEmail = new JRDesignField();
            fEmail.setName("email");
            fEmail.setValueClass(String.class);
            design.addField(fEmail);

            // Título
            JRDesignBand titleBand = new JRDesignBand();
            titleBand.setHeight(50);
            JRDesignStaticText titleText = new JRDesignStaticText();
            titleText.setText("PADRÓN OFICIAL DE ESTUDIANTES UNIVERSITARIOS");
            titleText.setX(0);
            titleText.setY(10);
            titleText.setWidth(555);
            titleText.setHeight(25);
            titleText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            titleText.setFontSize(14f);
            titleText.setBold(true);
            titleBand.addElement(titleText);
            design.setTitle(titleBand);

            // Column Header
            JRDesignBand colHeader = new JRDesignBand();
            colHeader.setHeight(25);

            String[] titulos = {"Código", "DNI", "Nombres y Apellidos", "Carrera", "Correo Institucional"};
            int[] xs = {5, 75, 145, 335, 455};
            int[] widths = {65, 65, 185, 115, 95};

            for (int i = 0; i < titulos.length; i++) {
                JRDesignStaticText st = new JRDesignStaticText();
                st.setText(titulos[i]);
                st.setX(xs[i]);
                st.setY(5);
                st.setWidth(widths[i]);
                st.setHeight(18);
                st.setBold(true);
                st.setFontSize(8.5f);
                colHeader.addElement(st);
            }
            design.setColumnHeader(colHeader);

            // Detalle
            JRDesignBand detail = new JRDesignBand();
            detail.setHeight(18);

            String[] exprs = {"$F{codigoEstudiante}", "$F{dni}", "$F{nombreCompleto}", "$F{carreraNombre}", "$F{email}"};

            for (int i = 0; i < exprs.length; i++) {
                JRDesignTextField tf = new JRDesignTextField();
                JRDesignExpression exp = new JRDesignExpression();
                exp.setText(exprs[i]);
                tf.setExpression(exp);
                tf.setX(xs[i]);
                tf.setY(2);
                tf.setWidth(widths[i]);
                tf.setHeight(15);
                tf.setFontSize(8f);
                detail.addElement(tf);
            }
            ((JRDesignSection) design.getDetailSection()).addBand(detail);

            JasperReport report = JasperCompileManager.compileReport(design);

            List<Map<String, Object>> dataList = new ArrayList<>();
            for (Estudiante e : estudiantes) {
                Map<String, Object> map = new HashMap<>();
                map.put("codigoEstudiante", e.getCodigoEstudiante());
                map.put("dni", e.getDni());
                map.put("nombreCompleto", e.getNombres() + " " + e.getApellidos());
                map.put("carreraNombre", e.getCarrera() != null ? e.getCarrera().getNombre() : "--");
                map.put("email", e.getEmail());
                dataList.add(map);
            }

            JasperPrint print = JasperFillManager.fillReport(report, new HashMap<>(), new JRBeanCollectionDataSource(dataList));
            return JasperExportManager.exportReportToPdf(print);

        } catch (Exception ex) {
            throw new RuntimeException("Error al generar el padrón de estudiantes: " + ex.getMessage(), ex);
        }
    }
}
