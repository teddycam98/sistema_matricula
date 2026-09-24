package com.example.backend.controller;

import com.example.backend.dto.ApiResponse;
import com.example.backend.entity.Horario;
import com.example.backend.service.IHorarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de Horarios.
 * Expone operaciones HTTP para consultar y asignar franjas horarias.
 */
@RestController
@RequestMapping("/api/horarios")
public class HorarioController {

    private final IHorarioService horarioService;

    public HorarioController(IHorarioService horarioService) {
        this.horarioService = horarioService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Horario>>> findAll(
            @RequestParam(required = false) Long seccionId,
            @RequestParam(required = false) Long aulaId) {
        List<Horario> list;
        if (seccionId != null) {
            list = horarioService.findBySeccionId(seccionId);
        } else if (aulaId != null) {
            list = horarioService.findByAulaId(aulaId);
        } else {
            list = horarioService.findAll();
        }
        return ResponseEntity.ok(ApiResponse.ok("Horarios listados con éxito", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Horario>> findById(@PathVariable Long id) {
        Horario h = horarioService.findById(id);
        return ResponseEntity.ok(ApiResponse.ok("Horario encontrado", h));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Horario>> save(@RequestBody Horario horario) {
        Horario creado = horarioService.save(horario);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Horario registrado exitosamente", creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Horario>> update(@PathVariable Long id, @RequestBody Horario horario) {
        Horario actualizado = horarioService.update(id, horario);
        return ResponseEntity.ok(ApiResponse.ok("Horario actualizado exitosamente", actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        horarioService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Horario eliminado exitosamente", null));
    }
}
