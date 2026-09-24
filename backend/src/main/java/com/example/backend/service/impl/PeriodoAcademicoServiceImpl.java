package com.example.backend.service.impl;

import com.example.backend.entity.PeriodoAcademico;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.PeriodoAcademicoRepository;
import com.example.backend.service.IPeriodoAcademicoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PeriodoAcademicoServiceImpl implements IPeriodoAcademicoService {

    private final PeriodoAcademicoRepository periodoRepository;

    public PeriodoAcademicoServiceImpl(PeriodoAcademicoRepository periodoRepository) {
        this.periodoRepository = periodoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PeriodoAcademico> findAll() {
        return periodoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PeriodoAcademico findById(Long id) {
        return periodoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Periodo académico no encontrado con id: " + id));
    }

    @Override
    public PeriodoAcademico save(PeriodoAcademico periodo) {
        return periodoRepository.save(periodo);
    }

    @Override
    public PeriodoAcademico update(Long id, PeriodoAcademico periodo) {
        PeriodoAcademico existing = findById(id);
        existing.setCodigo(periodo.getCodigo());
        existing.setFechaInicio(periodo.getFechaInicio());
        existing.setFechaFin(periodo.getFechaFin());
        existing.setEstado(periodo.getEstado());
        return periodoRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        PeriodoAcademico existing = findById(id);
        periodoRepository.delete(existing);
    }
}
