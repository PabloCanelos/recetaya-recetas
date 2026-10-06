package com.recetas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "detalle_receta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleRecetaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer idReceta;

    private Integer idMedicamento;

    private Integer cantidad;
}