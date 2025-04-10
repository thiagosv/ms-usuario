package br.com.thiagosv.usuario.domain.services;

import br.com.thiagosv.usuario.domain.entities.Usuario;
import br.com.thiagosv.usuario.domain.exceptions.AutenticacaoException;
import br.com.thiagosv.usuario.domain.ports.out.PasswordEncryptionPort;
import br.com.thiagosv.usuario.domain.repositories.UsuarioDomainRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutenticacaoDomainService {

    private final UsuarioDomainRepository usuarioRepository;
    private final PasswordEncryptionPort passwordEncryption;

    public Usuario autenticar(String email, String senha) {
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new AutenticacaoException("Credenciais inválidas"));

        if (!usuario.isAtivo())
            throw new AutenticacaoException("Usuário inativo");

        if (!passwordEncryption.verificar(senha, usuario.getSenha()))
            throw new AutenticacaoException("Credenciais inválidas");

        return usuario;
    }
}