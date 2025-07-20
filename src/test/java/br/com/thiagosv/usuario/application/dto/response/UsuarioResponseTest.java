package br.com.thiagosv.usuario.application.dto.response;

import br.com.thiagosv.usuario.domain.entities.StatusUsuario;
import br.com.thiagosv.usuario.util.ConstantUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

@Tag("unitario")
class UsuarioResponseTest {

    @Nested
    @DisplayName("Testes para o construtor")
    class ConstrutorTests {

        @Test
        @DisplayName("Deve criar UsuarioResponse vazio quando usar construtor sem argumentos")
        void deveCriarUsuarioResponseVazioQuandoUsarConstrutorSemArgumentos() {
            UsuarioResponse usuarioResponse = new UsuarioResponse();

            Assertions.assertThat(usuarioResponse.getId()).isNull();
            Assertions.assertThat(usuarioResponse.getNome()).isNull();
            Assertions.assertThat(usuarioResponse.getEmail()).isNull();
            Assertions.assertThat(usuarioResponse.getDataNascimento()).isNull();
            Assertions.assertThat(usuarioResponse.getNumeroCelular()).isNull();
            Assertions.assertThat(usuarioResponse.getStatus()).isNull();
        }

        @Test
        @DisplayName("Deve criar UsuarioResponse com valores quando usar construtor com argumentos")
        void deveCriarUsuarioResponseComValoresQuandoUsarConstrutorComArgumentos() {
            String id = ConstantUtil.ID_USUARIO;
            String nome = ConstantUtil.NOME_USUARIO;
            String email = ConstantUtil.EMAIL_VALIDO;
            LocalDate dataNascimento = ConstantUtil.DATA_NASCIMENTO;
            String numeroCelular = ConstantUtil.NUMERO_CELULAR;
            StatusUsuario status = ConstantUtil.USUARIO_STATUS;

            UsuarioResponse usuarioResponse = new UsuarioResponse(
                    id, nome, email, dataNascimento, numeroCelular, status);

            Assertions.assertThat(usuarioResponse.getId()).isEqualTo(id);
            Assertions.assertThat(usuarioResponse.getNome()).isEqualTo(nome);
            Assertions.assertThat(usuarioResponse.getEmail()).isEqualTo(email);
            Assertions.assertThat(usuarioResponse.getDataNascimento()).isEqualTo(dataNascimento);
            Assertions.assertThat(usuarioResponse.getNumeroCelular()).isEqualTo(numeroCelular);
            Assertions.assertThat(usuarioResponse.getStatus()).isEqualTo(status);
        }
    }

    @Nested
    @DisplayName("Testes para Getters e Setters")
    class GettersSettersTests {

        @Test
        @DisplayName("Deve usar getters e setters corretamente")
        void deveUsarGettersESettersCorretamente() {
            UsuarioResponse usuarioResponse = new UsuarioResponse();
            usuarioResponse.setId(ConstantUtil.ID_USUARIO);
            usuarioResponse.setNome(ConstantUtil.NOME_USUARIO);
            usuarioResponse.setEmail(ConstantUtil.NOME_USUARIO);
            usuarioResponse.setDataNascimento(ConstantUtil.DATA_NASCIMENTO);
            usuarioResponse.setNumeroCelular(ConstantUtil.NUMERO_CELULAR);
            usuarioResponse.setStatus(ConstantUtil.USUARIO_STATUS);

            Assertions.assertThat(usuarioResponse.getId()).isEqualTo(ConstantUtil.ID_USUARIO);
            Assertions.assertThat(usuarioResponse.getNome()).isEqualTo(ConstantUtil.NOME_USUARIO);
            Assertions.assertThat(usuarioResponse.getEmail()).isEqualTo(ConstantUtil.NOME_USUARIO);
            Assertions.assertThat(usuarioResponse.getDataNascimento()).isEqualTo(ConstantUtil.DATA_NASCIMENTO);
            Assertions.assertThat(usuarioResponse.getNumeroCelular()).isEqualTo(ConstantUtil.NUMERO_CELULAR);
            Assertions.assertThat(usuarioResponse.getStatus()).isEqualTo(ConstantUtil.USUARIO_STATUS);
        }
    }

    @Nested
    @DisplayName("Testes com valores nulos e valores limites")
    class ValoresNulosELimitesTests {

        @ParameterizedTest(name = "Caso {index}: id={0}, nome={1}, email={2}")
        @MethodSource("gerarUsuarioResponsesComValoresVariados")
        @DisplayName("Deve criar objeto com diferentes combinações de valores")
        void deveCriarObjetoComDiferentesCombinacoes(
                String id, String nome, String email, LocalDate dataNascimento,
                String numeroCelular, StatusUsuario status) {

            UsuarioResponse usuarioResponse = new UsuarioResponse(id, nome, email, dataNascimento, numeroCelular, status);

            Assertions.assertThat(usuarioResponse.getId()).isEqualTo(id);
            Assertions.assertThat(usuarioResponse.getNome()).isEqualTo(nome);
            Assertions.assertThat(usuarioResponse.getEmail()).isEqualTo(email);
            Assertions.assertThat(usuarioResponse.getDataNascimento()).isEqualTo(dataNascimento);
            Assertions.assertThat(usuarioResponse.getNumeroCelular()).isEqualTo(numeroCelular);
            Assertions.assertThat(usuarioResponse.getStatus()).isEqualTo(status);
        }

        private static Stream<Arguments> gerarUsuarioResponsesComValoresVariados() {
            return Stream.of(
                    Arguments.of(
                            ConstantUtil.ID_USUARIO,
                            ConstantUtil.NOME_USUARIO,
                            ConstantUtil.EMAIL_VALIDO,
                            ConstantUtil.DATA_NASCIMENTO,
                            ConstantUtil.NUMERO_CELULAR,
                            ConstantUtil.USUARIO_STATUS
                    ),
                    Arguments.of(
                            ConstantUtil.ID_USUARIO,
                            ConstantUtil.NOME_USUARIO_MINIMO,
                            ConstantUtil.EMAIL_VALIDO,
                            ConstantUtil.DATA_NASCIMENTO,
                            ConstantUtil.NUMERO_CELULAR,
                            ConstantUtil.USUARIO_STATUS
                    ),
                    Arguments.of(
                            "3",
                            ConstantUtil.NOME_USUARIO_MAXIMO,
                            "mais@umoutro.email",
                            ConstantUtil.DATA_NASCIMENTO,
                            "11977777777",
                            StatusUsuario.INATIVO
                    ),
                    Arguments.of(
                            null,
                            ConstantUtil.NOME_USUARIO,
                            ConstantUtil.EMAIL_VALIDO,
                            ConstantUtil.DATA_NASCIMENTO,
                            ConstantUtil.NUMERO_CELULAR,
                            ConstantUtil.USUARIO_STATUS
                    ),
                    Arguments.of(
                            ConstantUtil.ID_USUARIO,
                            ConstantUtil.NOME_USUARIO,
                            ConstantUtil.EMAIL_VALIDO,
                            ConstantUtil.DATA_NASCIMENTO,
                            ConstantUtil.NUMERO_CELULAR,
                            null
                    )
            );
        }

        @Test
        @DisplayName("Deve aceitar nome com tamanho limite mínimo")
        void deveAceitarNomeComTamanhoLimiteMinimo() {
            UsuarioResponse usuarioResponse = new UsuarioResponse();

            usuarioResponse.setNome(ConstantUtil.NOME_USUARIO_MINIMO);

            Assertions.assertThat(usuarioResponse.getNome()).isEqualTo(ConstantUtil.NOME_USUARIO_MINIMO);
            Assertions.assertThat(usuarioResponse.getNome()).hasSize(ConstantUtil.USUARIO_NOME_TAMANHO_MINIMO);
        }

        @Test
        @DisplayName("Deve aceitar nome com tamanho limite máximo")
        void deveAceitarNomeComTamanhoLimiteMaximo() {
            UsuarioResponse usuarioResponse = new UsuarioResponse();

            usuarioResponse.setNome(ConstantUtil.NOME_USUARIO_MAXIMO);

            Assertions.assertThat(usuarioResponse.getNome()).isEqualTo(ConstantUtil.NOME_USUARIO_MAXIMO);
            Assertions.assertThat(usuarioResponse.getNome()).hasSize(ConstantUtil.USUARIO_NOME_TAMANHO_MAXIMO);
        }
    }
}