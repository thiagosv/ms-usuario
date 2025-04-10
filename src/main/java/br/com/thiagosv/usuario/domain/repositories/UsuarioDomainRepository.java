package br.com.thiagosv.usuario.domain.repositories;

import br.com.thiagosv.usuario.domain.entities.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioDomainRepository {
    List<Usuario> buscarTodosAtivos();
    Optional<Usuario> buscarPorId(String id);
    Optional<Usuario> buscarPorEmail(String email);
    Usuario salvar(Usuario usuario);
    boolean existeUsuarioAtivoComEmail(String email);
}