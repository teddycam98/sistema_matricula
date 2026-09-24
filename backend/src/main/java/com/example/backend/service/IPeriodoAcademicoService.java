package com.example.backend.service;

import com.example.backend.entity.PeriodoAcademico;
import java.util.List;

public interface IPeriodoAcademicoService {
    List<PeriodoAcademico> findAll();
    PeriodoAcademico findById(Long id);
    PeriodoAcademico save(PeriodoAcademico periodo);
    PeriodoAcademico update(Long id, PeriodoAcademico periodo);
    void delete(Long id);
}
