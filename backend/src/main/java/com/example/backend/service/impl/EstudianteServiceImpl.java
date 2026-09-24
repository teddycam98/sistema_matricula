package com.example.backend.service.impl;

import com.example.backend.entity.Estudiante;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.EstudianteRepository;
import com.example.backend.service.IEstudianteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EstudianteServiceImpl implements IEstudianteService {

    private final EstudianteRepository estudianteRepository;

    public EstudianteServiceImpl(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Estudiante> findAll() {
        return estudianteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Estudiante findById(Long id) {
        return estudianteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Estudiante findByDni(String dni) {
        return estudianteRepository.findByDni(dni)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con DNI: " + dni));
    }

    @Override
    @Transactional(readOnly = true)
    public Estudiante findByCodigoEstudiante(String codigoEstudiante) {
        return estudianteRepository.findByCodigoEstudiante(codigoEstudiante)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con código: " + codigoEstudiante));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Estudiante> findByCarreraId(Long carreraId) {
        return estudianteRepository.findByCarreraId(carreraId);
    }

    @Override
    public Estudiante save(Estudiante estudiante) {
        if (estudiante.getDni() != null && estudianteRepository.findByDni(estudiante.getDni()).isPresent()) {
            throw new BadRequestException("Ya existe un estudiante registrado con el DNI: " + estudiante.getDni());
        }
        if (estudiante.getCodigoEstudiante() == null || estudiante.getCodigoEstudiante().isBlank()) {
            estudiante.setCodigoEstudiante("EST-" + System.currentTimeMillis() % 10000000);
        }
        return estudianteRepository.save(estudiante);
    }

    @Override
    public Estudiante update(Long id, Estudiante estudiante) {
        Estudiante existing = findById(id);
        if (estudiante.getCarrera() != null && estudiante.getCarrera().getId() != null) {
            existing.setCarrera(estudiante.getCarrera());
        }
        if (estudiante.getDni() != null) existing.setDni(estudiante.getDni());
        if (estudiante.getCodigoEstudiante() != null) existing.setCodigoEstudiante(estudiante.getCodigoEstudiante());
        if (estudiante.getNombres() != null) existing.setNombres(estudiante.getNombres());
        if (estudiante.getApellidos() != null) existing.setApellidos(estudiante.getApellidos());
        if (estudiante.getEmail() != null) existing.setEmail(estudiante.getEmail());
        if (estudiante.getTelefono() != null) existing.setTelefono(estudiante.getTelefono());
        if (estudiante.getFechaNacimiento() != null) existing.setFechaNacimiento(estudiante.getFechaNacimiento());
        if (estudiante.getDireccion() != null) existing.setDireccion(estudiante.getDireccion());
        if (estudiante.getEstado() != null) existing.setEstado(estudiante.getEstado());
        return estudianteRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        Estudiante existing = findById(id);
        estudianteRepository.delete(existing);
    }
}
