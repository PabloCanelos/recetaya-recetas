package com.recetas.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetalleRecetaRequestDTO {

    private Integer idMedicamento;
    private Integer cantidad;
}