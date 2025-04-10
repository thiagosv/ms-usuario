package br.com.thiagosv.usuario.domain.ports.out;

public interface PasswordEncryptionPort {

    String criptografar(String senhaTextoClaro);

    boolean verificar(String senhaTextoClaro, String senhaCriptografada);
}