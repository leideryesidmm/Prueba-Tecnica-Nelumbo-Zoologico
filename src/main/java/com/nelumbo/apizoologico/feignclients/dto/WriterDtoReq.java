package com.nelumbo.apizoologico.feignclients.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WriterDtoReq {
    @NotNull
    private Long id;
    @NotNull
    private String name;
}
