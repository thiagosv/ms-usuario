package br.com.thiagosv.usuario.application.controller;

import br.com.thiagosv.usuario.application.dto.request.AtualizarUsuarioRequest;
import br.com.thiagosv.usuario.application.dto.request.AutenticarUsuarioRequest;
import br.com.thiagosv.usuario.application.dto.request.CriarUsuarioRequest;
import br.com.thiagosv.usuario.application.dto.response.UsuarioResponse;
import br.com.thiagosv.usuario.application.port.in.AutenticacaoUseCase;
import br.com.thiagosv.usuario.application.port.in.UsuarioUseCase;
import br.com.thiagosv.usuario.infrastructure.repository.UsuarioRepository;
import br.com.thiagosv.usuario.infrastructure.repository.models.UsuarioModel;
import br.com.thiagosv.usuario.util.ConstantUtil;
import br.com.thiagosv.usuario.util.MockUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UsuarioControllerTest {

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
        private AutenticacaoUseCase autenticacaoUseCase;

        @Autowired
        private UsuarioRepository usuarioRepository;

        private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        private static final String ENDPOINT = "/api/v1/usuarios";
        private static String TOKEN = "";

        @BeforeEach
        void setUp() {
            UsuarioModel usuario = new UsuarioModel();
            usuario.setId(ConstantUtil.ID_USUARIO);
            usuario.setNome(ConstantUtil.NOME_USUARIO);
            usuario.setEmail(ConstantUtil.EMAIL_VALIDO);
            usuario.setSenha(passwordEncoder.encode(ConstantUtil.SENHA));
            usuario.setNumeroCelular(ConstantUtil.NUMERO_CELULAR);

            usuarioRepository.save(usuario);

            AutenticarUsuarioRequest autenticarUsuarioRequest =
                    new AutenticarUsuarioRequest(ConstantUtil.EMAIL_VALIDO, ConstantUtil.SENHA);
            TOKEN = String.join(" ", "Bearer", autenticacaoUseCase.autenticar(autenticarUsuarioRequest).getToken());
        }

        @AfterEach
        void limparBancoDeDados() {
            usuarioRepository.deleteAll();
        }

        @Test
        @DisplayName("Deve listar todos os usuários com sucesso")
        void deveListarTodosOsUsuariosComSucesso() throws Exception {
            ResultActions result = mockMvc.perform(
                    MockMvcRequestBuilders.get(ENDPOINT)
                            .header("Authorization", TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            result.andExpect(status().isOk());
        }

        @Test
        @DisplayName("Deve buscar um usuário por ID com sucesso")
        void deveBuscarUsuarioPorIdComSucesso() throws Exception {
            String id = ConstantUtil.ID_USUARIO;

            ResultActions result = mockMvc.perform(
                    MockMvcRequestBuilders.get(ENDPOINT + "/{id}", id)
                            .header("Authorization", TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").isNotEmpty());
        }

        @Test
        @DisplayName("Deve retornar not found ao buscar um usuário que não existe")
        void deveRetornarNotFoundAoBuscarUsuarioInexistente() throws Exception {
            String idInexistente = ConstantUtil.ID_USUARIO_INEXISTENTE;

            ResultActions result = mockMvc.perform(
                    MockMvcRequestBuilders.get(ENDPOINT + "/{id}", idInexistente)
                            .header("Authorization", TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            result.andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Deve criar um usuário com sucesso")
        void deveCriarUsuarioComSucesso() throws Exception {
            CriarUsuarioRequest request = MockUtil.criarOutroUsuarioRequest();

            ResultActions result = mockMvc.perform(
                    MockMvcRequestBuilders.post(ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", TOKEN)
                            .content(objectMapper.writeValueAsString(request))
            );

            result.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.nome").value(request.getNome()))
                    .andExpect(jsonPath("$.email").value(request.getEmail()))
                    .andExpect(jsonPath("$.dataNascimento").value(request.getDataNascimento().toString()))
                    .andExpect(jsonPath("$.numeroCelular").value(request.getNumeroCelular()));
        }

        @Test
        @DisplayName("Deve retornar bad request ao criar usuário com dados inválidos")
        void deveRetornarBadRequestAoCriarUsuarioInvalido() throws Exception {
            CriarUsuarioRequest request = new CriarUsuarioRequest();

            ResultActions result = mockMvc.perform(
                    MockMvcRequestBuilders.post(ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", TOKEN)
                            .content(objectMapper.writeValueAsString(request))
            );

            result.andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve atualizar um usuário com sucesso")
        void deveAtualizarUsuarioComSucesso() throws Exception {
            String id = ConstantUtil.ID_USUARIO;
            AtualizarUsuarioRequest request = MockUtil.criarAtualizarUsuarioRequest();

            ResultActions result = mockMvc.perform(
                    MockMvcRequestBuilders.put(ENDPOINT + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", TOKEN)
                            .content(objectMapper.writeValueAsString(request))
            );

            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.nome").value(request.getNome()))
                    .andExpect(jsonPath("$.email").value(request.getEmail()))
                    .andExpect(jsonPath("$.dataNascimento").value(request.getDataNascimento().toString()))
                    .andExpect(jsonPath("$.numeroCelular").value(request.getNumeroCelular()));
        }

        @Test
        @DisplayName("Deve retornar not found ao atualizar usuário inexistente")
        void deveRetornarNotFoundAoAtualizarUsuarioInexistente() throws Exception {
            String idInexistente = ConstantUtil.ID_USUARIO_INEXISTENTE;
            AtualizarUsuarioRequest request = MockUtil.criarAtualizarUsuarioRequest();

            ResultActions result = mockMvc.perform(
                    MockMvcRequestBuilders.put(ENDPOINT + "/{id}", idInexistente)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", TOKEN)
                            .content(objectMapper.writeValueAsString(request))
            );

            result.andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Deve excluir um usuário com sucesso")
        void deveExcluirUsuarioComSucesso() throws Exception {
            String id = ConstantUtil.ID_USUARIO;

            ResultActions result = mockMvc.perform(
                    MockMvcRequestBuilders.delete(ENDPOINT + "/{id}", id)
                            .header("Authorization", TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            result.andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Deve retornar not found ao excluir usuário inexistente")
        void deveRetornarNotFoundAoExcluirUsuarioInexistente() throws Exception {
            String idInexistente = ConstantUtil.ID_USUARIO_INEXISTENTE;

            ResultActions result = mockMvc.perform(
                    MockMvcRequestBuilders.delete(ENDPOINT + "/{id}", idInexistente)
                            .header("Authorization", TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            result.andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Testes unitários")
    @Tag("unitario")
    @ExtendWith(MockitoExtension.class)
    class UnitarioTest {

        @InjectMocks
        private UsuarioController usuarioController;

        @Mock
        private UsuarioUseCase usuarioUseCase;

        @Test
        @DisplayName("Deve listar todos os usuários com sucesso")
        void deveListarTodosOsUsuariosComSucesso() {
            List<UsuarioResponse> usuariosEsperados = MockUtil.criarListaUsuariosResponse();
            when(usuarioUseCase.listarUsuarios()).thenReturn(usuariosEsperados);

            ResponseEntity<List<UsuarioResponse>> response = usuarioController.listarUsuarios();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(usuariosEsperados);
        }

        @Test
        @DisplayName("Deve buscar um usuário por ID com sucesso")
        void deveBuscarUsuarioPorIdComSucesso() {
            String id = ConstantUtil.ID_USUARIO;
            UsuarioResponse usuarioEsperado = MockUtil.criarUsuarioResponse();
            when(usuarioUseCase.buscarPorId(id)).thenReturn(Optional.of(usuarioEsperado));

            ResponseEntity<UsuarioResponse> response = usuarioController.buscarUsuarioPorId(id);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(usuarioEsperado);
        }

        @Test
        @DisplayName("Deve retornar not found ao buscar um usuário que não existe")
        void deveRetornarNotFoundAoBuscarUsuarioInexistente() {
            String idInexistente = ConstantUtil.ID_USUARIO_INEXISTENTE;
            when(usuarioUseCase.buscarPorId(idInexistente)).thenReturn(Optional.empty());

            ResponseEntity<UsuarioResponse> response = usuarioController.buscarUsuarioPorId(idInexistente);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("Deve criar um usuário com sucesso")
        void deveCriarUsuarioComSucesso() {
            CriarUsuarioRequest request = MockUtil.criarUsuarioRequest();
            UsuarioResponse usuarioEsperado = MockUtil.criarUsuarioResponse();
            when(usuarioUseCase.criarUsuario(any(CriarUsuarioRequest.class))).thenReturn(usuarioEsperado);

            ResponseEntity<UsuarioResponse> response = usuarioController.criarUsuario(request);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isEqualTo(usuarioEsperado);
        }

        @Test
        @DisplayName("Deve atualizar um usuário com sucesso")
        void deveAtualizarUsuarioComSucesso() {
            String id = ConstantUtil.ID_USUARIO;
            AtualizarUsuarioRequest request = MockUtil.criarAtualizarUsuarioRequest();
            UsuarioResponse usuarioEsperado = MockUtil.criarUsuarioResponse();
            when(usuarioUseCase.atualizarUsuario(anyString(), any(AtualizarUsuarioRequest.class)))
                    .thenReturn(Optional.of(usuarioEsperado));

            ResponseEntity<UsuarioResponse> response = usuarioController.atualizarUsuario(id, request);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(usuarioEsperado);
        }

        @Test
        @DisplayName("Deve retornar not found ao atualizar usuário inexistente")
        void deveRetornarNotFoundAoAtualizarUsuarioInexistente() {
            String idInexistente = ConstantUtil.ID_USUARIO_INEXISTENTE;
            AtualizarUsuarioRequest request = MockUtil.criarAtualizarUsuarioRequest();
            when(usuarioUseCase.atualizarUsuario(anyString(), any(AtualizarUsuarioRequest.class)))
                    .thenReturn(Optional.empty());

            ResponseEntity<UsuarioResponse> response = usuarioController.atualizarUsuario(idInexistente, request);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("Deve excluir um usuário com sucesso")
        void deveExcluirUsuarioComSucesso() {
            String id = ConstantUtil.ID_USUARIO;
            when(usuarioUseCase.deletarUsuario(id)).thenReturn(true);

            ResponseEntity<Void> response = usuarioController.excluirUsuario(id);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        }

        @Test
        @DisplayName("Deve retornar not found ao excluir usuário inexistente")
        void deveRetornarNotFoundAoExcluirUsuarioInexistente() {
            String idInexistente = ConstantUtil.ID_USUARIO_INEXISTENTE;
            when(usuarioUseCase.deletarUsuario(idInexistente)).thenReturn(false);

            ResponseEntity<Void> response = usuarioController.excluirUsuario(idInexistente);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
}