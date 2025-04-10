package br.com.thiagosv.usuario.domain.exceptions;

public class AutenticacaoException extends RuntimeException {
    public AutenticacaoException(String mensagem) {
        super(mensagem);
    }
}