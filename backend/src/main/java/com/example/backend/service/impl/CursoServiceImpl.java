package com.example.backend.service.impl;

import com.example.backend.entity.Curso;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.CursoRepository;
import com.example.backend.service.ICursoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CursoServiceImpl implements ICursoService {

    private final CursoRepository cursoRepository;

    public CursoServiceImpl(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Curso> findAll() {
        return cursoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Curso findById(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Curso> findByPlanEstudioId(Long planEstudioId) {
        return cursoRepository.findByPlanEstudioId(planEstudioId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Curso> findByCiclo(Integer ciclo) {
        return cursoRepository.findByCiclo(ciclo);
    }

    @Override
    public Curso save(Curso curso) {
        return cursoRepository.save(curso);
    }

    @Override
    public Curso update(Long id, Curso curso) {
        Curso existing = findById(id);
        if (curso.getPlanEstudio() != null && curso.getPlanEstudio().getId() != null) {
            existing.setPlanEstudio(curso.getPlanEstudio());
        }
        if (curso.getCodigo() != null) existing.setCodigo(curso.getCodigo());
        if (curso.getNombre() != null) existing.setNombre(curso.getNombre());
        if (curso.getCreditos() != null) existing.setCreditos(curso.getCreditos());
        if (curso.getHorasTeoria() != null) existing.setHorasTeoria(curso.getHorasTeoria());
        if (curso.getHorasPractica() != null) existing.setHorasPractica(curso.getHorasPractica());
        if (curso.getCiclo() != null) existing.setCiclo(curso.getCiclo());
        if (curso.getCosto() != null) existing.setCosto(curso.getCosto());
        if (curso.getEstado() != null) existing.setEstado(curso.getEstado());
        return cursoRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        Curso existing = findById(id);
        cursoRepository.delete(existing);
    }
}
