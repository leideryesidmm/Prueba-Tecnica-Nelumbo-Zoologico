package com.nelumbo.apizoologico.controllers;

import com.nelumbo.apizoologico.services.ZoneService;
import com.nelumbo.apizoologico.services.dto.req.ZonaDtoReq;
import com.nelumbo.apizoologico.services.dto.res.ZoneDtoRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/zones")
@RequiredArgsConstructor
public class ZoneController {
    private final ZoneService zoneService;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ZoneDtoRes createZone(@Valid @RequestBody ZonaDtoReq zonaDtoReq) {
            return zoneService.createZone(zonaDtoReq);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ZoneDtoRes updateZone(@PathVariable Long id, @Valid @RequestBody ZonaDtoReq zonaDtoReq) {
            return zoneService.updateZone(id, zonaDtoReq);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('JEFE') or hasRole('EMPLEADO')")
    public ZoneDtoRes getZoneById(@PathVariable Long id) {
            return zoneService.getZoneById(id);
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<ZoneDtoRes> getAllZone(Pageable pageable) {
            return zoneService.getAllZone(pageable);
    }
    @GetMapping(value="user")
    @PreAuthorize("hasRole('EMPLEADO') or hasRole('JEFE')")
    public Page<ZoneDtoRes> getAllZoneByUser(Pageable pageable) {
        return zoneService.getAllZoneByUser(pageable);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteZone(@PathVariable Long id) {
            zoneService.deleteZone(id);
    }
}
