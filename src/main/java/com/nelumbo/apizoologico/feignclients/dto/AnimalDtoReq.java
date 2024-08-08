package com.nelumbo.apizoologico.feignclients.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class AnimalDtoReq{
    @NotNull
    private Long id;
    @NotBlank
    private String specie;
    @NotBlank
    private String name;
}
