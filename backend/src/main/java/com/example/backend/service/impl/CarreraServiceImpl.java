package com.example.backend.service.impl;

import com.example.backend.entity.Carrera;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.CarreraRepository;
import com.example.backend.service.ICarreraService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CarreraServiceImpl implements ICarreraService {

    private final CarreraRepository carreraRepository;

    public CarreraServiceImpl(CarreraRepository carreraRepository) {
        this.carreraRepository = carreraRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Carrera> findAll() {
        return carreraRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Carrera findById(Long id) {
        return carreraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrera no encontrada con id: " + id));
    }

    @Override
    public Carrera save(Carrera carrera) {
        return carreraRepository.save(carrera);
    }

    @Override
    public Carrera update(Long id, Carrera carrera) {
        Carrera existing = findById(id);
        existing.setCodigo(carrera.getCodigo());
        existing.setNombre(carrera.getNombre());
        existing.setFacultad(carrera.getFacultad());
        existing.setDuracionCiclos(carrera.getDuracionCiclos());
        existing.setEstado(carrera.getEstado());
        return carreraRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        Carrera existing = findById(id);
        carreraRepository.delete(existing);
    }
}
