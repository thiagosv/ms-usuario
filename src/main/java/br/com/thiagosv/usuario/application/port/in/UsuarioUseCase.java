package br.com.thiagosv.usuario.application.port.in;

import br.com.thiagosv.usuario.application.dto.response.UsuarioResponse;
import br.com.thiagosv.usuario.application.dto.request.AtualizarUsuarioRequest;
import br.com.thiagosv.usuario.application.dto.request.CriarUsuarioRequest;

import java.util.List;
import java.util.Optional;

public interface UsuarioUseCase {
    List<UsuarioResponse> listarUsuarios();
    Optional<UsuarioResponse> buscarPorId(String id);
    UsuarioResponse criarUsuario(CriarUsuarioRequest request);
    Optional<UsuarioResponse> atualizarUsuario(String id, AtualizarUsuarioRequest request);
    boolean deletarUsuario(String id);
}