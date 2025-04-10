package br.com.thiagosv.usuario.application.dto.response;

import br.com.thiagosv.usuario.domain.entities.StatusUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsuarioResponse {

    private String id;

    @NotBlank(message = "O nome é obrigatório!")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres!")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório!")
    @Email(message = "O e-mail deve ser válido!")
    private String email;

    @NotNull(message = "A data de nascimento é obrigatória!")
    private LocalDate dataNascimento;

    private StatusUsuario status;

}