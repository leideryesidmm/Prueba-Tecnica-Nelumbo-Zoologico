package com.nelumbo.apizoologico.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "zone")
public class Zone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_zone")
    private Long id;

    @Column(nullable = false,unique = true)
    private String name;
    @JoinColumn(referencedColumnName = "id_user", nullable = false)
    @ManyToOne
    private Users jefe;
}
