package com.example.backend.service.impl;

import com.example.backend.dto.DetalleMatriculaDTO;
import com.example.backend.dto.MatriculaRequestDTO;
import com.example.backend.dto.MatriculaResponseDTO;
import com.example.backend.entity.*;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.*;
import com.example.backend.service.IMatriculaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class MatriculaServiceImpl implements IMatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final EstudianteRepository estudianteRepository;
    private final PeriodoAcademicoRepository periodoRepository;
    private final SeccionRepository seccionRepository;
    private final PagoRepository pagoRepository;

    public MatriculaServiceImpl(MatriculaRepository matriculaRepository,
                                EstudianteRepository estudianteRepository,
                                PeriodoAcademicoRepository periodoRepository,
                                SeccionRepository seccionRepository,
                                PagoRepository pagoRepository) {
        this.matriculaRepository = matriculaRepository;
        this.estudianteRepository = estudianteRepository;
        this.periodoRepository = periodoRepository;
        this.seccionRepository = seccionRepository;
        this.pagoRepository = pagoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatriculaResponseDTO> findAll() {
        return matriculaRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MatriculaResponseDTO findById(Long id) {
        Matricula m = matriculaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula no encontrada con id: " + id));
        return mapToDTO(m);
    }

    @Override
    @Transactional(readOnly = true)
    public MatriculaResponseDTO findByCodigo(String codigo) {
        Matricula m = matriculaRepository.findByCodigoMatricula(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula no encontrada con código: " + codigo));
        return mapToDTO(m);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatriculaResponseDTO> findByEstudianteId(Long estudianteId) {
        return matriculaRepository.findByEstudianteId(estudianteId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MatriculaResponseDTO registrarMatricula(MatriculaRequestDTO request) {
        if (request.getEstudianteId() == null) {
            throw new BadRequestException("El ID del estudiante es obligatorio.");
        }
        if (request.getPeriodoId() == null) {
            throw new BadRequestException("El ID del periodo académico es obligatorio.");
        }
        if (request.getSeccionIds() == null || request.getSeccionIds().isEmpty()) {
            throw new BadRequestException("Debe seleccionar al menos una sección para matricular.");
        }

        Estudiante estudiante = estudianteRepository.findById(request.getEstudianteId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con ID: " + request.getEstudianteId()));

        if (!"ACTIVO".equalsIgnoreCase(estudiante.getEstado())) {
            throw new BadRequestException("El estudiante no se encuentra en estado ACTIVO.");
        }

        PeriodoAcademico periodo = periodoRepository.findById(request.getPeriodoId())
                .orElseThrow(() -> new ResourceNotFoundException("Periodo académico no encontrado con ID: " + request.getPeriodoId()));

        if ("CERRADO".equalsIgnoreCase(periodo.getEstado())) {
            throw new BadRequestException("No se pueden registrar matrículas en un periodo académico CERRADO.");
        }

        // Verificar que el estudiante no esté ya matriculado en este periodo
        Optional<Matricula> matriculaExistente = matriculaRepository.findByEstudianteIdAndPeriodoId(estudiante.getId(), periodo.getId());
        if (matriculaExistente.isPresent() && !"ANULADA".equalsIgnoreCase(matriculaExistente.get().getEstado())) {
            throw new BadRequestException("El estudiante ya cuenta con una matrícula activa en el periodo " + periodo.getCodigo());
        }

        // Crear matrícula
        Matricula matricula = new Matricula();
        matricula.setEstudiante(estudiante);
        matricula.setPeriodo(periodo);
        matricula.setFechaMatricula(LocalDateTime.now());
        matricula.setCodigoMatricula(generarCodigoMatricula(periodo.getCodigo()));

        int creditosTotales = 0;
        BigDecimal costoTotal = BigDecimal.ZERO;
        Set<Long> cursosProcesados = new HashSet<>();

        for (Long seccionId : request.getSeccionIds()) {
            Seccion seccion = seccionRepository.findById(seccionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Sección no encontrada con ID: " + seccionId));

            if (!seccion.getPeriodo().getId().equals(periodo.getId())) {
                throw new BadRequestException("La sección " + seccion.getCodigoSeccion() + " no pertenece al periodo " + periodo.getCodigo());
            }

            if (!"ACTIVO".equalsIgnoreCase(seccion.getEstado())) {
                throw new BadRequestException("La sección " + seccion.getCodigoSeccion() + " no está activa.");
            }

            if (seccion.getVacantesDisponibles() <= 0) {
                throw new BadRequestException("No hay vacantes disponibles para el curso " + seccion.getCurso().getNombre() + " (Sección " + seccion.getCodigoSeccion() + ")");
            }

            Long cursoId = seccion.getCurso().getId();
            if (cursosProcesados.contains(cursoId)) {
                throw new BadRequestException("No puede matricularse en dos secciones del mismo curso: " + seccion.getCurso().getNombre());
            }
            cursosProcesados.add(cursoId);

            // Reducir vacantes
            seccion.setMatriculados(seccion.getMatriculados() + 1);
            seccionRepository.save(seccion);

            // Detalle
            DetalleMatricula detalle = new DetalleMatricula();
            detalle.setSeccion(seccion);
            detalle.setCostoCurso(seccion.getCurso().getCosto());
            detalle.setEstadoCurso("CURSANDO");
            matricula.addDetalle(detalle);

            creditosTotales += seccion.getCurso().getCreditos();
            costoTotal = costoTotal.add(seccion.getCurso().getCosto());
        }

        matricula.setTotalCreditos(creditosTotales);
        matricula.setCostoTotal(costoTotal);

        boolean pagado = request.getMetodoPago() != null && !request.getMetodoPago().isBlank();
        matricula.setEstado(pagado ? "PAGADA" : "CONFIRMADA");

        Matricula guardada = matriculaRepository.save(matricula);

        if (pagado) {
            Pago pago = new Pago();
            pago.setMatricula(guardada);
            pago.setNumeroOperacion("OP-" + System.currentTimeMillis());
            pago.setMonto(costoTotal);
            pago.setMetodoPago(request.getMetodoPago().toUpperCase());
            pago.setFechaPago(LocalDateTime.now());
            pago.setEstado("PAGADO");
            pagoRepository.save(pago);
        }

        return mapToDTO(guardada);
    }

    @Override
    public void anularMatricula(Long id) {
        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula no encontrada con ID: " + id));

        if ("ANULADA".equalsIgnoreCase(matricula.getEstado())) {
            throw new BadRequestException("La matrícula ya se encuentra anulada.");
        }

        matricula.setEstado("ANULADA");

        // Liberar vacantes
        for (DetalleMatricula detalle : matricula.getDetalles()) {
            Seccion seccion = detalle.getSeccion();
            if (seccion.getMatriculados() > 0) {
                seccion.setMatriculados(seccion.getMatriculados() - 1);
                seccionRepository.save(seccion);
            }
            detalle.setEstadoCurso("RETIRADO");
        }

        matriculaRepository.save(matricula);
    }

    private String generarCodigoMatricula(String periodoCodigo) {
        String base = "MAT-" + periodoCodigo.replace("-", "") + "-";
        long count = matriculaRepository.count() + 1;
        return String.format("%s%04d", base, count);
    }

    private MatriculaResponseDTO mapToDTO(Matricula m) {
        MatriculaResponseDTO dto = new MatriculaResponseDTO();
        dto.setId(m.getId());
        dto.setCodigoMatricula(m.getCodigoMatricula());
        if (m.getEstudiante() != null) {
            dto.setEstudianteId(m.getEstudiante().getId());
            dto.setEstudianteCodigo(m.getEstudiante().getCodigoEstudiante());
            dto.setEstudianteNombreCompleto(m.getEstudiante().getNombres() + " " + m.getEstudiante().getApellidos());
            dto.setEstudianteDni(m.getEstudiante().getDni());
            if (m.getEstudiante().getCarrera() != null) {
                dto.setCarreraNombre(m.getEstudiante().getCarrera().getNombre());
            }
        }
        if (m.getPeriodo() != null) {
            dto.setPeriodoId(m.getPeriodo().getId());
            dto.setPeriodoCodigo(m.getPeriodo().getCodigo());
        }
        dto.setFechaMatricula(m.getFechaMatricula());
        dto.setTotalCreditos(m.getTotalCreditos());
        dto.setCostoTotal(m.getCostoTotal());
        dto.setEstado(m.getEstado());

        List<DetalleMatriculaDTO> cursos = new ArrayList<>();
        if (m.getDetalles() != null) {
            for (DetalleMatricula d : m.getDetalles()) {
                DetalleMatriculaDTO cdto = new DetalleMatriculaDTO();
                cdto.setDetalleId(d.getId());
                if (d.getSeccion() != null) {
                    cdto.setSeccionId(d.getSeccion().getId());
                    cdto.setCodigoSeccion(d.getSeccion().getCodigoSeccion());
                    if (d.getSeccion().getCurso() != null) {
                        cdto.setCursoCodigo(d.getSeccion().getCurso().getCodigo());
                        cdto.setCursoNombre(d.getSeccion().getCurso().getNombre());
                        cdto.setCreditos(d.getSeccion().getCurso().getCreditos());
                        cdto.setCiclo(d.getSeccion().getCurso().getCiclo());
                    }
                    if (d.getSeccion().getDocente() != null) {
                        cdto.setDocenteNombre(d.getSeccion().getDocente().getNombres() + " " + d.getSeccion().getDocente().getApellidos());
                    }
                }
                cdto.setCostoCurso(d.getCostoCurso());
                cdto.setEstadoCurso(d.getEstadoCurso());
                cursos.add(cdto);
            }
        }
        dto.setCursos(cursos);
        return dto;
    }
}
