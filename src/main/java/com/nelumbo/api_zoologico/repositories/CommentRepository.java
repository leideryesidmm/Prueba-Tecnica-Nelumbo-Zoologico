package com.nelumbo.api_zoologico.repositories;

import com.nelumbo.api_zoologico.entities.Animal;
import com.nelumbo.api_zoologico.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findAllByInitialComment(Comment initialComment);
    List<Comment> findByInitialCommentIsNullAndAnimal(Animal animal);
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.id IN (SELECT r.initialComment.id FROM Comment r WHERE r.initialComment IS NOT NULL)")
    Long countCommentsWithResponses();
    Long countByInitialComment(Comment initialComment);

    List<Comment> findCommentsByMenssageContainingIgnoreCaseAndInitialCommentIsNull(String word);
    List<Comment> findCommentsByMenssageContainingIgnoreCaseAndInitialCommentIsNotNull(String word);
}
