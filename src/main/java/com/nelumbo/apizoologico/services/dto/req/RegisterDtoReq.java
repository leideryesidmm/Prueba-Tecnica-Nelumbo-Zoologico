package com.nelumbo.apizoologico.services.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDtoReq {
    @NotBlank(message = "El nombre del usuario no puede estar vacío")
    private String name;
    @Email(message = "Debe proporcionar un correo electrónico válido")
    @NotBlank(message = "El email del usuario no puede estar vacío")
    private String userEmail;
    @NotBlank(message = "La contraseña del usuario no puede estar vacía")
    private String pass;
    private long jefe;
}
