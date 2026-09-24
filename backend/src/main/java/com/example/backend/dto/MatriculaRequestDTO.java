package com.example.backend.dto;

import java.util.List;

public class MatriculaRequestDTO {
    private Long estudianteId;
    private Long periodoId;
    private List<Long> seccionIds;
    private String metodoPago; // Opcional: TARJETA, TRANSFERENCIA, YAPE, EFECTIVO

    public MatriculaRequestDTO() {
    }

    public MatriculaRequestDTO(Long estudianteId, Long periodoId, List<Long> seccionIds, String metodoPago) {
        this.estudianteId = estudianteId;
        this.periodoId = periodoId;
        this.seccionIds = seccionIds;
        this.metodoPago = metodoPago;
    }

    public Long getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(Long estudianteId) {
        this.estudianteId = estudianteId;
    }

    public Long getPeriodoId() {
        return periodoId;
    }

    public void setPeriodoId(Long periodoId) {
        this.periodoId = periodoId;
    }

    public List<Long> getSeccionIds() {
        return seccionIds;
    }

    public void setSeccionIds(List<Long> seccionIds) {
        this.seccionIds = seccionIds;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
}
