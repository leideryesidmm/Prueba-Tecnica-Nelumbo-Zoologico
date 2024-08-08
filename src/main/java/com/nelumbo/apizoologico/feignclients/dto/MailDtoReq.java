package com.nelumbo.apizoologico.feignclients.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MailDtoReq {
    @NotBlank
    private String destinationEmail;
    @NotNull
    private AnimalDtoReq animal;
    @NotBlank
    private WriterDtoReq writer;
    @NotBlank
    private String message;
}