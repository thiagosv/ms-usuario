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
class CriarUsuarioRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve validar todos os campos quando for uma requisicao valida")
    void deveValidarNomeQuandoForValido() {
        CriarUsuarioRequest request = MockUtil.criarUsuarioRequest();

        Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Nested
    @DisplayName("Testes para o campo nome")
    class NomeTests {

        @ParameterizedTest(name = "Caso {index}: nome={0}")
        @MethodSource("nomesNullOuBranco")
        @DisplayName("Deve rejeitar quando nome for muito branco ou null")
        void deveRejeitarQuandoNomeForEmBranco(String nomeInvalido) {
            CriarUsuarioRequest request = MockUtil.criarUsuarioRequest();
            request.setNome(nomeInvalido);

            Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);

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
        void deveRejeitarQuandoNomeForMuitoCurto(String nomeInvalido) {
            CriarUsuarioRequest request = MockUtil.criarUsuarioRequest();
            request.setNome(nomeInvalido);

            Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("O nome deve ter entre 2 e 100 caracteres!");
        }

        private static Stream<String> nomesCurtosOuLongos() {
            return Stream.of(
                    "A",
                    "A".repeat(101)
            );
        }


        @Test
        @DisplayName("Deve rejeitar quando nome for invalido")
        void deveRejeitarQuandoNomeForInvalido() {
            CriarUsuarioRequest request = MockUtil.criarUsuarioRequest();
            request.setNome("");

            Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);

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

        @ParameterizedTest(name = "Deve rejeitar quando email for \"{0}\"")
        @MethodSource("emailsInvalidos")
        void deveRejeitarQuandoEmailForEmBrancoOuNull(String emailInvalido) {
            CriarUsuarioRequest request = MockUtil.criarUsuarioRequest();
            request.setEmail(emailInvalido);

            Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);

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
            CriarUsuarioRequest request = MockUtil.criarUsuarioRequest();
            request.setEmail("email-invalido");

            Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("O e-mail deve ser válido!");
        }
    }

    @Nested
    @DisplayName("Testes para o campo senha")
    class SenhaTests {

        @Test
        void deveRejeitarQuandoSenhaForNull() {
            CriarUsuarioRequest request =  MockUtil.criarUsuarioRequest();
            request.setSenha(null);

            Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("A senha é obrigatória!");
        }

        @Test
        void deveRejeitarQuandoSenhaEmBranco() {
            CriarUsuarioRequest request = MockUtil.criarUsuarioRequest();
            request.setSenha("");

            Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(2);
            assertThat(violations)
                    .extracting(ConstraintViolation::getMessage)
                    .containsExactlyInAnyOrder("A senha é obrigatória!", "A senha deve ter pelo menos 6 caracteres!");
        }


        @Test
        @DisplayName("Deve rejeitar quando senha for muito curta")
        void deveRejeitarQuandoSenhaForMuitoCurta() {
            CriarUsuarioRequest request = MockUtil.criarUsuarioRequest();
            request.setSenha("12345");

            Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("A senha deve ter pelo menos 6 caracteres!");
        }
    }

    @Nested
    @DisplayName("Testes para o campo dataNascimento")
    class DataNascimentoTests {

        @Test
        @DisplayName("Deve rejeitar quando dataNascimento for nula")
        void deveRejeitarQuandoDataNascimentoForNula() {
            CriarUsuarioRequest request = MockUtil.criarUsuarioRequest();
            request.setDataNascimento(null);

            Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);

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
            CriarUsuarioRequest request = MockUtil.criarUsuarioRequest();
            request.setNumeroCelular(null);

            Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
            assertThat(violations)
                    .extracting(ConstraintViolation::getMessage)
                    .containsExactlyInAnyOrder("O número é obrigatório!");
        }

        @ParameterizedTest
        @MethodSource("numerosCelularInvalidos")
        @DisplayName("Deve rejeitar quando numeroCelular for invalido")
        void deveRejeitarQuandoNumeroCeelularForInvalido(String numeroCelularInvalido) {
            CriarUsuarioRequest request = MockUtil.criarUsuarioRequest();
            request.setNumeroCelular(numeroCelularInvalido);

            Set<ConstraintViolation<CriarUsuarioRequest>> violations = validator.validate(request);

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
            CriarUsuarioRequest request = new CriarUsuarioRequest();

            request.setNome(ConstantUtil.NOME_USUARIO);
            request.setEmail(ConstantUtil.EMAIL_VALIDO);
            request.setSenha(ConstantUtil.SENHA);
            request.setDataNascimento(ConstantUtil.DATA_NASCIMENTO);
            request.setNumeroCelular(ConstantUtil.NUMERO_CELULAR);

            assertThat(request.getNome()).isEqualTo(ConstantUtil.NOME_USUARIO);
            assertThat(request.getEmail()).isEqualTo(ConstantUtil.EMAIL_VALIDO);
            assertThat(request.getSenha()).isEqualTo(ConstantUtil.SENHA);
            assertThat(request.getDataNascimento()).isEqualTo(ConstantUtil.DATA_NASCIMENTO);
            assertThat(request.getNumeroCelular()).isEqualTo(ConstantUtil.NUMERO_CELULAR);
        }
    }

    @Test
    @DisplayName("Deve criar instância através do construtor com todos os parâmetros")
    void deveCriarInstanciaAtravesDoConstrutorComTodosOsParametros() {
        String nome = ConstantUtil.NOME_USUARIO;
        String email = ConstantUtil.EMAIL_VALIDO;
        String senha = ConstantUtil.SENHA;

        CriarUsuarioRequest request = new CriarUsuarioRequest(
                nome,
                email,
                senha,
                ConstantUtil.DATA_NASCIMENTO,
                ConstantUtil.NUMERO_CELULAR
        );

        assertThat(request.getNome()).isEqualTo(nome);
        assertThat(request.getEmail()).isEqualTo(email);
        assertThat(request.getSenha()).isEqualTo(senha);
        assertThat(request.getDataNascimento()).isEqualTo(ConstantUtil.DATA_NASCIMENTO);
        assertThat(request.getNumeroCelular()).isEqualTo(ConstantUtil.NUMERO_CELULAR);
    }
}