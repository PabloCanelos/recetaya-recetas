package com.recetas.service;

import com.recetas.entity.DetalleRecetaEntity;
import com.recetas.repository.DetalleRecetaRepository;
import com.recetas.repository.RecetaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleRecetaServiceImpl implements DetalleRecetaService {

    private final DetalleRecetaRepository detalleRecetaRepository;
    private final RecetaRepository recetaRepository;

    public DetalleRecetaServiceImpl(
            DetalleRecetaRepository detalleRecetaRepository,
            RecetaRepository recetaRepository) {

        this.detalleRecetaRepository = detalleRecetaRepository;
        this.recetaRepository = recetaRepository;
    }

    @Override
    public DetalleRecetaEntity guardarDetalle(DetalleRecetaEntity detalle) {

        if (detalle.getReceta() == null
                || detalle.getReceta().getId() == null
                || !recetaRepository.existsById(detalle.getReceta().getId())) {

            throw new IllegalArgumentException("La receta indicada no existe");
        }

        if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        return detalleRecetaRepository.save(detalle);
    }

    @Override
    public List<DetalleRecetaEntity> listarPorReceta(Integer idReceta) {
        return detalleRecetaRepository.findByRecetaId(idReceta);
    }
}