package com.recetas.controller;

import com.recetas.entity.DetalleRecetaEntity;
import com.recetas.service.DetalleRecetaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detalle-recetas")
public class DetalleRecetaController {

    private final DetalleRecetaService detalleRecetaService;

    public DetalleRecetaController(DetalleRecetaService detalleRecetaService) {
        this.detalleRecetaService = detalleRecetaService;
    }

    @PostMapping
    public ResponseEntity<DetalleRecetaEntity> guardarDetalle(
            @RequestBody DetalleRecetaEntity detalle) {

        return ResponseEntity.ok(
                detalleRecetaService.guardarDetalle(detalle)
        );
    }

    @GetMapping("/receta/{idReceta}")
    public ResponseEntity<List<DetalleRecetaEntity>> listarPorReceta(
            @PathVariable Integer idReceta) {

        return ResponseEntity.ok(
                detalleRecetaService.listarPorReceta(idReceta)
        );
    }
}
