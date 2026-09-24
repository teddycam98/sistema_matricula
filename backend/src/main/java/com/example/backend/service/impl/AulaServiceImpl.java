package com.example.backend.service.impl;

import com.example.backend.entity.Aula;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.AulaRepository;
import com.example.backend.service.IAulaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AulaServiceImpl implements IAulaService {

    private final AulaRepository aulaRepository;

    public AulaServiceImpl(AulaRepository aulaRepository) {
        this.aulaRepository = aulaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Aula> findAll() {
        return aulaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Aula findById(Long id) {
        return aulaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aula no encontrada con id: " + id));
    }

    @Override
    public Aula save(Aula aula) {
        return aulaRepository.save(aula);
    }

    @Override
    public Aula update(Long id, Aula aula) {
        Aula existing = findById(id);
        existing.setCodigo(aula.getCodigo());
        existing.setPabellon(aula.getPabellon());
        existing.setCapacidad(aula.getCapacidad());
        existing.setTipo(aula.getTipo());
        existing.setEstado(aula.getEstado());
        return aulaRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        Aula existing = findById(id);
        aulaRepository.delete(existing);
    }
}
