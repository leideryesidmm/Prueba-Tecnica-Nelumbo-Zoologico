package com.nelumbo.apizoologico.repositories;

import com.nelumbo.apizoologico.entities.Users;
import com.nelumbo.apizoologico.entities.Zone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ZoneRepository extends JpaRepository<Zone,Long> {
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM zone WHERE LOWER(name) = LOWER(:name)", nativeQuery = true)
    boolean existsByNameIgnoreCase(@Param("name") String name);
    Page<Zone> findByNameContainingIgnoreCase(String word,Pageable pageable);
    Page<Zone> findByNameContainingIgnoreCaseAndJefe(String word,Users jefe, Pageable pageable);

    Page<Zone> findAllByJefe(Users jefe, Pageable pageable);
}
