package com.nelumbo.apizoologico.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Zone zone;
    @Column(nullable = false,unique = true)
    private String name;
}
