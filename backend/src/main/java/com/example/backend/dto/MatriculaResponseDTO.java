package com.example.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MatriculaResponseDTO {
    private Long id;
    private String codigoMatricula;
    private Long estudianteId;
    private String estudianteCodigo;
    private String estudianteNombreCompleto;
    private String estudianteDni;
    private String carreraNombre;
    private Long periodoId;
    private String periodoCodigo;
    private LocalDateTime fechaMatricula;
    private Integer totalCreditos;
    private BigDecimal costoTotal;
    private String estado;
    private List<DetalleMatriculaDTO> cursos = new ArrayList<>();

    public MatriculaResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoMatricula() {
        return codigoMatricula;
    }

    public void setCodigoMatricula(String codigoMatricula) {
        this.codigoMatricula = codigoMatricula;
    }

    public Long getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(Long estudianteId) {
        this.estudianteId = estudianteId;
    }

    public String getEstudianteCodigo() {
        return estudianteCodigo;
    }

    public void setEstudianteCodigo(String estudianteCodigo) {
        this.estudianteCodigo = estudianteCodigo;
    }

    public String getEstudianteNombreCompleto() {
        return estudianteNombreCompleto;
    }

    public void setEstudianteNombreCompleto(String estudianteNombreCompleto) {
        this.estudianteNombreCompleto = estudianteNombreCompleto;
    }

    public String getEstudianteDni() {
        return estudianteDni;
    }

    public void setEstudianteDni(String estudianteDni) {
        this.estudianteDni = estudianteDni;
    }

    public String getCarreraNombre() {
        return carreraNombre;
    }

    public void setCarreraNombre(String carreraNombre) {
        this.carreraNombre = carreraNombre;
    }

    public Long getPeriodoId() {
        return periodoId;
    }

    public void setPeriodoId(Long periodoId) {
        this.periodoId = periodoId;
    }

    public String getPeriodoCodigo() {
        return periodoCodigo;
    }

    public void setPeriodoCodigo(String periodoCodigo) {
        this.periodoCodigo = periodoCodigo;
    }

    public LocalDateTime getFechaMatricula() {
        return fechaMatricula;
    }

    public void setFechaMatricula(LocalDateTime fechaMatricula) {
        this.fechaMatricula = fechaMatricula;
    }

    public Integer getTotalCreditos() {
        return totalCreditos;
    }

    public void setTotalCreditos(Integer totalCreditos) {
        this.totalCreditos = totalCreditos;
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<DetalleMatriculaDTO> getCursos() {
        return cursos;
    }

    public void setCursos(List<DetalleMatriculaDTO> cursos) {
        this.cursos = cursos;
    }
}
