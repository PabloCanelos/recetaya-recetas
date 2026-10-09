package com.recetas.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_receta", nullable = false)
    private RecetaEntity receta;

    private Integer idMedicamento;

    private Integer cantidad;
}