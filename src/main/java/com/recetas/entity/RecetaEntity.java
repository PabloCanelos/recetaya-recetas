package com.recetas.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "recetas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecetaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer idMedico;

    private String pacienteNombre;

    private Integer idSucursal;

    private LocalDateTime fechaEmision;

    @Enumerated(EnumType.STRING)
    private EstadoReceta estado;

    @JsonManagedReference
    @OneToMany(
            mappedBy = "receta",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DetalleRecetaEntity> detalles;
}