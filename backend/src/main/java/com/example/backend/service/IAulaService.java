package com.example.backend.service;

import com.example.backend.entity.Aula;
import java.util.List;

public interface IAulaService {
    List<Aula> findAll();
    Aula findById(Long id);
    Aula save(Aula aula);
    Aula update(Long id, Aula aula);
    void delete(Long id);
}
