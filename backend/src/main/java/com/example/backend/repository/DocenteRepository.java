package com.example.backend.repository;

import com.example.backend.entity.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {
    Optional<Docente> findByDni(String dni);
    Optional<Docente> findByEmail(String email);
    List<Docente> findByEstado(String estado);
}
