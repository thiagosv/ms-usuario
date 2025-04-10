package br.com.thiagosv.usuario.domain.services;

import br.com.thiagosv.usuario.domain.entities.Usuario;
import br.com.thiagosv.usuario.domain.events.UsuarioEvent;
import br.com.thiagosv.usuario.domain.ports.out.EventPublisherPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventoDomainService {

    private final EventPublisherPort eventPublisher;
    
    public void publicarEventoUsuarioCriado(Usuario usuario) {
        this.publicarEvento(usuario, UsuarioEvent.TipoEventoUsuario.USUARIO_CRIADO);
    }
    
    public void publicarEventoAutenticacaoSucesso(Usuario usuario) {
        this.publicarEvento(usuario, UsuarioEvent.TipoEventoUsuario.USUARIO_AUTENTICACAO_SUCESSO);
    }
    
    public void publicarEventoAutenticacaoErro(String email) {
        UsuarioEvent evento = UsuarioEvent.builder()
                .evento(UsuarioEvent.TipoEventoUsuario.USUARIO_AUTENTICACAO_ERRO)
                .id(null)
                .email(email)
                .nome(null)
                .timestamp(LocalDateTime.now())
                .build();
                
        eventPublisher.publicar(evento);
    }

    private void publicarEvento(Usuario usuario, UsuarioEvent.TipoEventoUsuario evento) {
        UsuarioEvent usuarioEvent = UsuarioEvent.builder()
                .evento(evento)
                .id(usuario.getId())
                .email(usuario.getEmail())
                .nome(usuario.getNome())
                .timestamp(LocalDateTime.now())
                .build();

        eventPublisher.publicar(usuarioEvent);
    }
}