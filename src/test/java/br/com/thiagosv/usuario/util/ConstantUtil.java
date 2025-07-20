package br.com.thiagosv.usuario.util;

import br.com.thiagosv.usuario.domain.entities.StatusUsuario;

import java.time.LocalDate;

public class ConstantUtil {

    public static final String EMAIL_VALIDO = "user@email.com";
    public static final String SENHA = "senha123";
    public static final String SENHA_INVALIDA = "SenhaInvalida@123";

    public static final String OUTRO_EMAIL_VALIDO = "user@email.com";

    public static final String NUMERO_CELULAR = "51999999999";
    public static final String NUMERO_CELULAR_ATUALIZADO = "51999999990";

    public static final String ID_USUARIO = "123";
    public static final String ID_USUARIO_INEXISTENTE = "999";

    public static final int USUARIO_NOME_TAMANHO_MINIMO = 2;
    public static final int USUARIO_NOME_TAMANHO_MAXIMO = 100;
    public static final String NOME_USUARIO = "Usuário Teste";
    public static final String NOME_USUARIO_ATUALIZADO = "Usuário Atualizado";
    public static final String NOME_USUARIO_MINIMO = "AB";
    public static final String NOME_USUARIO_MAXIMO = "A".repeat(USUARIO_NOME_TAMANHO_MAXIMO);

    public static final StatusUsuario USUARIO_STATUS = StatusUsuario.ATIVO;

    public static final String OUTRO_NOME_USUARIO = "Usuário Teste";

    public static final LocalDate DATA_NASCIMENTO = LocalDate.of(1997, 9, 1);

    public static final String TOKEN_EXEMPLO = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.exemplo";
    public static final String TIPO_TOKEN = "Bearer";
    public static final long EXPIRACAO_TOKEN = 3600;


    public static final String ERRO_CAMPO_NOME = "nome";
    public static final String ERRO_MENSAGEM_NOME = "Nome é obrigatório";
    public static final String ERRO_CAMPO_EMAIL = "email";
    public static final String ERRO_MENSAGEM_EMAIL = "Email inválido";
    public static final String ERRO_DOMAIN_MESSAGE = "Erro de domínio teste";
    public static final String ERRO_AUTENTICACAO_MESSAGE = "Erro de autenticação teste";
    public static final String ERRO_INTERNO_MESSAGE = "Ocorreu um erro interno no servidor";

    public static final String JWT_TOKEN_TESTE = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";
    public static final String JWT_TOKEN_TIPO = "Bearer";
    public static final long JWT_EXPIRACAO_SEGUNDOS = 3600L;



}
