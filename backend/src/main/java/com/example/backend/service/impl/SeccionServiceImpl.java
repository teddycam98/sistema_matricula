package com.example.backend.service.impl;

import com.example.backend.entity.Seccion;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.SeccionRepository;
import com.example.backend.service.ISeccionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SeccionServiceImpl implements ISeccionService {

    private final SeccionRepository seccionRepository;

    public SeccionServiceImpl(SeccionRepository seccionRepository) {
        this.seccionRepository = seccionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Seccion> findAll() {
        return seccionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Seccion findById(Long id) {
        return seccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sección no encontrada con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Seccion> findByPeriodoId(Long periodoId) {
        return seccionRepository.findByPeriodoId(periodoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Seccion> findDisponiblesByPeriodo(Long periodoId) {
        return seccionRepository.findDisponiblesByPeriodo(periodoId);
    }

    @Override
    public Seccion save(Seccion seccion) {
        return seccionRepository.save(seccion);
    }

    @Override
    public Seccion update(Long id, Seccion seccion) {
        Seccion existing = findById(id);
        existing.setCurso(seccion.getCurso());
        existing.setPeriodo(seccion.getPeriodo());
        existing.setDocente(seccion.getDocente());
        existing.setCodigoSeccion(seccion.getCodigoSeccion());
        existing.setVacantes(seccion.getVacantes());
        existing.setMatriculados(seccion.getMatriculados());
        existing.setTurno(seccion.getTurno());
        existing.setEstado(seccion.getEstado());
        return seccionRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        Seccion existing = findById(id);
        seccionRepository.delete(existing);
    }
}
