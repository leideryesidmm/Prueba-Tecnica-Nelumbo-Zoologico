package com.nelumbo.api_zoologico.services.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ZonaDtoReq {
    @NotBlank(message = "El nombre de la zona no puede estar vacío")
    private String name;
}
