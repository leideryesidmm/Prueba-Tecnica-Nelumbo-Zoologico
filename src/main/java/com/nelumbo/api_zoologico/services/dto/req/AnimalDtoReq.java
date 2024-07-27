package com.nelumbo.api_zoologico.services.dto.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnimalDtoReq {
    @NotBlank(message = "El nombre del animal no puede estar vacío")
    private String name;
    @Min(value = 1, message = "El ID de la especie debe ser mayor que 0")
    private Long species;
}
