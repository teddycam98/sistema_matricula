package com.example.backend.service;

import com.example.backend.entity.Horario;
import java.util.List;

/**
 * Interfaz de servicio para la gestión de Horarios.
 * Define las operaciones comerciales asociadas al cronograma de clases.
 */
public interface IHorarioService {
    List<Horario> findAll();
    Horario findById(Long id);
    List<Horario> findBySeccionId(Long seccionId);
    List<Horario> findByAulaId(Long aulaId);
    Horario save(Horario horario);
    Horario update(Long id, Horario horario);
    void delete(Long id);
}
