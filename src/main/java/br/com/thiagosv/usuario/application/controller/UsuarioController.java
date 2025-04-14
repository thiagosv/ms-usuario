package br.com.thiagosv.usuario.application.controller;

import br.com.thiagosv.usuario.application.dto.response.UsuarioResponse;
import br.com.thiagosv.usuario.application.port.in.UsuarioUseCase;
import br.com.thiagosv.usuario.application.dto.request.AtualizarUsuarioRequest;
import br.com.thiagosv.usuario.application.dto.request.CriarUsuarioRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Log4j2
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        List<UsuarioResponse> usuarios = usuarioUseCase.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuarioPorId(@PathVariable String id) {
        log.info("Realizando busca por id: {}", id);
        return usuarioUseCase.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> criarUsuario(@RequestBody @Valid CriarUsuarioRequest request) {
        log.info("Realizando criacao de usuiario: {}", request);
        UsuarioResponse novoUsuario = usuarioUseCase.criarUsuario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizarUsuario(
            @PathVariable String id,
            @RequestBody @Valid AtualizarUsuarioRequest request
    ) {
        log.info("Realizando atualizacao de usuiario: id [{}] | {}", id, request);
        return usuarioUseCase.atualizarUsuario(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirUsuario(@PathVariable String id) {
        log.info("Realizando delecao de usuario {}", id);
        if (usuarioUseCase.deletarUsuario(id))
            return ResponseEntity.noContent().build();

        return ResponseEntity.notFound().build();
    }
}