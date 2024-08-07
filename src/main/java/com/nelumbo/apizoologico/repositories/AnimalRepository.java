package com.nelumbo.apizoologico.repositories;

import com.nelumbo.apizoologico.entities.Animal;
import com.nelumbo.apizoologico.entities.Species;
import com.nelumbo.apizoologico.entities.Users;
import com.nelumbo.apizoologico.entities.Zone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;


public interface AnimalRepository extends JpaRepository<Animal,Long> {
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM animal WHERE LOWER(name) = LOWER(:name)", nativeQuery = true)
    boolean existsByNameIgnoreCase(@Param("name") String name);
    long countBySpeciesZone(Zone zone);
    long countBySpecies(Species species);
    @Query("SELECT a FROM Animal a WHERE DATE(a.createDate) = :date")
    Page<Animal> findAllByCreatedDate(@Param("date") LocalDate date, Pageable pageable);

    Page<Animal> findByNameContainingIgnoreCase(String word,Pageable pageable);
    Page<Animal> findByNameContainingIgnoreCaseAndSpeciesZoneJefe(String word, Users user, Pageable pageable);
    Page<Animal> findAllBySpeciesZoneJefe(Users user1, Pageable pageable);

}
