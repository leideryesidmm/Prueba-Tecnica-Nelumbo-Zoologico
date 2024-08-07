package com.nelumbo.apizoologico.repositories;

import com.nelumbo.apizoologico.entities.Animal;
import com.nelumbo.apizoologico.entities.Comment;
import com.nelumbo.apizoologico.entities.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findAllByInitialComment(Comment initialComment);
    List<Comment> findByInitialCommentIsNullAndAnimal(Animal animal);
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.id IN (SELECT r.initialComment.id FROM Comment r WHERE r.initialComment IS NOT NULL)")
    Long countCommentsWithResponses();
    Long countByInitialComment(Comment initialComment);

    Page<Comment> findCommentsByMenssageContainingIgnoreCaseAndInitialCommentIsNull(String word, Pageable pageable);
    Page<Comment> findCommentsByMenssageContainingIgnoreCaseAndInitialCommentIsNullAndAnimalSpeciesZoneJefe(String word, Users user, Pageable pageable);
    Page<Comment> findCommentsByMenssageContainingIgnoreCaseAndInitialCommentIsNotNull(String word, Pageable pageable);
    Page<Comment> findCommentsByMenssageContainingIgnoreCaseAndInitialCommentIsNotNullAndAnimalSpeciesZoneJefe(String word, Users user, Pageable pageable);
}
