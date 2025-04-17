package br.com.thiagosv.usuario.domain.services;

import br.com.thiagosv.usuario.application.dto.request.AtualizarUsuarioRequest;
import br.com.thiagosv.usuario.application.dto.request.CriarUsuarioRequest;
import br.com.thiagosv.usuario.domain.entities.Usuario;
import br.com.thiagosv.usuario.domain.exceptions.DomainException;
import br.com.thiagosv.usuario.domain.ports.out.PasswordEncryptionPort;
import br.com.thiagosv.usuario.domain.repositories.UsuarioDomainRepository;
import br.com.thiagosv.usuario.infrastructure.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioDomainService {

    private final UsuarioDomainRepository usuarioRepository;
    private final PasswordEncryptionPort passwordEncryption;

    private final UsuarioMapper usuarioMapper = UsuarioMapper.INSTANCE;

    public Usuario criarUsuario(CriarUsuarioRequest request) {
        if (usuarioRepository.existeUsuarioAtivoComEmail(request.getEmail()))
            throw new DomainException("Já existe um usuário ativo com este email");

        return usuarioMapper.toEntity(
                request.getNome(), request.getEmail(), passwordEncryption.criptografar(request.getSenha()),
                request.getDataNascimento(), request.getNumeroCelular()
        );
    }

    public Usuario atualizarUsuario(Usuario usuario, AtualizarUsuarioRequest request) {
        if (!usuario.isAtivo())
            throw new DomainException("Não é possível atualizar um usuário inativo");

        if (!usuario.getEmail().equals(request.getEmail()) && usuarioRepository.existeUsuarioAtivoComEmail(request.getEmail()))
            throw new DomainException("Este email já está em uso por outro usuário ativo");

        usuario.atualizarDados(request.getNome(), request.getEmail(), request.getDataNascimento(), request.getNumeroCelular());

        return usuario;
    }
}