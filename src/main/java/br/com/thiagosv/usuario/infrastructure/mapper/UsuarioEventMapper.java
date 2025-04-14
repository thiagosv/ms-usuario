package br.com.thiagosv.usuario.infrastructure.mapper;

import br.com.thiagosv.usuario.domain.entities.Usuario;
import br.com.thiagosv.usuario.domain.events.UsuarioEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UsuarioEventMapper {

    UsuarioEventMapper INSTANCE = Mappers.getMapper(UsuarioEventMapper.class);

    @Mapping(target = "timestamp", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "evento", source = "evento")
    UsuarioEvent toEvent(Usuario usuario, UsuarioEvent.TipoEventoUsuario evento);

    @Mapping(target = "timestamp", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "evento", source = "evento")
    UsuarioEvent toEvent(String email, UsuarioEvent.TipoEventoUsuario evento);

}