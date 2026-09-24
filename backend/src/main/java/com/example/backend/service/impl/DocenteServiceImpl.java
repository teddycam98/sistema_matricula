package com.example.backend.service.impl;

import com.example.backend.entity.Docente;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.DocenteRepository;
import com.example.backend.service.IDocenteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DocenteServiceImpl implements IDocenteService {

    private final DocenteRepository docenteRepository;

    public DocenteServiceImpl(DocenteRepository docenteRepository) {
        this.docenteRepository = docenteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Docente> findAll() {
        return docenteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Docente findById(Long id) {
        return docenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Docente findByDni(String dni) {
        return docenteRepository.findByDni(dni)
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado con DNI: " + dni));
    }

    @Override
    public Docente save(Docente docente) {
        return docenteRepository.save(docente);
    }

    @Override
    public Docente update(Long id, Docente docente) {
        Docente existing = findById(id);
        existing.setDni(docente.getDni());
        existing.setNombres(docente.getNombres());
        existing.setApellidos(docente.getApellidos());
        existing.setEmail(docente.getEmail());
        existing.setTelefono(docente.getTelefono());
        existing.setEspecialidad(docente.getEspecialidad());
        existing.setGradoAcademico(docente.getGradoAcademico());
        existing.setEstado(docente.getEstado());
        return docenteRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        Docente existing = findById(id);
        docenteRepository.delete(existing);
    }
}
