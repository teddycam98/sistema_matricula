package com.example.backend.service;

import com.example.backend.entity.Curso;
import java.util.List;

public interface ICursoService {
    List<Curso> findAll();
    Curso findById(Long id);
    List<Curso> findByPlanEstudioId(Long planEstudioId);
    List<Curso> findByCiclo(Integer ciclo);
    Curso save(Curso curso);
    Curso update(Long id, Curso curso);
    void delete(Long id);
}
