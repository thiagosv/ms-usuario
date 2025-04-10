package br.com.thiagosv.usuario.infrastructure.repository;

import br.com.thiagosv.usuario.domain.entities.StatusUsuario;
import br.com.thiagosv.usuario.infrastructure.repository.models.UsuarioModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends MongoRepository<UsuarioModel, String> {

    @Query("{ 'status' : 'ATIVO' }")
    List<UsuarioModel> findByStatusAtivo();

    Optional<UsuarioModel> findByEmail(String email);

    boolean existsByEmailAndStatus(String email, StatusUsuario status);
}