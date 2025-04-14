package br.com.thiagosv.usuario.infrastructure.adapters;

import br.com.thiagosv.usuario.domain.entities.Usuario;
import br.com.thiagosv.usuario.domain.events.UsuarioEvent;
import br.com.thiagosv.usuario.domain.repositories.EventoDomainRepository;
import br.com.thiagosv.usuario.infrastructure.mapper.UsuarioEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventoRepositoryAdapter implements EventoDomainRepository {

    private final ApplicationEventPublisher eventPublisher;
    private final UsuarioEventMapper mapper = UsuarioEventMapper.INSTANCE;

    public void usuarioCriado(Usuario usuario) {
        eventPublisher.publishEvent(mapper.toEvent(usuario, UsuarioEvent.TipoEventoUsuario.USUARIO_CRIADO));
    }

    public void autenticacaoSucesso(Usuario usuario) {
        eventPublisher.publishEvent(mapper.toEvent(usuario, UsuarioEvent.TipoEventoUsuario.USUARIO_AUTENTICACAO_SUCESSO));
    }

    public void autenticacaoErro(String email) {
        eventPublisher.publishEvent(mapper.toEvent(email, UsuarioEvent.TipoEventoUsuario.USUARIO_AUTENTICACAO_ERRO));
    }
}