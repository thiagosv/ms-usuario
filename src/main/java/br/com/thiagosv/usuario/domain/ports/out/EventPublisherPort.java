package br.com.thiagosv.usuario.domain.ports.out;

import br.com.thiagosv.usuario.domain.events.UsuarioEvent;

public interface EventPublisherPort {
    void publicar(UsuarioEvent evento);
}