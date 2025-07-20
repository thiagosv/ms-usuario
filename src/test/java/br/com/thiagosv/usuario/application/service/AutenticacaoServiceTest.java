package br.com.thiagosv.usuario.application.service;

import br.com.thiagosv.usuario.application.dto.request.AutenticarUsuarioRequest;
import br.com.thiagosv.usuario.application.dto.response.TokenResponse;
import br.com.thiagosv.usuario.domain.entities.Usuario;
import br.com.thiagosv.usuario.domain.exceptions.AutenticacaoException;
import br.com.thiagosv.usuario.domain.repositories.EventoDomainRepository;
import br.com.thiagosv.usuario.domain.services.AutenticacaoDomainService;
import br.com.thiagosv.usuario.infrastructure.security.JwtService;
import br.com.thiagosv.usuario.util.ConstantUtil;
import br.com.thiagosv.usuario.util.MockUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unitario")
@ExtendWith(MockitoExtension.class)
class AutenticacaoServiceTest {

    @Mock
    private AutenticacaoDomainService autenticacaoDomainService;

    @Mock
    private EventoDomainRepository eventoRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AutenticacaoService autenticacaoService;

    @Test
    @DisplayName("Deve autenticar usuário com credenciais válidas")
    void deveAutenticarUsuarioComCredenciaisValidas() {
        Usuario usuario = MockUtil.criarUsuario();
        AutenticarUsuarioRequest request = MockUtil.criarAutenticarUsuarioRequest();

        BDDMockito.given(autenticacaoDomainService.autenticar(Mockito.anyString(), Mockito.anyString())).willReturn(usuario);
        when(autenticacaoDomainService.autenticar(Mockito.anyString(), Mockito.anyString())).thenReturn(usuario);
        when(jwtService.gerarToken(usuario.getId(), usuario.getEmail())).thenReturn(ConstantUtil.JWT_TOKEN_TESTE);
        when(jwtService.getExpiracaoEmSegundos()).thenReturn(ConstantUtil.JWT_EXPIRACAO_SEGUNDOS);

        TokenResponse response = autenticacaoService.autenticar(request);

        assertThat(response)
                .isNotNull()
                .satisfies(token -> {
                    assertThat(token.getToken()).isEqualTo(ConstantUtil.JWT_TOKEN_TESTE);
                    assertThat(token.getTipo()).isEqualTo(ConstantUtil.JWT_TOKEN_TIPO);
                    assertThat(token.getExpiracaoEmSegundos()).isEqualTo(ConstantUtil.JWT_EXPIRACAO_SEGUNDOS);
                });

        verify(eventoRepository).autenticacaoSucesso(usuario);
        verify(jwtService).gerarToken(usuario.getId(), usuario.getEmail());
        verify(jwtService).getExpiracaoEmSegundos();
    }

    @Test
    @DisplayName("Deve lançar exceção quando credenciais forem inválidas")
    void deveLancarExcecaoQuandoCredenciaisForemInvalidas() {
        String mensagemErro = "Credenciais inválidas";
        AutenticacaoException autenticacaoException = new AutenticacaoException(mensagemErro);
        AutenticarUsuarioRequest request = MockUtil.criarAutenticarUsuarioRequest();

        doThrow(autenticacaoException).when(autenticacaoDomainService).autenticar(Mockito.anyString(), Mockito.anyString());

        Assertions.assertThatRuntimeException()
                .isThrownBy(() -> autenticacaoService.autenticar(request))
                .isSameAs(autenticacaoException);
        verify(eventoRepository).autenticacaoErro(ConstantUtil.EMAIL_VALIDO);
    }
}