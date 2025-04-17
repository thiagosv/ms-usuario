package br.com.thiagosv.usuario.application.controller;

import br.com.thiagosv.usuario.application.dto.request.AutenticarUsuarioRequest;
import br.com.thiagosv.usuario.application.dto.response.TokenResponse;
import br.com.thiagosv.usuario.application.port.in.AutenticacaoUseCase;
import br.com.thiagosv.usuario.infrastructure.repository.UsuarioRepository;
import br.com.thiagosv.usuario.infrastructure.repository.models.UsuarioModel;
import br.com.thiagosv.usuario.util.ConstantUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AutenticacaoControllerTest {

    @Nested
    @DisplayName("Testes de integração")
    @Tag("integracao")
    @ActiveProfiles("integracao")
    @SpringBootTest
    @AutoConfigureMockMvc
    class IntegracaoTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private UsuarioRepository usuarioRepository;

        private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        @BeforeEach
        void setUp() {
            UsuarioModel usuario = new UsuarioModel();
            usuario.setId(ConstantUtil.ID_USUARIO);
            usuario.setNome(ConstantUtil.NOME_USUARIO);
            usuario.setEmail(ConstantUtil.EMAIL_VALIDO);
            usuario.setSenha(passwordEncoder.encode(ConstantUtil.SENHA));

            usuarioRepository.save(usuario);
        }

        @Test
        @DisplayName("Deve gerar token com sucesso e retornar 200")
        void deveGerarTokenComSucesso() throws Exception {
            AutenticarUsuarioRequest request = new AutenticarUsuarioRequest(ConstantUtil.EMAIL_VALIDO, ConstantUtil.SENHA);

            getMockByRequest(request)
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("$.token").exists());
        }

        @Test
        @DisplayName("Deve retornar bad request (400) com o email vazio")
        void shouldReturnBadRequestForMissingEmail() throws Exception {
            AutenticarUsuarioRequest request = new AutenticarUsuarioRequest(null, ConstantUtil.SENHA);

            getMockByRequest(request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.email").exists());
        }

        @Test
        @DisplayName("Deve retornar bad request (400) com o formato de email invalido")
        void shouldReturnBadRequestForInvalidEmailFormat() throws Exception {
            AutenticarUsuarioRequest request = new AutenticarUsuarioRequest("invalid-email", ConstantUtil.SENHA);

            getMockByRequest(request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.email").exists());
        }

        @Test
        @DisplayName("Deve retornar bad request (400) com a senha vazio")
        void shouldReturnBadRequestForMissingPassword() throws Exception {
            AutenticarUsuarioRequest request = new AutenticarUsuarioRequest(ConstantUtil.EMAIL_VALIDO, null);

            getMockByRequest(request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.senha").exists());
        }

        private ResultActions getMockByRequest(AutenticarUsuarioRequest request) throws Exception {
            return mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));
        }
    }

    @Nested
    @DisplayName("Testes unitários")
    @Tag("unitario")
    @ExtendWith(MockitoExtension.class)
    class UnitarioTest {

        @InjectMocks
        private AutenticacaoController autenticacaoController;

        @Mock
        private AutenticacaoUseCase autenticacaoUseCase;

        @Test
        @DisplayName("Deve autenticar com sucesso e retornar o token")
        void shouldAuthenticateSuccessfully() {
            AutenticarUsuarioRequest request = new AutenticarUsuarioRequest(ConstantUtil.EMAIL_VALIDO, ConstantUtil.SENHA);
            TokenResponse response = TokenResponse.builder().token("mock-token").build();

            Mockito.when(autenticacaoUseCase.autenticar(request)).thenReturn(response);

            ResponseEntity<TokenResponse> result = autenticacaoController.autenticar(request);

            Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            Assertions.assertThat(result.getBody()).isNotNull();
            Assertions.assertThat(result.getBody().getToken()).isEqualTo("mock-token");
        }
    }
}