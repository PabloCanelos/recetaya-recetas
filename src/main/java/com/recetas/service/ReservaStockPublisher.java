package com.recetas.service;

import com.recetas.dto.ReservaStockRequestDTO;

public interface ReservaStockPublisher {

    void publicarReserva(ReservaStockRequestDTO solicitud);
}