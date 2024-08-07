package com.nelumbo.apizoologico.repositories;

import com.nelumbo.apizoologico.entities.Species;
import com.nelumbo.apizoologico.entities.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface SpeciesRepository extends JpaRepository<Species,Long> {
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM species WHERE LOWER(name) = LOWER(:name)", nativeQuery = true)
    boolean existsByNameIgnoreCase(@Param("name") String name);
    Page<Species> findByNameContainingIgnoreCase(String word, Pageable pageable);
    Page<Species> findByNameContainingIgnoreCaseAndZoneJefe(String word, Users user,Pageable pageable);
    Page<Species> findAllByZoneJefe(Users user1, Pageable pageable);
}