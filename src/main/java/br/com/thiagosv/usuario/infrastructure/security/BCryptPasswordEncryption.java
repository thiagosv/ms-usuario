package br.com.thiagosv.usuario.infrastructure.security;

import br.com.thiagosv.usuario.domain.ports.out.PasswordEncryptionPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordEncryption implements PasswordEncryptionPort {

    private final BCryptPasswordEncoder passwordEncoder;

    public BCryptPasswordEncryption() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public String criptografar(String senhaTextoClaro) {
        return passwordEncoder.encode(senhaTextoClaro);
    }

    @Override
    public boolean verificar(String senhaTextoClaro, String senhaCriptografada) {
        return passwordEncoder.matches(senhaTextoClaro, senhaCriptografada);
    }
}