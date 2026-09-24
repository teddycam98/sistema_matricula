package com.example.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "detalles_matricula", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"matricula_id", "seccion_id"})
})
public class DetalleMatricula implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matricula_id", nullable = false)
    @JsonBackReference
    private Matricula matricula;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seccion_id", nullable = false)
    private Seccion seccion;

    @Column(name = "costo_curso", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoCurso;

    @Column(name = "promedio_final", precision = 4, scale = 2)
    private BigDecimal promedioFinal;

    @Column(name = "estado_curso", nullable = false, length = 20)
    private String estadoCurso = "CURSANDO"; // CURSANDO, APROBADO, DESAPROBADO, RETIRADO

    public DetalleMatricula() {
    }

    public DetalleMatricula(Long id, Matricula matricula, Seccion seccion, BigDecimal costoCurso, BigDecimal promedioFinal, String estadoCurso) {
        this.id = id;
        this.matricula = matricula;
        this.seccion = seccion;
        this.costoCurso = costoCurso;
        this.promedioFinal = promedioFinal;
        this.estadoCurso = estadoCurso;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Matricula getMatricula() {
        return matricula;
    }

    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
    }

    public Seccion getSeccion() {
        return seccion;
    }

    public void setSeccion(Seccion seccion) {
        this.seccion = seccion;
    }

    public BigDecimal getCostoCurso() {
        return costoCurso;
    }

    public void setCostoCurso(BigDecimal costoCurso) {
        this.costoCurso = costoCurso;
    }

    public BigDecimal getPromedioFinal() {
        return promedioFinal;
    }

    public void setPromedioFinal(BigDecimal promedioFinal) {
        this.promedioFinal = promedioFinal;
    }

    public String getEstadoCurso() {
        return estadoCurso;
    }

    public void setEstadoCurso(String estadoCurso) {
        this.estadoCurso = estadoCurso;
    }
}
