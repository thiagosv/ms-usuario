package br.com.thiagosv.usuario.application.dto.request;

import br.com.thiagosv.usuario.util.ConstantUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.time.LocalDate;
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

    @Nested
    @DisplayName("Testes para o campo nome")
    class NomeTests {

        @Test
        @DisplayName("Deve validar nome quando for válido")
        void deveValidarNomeQuandoForValido() {
            // GIVEN
            AtualizarUsuarioRequest request = new AtualizarUsuarioRequest(
                    ConstantUtil.NOME_USUARIO,
                    ConstantUtil.EMAIL_VALIDO,
                    ConstantUtil.DATA_NASCIMENTO
            );

            // WHEN
            Set<ConstraintViolation<AtualizarUsuarioRequest>> violations = validator.validate(request);

            // THEN
            assertThat(violations).isEmpty();
        }

        @ParameterizedTest(name = "Caso {index}: senha={0}")
        @MethodSource("nomesInvalidos")
        @DisplayName("Deve rejeitar quando nome for invalido")
        void deveRejeitarQuandoNomeForInvalido(String nomeInvalido) {
            // GIVEN
            AtualizarUsuarioRequest request = new AtualizarUsuarioRequest(
                    nomeInvalido,
                    ConstantUtil.EMAIL_VALIDO,
                    ConstantUtil.DATA_NASCIMENTO
            );

            // WHEN
            Set<ConstraintViolation<AtualizarUsuarioRequest>> violations = validator.validate(request);

            // THEN
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

        @Test
        @DisplayName("Deve validar email quando for válido")
        void deveValidarEmailQuandoForValido() {
            // GIVEN
            AtualizarUsuarioRequest request = new AtualizarUsuarioRequest(
                    ConstantUtil.NOME_USUARIO,
                    ConstantUtil.EMAIL_VALIDO,
                    ConstantUtil.DATA_NASCIMENTO
            );

            // WHEN
            Set<ConstraintViolation<AtualizarUsuarioRequest>> violations = validator.validate(request);

            // THEN
            assertThat(violations).isEmpty();
        }

        @ParameterizedTest(name = "Caso {index}: senha={0}")
        @MethodSource("emailsInvalidos")
        @DisplayName("Deve rejeitar quando email for invalido")
        void deveRejeitarQuandoEmailForInvalido(String emailInvalido) {
            // GIVEN
            AtualizarUsuarioRequest request = new AtualizarUsuarioRequest(
                    ConstantUtil.NOME_USUARIO,
                    emailInvalido,
                    ConstantUtil.DATA_NASCIMENTO
            );

            // WHEN
            Set<ConstraintViolation<AtualizarUsuarioRequest>> violations = validator.validate(request);

            // THEN
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
        @DisplayName("Deve validar dataNascimento quando for válida")
        void deveValidarDataNascimentoQuandoForValida() {
            // GIVEN
            AtualizarUsuarioRequest request = new AtualizarUsuarioRequest(
                    ConstantUtil.NOME_USUARIO,
                    ConstantUtil.EMAIL_VALIDO,
                    ConstantUtil.DATA_NASCIMENTO
            );

            // WHEN
            Set<ConstraintViolation<AtualizarUsuarioRequest>> violations = validator.validate(request);

            // THEN
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve rejeitar quando dataNascimento for nula")
        void deveRejeitarQuandoDataNascimentoForNula() {
            // GIVEN
            AtualizarUsuarioRequest request = new AtualizarUsuarioRequest(
                    ConstantUtil.NOME_USUARIO,
                    ConstantUtil.EMAIL_VALIDO,
                    null
            );

            // WHEN
            Set<ConstraintViolation<AtualizarUsuarioRequest>> violations = validator.validate(request);

            // THEN
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("A data de nascimento é obrigatória!");
        }
    }

    @Nested
    @DisplayName("Testes para Getters e Setters")
    class GettersSettersTests {

        @Test
        @DisplayName("Deve usar getters e setters corretamente")
        void deveUsarGettersESettersCorretamente() {
            // GIVEN
            AtualizarUsuarioRequest request = new AtualizarUsuarioRequest();
            
            // WHEN
            request.setNome(ConstantUtil.NOME_USUARIO);
            request.setEmail(ConstantUtil.EMAIL_VALIDO);
            request.setDataNascimento(ConstantUtil.DATA_NASCIMENTO);
            
            // THEN
            assertThat(request.getNome()).isEqualTo(ConstantUtil.NOME_USUARIO);
            assertThat(request.getEmail()).isEqualTo(ConstantUtil.EMAIL_VALIDO);
            assertThat(request.getDataNascimento()).isEqualTo(ConstantUtil.DATA_NASCIMENTO);
        }
    }
}