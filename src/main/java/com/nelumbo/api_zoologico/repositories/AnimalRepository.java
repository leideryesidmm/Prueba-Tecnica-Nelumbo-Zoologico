package com.nelumbo.api_zoologico.repositories;

import com.nelumbo.api_zoologico.entities.Animal;
import com.nelumbo.api_zoologico.entities.Species;
import com.nelumbo.api_zoologico.entities.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AnimalRepository extends JpaRepository<Animal,Long> {
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM animal WHERE LOWER(name) = LOWER(:name)", nativeQuery = true)
    boolean existsByNameIgnoreCase(@Param("name") String name);
    long countBySpeciesZone(Zone zone);
    long countBySpecies(Species species);
    @Query("SELECT a FROM Animal a WHERE DATE(a.createDate) = :date")
    List<Animal> findAllByCreatedDate(@Param("date") LocalDate date);

    List<Animal> findByNameContainingIgnoreCase(String word);
}
