package com.example.backend.controller;

import com.example.backend.dto.ApiResponse;
import com.example.backend.dto.MatriculaRequestDTO;
import com.example.backend.dto.MatriculaResponseDTO;
import com.example.backend.service.IMatriculaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final IMatriculaService matriculaService;

    public MatriculaController(IMatriculaService matriculaService) {
        this.matriculaService = matriculaService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MatriculaResponseDTO>>> findAll(@RequestParam(required = false) Long estudianteId) {
        List<MatriculaResponseDTO> list;
        if (estudianteId != null) {
            list = matriculaService.findByEstudianteId(estudianteId);
        } else {
            list = matriculaService.findAll();
        }
        return ResponseEntity.ok(ApiResponse.ok("Matrículas listadas con éxito", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MatriculaResponseDTO>> findById(@PathVariable Long id) {
        MatriculaResponseDTO dto = matriculaService.findById(id);
        return ResponseEntity.ok(ApiResponse.ok("Matrícula encontrada", dto));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ApiResponse<MatriculaResponseDTO>> findByCodigo(@PathVariable String codigo) {
        MatriculaResponseDTO dto = matriculaService.findByCodigo(codigo);
        return ResponseEntity.ok(ApiResponse.ok("Matrícula encontrada", dto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MatriculaResponseDTO>> registrarMatricula(@RequestBody MatriculaRequestDTO request) {
        MatriculaResponseDTO dto = matriculaService.registrarMatricula(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Matrícula registrada exitosamente", dto));
    }

    @PatchMapping("/{id}/anular")
    public ResponseEntity<ApiResponse<Void>> anularMatricula(@PathVariable Long id) {
        matriculaService.anularMatricula(id);
        return ResponseEntity.ok(ApiResponse.ok("Matrícula anulada exitosamente", null));
    }
}
