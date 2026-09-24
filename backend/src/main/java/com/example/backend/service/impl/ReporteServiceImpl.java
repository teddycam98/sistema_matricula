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
 * en formato PDF utilizando el motor JasperReports Library v6.21.3.
 * 
 * Flujo de Trabajo en JasperReports:
 * 1. Diseño / Plantilla: Archivo XML (.jrxml) diseñado con Jaspersoft Studio
 *    o generado en memoria mediante la API JasperDesign.
 * 2. Compilación: JasperCompileManager compila el diseño a un objeto JasperReport.
 * 3. Llenado (Fill): JasperFillManager combina el reporte compilado con los
 *    parámetros (Map) y los datos de la fuente (JRBeanCollectionDataSource).
 * 4. Exportación: JasperExportManager convierte el JasperPrint resultante en
 *    un PDF binario (byte[]) listo para ser descargado o visualizado.
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
     * Genera la Constancia / Ficha Oficial de Matrícula en PDF para una matrícula específica.
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
                    dto.setCostoCurso(d.getCostoCurso() != null ? d.getCostoCurso() : BigDecimal.ZERO);
                    dto.setEstadoCurso(d.getEstadoCurso() != null ? d.getEstadoCurso() : "INSCRITO");
                    cursosDTO.add(dto);
                }
            }

            // 3. Crear el BeanCollectionDataSource de JasperReports con la lista de asignaturas
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(cursosDTO);

            // 4. Preparar el mapa de Parámetros generales de la matrícula
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("P_CODIGO_MATRICULA", matricula.getCodigoMatricula() != null ? matricula.getCodigoMatricula() : "MAT-2026-0000");
            parameters.put("P_ESTUDIANTE_NOMBRE", matricula.getEstudiante() != null ? 
                    (matricula.getEstudiante().getNombres() + " " + matricula.getEstudiante().getApellidos()) : "N/A");
            parameters.put("P_ESTUDIANTE_DNI", matricula.getEstudiante() != null ? matricula.getEstudiante().getDni() : "N/A");
            parameters.put("P_ESTUDIANTE_CODIGO", matricula.getEstudiante() != null ? matricula.getEstudiante().getCodigoEstudiante() : "N/A");
            parameters.put("P_CARRERA", (matricula.getEstudiante() != null && matricula.getEstudiante().getCarrera() != null) ? 
                    matricula.getEstudiante().getCarrera().getNombre() : "N/A");
            parameters.put("P_PERIODO", matricula.getPeriodo() != null ? matricula.getPeriodo().getCodigo() : "2026-I");
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
     * Genera el Reporte General Consolidado de Matrículas utilizando JasperDesign programático.
     * Diseño institucional mejorado con encabezado corporativo, bordes y resumen.
     */
    @Override
    public byte[] generarReporteMatriculasPdf() {
        try {
            List<Matricula> matriculas = matriculaRepository.findAll();

            // Construir el diseño dinámico del reporte con JasperDesign
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
            String[] campos = {"codigoMatricula", "periodoCodigo", "estudianteNombreCompleto", "totalCreditos", "costoTotal", "estado"};
            for (String campo : campos) {
                JRDesignField field = new JRDesignField();
                field.setName(campo);
                field.setValueClass(String.class);
                design.addField(field);
            }

            // ==========================================
            // BANDA DE TÍTULO INSTITUCIONAL (TITLE)
            // ==========================================
            JRDesignBand titleBand = new JRDesignBand();
            titleBand.setHeight(80);

            // Rectángulo azul oscuro de cabecera
            JRDesignRectangle banner = new JRDesignRectangle();
            banner.setX(0);
            banner.setY(0);
            banner.setWidth(555);
            banner.setHeight(52);
            banner.setBackcolor(new java.awt.Color(15, 23, 42)); // Slate 900
            banner.getLinePen().setLineWidth(0f);
            titleBand.addElement(banner);

            // Franja de acento azul brillante
            JRDesignRectangle accent = new JRDesignRectangle();
            accent.setX(0);
            accent.setY(52);
            accent.setWidth(555);
            accent.setHeight(3);
            accent.setBackcolor(new java.awt.Color(37, 99, 235)); // Blue 600
            accent.getLinePen().setLineWidth(0f);
            titleBand.addElement(accent);

            JRDesignStaticText univText = new JRDesignStaticText();
            univText.setText("UNIVERSIDAD NACIONAL DE TECNOLOGÍA");
            univText.setX(10);
            univText.setY(6);
            univText.setWidth(535);
            univText.setHeight(20);
            univText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            univText.setFontSize(14f);
            univText.setBold(true);
            univText.setForecolor(java.awt.Color.WHITE);
            titleBand.addElement(univText);

            JRDesignStaticText titleText = new JRDesignStaticText();
            titleText.setText("REPORTE CONSOLIDADO DE MATRÍCULAS ACADÉMICAS");
            titleText.setX(10);
            titleText.setY(28);
            titleText.setWidth(535);
            titleText.setHeight(18);
            titleText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            titleText.setFontSize(11f);
            titleText.setBold(true);
            titleText.setForecolor(new java.awt.Color(191, 219, 254)); // Light Blue
            titleBand.addElement(titleText);

            JRDesignStaticText subText = new JRDesignStaticText();
            subText.setText("Generado automáticamente mediante JasperReports Library v6.21.3 • Fecha: " + 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            subText.setX(0);
            subText.setY(60);
            subText.setWidth(555);
            subText.setHeight(16);
            subText.setFontSize(8f);
            subText.setForecolor(new java.awt.Color(100, 116, 139));
            titleBand.addElement(subText);

            design.setTitle(titleBand);

            // ==========================================
            // BANDA DE ENCABEZADO DE COLUMNAS (COLUMN HEADER)
            // ==========================================
            JRDesignBand colHeader = new JRDesignBand();
            colHeader.setHeight(24);

            JRDesignRectangle colBg = new JRDesignRectangle();
            colBg.setX(0);
            colBg.setY(0);
            colBg.setWidth(555);
            colBg.setHeight(24);
            colBg.setBackcolor(new java.awt.Color(30, 41, 59)); // Slate 800
            colBg.getLinePen().setLineWidth(0f);
            colHeader.addElement(colBg);

            String[] titulos = {"Código", "Periodo", "Estudiante", "Créditos", "Costo Total", "Estado"};
            int[] xs = {6, 95, 170, 365, 420, 490};
            int[] widths = {85, 70, 190, 50, 65, 60};

            for (int i = 0; i < titulos.length; i++) {
                JRDesignStaticText st = new JRDesignStaticText();
                st.setText(titulos[i]);
                st.setX(xs[i]);
                st.setY(4);
                st.setWidth(widths[i]);
                st.setHeight(16);
                st.setBold(true);
                st.setFontSize(8.5f);
                st.setForecolor(java.awt.Color.WHITE);
                colHeader.addElement(st);
            }
            design.setColumnHeader(colHeader);

            // ==========================================
            // BANDA DE DETALLE (DETAIL)
            // ==========================================
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
                tf.setFontSize(8f);
                tf.setForecolor(new java.awt.Color(15, 23, 42));
                if (i == 0) {
                    tf.setBold(true);
                    tf.setForecolor(new java.awt.Color(37, 99, 235));
                }
                detail.addElement(tf);
            }

            // Línea divisoria suave
            JRDesignLine line = new JRDesignLine();
            line.setX(0);
            line.setY(19);
            line.setWidth(555);
            line.setHeight(1);
            line.getLinePen().setLineWidth(0.5f);
            line.getLinePen().setLineColor(new java.awt.Color(226, 232, 240));
            detail.addElement(line);

            ((JRDesignSection) design.getDetailSection()).addBand(detail);

            // ==========================================
            // BANDA DE RESUMEN Y PIE (SUMMARY)
            // ==========================================
            JRDesignBand summaryBand = new JRDesignBand();
            summaryBand.setHeight(45);

            JRDesignRectangle sumBox = new JRDesignRectangle();
            sumBox.setX(0);
            sumBox.setY(8);
            sumBox.setWidth(555);
            sumBox.setHeight(28);
            sumBox.setBackcolor(new java.awt.Color(241, 245, 249));
            sumBox.getLinePen().setLineWidth(0.5f);
            sumBox.getLinePen().setLineColor(new java.awt.Color(203, 213, 225));
            summaryBand.addElement(sumBox);

            JRDesignStaticText sumText = new JRDesignStaticText();
            sumText.setText("TOTAL DE REGISTROS DE MATRÍCULA: " + matriculas.size() + " matrículas procesadas con éxito.");
            sumText.setX(10);
            sumText.setY(14);
            sumText.setWidth(535);
            sumText.setHeight(16);
            sumText.setFontSize(8.5f);
            sumText.setBold(true);
            sumText.setForecolor(new java.awt.Color(30, 41, 59));
            summaryBand.addElement(sumText);

            design.setSummary(summaryBand);

            // Compilar reporte
            JasperReport report = JasperCompileManager.compileReport(design);

            List<Map<String, Object>> dataList = new ArrayList<>();
            for (Matricula m : matriculas) {
                Map<String, Object> map = new HashMap<>();
                map.put("codigoMatricula", m.getCodigoMatricula() != null ? m.getCodigoMatricula() : "MAT-0000");
                map.put("periodoCodigo", m.getPeriodo() != null ? m.getPeriodo().getCodigo() : "--");
                map.put("estudianteNombreCompleto", m.getEstudiante() != null ? 
                        (m.getEstudiante().getNombres() + " " + m.getEstudiante().getApellidos()) : "--");
                map.put("totalCreditos", (m.getTotalCreditos() != null ? m.getTotalCreditos() : 0) + " cred.");
                map.put("costoTotal", "S/ " + (m.getCostoTotal() != null ? m.getCostoTotal() : BigDecimal.ZERO));
                map.put("estado", m.getEstado() != null ? m.getEstado() : "REGISTRADA");
                dataList.add(map);
            }

            JasperPrint print = JasperFillManager.fillReport(report, new HashMap<>(), new JRBeanCollectionDataSource(dataList));
            return JasperExportManager.exportReportToPdf(print);

        } catch (Exception ex) {
            throw new RuntimeException("Error al generar el reporte consolidado de matrículas: " + ex.getMessage(), ex);
        }
    }

    /**
     * Genera el Padrón Oficial de Estudiantes Universitarios en PDF.
     * Diseño institucional mejorado con encabezado corporativo, bordes y resumen.
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

            // Campos para el DataSource
            String[] campos = {"codigoEstudiante", "dni", "nombreCompleto", "carreraNombre", "email"};
            for (String campo : campos) {
                JRDesignField field = new JRDesignField();
                field.setName(campo);
                field.setValueClass(String.class);
                design.addField(field);
            }

            // ==========================================
            // BANDA DE TÍTULO INSTITUCIONAL (TITLE)
            // ==========================================
            JRDesignBand titleBand = new JRDesignBand();
            titleBand.setHeight(80);

            JRDesignRectangle banner = new JRDesignRectangle();
            banner.setX(0);
            banner.setY(0);
            banner.setWidth(555);
            banner.setHeight(52);
            banner.setBackcolor(new java.awt.Color(15, 23, 42)); // Slate 900
            banner.getLinePen().setLineWidth(0f);
            titleBand.addElement(banner);

            JRDesignRectangle accent = new JRDesignRectangle();
            accent.setX(0);
            accent.setY(52);
            accent.setWidth(555);
            accent.setHeight(3);
            accent.setBackcolor(new java.awt.Color(16, 185, 129)); // Emerald 500
            accent.getLinePen().setLineWidth(0f);
            titleBand.addElement(accent);

            JRDesignStaticText univText = new JRDesignStaticText();
            univText.setText("UNIVERSIDAD NACIONAL DE TECNOLOGÍA");
            univText.setX(10);
            univText.setY(6);
            univText.setWidth(535);
            univText.setHeight(20);
            univText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            univText.setFontSize(14f);
            univText.setBold(true);
            univText.setForecolor(java.awt.Color.WHITE);
            titleBand.addElement(univText);

            JRDesignStaticText titleText = new JRDesignStaticText();
            titleText.setText("PADRÓN GENERAL DE ESTUDIANTES UNIVERSITARIOS");
            titleText.setX(10);
            titleText.setY(28);
            titleText.setWidth(535);
            titleText.setHeight(18);
            titleText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            titleText.setFontSize(11f);
            titleText.setBold(true);
            titleText.setForecolor(new java.awt.Color(167, 243, 208)); // Light Emerald
            titleBand.addElement(titleText);

            JRDesignStaticText subText = new JRDesignStaticText();
            subText.setText("Documento oficial expedido mediante JasperReports Library v6.21.3 • Fecha: " + 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            subText.setX(0);
            subText.setY(60);
            subText.setWidth(555);
            subText.setHeight(16);
            subText.setFontSize(8f);
            subText.setForecolor(new java.awt.Color(100, 116, 139));
            titleBand.addElement(subText);

            design.setTitle(titleBand);

            // ==========================================
            // BANDA DE ENCABEZADO DE COLUMNAS (COLUMN HEADER)
            // ==========================================
            JRDesignBand colHeader = new JRDesignBand();
            colHeader.setHeight(24);

            JRDesignRectangle colBg = new JRDesignRectangle();
            colBg.setX(0);
            colBg.setY(0);
            colBg.setWidth(555);
            colBg.setHeight(24);
            colBg.setBackcolor(new java.awt.Color(30, 41, 59));
            colBg.getLinePen().setLineWidth(0f);
            colHeader.addElement(colBg);

            String[] titulos = {"Código", "DNI", "Nombres y Apellidos", "Carrera", "Correo Institucional"};
            int[] xs = {6, 75, 145, 335, 450};
            int[] widths = {65, 65, 185, 110, 100};

            for (int i = 0; i < titulos.length; i++) {
                JRDesignStaticText st = new JRDesignStaticText();
                st.setText(titulos[i]);
                st.setX(xs[i]);
                st.setY(4);
                st.setWidth(widths[i]);
                st.setHeight(16);
                st.setBold(true);
                st.setFontSize(8.5f);
                st.setForecolor(java.awt.Color.WHITE);
                colHeader.addElement(st);
            }
            design.setColumnHeader(colHeader);

            // ==========================================
            // BANDA DE DETALLE (DETAIL)
            // ==========================================
            JRDesignBand detail = new JRDesignBand();
            detail.setHeight(19);

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
                tf.setForecolor(new java.awt.Color(15, 23, 42));
                if (i == 0) {
                    tf.setBold(true);
                    tf.setForecolor(new java.awt.Color(16, 185, 129));
                }
                detail.addElement(tf);
            }

            JRDesignLine line = new JRDesignLine();
            line.setX(0);
            line.setY(18);
            line.setWidth(555);
            line.setHeight(1);
            line.getLinePen().setLineWidth(0.5f);
            line.getLinePen().setLineColor(new java.awt.Color(226, 232, 240));
            detail.addElement(line);

            ((JRDesignSection) design.getDetailSection()).addBand(detail);

            // ==========================================
            // BANDA DE RESUMEN Y PIE (SUMMARY)
            // ==========================================
            JRDesignBand summaryBand = new JRDesignBand();
            summaryBand.setHeight(45);

            JRDesignRectangle sumBox = new JRDesignRectangle();
            sumBox.setX(0);
            sumBox.setY(8);
            sumBox.setWidth(555);
            sumBox.setHeight(28);
            sumBox.setBackcolor(new java.awt.Color(241, 245, 249));
            sumBox.getLinePen().setLineWidth(0.5f);
            sumBox.getLinePen().setLineColor(new java.awt.Color(203, 213, 225));
            summaryBand.addElement(sumBox);

            JRDesignStaticText sumText = new JRDesignStaticText();
            sumText.setText("TOTAL DE ESTUDIANTES EN PADRÓN: " + estudiantes.size() + " alumnos en condición regular.");
            sumText.setX(10);
            sumText.setY(14);
            sumText.setWidth(535);
            sumText.setHeight(16);
            sumText.setFontSize(8.5f);
            sumText.setBold(true);
            sumText.setForecolor(new java.awt.Color(30, 41, 59));
            summaryBand.addElement(sumText);

            design.setSummary(summaryBand);

            JasperReport report = JasperCompileManager.compileReport(design);

            List<Map<String, Object>> dataList = new ArrayList<>();
            for (Estudiante e : estudiantes) {
                Map<String, Object> map = new HashMap<>();
                map.put("codigoEstudiante", e.getCodigoEstudiante() != null ? e.getCodigoEstudiante() : "--");
                map.put("dni", e.getDni() != null ? e.getDni() : "--");
                map.put("nombreCompleto", e.getNombres() + " " + e.getApellidos());
                map.put("carreraNombre", e.getCarrera() != null ? e.getCarrera().getNombre() : "--");
                map.put("email", e.getEmail() != null ? e.getEmail() : "--");
                dataList.add(map);
            }

            JasperPrint print = JasperFillManager.fillReport(report, new HashMap<>(), new JRBeanCollectionDataSource(dataList));
            return JasperExportManager.exportReportToPdf(print);

        } catch (Exception ex) {
            throw new RuntimeException("Error al generar el padrón de estudiantes: " + ex.getMessage(), ex);
        }
    }
}
