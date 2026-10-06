package com.recetas.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReservaStockRequestDTO {

    private Integer idReceta;
    private Integer idSucursal;
    private List<DetalleRecetaRequestDTO> medicamentos;
}