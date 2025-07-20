package br.com.thiagosv.usuario.application.dto.request;

import br.com.thiagosv.usuario.util.ConstantUtil;
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
class AutenticarUsuarioRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    @DisplayName("Testes para o campo email")
    class EmailTests {

        @Test
        @DisplayName("Deve validar email quando for válido")
        void deveValidarEmailQuandoForValido() {
            AutenticarUsuarioRequest request = new AutenticarUsuarioRequest(
                    ConstantUtil.EMAIL_VALIDO,
                    ConstantUtil.SENHA
            );

            Set<ConstraintViolation<AutenticarUsuarioRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @ParameterizedTest(name = "Caso {index}: email={0}")
        @MethodSource("emailsInvalidos")
        @DisplayName("Deve rejeitar quando email for invalido")
        void deveRejeitarQuandoEmailForInvalido(String emailInvalido) {
            AutenticarUsuarioRequest request = new AutenticarUsuarioRequest(
                    emailInvalido,
                    ConstantUtil.SENHA
            );

            Set<ConstraintViolation<AutenticarUsuarioRequest>> violations = validator.validate(request);

            assertThat(violations).isNotEmpty();
        }

        private static Stream<String> emailsInvalidos() {
            return Stream.of(
                    null,
                    "",
                    "email-invalido"
            );
        }
    }

    @Nested
    @DisplayName("Testes para o campo senha")
    class DataNascimentoTests {

        @Test
        @DisplayName("Deve validar senha quando for válida")
        void deveValidarDataNascimentoQuandoForValida() {
            AutenticarUsuarioRequest request = new AutenticarUsuarioRequest(
                    ConstantUtil.EMAIL_VALIDO,
                    ConstantUtil.SENHA
            );

            Set<ConstraintViolation<AutenticarUsuarioRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @ParameterizedTest(name = "Caso {index}: senha={0}")
        @MethodSource("senhasInvalidas")
        @DisplayName("Deve rejeitar quando senha for invalida")
        void deveRejeitarQuandoSenhaForInvalida(String senhaInvalida) {
            AutenticarUsuarioRequest request = new AutenticarUsuarioRequest(
                    ConstantUtil.EMAIL_VALIDO,
                    senhaInvalida
            );

            Set<ConstraintViolation<AutenticarUsuarioRequest>> violations = validator.validate(request);

            assertThat(violations).isNotEmpty();
        }

        private static Stream<String> senhasInvalidas() {
            return Stream.of(
                    null,
                    ""
            );
        }
    }

    @Nested
    @DisplayName("Testes para Getters e Setters")
    class GettersSettersTests {

        @Test
        @DisplayName("Deve usar getters e setters corretamente")
        void deveUsarGettersESettersCorretamente() {
            AutenticarUsuarioRequest request = new AutenticarUsuarioRequest();

            request.setEmail(ConstantUtil.EMAIL_VALIDO);
            request.setSenha(ConstantUtil.SENHA);

            assertThat(request.getEmail()).isEqualTo(ConstantUtil.EMAIL_VALIDO);
            assertThat(request.getSenha()).isEqualTo(ConstantUtil.SENHA);
        }
    }
}