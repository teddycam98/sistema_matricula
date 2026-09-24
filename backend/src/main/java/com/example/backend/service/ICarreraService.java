package com.example.backend.service;

import com.example.backend.entity.Carrera;
import java.util.List;

public interface ICarreraService {
    List<Carrera> findAll();
    Carrera findById(Long id);
    Carrera save(Carrera carrera);
    Carrera update(Long id, Carrera carrera);
    void delete(Long id);
}
