package com.example.backend.controller;

import com.example.backend.service.IReporteService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * =========================================================================
 * CONTROLADOR REST PARA REPORTES CON JASPERREPORTS
 * =========================================================================
 * Este controlador expone endpoints HTTP para la descarga y visualización en línea
 * de documentos PDF oficiales generados dinámicamente con JasperReports.
 * 
 * Uso de Cabeceras HTTP:
 * - Content-Type: application/pdf (Indica al navegador que el contenido es un documento PDF)
 * - Content-Disposition: inline (Permite que el navegador abra el visor PDF incrustado
 *   en lugar de forzar una descarga ciega)
 */
@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final IReporteService reporteService;

    /**
     * Inyección del servicio de reportes mediante el constructor.
     */
    public ReporteController(IReporteService reporteService) {
        this.reporteService = reporteService;
    }

    /**
     * Endpoint para generar y visualizar la Ficha / Constancia de Matrícula en PDF.
     * Ejemplo: GET http://localhost:8080/api/reportes/matricula/1/pdf
     * 
     * @param id ID de la matrícula
     * @return Archivo binario PDF con código HTTP 200
     */
    @GetMapping("/matricula/{id}/pdf")
    public ResponseEntity<byte[]> descargarFichaMatriculaPdf(@PathVariable Long id) {
        // 1. Invocar al servicio JasperReports para compilar y generar los bytes del PDF
        byte[] pdfBytes = reporteService.generarFichaMatriculaPdf(id);

        // 2. Configurar las cabeceras HTTP de respuesta
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "ficha_matricula_" + id + ".pdf");
        headers.setContentLength(pdfBytes.length);

        // 3. Retornar el documento PDF listo para visualización o impresión
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    /**
     * Endpoint para generar el Reporte General de Matrículas en PDF.
     * Ejemplo: GET http://localhost:8080/api/reportes/matriculas/pdf
     */
    @GetMapping("/matriculas/pdf")
    public ResponseEntity<byte[]> descargarReporteMatriculasPdf() {
        byte[] pdfBytes = reporteService.generarReporteMatriculasPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "reporte_general_matriculas.pdf");
        headers.setContentLength(pdfBytes.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    /**
     * Endpoint para generar el Padrón Oficial de Estudiantes en PDF.
     * Ejemplo: GET http://localhost:8080/api/reportes/estudiantes/pdf
     */
    @GetMapping("/estudiantes/pdf")
    public ResponseEntity<byte[]> descargarPadronEstudiantesPdf() {
        byte[] pdfBytes = reporteService.generarReporteEstudiantesPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "padron_estudiantes.pdf");
        headers.setContentLength(pdfBytes.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
