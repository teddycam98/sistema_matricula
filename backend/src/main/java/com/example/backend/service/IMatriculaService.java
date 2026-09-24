package com.example.backend.service;

import com.example.backend.dto.MatriculaRequestDTO;
import com.example.backend.dto.MatriculaResponseDTO;

import java.util.List;

public interface IMatriculaService {
    List<MatriculaResponseDTO> findAll();
    MatriculaResponseDTO findById(Long id);
    MatriculaResponseDTO findByCodigo(String codigo);
    List<MatriculaResponseDTO> findByEstudianteId(Long estudianteId);
    MatriculaResponseDTO registrarMatricula(MatriculaRequestDTO request);
    void anularMatricula(Long id);
    void delete(Long id);
}
