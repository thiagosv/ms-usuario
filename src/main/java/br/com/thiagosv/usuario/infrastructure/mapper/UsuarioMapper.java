package br.com.thiagosv.usuario.infrastructure.mapper;

import br.com.thiagosv.usuario.application.dto.response.UsuarioResponse;
import br.com.thiagosv.usuario.domain.entities.Usuario;
import br.com.thiagosv.usuario.infrastructure.repository.models.UsuarioModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    default Usuario toEntity(UsuarioModel model) {
        if (model == null) {
            return null;
        }

        return new Usuario(
                model.getId(),
                model.getNome(),
                model.getEmail(),
                model.getSenha(),
                model.getDataNascimento(),
                model.getStatus(),
                model.getDataCadastro(),
                model.getDataAtualizacao()
        );
    }

    UsuarioResponse toDTO(Usuario usuario);

    UsuarioModel toDocument(Usuario usuario);
}