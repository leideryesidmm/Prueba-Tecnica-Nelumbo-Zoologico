package com.nelumbo.api_zoologico.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "animal")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_animal")
    private Long id;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id_species", nullable = false)
    private Species species;
    @Column(nullable = false,unique = true)
    private String name;
    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;
}
