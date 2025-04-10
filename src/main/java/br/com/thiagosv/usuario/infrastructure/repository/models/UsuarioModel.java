package br.com.thiagosv.usuario.infrastructure.repository.models;

import br.com.thiagosv.usuario.domain.entities.StatusUsuario;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "usuarios")
public class UsuarioModel {

    @Id
    private String id;
    private String nome;
    private String email;
    private String senha;
    private LocalDate dataNascimento;
    private StatusUsuario status;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;
}