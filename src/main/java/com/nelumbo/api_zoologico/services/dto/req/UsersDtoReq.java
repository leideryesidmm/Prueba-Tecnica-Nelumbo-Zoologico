package com.nelumbo.api_zoologico.services.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsersDtoReq {

    private String name;
    @Email(message = "Debe proporcionar un correo electrónico válido")
    private String userEmail;
    private String pass;
}
