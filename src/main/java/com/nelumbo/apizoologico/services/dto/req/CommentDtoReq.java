package com.nelumbo.apizoologico.services.dto.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CommentDtoReq {
    private Long initialComment;
    @Min(value = 1, message = "El ID del animal debe ser mayor que 0")
    @NotNull
    private Long animal;
    @NotBlank(message = "El mensaje no puede estar vacío")
    private String menssage;

}
