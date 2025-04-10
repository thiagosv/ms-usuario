package br.com.thiagosv.usuario.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutenticarUsuarioRequest {
    
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    private String email;
    
    @NotBlank(message = "A senha é obrigatória")
    private String senha;
}