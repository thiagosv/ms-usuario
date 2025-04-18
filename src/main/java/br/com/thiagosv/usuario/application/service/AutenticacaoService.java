package br.com.thiagosv.usuario.application.service;

import br.com.thiagosv.usuario.application.dto.request.AutenticarUsuarioRequest;
import br.com.thiagosv.usuario.application.dto.response.TokenResponse;
import br.com.thiagosv.usuario.application.port.in.AutenticacaoUseCase;
import br.com.thiagosv.usuario.domain.entities.Usuario;
import br.com.thiagosv.usuario.domain.exceptions.AutenticacaoException;
import br.com.thiagosv.usuario.domain.repositories.EventoDomainRepository;
import br.com.thiagosv.usuario.domain.services.AutenticacaoDomainService;
import br.com.thiagosv.usuario.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutenticacaoService implements AutenticacaoUseCase {

    private final AutenticacaoDomainService autenticacaoDomainService;
    private final EventoDomainRepository eventoRepository;
    private final JwtService jwtService;

    @Override
    public TokenResponse autenticar(AutenticarUsuarioRequest request) {
        try {
            Usuario usuario = autenticacaoDomainService.autenticar(
                    request.getEmail(),
                    request.getSenha()
            );
            eventoRepository.autenticacaoSucesso(usuario);

            String token = jwtService.gerarToken(usuario.getId(), usuario.getEmail());

            return TokenResponse.builder()
                    .token(token)
                    .tipo("Bearer")
                    .expiracaoEmSegundos(jwtService.getExpiracaoEmSegundos())
                    .build();
        } catch (AutenticacaoException ae) {
            eventoRepository.autenticacaoErro(request.getEmail());
            throw ae;
        }
    }
}