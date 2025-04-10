package br.com.thiagosv.usuario.domain.exceptions;

public class DomainException extends RuntimeException {

    public DomainException(String mensagem) {
        super(mensagem);
    }
}