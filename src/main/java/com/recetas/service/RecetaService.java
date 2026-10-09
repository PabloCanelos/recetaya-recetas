package com.recetas.service;

import com.recetas.dto.DetalleRecetaRequestDTO;
import com.recetas.dto.RecetaRequestDTO;

import java.util.ArrayList;
import java.util.List;

import com.recetas.entity.DetalleRecetaEntity;
import com.recetas.entity.EstadoReceta;
import com.recetas.entity.RecetaEntity;

import com.recetas.repository.RecetaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class RecetaService {

    private final RecetaRepository recetaRepository;


    private boolean transicionValida(
            EstadoReceta estadoActual,
            EstadoReceta nuevoEstado) {

        return switch (estadoActual) {

            case PENDIENTE_RESERVA ->
                    nuevoEstado == EstadoReceta.STOCK_RESERVADO
                            || nuevoEstado == EstadoReceta.RESERVA_RECHAZADA;

            case STOCK_RESERVADO ->
                    nuevoEstado == EstadoReceta.DISPENSADA;

            case RESERVA_RECHAZADA, DISPENSADA ->
                    false;
        };
    }

    public RecetaService(RecetaRepository recetaRepository) {
        this.recetaRepository = recetaRepository;
    }

    @Transactional
    public RecetaEntity crearReceta(RecetaRequestDTO request) {

        RecetaEntity receta = new RecetaEntity();

        receta.setIdMedico(request.getIdMedico());
        receta.setPacienteNombre(request.getPacienteNombre());
        receta.setIdSucursal(request.getIdSucursal());
        receta.setFechaEmision(LocalDateTime.now());
        receta.setEstado(EstadoReceta.PENDIENTE_RESERVA);

        List<DetalleRecetaEntity> detalles = new ArrayList<>();

        for (DetalleRecetaRequestDTO detalleRequest : request.getDetalles()) {

            // La cantidad de medicamentos debe ser válida.
            if (detalleRequest.getCantidad() == null || detalleRequest.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
            }

            DetalleRecetaEntity detalle = new DetalleRecetaEntity();

            detalle.setReceta(receta);
            detalle.setIdMedicamento(detalleRequest.getIdMedicamento());
            detalle.setCantidad(detalleRequest.getCantidad());

            detalles.add(detalle);
        }

        receta.setDetalles(detalles);

        return recetaRepository.save(receta);
    }

    public List<RecetaEntity> listarRecetas() {
        return recetaRepository.findAll();
    }

    public RecetaEntity buscarPorId(Integer id) {
        return recetaRepository.findById(id).orElse(null);
    }

    public List<RecetaEntity> listarPorEstado(EstadoReceta estado) {
        return recetaRepository.findByEstado(estado);
    }

    public RecetaEntity actualizarEstado(Integer id, EstadoReceta nuevoEstado) {

        RecetaEntity receta = recetaRepository.findById(id).orElse(null);

        if (receta == null) {
            return null;
        }

        EstadoReceta estadoActual = receta.getEstado();

        if (!transicionValida(estadoActual, nuevoEstado)) {
            throw new IllegalStateException(
                    "No se puede cambiar el estado de "
                            + estadoActual
                            + " a "
                            + nuevoEstado
            );
        }

        receta.setEstado(nuevoEstado);

        return recetaRepository.save(receta);

    }
    public boolean eliminarReceta(Integer id) {

        if (!recetaRepository.existsById(id)) {
            return false;
        }

        recetaRepository.deleteById(id);
        return true;
    }
}