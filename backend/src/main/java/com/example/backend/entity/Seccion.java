package com.example.backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "secciones", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"curso_id", "periodo_id", "codigo_seccion"})
})
public class Seccion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "periodo_id", nullable = false)
    private PeriodoAcademico periodo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "docente_id", nullable = false)
    private Docente docente;

    @Column(name = "codigo_seccion", nullable = false, length = 10)
    private String codigoSeccion;

    @Column(nullable = false)
    private Integer vacantes = 35;

    @Column(nullable = false)
    private Integer matriculados = 0;

    @Column(nullable = false, length = 20)
    private String turno = "MAÑANA"; // MAÑANA, TARDE, NOCHE

    @Column(nullable = false, length = 20)
    private String estado = "ACTIVO";

    public Seccion() {
    }

    public Seccion(Long id, Curso curso, PeriodoAcademico periodo, Docente docente, String codigoSeccion, Integer vacantes, Integer matriculados, String turno, String estado) {
        this.id = id;
        this.curso = curso;
        this.periodo = periodo;
        this.docente = docente;
        this.codigoSeccion = codigoSeccion;
        this.vacantes = vacantes;
        this.matriculados = matriculados;
        this.turno = turno;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public PeriodoAcademico getPeriodo() {
        return periodo;
    }

    public void setPeriodo(PeriodoAcademico periodo) {
        this.periodo = periodo;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public String getCodigoSeccion() {
        return codigoSeccion;
    }

    public void setCodigoSeccion(String codigoSeccion) {
        this.codigoSeccion = codigoSeccion;
    }

    public Integer getVacantes() {
        return vacantes;
    }

    public void setVacantes(Integer vacantes) {
        this.vacantes = vacantes;
    }

    public Integer getMatriculados() {
        return matriculados;
    }

    public void setMatriculados(Integer matriculados) {
        this.matriculados = matriculados;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getVacantesDisponibles() {
        return Math.max(0, vacantes - matriculados);
    }
}
