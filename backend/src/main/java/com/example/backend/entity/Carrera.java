package com.example.backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "carreras")
public class Carrera implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String facultad;

    @Column(name = "duracion_ciclos", nullable = false)
    private Integer duracionCiclos = 10;

    @Column(nullable = false, length = 20)
    private String estado = "ACTIVO";

    public Carrera() {
    }

    public Carrera(Long id, String codigo, String nombre, String facultad, Integer duracionCiclos, String estado) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.facultad = facultad;
        this.duracionCiclos = duracionCiclos;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    public Integer getDuracionCiclos() {
        return duracionCiclos;
    }

    public void setDuracionCiclos(Integer duracionCiclos) {
        this.duracionCiclos = duracionCiclos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
