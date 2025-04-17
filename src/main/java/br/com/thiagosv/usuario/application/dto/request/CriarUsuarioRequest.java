package br.com.thiagosv.usuario.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CriarUsuarioRequest {
    @NotBlank(message = "O nome é obrigatório!")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres!")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório!")
    @Email(message = "O e-mail deve ser válido!")
    private String email;

    @NotBlank(message = "A senha é obrigatória!")
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres!")
    private String senha;

    @NotNull(message = "A data de nascimento é obrigatória!")
    private LocalDate dataNascimento;

    @NotNull(message = "O número é obrigatório!")
    @Pattern(regexp = "^[0-9]{11}$", message = "O número deve possuir 11 digítos, contando com o DDD")
    private String numeroCelular;
}