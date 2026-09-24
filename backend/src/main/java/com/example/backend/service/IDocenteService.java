package com.example.backend.service;

import com.example.backend.entity.Docente;
import java.util.List;

public interface IDocenteService {
    List<Docente> findAll();
    Docente findById(Long id);
    Docente findByDni(String dni);
    Docente save(Docente docente);
    Docente update(Long id, Docente docente);
    void delete(Long id);
}
