package br.com.thiagosv.usuario.application.dto.request;

import br.com.thiagosv.usuario.util.ConstantUtil;
import br.com.thiagosv.usuario.util.MockUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unitario")
class AtualizarUsuarioRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve validar todos os campos quando for uma requisicao valida")
    void deveValidarNomeQuandoForValido() {
        AtualizarUsuarioRequest request = MockUtil.criarAtualizarUsuarioRequest();

        Set<ConstraintViolation<AtualizarUsuarioRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Nested
    @DisplayName("Testes para o campo nome")
    class NomeTests {

        @ParameterizedTest(name = "Caso {index}: senha={0}")
        @MethodSource("nomesInvalidos")
        @DisplayName("Deve rejeitar quando nome for invalido")
        void deveRejeitarQuandoNomeForInvalido(String nomeInvalido) {
            AtualizarUsuarioRequest request = MockUtil.criarAtualizarUsuarioRequest();
            request.setNome(nomeInvalido);

            Set<ConstraintViolation<AtualizarUsuarioRequest>> violations = validator.validate(request);

            assertThat(violations).isNotEmpty();
        }

        private static Stream<String> nomesInvalidos(){
            return Stream.of(
                null,
                    "",
                    "A",
                    "A".repeat(101)
            );
        }
    }

    @Nested
    @DisplayName("Testes para o campo email")
    class EmailTests {

        @ParameterizedTest(name = "Caso {index}: senha={0}")
        @MethodSource("emailsInvalidos")
        @DisplayName("Deve rejeitar quando email for invalido")
        void deveRejeitarQuandoEmailForInvalido(String emailInvalido) {
            AtualizarUsuarioRequest request = MockUtil.criarAtualizarUsuarioRequest();
            request.setEmail(emailInvalido);

            Set<ConstraintViolation<AtualizarUsuarioRequest>> violations = validator.validate(request);

            assertThat(violations).isNotEmpty();
        }

        private static Stream<String> emailsInvalidos(){
            return Stream.of(
                    null,
                    "",
                    "email-invalido"
            );
        }
    }

    @Nested
    @DisplayName("Testes para o campo dataNascimento")
    class DataNascimentoTests {

        @Test
        @DisplayName("Deve rejeitar quando dataNascimento for nula")
        void deveRejeitarQuandoDataNascimentoForNula() {
            AtualizarUsuarioRequest request = MockUtil.criarAtualizarUsuarioRequest();
            request.setDataNascimento(null);

            Set<ConstraintViolation<AtualizarUsuarioRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("A data de nascimento é obrigatória!");
        }
    }

    @Nested
    @DisplayName("Testes para o campo numeroCelular")
    class NumeroCelularTests {

        @Test
        @DisplayName("Deve rejeitar quando numeroCelular for null")
        void deveRejeitarQuandoNumeroCelularForNull() {
            AtualizarUsuarioRequest request = MockUtil.criarAtualizarUsuarioRequest();
            request.setNumeroCelular(null);

            Set<ConstraintViolation<AtualizarUsuarioRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
            assertThat(violations)
                    .extracting(ConstraintViolation::getMessage)
                    .containsExactlyInAnyOrder("O número é obrigatório!");
        }

        @ParameterizedTest
        @MethodSource("numerosCelularInvalidos")
        @DisplayName("Deve rejeitar quando numeroCelular for invalido")
        void deveRejeitarQuandoNumeroCelularForInvalido(String numeroCelularInvalido) {
            AtualizarUsuarioRequest request = MockUtil.criarAtualizarUsuarioRequest();
            request.setNumeroCelular(numeroCelularInvalido);

            Set<ConstraintViolation<AtualizarUsuarioRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
            assertThat(violations)
                    .extracting(ConstraintViolation::getMessage)
                    .containsExactlyInAnyOrder("O número deve possuir 11 digítos, contando com o DDD");
        }

        private static Stream<String> numerosCelularInvalidos(){
            return Stream.of("5199999999","519999999999", "");
        }
    }

    @Nested
    @DisplayName("Testes para Getters e Setters")
    class GettersSettersTests {

        @Test
        @DisplayName("Deve usar getters e setters corretamente")
        void deveUsarGettersESettersCorretamente() {
            AtualizarUsuarioRequest request = new AtualizarUsuarioRequest();
            
            request.setNome(ConstantUtil.NOME_USUARIO);
            request.setEmail(ConstantUtil.EMAIL_VALIDO);
            request.setDataNascimento(ConstantUtil.DATA_NASCIMENTO);
            
            assertThat(request.getNome()).isEqualTo(ConstantUtil.NOME_USUARIO);
            assertThat(request.getEmail()).isEqualTo(ConstantUtil.EMAIL_VALIDO);
            assertThat(request.getDataNascimento()).isEqualTo(ConstantUtil.DATA_NASCIMENTO);
        }
    }
}