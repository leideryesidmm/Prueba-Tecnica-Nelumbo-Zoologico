package com.nelumbo.apizoologico.controllers;

import com.nelumbo.apizoologico.services.ZoneService;
import com.nelumbo.apizoologico.services.dto.req.ZonaDtoReq;
import com.nelumbo.apizoologico.services.dto.res.ZoneDtoRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/zones")
@RequiredArgsConstructor
public class ZoneController {
    private final ZoneService zoneService;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ZoneDtoRes> createZone(@Valid @RequestBody ZonaDtoReq zonaDtoReq) {
            ZoneDtoRes response = zoneService.createZone(zonaDtoReq);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ZoneDtoRes> updateZone(@PathVariable Long id, @Valid @RequestBody ZonaDtoReq zonaDtoReq) {
            ZoneDtoRes response = zoneService.updateZone(id, zonaDtoReq);
            return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('JEFE') or hasRole('EMPLEADO')")
    public ResponseEntity<ZoneDtoRes> getZoneById(@PathVariable Long id) {
            ZoneDtoRes response = zoneService.getZoneById(id);
            return ResponseEntity.ok(response);
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ZoneDtoRes>> getAllZone(Pageable pageable) {
            Page<ZoneDtoRes> response = zoneService.getAllZone(pageable);
            return ResponseEntity.ok(response);
    }
    @GetMapping(value="user")
    @PreAuthorize("hasRole('EMPLEADO') or hasRole('JEFE')")
    public ResponseEntity<Page<ZoneDtoRes>> getAllZoneByUser(Pageable pageable) {
        Page<ZoneDtoRes> response = zoneService.getAllZoneByUser(pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteZone(@PathVariable Long id) {
            zoneService.deleteZone(id);
            return ResponseEntity.noContent().build();
    }
}
