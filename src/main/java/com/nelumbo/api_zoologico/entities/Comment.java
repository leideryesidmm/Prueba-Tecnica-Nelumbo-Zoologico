package com.nelumbo.api_zoologico.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comment")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "initial_comment", referencedColumnName = "id_comment",nullable = true)
    private Comment initialComment;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(referencedColumnName = "id_animal", nullable = false)
    private Animal animal;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id_user", nullable = false)
    private Users user;
    @Column(nullable = false)
    private LocalDateTime date;
    @Column(nullable = false)
    private String menssage;
}
