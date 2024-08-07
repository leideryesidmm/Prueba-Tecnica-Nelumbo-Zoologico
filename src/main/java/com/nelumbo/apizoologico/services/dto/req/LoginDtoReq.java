package com.nelumbo.apizoologico.services.dto.req;

import jakarta.validation.constraints.Email;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDtoReq {
    @Email(message = "Debe proporcionar un correo electrónico válido")
    private String userEmail;
    private String pass;
}
