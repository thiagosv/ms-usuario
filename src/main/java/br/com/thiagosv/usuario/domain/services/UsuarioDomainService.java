package br.com.thiagosv.usuario.domain.services;

import br.com.thiagosv.usuario.domain.entities.Usuario;
import br.com.thiagosv.usuario.domain.exceptions.DomainException;
import br.com.thiagosv.usuario.domain.ports.out.PasswordEncryptionPort;
import br.com.thiagosv.usuario.domain.repositories.UsuarioDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UsuarioDomainService {

    private final UsuarioDomainRepository usuarioRepository;
    private final PasswordEncryptionPort passwordEncryption;

    public Usuario criarUsuario(String nome, String email, String senha, LocalDate dataNascimento) {
        if (usuarioRepository.existeUsuarioAtivoComEmail(email))
            throw new DomainException("Já existe um usuário ativo com este email");

        return new Usuario(nome, email, passwordEncryption.criptografar(senha), dataNascimento);
    }

    public Usuario atualizarUsuario(Usuario usuario, String novoNome, String novoEmail, LocalDate novaDataNascimento) {
        if (!usuario.isAtivo())
            throw new DomainException("Não é possível atualizar um usuário inativo");

        if (!usuario.getEmail().equals(novoEmail) && usuarioRepository.existeUsuarioAtivoComEmail(novoEmail))
            throw new DomainException("Este email já está em uso por outro usuário ativo");

        usuario.atualizarDados(novoNome, novoEmail, novaDataNascimento);

        return usuario;
    }
}