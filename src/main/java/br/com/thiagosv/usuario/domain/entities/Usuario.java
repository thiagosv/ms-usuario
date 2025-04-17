package br.com.thiagosv.usuario.domain.entities;

import br.com.thiagosv.usuario.domain.exceptions.DomainException;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.regex.Pattern;

@Getter
public class Usuario {
    private String id;
    private String nome;
    private String email;
    private final String senha;
    private LocalDate dataNascimento;
    private String numeroCelular;
    private StatusUsuario status;
    private final LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;

    private static final int NOME_MIN_LENGTH = 2;
    private static final int NOME_MAX_LENGTH = 100;
    private static final int IDADE_MINIMA = 18;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    private static final Pattern NUMERO_CELULAR = Pattern.compile("^[0-9]{11}$");

    public Usuario(String nome, String email, String senha, LocalDate dataNascimento, String numeroCelular) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.status = StatusUsuario.ATIVO;
        this.dataCadastro = LocalDateTime.now();
        this.numeroCelular = numeroCelular;

        validarNome();
        validarEmail();
        validarIdade();
        validarCelular();
    }

    public Usuario(String id, String nome, String email, String senha, LocalDate dataNascimento,
                   StatusUsuario status, LocalDateTime dataCadastro, LocalDateTime dataAtualizacao, String numeroCelular) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.numeroCelular = numeroCelular;
        this.status = status != null ? status : StatusUsuario.ATIVO;
        this.dataCadastro = dataCadastro != null ? dataCadastro : LocalDateTime.now();
        this.dataAtualizacao = dataAtualizacao;

    }

    public void atualizarDados(String nome, String email, LocalDate dataNascimento, String numeroCelular) {
        String nomeAtual = this.nome;
        String emailAtual = this.email;
        LocalDate dataNascimentoAtual = this.dataNascimento;
        String numeroCelularAtual = this.numeroCelular;

        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.numeroCelular = numeroCelular;

        try {
            validarNome();
            validarEmail();
            validarIdade();
            validarCelular();

            this.dataAtualizacao = LocalDateTime.now();
        } catch (DomainException e) {
            this.nome = nomeAtual;
            this.email = emailAtual;
            this.dataNascimento = dataNascimentoAtual;
            this.numeroCelular = numeroCelularAtual;

            throw e;
        }
    }

    public void inativar() {
        if (this.status == StatusUsuario.INATIVO) {
            throw new DomainException("Usuário já está inativo");
        }

        this.status = StatusUsuario.INATIVO;
        this.dataAtualizacao = LocalDateTime.now();
    }

    private void validarNome() {
        if (nome == null || nome.trim().isEmpty()) {
            throw new DomainException("O nome não pode ser vazio");
        }

        if (nome.trim().length() < NOME_MIN_LENGTH) {
            throw new DomainException(
                    String.format("O nome deve ter pelo menos %d caracteres", NOME_MIN_LENGTH));
        }

        if (nome.trim().length() > NOME_MAX_LENGTH) {
            throw new DomainException(
                    String.format("O nome deve ter no máximo %d caracteres", NOME_MAX_LENGTH));
        }
    }

    private void validarEmail() {
        if (email == null || email.trim().isEmpty()) {
            throw new DomainException("O email não pode ser vazio");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new DomainException("O email fornecido não é válido");
        }
    }

    private void validarIdade() {
        if (dataNascimento == null) {
            throw new DomainException("A data de nascimento é obrigatória");
        }

        LocalDate hoje = LocalDate.now();
        Period periodo = Period.between(dataNascimento, hoje);

        if (periodo.getYears() < IDADE_MINIMA) {
            throw new DomainException(
                    String.format("O usuário deve ter pelo menos %d anos", IDADE_MINIMA));
        }

        if (dataNascimento.isAfter(hoje)) {
            throw new DomainException("A data de nascimento não pode ser no futuro");
        }
    }


    private void validarCelular() {
        if (numeroCelular == null || numeroCelular.trim().isEmpty()) {
            throw new DomainException("O celular não pode ser vazio");
        }

        if (!NUMERO_CELULAR.matcher(numeroCelular).matches()) {
            throw new DomainException("O celular fornecido não é válido");
        }
    }
    public boolean isAtivo() {
        return this.status == StatusUsuario.ATIVO;
    }
}