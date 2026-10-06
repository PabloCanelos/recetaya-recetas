package com.recetas.service;

import com.recetas.entity.DetalleRecetaEntity;
import com.recetas.repository.DetalleRecetaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleRecetaServiceImpl implements DetalleRecetaService {

    private final DetalleRecetaRepository detalleRecetaRepository;

    public DetalleRecetaServiceImpl(DetalleRecetaRepository detalleRecetaRepository) {
        this.detalleRecetaRepository = detalleRecetaRepository;
    }

    @Override
    public DetalleRecetaEntity guardarDetalle(DetalleRecetaEntity detalle) {
        return detalleRecetaRepository.save(detalle);
    }

    @Override
    public List<DetalleRecetaEntity> listarPorReceta(Integer idReceta) {
        return detalleRecetaRepository.findByIdReceta(idReceta);
    }
}