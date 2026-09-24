package com.example.backend.dto;

import java.math.BigDecimal;

public class DashboardStatsDTO {
    private Long totalEstudiantes;
    private Long totalCursos;
    private Long totalDocentes;
    private Long totalMatriculas;
    private Long totalSecciones;
    private BigDecimal totalRecaudado;

    public DashboardStatsDTO() {
    }

    public DashboardStatsDTO(Long totalEstudiantes, Long totalCursos, Long totalDocentes, Long totalMatriculas, Long totalSecciones, BigDecimal totalRecaudado) {
        this.totalEstudiantes = totalEstudiantes;
        this.totalCursos = totalCursos;
        this.totalDocentes = totalDocentes;
        this.totalMatriculas = totalMatriculas;
        this.totalSecciones = totalSecciones;
        this.totalRecaudado = totalRecaudado;
    }

    public Long getTotalEstudiantes() {
        return totalEstudiantes;
    }

    public void setTotalEstudiantes(Long totalEstudiantes) {
        this.totalEstudiantes = totalEstudiantes;
    }

    public Long getTotalCursos() {
        return totalCursos;
    }

    public void setTotalCursos(Long totalCursos) {
        this.totalCursos = totalCursos;
    }

    public Long getTotalDocentes() {
        return totalDocentes;
    }

    public void setTotalDocentes(Long totalDocentes) {
        this.totalDocentes = totalDocentes;
    }

    public Long getTotalMatriculas() {
        return totalMatriculas;
    }

    public void setTotalMatriculas(Long totalMatriculas) {
        this.totalMatriculas = totalMatriculas;
    }

    public Long getTotalSecciones() {
        return totalSecciones;
    }

    public void setTotalSecciones(Long totalSecciones) {
        this.totalSecciones = totalSecciones;
    }

    public BigDecimal getTotalRecaudado() {
        return totalRecaudado;
    }

    public void setTotalRecaudado(BigDecimal totalRecaudado) {
        this.totalRecaudado = totalRecaudado;
    }
}
