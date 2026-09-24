package com.example.backend.controller;

import com.example.backend.dto.ApiResponse;
import com.example.backend.entity.Pago;
import com.example.backend.service.IPagoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final IPagoService pagoService;

    public PagoController(IPagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Pago>>> findAll(@RequestParam(required = false) Long matriculaId) {
        List<Pago> list;
        if (matriculaId != null) {
            list = pagoService.findByMatriculaId(matriculaId);
        } else {
            list = pagoService.findAll();
        }
        return ResponseEntity.ok(ApiResponse.ok("Pagos listados con éxito", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Pago>> findById(@PathVariable Long id) {
        Pago pago = pagoService.findById(id);
        return ResponseEntity.ok(ApiResponse.ok("Pago encontrado", pago));
    }

    @PostMapping("/matricula/{matriculaId}")
    public ResponseEntity<ApiResponse<Pago>> registrarPago(
            @PathVariable Long matriculaId,
            @RequestBody(required = false) Map<String, String> body) {
        String metodoPago = body != null ? body.get("metodoPago") : "EFECTIVO";
        Pago pago = pagoService.registrarPago(matriculaId, metodoPago);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Pago registrado exitosamente", pago));
    }
}
