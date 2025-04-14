package br.com.thiagosv.usuario.infrastructure.adapters;

import br.com.thiagosv.usuario.domain.entities.StatusUsuario;
import br.com.thiagosv.usuario.domain.entities.Usuario;
import br.com.thiagosv.usuario.domain.repositories.UsuarioDomainRepository;
import br.com.thiagosv.usuario.infrastructure.mapper.UsuarioMapper;
import br.com.thiagosv.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UsuarioRepositoryAdapter implements UsuarioDomainRepository {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper = UsuarioMapper.INSTANCE;

    @Override
    public List<Usuario> buscarTodosAtivos() {
        return usuarioRepository.findByStatusAtivo()
                .stream()
                .map(usuarioMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Usuario> buscarPorId(String id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toEntity);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(usuarioMapper::toEntity);
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        return usuarioMapper.toEntity(
                usuarioRepository.save(usuarioMapper.toDocument(usuario))
        );
    }

    @Override
    public boolean existeUsuarioAtivoComEmail(String email) {
        return usuarioRepository.existsByEmailAndStatus(email, StatusUsuario.ATIVO);
    }
}