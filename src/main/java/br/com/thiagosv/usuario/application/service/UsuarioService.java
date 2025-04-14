package br.com.thiagosv.usuario.application.service;

import br.com.thiagosv.usuario.application.dto.request.AtualizarUsuarioRequest;
import br.com.thiagosv.usuario.application.dto.request.CriarUsuarioRequest;
import br.com.thiagosv.usuario.application.dto.response.UsuarioResponse;
import br.com.thiagosv.usuario.application.port.in.UsuarioUseCase;
import br.com.thiagosv.usuario.domain.entities.Usuario;
import br.com.thiagosv.usuario.domain.repositories.EventoDomainRepository;
import br.com.thiagosv.usuario.domain.repositories.UsuarioDomainRepository;
import br.com.thiagosv.usuario.domain.services.UsuarioDomainService;
import br.com.thiagosv.usuario.infrastructure.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UsuarioUseCase {

    private final UsuarioDomainRepository usuarioRepository;
    private final UsuarioDomainService domainService;
    private final EventoDomainRepository eventoRepository;
    private final UsuarioMapper usuarioMapper = UsuarioMapper.INSTANCE;

    @Override
    public List<UsuarioResponse> listarUsuarios() {
        return usuarioRepository.buscarTodosAtivos()
                .stream()
                .map(usuarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UsuarioResponse> buscarPorId(String id) {
        return usuarioRepository.buscarPorId(id)
                .filter(Usuario::isAtivo)
                .map(usuarioMapper::toDTO);
    }

    @Override
    @Transactional
    public UsuarioResponse criarUsuario(CriarUsuarioRequest request) {
        Usuario usuario = domainService.criarUsuario(
                request.getNome(),
                request.getEmail(),
                request.getSenha(),
                request.getDataNascimento()
        );

        Usuario usuarioSalvo = usuarioRepository.salvar(usuario);
        eventoRepository.usuarioCriado(usuarioSalvo);

        return usuarioMapper.toDTO(usuarioSalvo);
    }

    @Override
    public Optional<UsuarioResponse> atualizarUsuario(String id, AtualizarUsuarioRequest request) {
        return usuarioRepository.buscarPorId(id)
                .map(usuario -> {
                    Usuario atualizado = domainService.atualizarUsuario(
                            usuario,
                            request.getNome(),
                            request.getEmail(),
                            request.getDataNascimento()
                    );

                    return usuarioRepository.salvar(atualizado);
                })
                .map(usuarioMapper::toDTO);
    }

    @Override
    public boolean deletarUsuario(String id) {
        return usuarioRepository.buscarPorId(id)
                .map(usuario -> {
                    usuario.inativar();
                    usuarioRepository.salvar(usuario);
                    return true;
                })
                .orElse(false);
    }

}