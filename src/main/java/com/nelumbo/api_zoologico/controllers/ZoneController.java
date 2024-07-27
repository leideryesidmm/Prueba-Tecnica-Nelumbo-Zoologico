package com.nelumbo.api_zoologico.controllers;

import com.nelumbo.api_zoologico.services.ZoneService;
import com.nelumbo.api_zoologico.services.dto.req.ZonaDtoReq;
import com.nelumbo.api_zoologico.services.dto.res.ZoneDtoRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zone")
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ZoneDtoRes> getZoneById(@PathVariable Long id) {
            ZoneDtoRes response = zoneService.getZoneById(id);
            return ResponseEntity.ok(response);
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<ZoneDtoRes>> getAllZone() {
            List<ZoneDtoRes> response = zoneService.getAllZone();
            return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteZone(@PathVariable Long id) {
            zoneService.deleteZone(id);
            return ResponseEntity.noContent().build();
    }
}
