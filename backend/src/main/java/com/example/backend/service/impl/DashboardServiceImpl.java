package com.example.backend.service.impl;

import com.example.backend.dto.DashboardStatsDTO;
import com.example.backend.repository.*;
import com.example.backend.service.IDashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements IDashboardService {

    private final EstudianteRepository estudianteRepository;
    private final CursoRepository cursoRepository;
    private final DocenteRepository docenteRepository;
    private final MatriculaRepository matriculaRepository;
    private final SeccionRepository seccionRepository;
    private final PagoRepository pagoRepository;

    public DashboardServiceImpl(EstudianteRepository estudianteRepository,
                                CursoRepository cursoRepository,
                                DocenteRepository docenteRepository,
                                MatriculaRepository matriculaRepository,
                                SeccionRepository seccionRepository,
                                PagoRepository pagoRepository) {
        this.estudianteRepository = estudianteRepository;
        this.cursoRepository = cursoRepository;
        this.docenteRepository = docenteRepository;
        this.matriculaRepository = matriculaRepository;
        this.seccionRepository = seccionRepository;
        this.pagoRepository = pagoRepository;
    }

    @Override
    public DashboardStatsDTO getStats() {
        Long estudiantes = estudianteRepository.count();
        Long cursos = cursoRepository.count();
        Long docentes = docenteRepository.count();
        Long matriculas = matriculaRepository.count();
        Long secciones = seccionRepository.count();
        BigDecimal recaudado = pagoRepository.sumTotalRecaudado();

        return new DashboardStatsDTO(estudiantes, cursos, docentes, matriculas, secciones, recaudado != null ? recaudado : BigDecimal.ZERO);
    }
}
