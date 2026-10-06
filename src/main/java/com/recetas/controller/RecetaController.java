package com.recetas.controller;

import com.recetas.entity.RecetaEntity;
import com.recetas.service.RecetaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.recetas.entity.EstadoReceta;
import com.recetas.dto.RecetaRequestDTO;

import java.util.List;

@RestController
@RequestMapping("/recetas")
public class RecetaController {

    private final RecetaService recetaService;

    public RecetaController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    @PostMapping
    public ResponseEntity<RecetaEntity> crearReceta(
            @RequestBody RecetaRequestDTO request) {

        RecetaEntity nuevaReceta = recetaService.crearReceta(request);

        return ResponseEntity.ok(nuevaReceta);
    }

    @GetMapping
    public ResponseEntity<List<RecetaEntity>> listarRecetas() {
        return ResponseEntity.ok(recetaService.listarRecetas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecetaEntity> buscarPorId(@PathVariable Integer id) {
        RecetaEntity receta = recetaService.buscarPorId(id);

        if (receta == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(receta);
    }

    @GetMapping(params = "estado")
    public ResponseEntity<List<RecetaEntity>> listarPorEstado(
            @RequestParam EstadoReceta estado) {

        return ResponseEntity.ok(recetaService.listarPorEstado(estado));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(
            @PathVariable Integer id,
            @RequestParam EstadoReceta estado) {

        try {

            RecetaEntity recetaActualizada =
                    recetaService.actualizarEstado(id, estado);

            if (recetaActualizada == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(recetaActualizada);

        } catch (IllegalStateException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}