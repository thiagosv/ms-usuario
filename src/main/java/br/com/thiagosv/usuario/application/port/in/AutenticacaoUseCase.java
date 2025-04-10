package br.com.thiagosv.usuario.application.port.in;

import br.com.thiagosv.usuario.application.dto.request.AutenticarUsuarioRequest;
import br.com.thiagosv.usuario.application.dto.response.TokenResponse;

public interface AutenticacaoUseCase {
    TokenResponse autenticar(AutenticarUsuarioRequest request);
}