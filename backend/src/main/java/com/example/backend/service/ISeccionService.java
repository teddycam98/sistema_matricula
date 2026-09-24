package com.example.backend.service;

import com.example.backend.entity.Seccion;
import java.util.List;

public interface ISeccionService {
    List<Seccion> findAll();
    Seccion findById(Long id);
    List<Seccion> findByPeriodoId(Long periodoId);
    List<Seccion> findDisponiblesByPeriodo(Long periodoId);
    Seccion save(Seccion seccion);
    Seccion update(Long id, Seccion seccion);
    void delete(Long id);
}
