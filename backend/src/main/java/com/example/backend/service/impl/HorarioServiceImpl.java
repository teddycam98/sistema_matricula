package com.example.backend.service.impl;

import com.example.backend.entity.Horario;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.HorarioRepository;
import com.example.backend.service.IHorarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación de las reglas de negocio para Horarios.
 * Gestiona la asignación de aulas, días y franjas horarias a cada sección.
 */
@Service
@Transactional
public class HorarioServiceImpl implements IHorarioService {

    private final HorarioRepository horarioRepository;

    public HorarioServiceImpl(HorarioRepository horarioRepository) {
        this.horarioRepository = horarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Horario> findAll() {
        return horarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Horario findById(Long id) {
        return horarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horario no encontrado con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Horario> findBySeccionId(Long seccionId) {
        return horarioRepository.findBySeccionId(seccionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Horario> findByAulaId(Long aulaId) {
        return horarioRepository.findByAulaId(aulaId);
    }

    @Override
    public Horario save(Horario horario) {
        return horarioRepository.save(horario);
    }

    @Override
    public Horario update(Long id, Horario horario) {
        Horario existing = findById(id);
        existing.setSeccion(horario.getSeccion());
        existing.setAula(horario.getAula());
        existing.setDiaSemana(horario.getDiaSemana());
        existing.setHoraInicio(horario.getHoraInicio());
        existing.setHoraFin(horario.getHoraFin());
        return horarioRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        Horario existing = findById(id);
        horarioRepository.delete(existing);
    }
}
