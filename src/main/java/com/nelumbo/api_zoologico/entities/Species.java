package com.nelumbo.api_zoologico.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "species")
public class Species {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_species")
    private Long id;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id_zone", nullable = false)
    private Zone zone;
    @Column(nullable = false,unique = true)
    private String name;
}
