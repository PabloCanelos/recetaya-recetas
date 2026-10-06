package com.recetas.repository;

import com.recetas.entity.EstadoReceta;
import com.recetas.entity.RecetaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecetaRepository extends JpaRepository<RecetaEntity, Integer> {

    List<RecetaEntity> findByEstado(EstadoReceta estado);
}