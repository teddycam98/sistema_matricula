package com.example.backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "cursos")
public class Curso implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plan_estudio_id", nullable = false)
    private PlanEstudio planEstudio;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false)
    private Integer creditos;

    @Column(name = "horas_teoria", nullable = false)
    private Integer horasTeoria = 2;

    @Column(name = "horas_practica", nullable = false)
    private Integer horasPractica = 2;

    @Column(nullable = false)
    private Integer ciclo = 1;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal costo = BigDecimal.valueOf(200.00);

    @Column(nullable = false, length = 20)
    private String estado = "ACTIVO";

    public Curso() {
    }

    public Curso(Long id, PlanEstudio planEstudio, String codigo, String nombre, Integer creditos, Integer horasTeoria, Integer horasPractica, Integer ciclo, BigDecimal costo, String estado) {
        this.id = id;
        this.planEstudio = planEstudio;
        this.codigo = codigo;
        this.nombre = nombre;
        this.creditos = creditos;
        this.horasTeoria = horasTeoria;
        this.horasPractica = horasPractica;
        this.ciclo = ciclo;
        this.costo = costo;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlanEstudio getPlanEstudio() {
        return planEstudio;
    }

    public void setPlanEstudio(PlanEstudio planEstudio) {
        this.planEstudio = planEstudio;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCreditos() {
        return creditos;
    }

    public void setCreditos(Integer creditos) {
        this.creditos = creditos;
    }

    public Integer getHorasTeoria() {
        return horasTeoria;
    }

    public void setHorasTeoria(Integer horasTeoria) {
        this.horasTeoria = horasTeoria;
    }

    public Integer getHorasPractica() {
        return horasPractica;
    }

    public void setHorasPractica(Integer horasPractica) {
        this.horasPractica = horasPractica;
    }

    public Integer getCiclo() {
        return ciclo;
    }

    public void setCiclo(Integer ciclo) {
        this.ciclo = ciclo;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
