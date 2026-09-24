package com.example.backend.service;

import com.example.backend.entity.Estudiante;
import java.util.List;

public interface IEstudianteService {
    List<Estudiante> findAll();
    Estudiante findById(Long id);
    Estudiante findByDni(String dni);
    Estudiante findByCodigoEstudiante(String codigoEstudiante);
    List<Estudiante> findByCarreraId(Long carreraId);
    Estudiante save(Estudiante estudiante);
    Estudiante update(Long id, Estudiante estudiante);
    void delete(Long id);
}
