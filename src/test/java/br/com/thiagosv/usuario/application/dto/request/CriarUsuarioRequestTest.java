package br.com.thiagosv.usuario.application.dto.request;

import br.com.thiagosv.usuario.util.ConstantUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unitario")
class CriarUsuarioRequestTest {

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
            CriarUsuarioRequest request = new CriarUsuarioRequest(
                    ConstantUtil.NOME_USUARIO,
                    ConstantUtil.EMAIL_VALIDO,
                    ConstantUtil.SENHA,
                    ConstantUtil.DATA_NASCIMENTO
            );

            // WHEN
            Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);

            // THEN
            assertThat(violations).isEmpty();
        }

        @ParameterizedTest(name = "Caso {index}: nome={0}")
        @MethodSource("nomesNullOuBranco")
        @DisplayName("Deve rejeitar quando nome for muito branco ou null")
        void deveRejeitarQuandoNomeForEmBranco(String nomeInvalido) {
            // GIVEN
            CriarUsuarioRequest request = new CriarUsuarioRequest(
                    nomeInvalido,
                    ConstantUtil.EMAIL_VALIDO,
                    ConstantUtil.SENHA,
                    ConstantUtil.DATA_NASCIMENTO
            );

            // WHEN
            Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);

            // THEN
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("O nome é obrigatório!");
        }

        private static Stream<String> nomesNullOuBranco() {
            return Stream.of(
                    null,
                    "  "
            );
        }

        @ParameterizedTest(name = "Caso {index}: nome={0}")
        @MethodSource("nomesCurtosOuLongos")
        @DisplayName("Deve rejeitar quando nome for muito curto ou longo")
        void deveRejeitarQuandoNomeForMuitoCurto() {
            // GIVEN
            CriarUsuarioRequest request = new CriarUsuarioRequest(
                    "A",
                    ConstantUtil.EMAIL_VALIDO,
                    ConstantUtil.SENHA,
                    ConstantUtil.DATA_NASCIMENTO
            );

            // WHEN
            Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);

            // THEN
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("O nome deve ter entre 2 e 100 caracteres!");
        }

        private static Stream<String> nomesCurtosOuLongos() {
            return Stream.of(
                    "A",
                    "A".repeat(100)
            );
        }


        @Test
        @DisplayName("Deve rejeitar quando nome for invalido")
        void deveRejeitarQuandoNomeForInvalido() {
            // GIVEN
            CriarUsuarioRequest request = new CriarUsuarioRequest(
                    "",
                    ConstantUtil.EMAIL_VALIDO,
                    ConstantUtil.SENHA,
                    ConstantUtil.DATA_NASCIMENTO
            );

            // WHEN
            Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);

            // THEN
            assertThat(violations).hasSize(2);
            assertThat(violations)
                    .extracting(ConstraintViolation::getMessage)
                    .containsExactlyInAnyOrder(
                            "O nome é obrigatório!",
                            "O nome deve ter entre 2 e 100 caracteres!"
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
            CriarUsuarioRequest request = new CriarUsuarioRequest(
                    ConstantUtil.NOME_USUARIO,
                    ConstantUtil.EMAIL_VALIDO,
                    ConstantUtil.SENHA,
                    ConstantUtil.DATA_NASCIMENTO
            );

            // WHEN
            Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);

            // THEN
            assertThat(violations).isEmpty();
        }

        @ParameterizedTest(name = "Deve rejeitar quando email for \"{0}\"")
        @MethodSource("emailsInvalidos")
        void deveRejeitarQuandoEmailForEmBrancoOuNull(String emailInvalido) {
            // GIVEN
            CriarUsuarioRequest request = new CriarUsuarioRequest(
                    ConstantUtil.NOME_USUARIO,
                    emailInvalido,
                    ConstantUtil.SENHA,
                    ConstantUtil.DATA_NASCIMENTO
            );

            // WHEN
            Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);

            // THEN
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("O e-mail é obrigatório!");
        }

        private static Stream<String> emailsInvalidos() {
            return Stream.of(
                    null,
                    ""
            );
        }

        @Test
        @DisplayName("Deve rejeitar quando email for inválido")
        void deveRejeitarQuandoEmailForInvalido() {
            // GIVEN
            CriarUsuarioRequest request = new CriarUsuarioRequest(
                    ConstantUtil.NOME_USUARIO,
                    "email-invalido",
                    ConstantUtil.SENHA,
                    ConstantUtil.DATA_NASCIMENTO
            );

            // WHEN
            Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);

            // THEN
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("O e-mail deve ser válido!");
        }
    }

    @Nested
    @DisplayName("Testes para o campo senha")
    class SenhaTests {

        @Test
        @DisplayName("Deve validar senha quando for válida")
        void deveValidarSenhaQuandoForValida() {
            // GIVEN
            CriarUsuarioRequest request = new CriarUsuarioRequest(
                    ConstantUtil.NOME_USUARIO,
                    ConstantUtil.EMAIL_VALIDO,
                    ConstantUtil.SENHA,
                    ConstantUtil.DATA_NASCIMENTO
            );

            // WHEN
            Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);

            // THEN
            assertThat(violations).isEmpty();
        }

        @Test
        void deveRejeitarQuandoSenhaForNull() {
            // GIVEN
            CriarUsuarioRequest request = new CriarUsuarioRequest(
                    ConstantUtil.NOME_USUARIO,
                    ConstantUtil.EMAIL_VALIDO,
                    null,
                    ConstantUtil.DATA_NASCIMENTO
            );

            // WHEN
            Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);

            // THEN
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("A senha é obrigatória!");
        }

        @Test
        void deveRejeitarQuandoSenhaEmBranco() {
            // GIVEN
            CriarUsuarioRequest request = new CriarUsuarioRequest(
                    ConstantUtil.NOME_USUARIO,
                    ConstantUtil.EMAIL_VALIDO,
                    "",
                    ConstantUtil.DATA_NASCIMENTO
            );

            // WHEN
            Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);

            // THEN
            assertThat(violations).hasSize(2);
            assertThat(violations)
                    .extracting(ConstraintViolation::getMessage)
                    .containsExactlyInAnyOrder("A senha é obrigatória!", "A senha deve ter pelo menos 6 caracteres!");
        }


        @Test
        @DisplayName("Deve rejeitar quando senha for muito curta")
        void deveRejeitarQuandoSenhaForMuitoCurta() {
            // GIVEN
            CriarUsuarioRequest request = new CriarUsuarioRequest(
                    ConstantUtil.NOME_USUARIO,
                    ConstantUtil.EMAIL_VALIDO,
                    "12345",
                    ConstantUtil.DATA_NASCIMENTO
            );

            // WHEN
            Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);

            // THEN
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("A senha deve ter pelo menos 6 caracteres!");
        }
    }

    @Nested
    @DisplayName("Testes para o campo dataNascimento")
    class DataNascimentoTests {

        @Test
        @DisplayName("Deve validar dataNascimento quando for válida")
        void deveValidarDataNascimentoQuandoForValida() {
            // GIVEN
            CriarUsuarioRequest request = new CriarUsuarioRequest(
                    ConstantUtil.NOME_USUARIO,
                    ConstantUtil.EMAIL_VALIDO,
                    ConstantUtil.SENHA,
                    ConstantUtil.DATA_NASCIMENTO
            );

            // WHEN
            Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);

            // THEN
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve rejeitar quando dataNascimento for nula")
        void deveRejeitarQuandoDataNascimentoForNula() {
            // GIVEN
            CriarUsuarioRequest request = new CriarUsuarioRequest(
                    ConstantUtil.NOME_USUARIO,
                    ConstantUtil.EMAIL_VALIDO,
                    ConstantUtil.SENHA,
                    null
            );

            // WHEN
            Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);

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
            CriarUsuarioRequest request = new CriarUsuarioRequest();

            // WHEN
            request.setNome(ConstantUtil.NOME_USUARIO);
            request.setEmail(ConstantUtil.EMAIL_VALIDO);
            request.setSenha(ConstantUtil.SENHA);
            request.setDataNascimento(ConstantUtil.DATA_NASCIMENTO);

            // THEN
            assertThat(request.getNome()).isEqualTo(ConstantUtil.NOME_USUARIO);
            assertThat(request.getEmail()).isEqualTo(ConstantUtil.EMAIL_VALIDO);
            assertThat(request.getSenha()).isEqualTo(ConstantUtil.SENHA);
            assertThat(request.getDataNascimento()).isEqualTo(ConstantUtil.DATA_NASCIMENTO);
        }
    }

    @Test
    @DisplayName("Deve criar instância através do construtor com todos os parâmetros")
    void deveCriarInstanciaAtravesDoConstrutorComTodosOsParametros() {
        // GIVEN
        String nome = ConstantUtil.NOME_USUARIO;
        String email = ConstantUtil.EMAIL_VALIDO;
        String senha = ConstantUtil.SENHA;

        // WHEN
        CriarUsuarioRequest request = new CriarUsuarioRequest(
                nome,
                email,
                senha,
                ConstantUtil.DATA_NASCIMENTO
        );

        // THEN
        assertThat(request.getNome()).isEqualTo(nome);
        assertThat(request.getEmail()).isEqualTo(email);
        assertThat(request.getSenha()).isEqualTo(senha);
        assertThat(request.getDataNascimento()).isEqualTo(ConstantUtil.DATA_NASCIMENTO);
    }
}