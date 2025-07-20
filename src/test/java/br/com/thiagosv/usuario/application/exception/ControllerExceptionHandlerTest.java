package br.com.thiagosv.usuario.application.exception;

import br.com.thiagosv.usuario.domain.exceptions.AutenticacaoException;
import br.com.thiagosv.usuario.domain.exceptions.DomainException;
import br.com.thiagosv.usuario.util.ConstantUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("unitario")
class ControllerExceptionHandlerTest {

    private final ControllerExceptionHandler handler = new ControllerExceptionHandler();

    @Nested
    @DisplayName("Testes para handleValidationExceptions")
    class HandleValidationExceptionsTests {

        @ParameterizedTest(name = "Caso {index}: campo={0}, mensagem={1}")
        @MethodSource("gerarErrosValidacao")
        @DisplayName("Deve retornar mapa de erros quando houver violações de validação")
        void deveRetornarMapaDeErrosQuandoHouverViolacoesValidacao(String fieldName, String errorMessage) {
            MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
            BindingResult bindingResult = mock(BindingResult.class);
            FieldError fieldError = new FieldError("object", fieldName, errorMessage);

            when(ex.getBindingResult()).thenReturn(bindingResult);
            when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

            Map<String, String> result = handler.handleValidationExceptions(ex);

            assertThat(result)
                    .hasSize(1)
                    .containsEntry(fieldName, errorMessage);
        }

        private static Stream<Arguments> gerarErrosValidacao() {
            return Stream.of(
                    Arguments.of(ConstantUtil.ERRO_CAMPO_NOME, ConstantUtil.ERRO_MENSAGEM_NOME),
                    Arguments.of(ConstantUtil.ERRO_CAMPO_EMAIL, ConstantUtil.ERRO_MENSAGEM_EMAIL)
            );
        }

        @Test
        @DisplayName("Deve retornar mapa vazio quando não houver erros de validação")
        void deveRetornarMapaVazioQuandoNaoHouverErrosValidacao() {
            MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
            BindingResult bindingResult = mock(BindingResult.class);

            when(ex.getBindingResult()).thenReturn(bindingResult);
            when(bindingResult.getAllErrors()).thenReturn(List.of());

            Map<String, String> result = handler.handleValidationExceptions(ex);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Testes para handleDomainException")
    class HandleDomainExceptionTests {

        @Test
        @DisplayName("Deve retornar erro 400 com mensagem quando ocorrer DomainException")
        void deveRetornarErro400ComMensagemQuandoOcorrerDomainException() {
            DomainException ex = new DomainException(ConstantUtil.ERRO_DOMAIN_MESSAGE);

            ControllerExceptionHandler.ErrorResponse response = handler.handleDomainException(ex);

            assertThat(response.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(response.mensagem()).isEqualTo(ConstantUtil.ERRO_DOMAIN_MESSAGE);
        }
    }

    @Nested
    @DisplayName("Testes para handleAuthenticationException")
    class HandleAuthenticationExceptionTests {

        @Test
        @DisplayName("Deve retornar erro 401 com mensagem quando ocorrer AutenticacaoException")
        void deveRetornarErro401ComMensagemQuandoOcorrerAutenticacaoException() {
            AutenticacaoException ex = new AutenticacaoException(ConstantUtil.ERRO_AUTENTICACAO_MESSAGE);

            ControllerExceptionHandler.ErrorResponse response = handler.handleAuthenticationException(ex);

            assertThat(response.status()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            assertThat(response.mensagem()).isEqualTo(ConstantUtil.ERRO_AUTENTICACAO_MESSAGE);
        }
    }

    @Nested
    @DisplayName("Testes para handleGlobalException")
    class HandleGlobalExceptionTests {

        @ParameterizedTest(name = "Caso {index}: exception={0}")
        @MethodSource("gerarExceptions")
        @DisplayName("Deve retornar erro 500 com mensagem padrão para exceções não tratadas")
        void deveRetornarErro500ComMensagemPadraoParaExcecoesNaoTratadas(Exception ex) {
            ControllerExceptionHandler.ErrorResponse response = handler.handleGlobalException(ex);

            assertThat(response.status()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
            assertThat(response.mensagem()).isEqualTo(ConstantUtil.ERRO_INTERNO_MESSAGE);
        }

        private static Stream<Arguments> gerarExceptions() {
            return Stream.of(
                    Arguments.of(new RuntimeException("Erro runtime")),
                    Arguments.of(new IllegalArgumentException("Erro argumento")),
                    Arguments.of(new NullPointerException("Erro null pointer"))
            );
        }
    }

    @Nested
    @DisplayName("Testes para ErrorResponse")
    class ErrorResponseTests {

        @ParameterizedTest(name = "Caso {index}: status={0}, mensagem={1}")
        @MethodSource("gerarErrorResponses")
        @DisplayName("Deve criar ErrorResponse com valores corretos")
        void deveCriarErrorResponseComValoresCorretos(int status, String mensagem) {
            ControllerExceptionHandler.ErrorResponse response = new ControllerExceptionHandler.ErrorResponse(status, mensagem);

            assertThat(response.status()).isEqualTo(status);
            assertThat(response.mensagem()).isEqualTo(mensagem);
        }

        private static Stream<Arguments> gerarErrorResponses() {
            return Stream.of(
                    Arguments.of(HttpStatus.BAD_REQUEST.value(), ConstantUtil.ERRO_DOMAIN_MESSAGE),
                    Arguments.of(HttpStatus.UNAUTHORIZED.value(), ConstantUtil.ERRO_AUTENTICACAO_MESSAGE),
                    Arguments.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), ConstantUtil.ERRO_INTERNO_MESSAGE)
            );
        }
    }
}