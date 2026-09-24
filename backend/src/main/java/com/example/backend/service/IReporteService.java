package com.example.backend.service;

/**
 * Interfaz de servicio para la generación de reportes con JasperReports.
 * Proporciona métodos para exportar documentos PDF oficiales del sistema.
 */
public interface IReporteService {

    /**
     * Genera la Ficha / Constancia Oficial de Matrícula en formato PDF para un estudiante.
     * @param matriculaId Identificador de la matrícula
     * @return Arreglo de bytes del PDF generado
     */
    byte[] generarFichaMatriculaPdf(Long matriculaId);

    /**
     * Genera el Reporte General de Matrículas registradas en formato PDF.
     * @return Arreglo de bytes del PDF generado
     */
    byte[] generarReporteMatriculasPdf();

    /**
     * Genera el Padrón Oficial de Estudiantes registrados en formato PDF.
     * @return Arreglo de bytes del PDF generado
     */
    byte[] generarReporteEstudiantesPdf();
}
