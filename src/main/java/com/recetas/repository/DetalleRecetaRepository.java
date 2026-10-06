package com.recetas.repository;

import com.recetas.entity.DetalleRecetaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleRecetaRepository extends JpaRepository<DetalleRecetaEntity, Integer> {

    List<DetalleRecetaEntity> findByIdReceta(Integer idReceta);
}