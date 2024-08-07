package com.nelumbo.apizoologico.services.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SpeciesDtoReq {
    private Long zone;

    @NotBlank(message = "El nombre de la especie no puede estar vacío")
    private String name;
}
