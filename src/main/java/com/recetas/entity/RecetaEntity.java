package com.recetas.entity;

import java.time.LocalDateTime;
public class RecetaEntity {
    private Integer id;
    private Integer idMedico;
    private String pacienteNombre;
    private Integer idSucursal;
    private LocalDateTime fechaEmision;
    private EstadoReceta estado;
}
