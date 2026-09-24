package com.example.backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "planes_estudio")
public class PlanEstudio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "carrera_id", nullable = false)
    private Carrera carrera;

    @Column(name = "codigo_plan", nullable = false, unique = true, length = 30)
    private String codigoPlan;

    @Column(name = "anio_vigencia", nullable = false)
    private Integer anioVigencia;

    @Column(name = "total_creditos", nullable = false)
    private Integer totalCreditos;

    @Column(nullable = false, length = 20)
    private String estado = "ACTIVO";

    public PlanEstudio() {
    }

    public PlanEstudio(Long id, Carrera carrera, String codigoPlan, Integer anioVigencia, Integer totalCreditos, String estado) {
        this.id = id;
        this.carrera = carrera;
        this.codigoPlan = codigoPlan;
        this.anioVigencia = anioVigencia;
        this.totalCreditos = totalCreditos;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public String getCodigoPlan() {
        return codigoPlan;
    }

    public void setCodigoPlan(String codigoPlan) {
        this.codigoPlan = codigoPlan;
    }

    public Integer getAnioVigencia() {
        return anioVigencia;
    }

    public void setAnioVigencia(Integer anioVigencia) {
        this.anioVigencia = anioVigencia;
    }

    public Integer getTotalCreditos() {
        return totalCreditos;
    }

    public void setTotalCreditos(Integer totalCreditos) {
        this.totalCreditos = totalCreditos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
