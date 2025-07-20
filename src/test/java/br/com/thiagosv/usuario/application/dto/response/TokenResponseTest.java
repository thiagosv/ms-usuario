package br.com.thiagosv.usuario.application.dto.response;

import br.com.thiagosv.usuario.util.ConstantUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unitario")
class TokenResponseTest {

    @Nested
    @DisplayName("Testes para o construtor e Builder")
    class ConstrutorEBuilderTests {

        @Test
        @DisplayName("Deve criar TokenResponse vazio quando usar construtor sem argumentos")
        void deveCriarTokenResponseVazioQuandoUsarConstrutorSemArgumentos() {
            TokenResponse tokenResponse = new TokenResponse();

            assertThat(tokenResponse).hasAllNullFieldsOrPropertiesExcept("expiracaoEmSegundos");
            assertThat(tokenResponse.getExpiracaoEmSegundos()).isZero();
        }

        @Test
        @DisplayName("Deve criar TokenResponse com valores quando usar construtor com argumentos")
        void deveCriarTokenResponseComValoresQuandoUsarConstrutorComArgumentos() {
            String token = ConstantUtil.TOKEN_EXEMPLO;
            String tipo = ConstantUtil.TIPO_TOKEN;
            long expiracao = ConstantUtil.EXPIRACAO_TOKEN;

            TokenResponse tokenResponse = new TokenResponse(token, tipo, expiracao);

            assertThat(tokenResponse.getToken()).isEqualTo(token);
            assertThat(tokenResponse.getTipo()).isEqualTo(tipo);
            assertThat(tokenResponse.getExpiracaoEmSegundos()).isEqualTo(expiracao);
        }

        @Test
        @DisplayName("Deve criar TokenResponse quando usar Builder")
        void deveCriarTokenResponseQuandoUsarBuilder() {
            String token = ConstantUtil.TOKEN_EXEMPLO;
            String tipo = ConstantUtil.TIPO_TOKEN;
            long expiracao = ConstantUtil.EXPIRACAO_TOKEN;

            TokenResponse tokenResponse = TokenResponse.builder()
                    .token(token)
                    .tipo(tipo)
                    .expiracaoEmSegundos(expiracao)
                    .build();

            assertThat(tokenResponse.getToken()).isEqualTo(token);
            assertThat(tokenResponse.getTipo()).isEqualTo(tipo);
            assertThat(tokenResponse.getExpiracaoEmSegundos()).isEqualTo(expiracao);
        }
    }

    @Nested
    @DisplayName("Testes para Getters e Setters")
    class GettersSettersTests {

        @Test
        @DisplayName("Deve usar getters e setters corretamente")
        void deveUsarGettersESettersCorretamente() {
            TokenResponse tokenResponse = new TokenResponse();
            String token = ConstantUtil.TOKEN_EXEMPLO;
            String tipo = ConstantUtil.TIPO_TOKEN;
            long expiracao = ConstantUtil.EXPIRACAO_TOKEN;
            
            tokenResponse.setToken(token);
            tokenResponse.setTipo(tipo);
            tokenResponse.setExpiracaoEmSegundos(expiracao);
            
            assertThat(tokenResponse.getToken()).isEqualTo(token);
            assertThat(tokenResponse.getTipo()).isEqualTo(tipo);
            assertThat(tokenResponse.getExpiracaoEmSegundos()).isEqualTo(expiracao);
        }
    }

    @Nested
    @DisplayName("Testes de Builder com valores nulos")
    class BuilderNullValuesTests {

        @ParameterizedTest(name = "Caso {index}: token={0}, tipo={1}")
        @MethodSource("gerarTokenResponsesComValoresNulos")
        @DisplayName("Deve criar objeto mesmo com valores nulos no Builder")
        void deveCriarObjetoMesmoComValoresNulosNoBuilder(String token, String tipo) {
            // GIVEN & WHEN
            TokenResponse tokenResponse = TokenResponse.builder()
                    .token(token)
                    .tipo(tipo)
                    .expiracaoEmSegundos(ConstantUtil.EXPIRACAO_TOKEN)
                    .build();
            
            assertThat(tokenResponse.getToken()).isEqualTo(token);
            assertThat(tokenResponse.getTipo()).isEqualTo(tipo);
            assertThat(tokenResponse.getExpiracaoEmSegundos()).isEqualTo(ConstantUtil.EXPIRACAO_TOKEN);
        }

        private static Stream<Arguments> gerarTokenResponsesComValoresNulos() {
            return Stream.of(
                Arguments.of(null, ConstantUtil.TIPO_TOKEN),
                Arguments.of(ConstantUtil.TOKEN_EXEMPLO, null),
                Arguments.of(null, null)
            );
        }
    }
    
    @ParameterizedTest(name = "Caso {index}: token={0}, tipo={1}, expiracao={2}")
    @MethodSource("gerarTokenResponsesCombinacoes")
    @DisplayName("Deve construir objeto com diferentes combinações de valores")
    void deveConstruirObjetoComDiferentesCombinacoes(String token, String tipo, long expiracao) {
        // GIVEN & WHEN
        TokenResponse tokenResponse = TokenResponse.builder()
                .token(token)
                .tipo(tipo)
                .expiracaoEmSegundos(expiracao)
                .build();
        
        assertThat(tokenResponse.getToken()).isEqualTo(token);
        assertThat(tokenResponse.getTipo()).isEqualTo(tipo);
        assertThat(tokenResponse.getExpiracaoEmSegundos()).isEqualTo(expiracao);
    }

    private static Stream<Arguments> gerarTokenResponsesCombinacoes() {
        return Stream.of(
            Arguments.of(ConstantUtil.TOKEN_EXEMPLO, ConstantUtil.TIPO_TOKEN, ConstantUtil.EXPIRACAO_TOKEN),
            Arguments.of("token123", "JWT", 7200L),
            Arguments.of("outro-token", "Basic", 1800L),
            Arguments.of(ConstantUtil.TOKEN_EXEMPLO, ConstantUtil.TIPO_TOKEN, 0L)
        );
    }
}