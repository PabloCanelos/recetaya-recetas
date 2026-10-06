package com.recetas.service;

import com.recetas.entity.DetalleRecetaEntity;

import java.util.List;

public interface DetalleRecetaService {

    DetalleRecetaEntity guardarDetalle(DetalleRecetaEntity detalle);

    List<DetalleRecetaEntity> listarPorReceta(Integer idReceta);
}