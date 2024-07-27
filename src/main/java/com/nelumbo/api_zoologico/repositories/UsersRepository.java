package com.nelumbo.api_zoologico.repositories;

import com.nelumbo.api_zoologico.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByUserEmail(String userEmail);

    boolean existsByUserEmail(String email);
    @Modifying
    @Transactional
    @Query("UPDATE Users u SET u.disable = :disable WHERE u.id = :id")
    void disableUser(@Param("id")Long id,
                     @Param("disable") boolean disable);

    List<Users> findByDisable(boolean disabled);
}