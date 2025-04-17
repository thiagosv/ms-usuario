package br.com.thiagosv.usuario.infrastructure.mapper;

import br.com.thiagosv.usuario.application.dto.response.UsuarioResponse;
import br.com.thiagosv.usuario.domain.entities.Usuario;
import br.com.thiagosv.usuario.infrastructure.repository.models.UsuarioModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    default Usuario toEntity(UsuarioModel model) {
        if (model == null)
            return null;

        return new Usuario(
                model.getId(),
                model.getNome(),
                model.getEmail(),
                model.getSenha(),
                model.getDataNascimento(),
                model.getStatus(),
                model.getDataCadastro(),
                model.getDataAtualizacao(),
                model.getNumeroCelular()
        );
    }

    default Usuario toEntity(String nome, String email, String senha, LocalDate dataNascimento, String numeroCelular) {
        return new Usuario(nome, email, senha, dataNascimento, numeroCelular);
    }

    UsuarioResponse toDTO(Usuario usuario);

    UsuarioModel toDocument(Usuario usuario);
}