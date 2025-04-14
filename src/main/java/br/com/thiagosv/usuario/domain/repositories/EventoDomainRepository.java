package br.com.thiagosv.usuario.domain.repositories;

import br.com.thiagosv.usuario.domain.entities.Usuario;

public interface EventoDomainRepository {

    void usuarioCriado(Usuario usuario);
    void autenticacaoSucesso(Usuario usuario);
    void autenticacaoErro(String email);

}
