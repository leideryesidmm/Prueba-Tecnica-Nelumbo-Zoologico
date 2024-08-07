package com.nelumbo.apizoologico.services.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ZonaDtoReq {
    @NotBlank(message = "El nombre de la zona no puede estar vacío")
    private String name;
    @NotNull(message = "El jefe de la zona no puede ser nulo")
    private Long jefe;
}
