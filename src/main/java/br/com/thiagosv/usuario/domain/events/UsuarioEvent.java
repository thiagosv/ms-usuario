package br.com.thiagosv.usuario.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class UsuarioEvent {
    private final TipoEventoUsuario evento;
    private final String id;
    private final String email;
    private final String nome;
    private final LocalDateTime timestamp;
    
    public enum TipoEventoUsuario {
        USUARIO_CRIADO,
        USUARIO_AUTENTICACAO_SUCESSO,
        USUARIO_AUTENTICACAO_ERRO
    }
}