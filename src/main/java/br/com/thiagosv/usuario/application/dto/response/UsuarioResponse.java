package br.com.thiagosv.usuario.application.dto.response;

import br.com.thiagosv.usuario.domain.entities.StatusUsuario;
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
    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private String numeroCelular;
    private StatusUsuario status;

}