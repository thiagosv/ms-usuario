package br.com.thiagosv.usuario.application.controller;

import br.com.thiagosv.usuario.application.dto.request.AutenticarUsuarioRequest;
import br.com.thiagosv.usuario.application.dto.response.TokenResponse;
import br.com.thiagosv.usuario.application.port.in.AutenticacaoUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AutenticacaoController {

    private final AutenticacaoUseCase autenticacaoUseCase;

    @PostMapping
    public ResponseEntity<TokenResponse> autenticar(@RequestBody @Valid AutenticarUsuarioRequest request) {
        TokenResponse tokenDTO = autenticacaoUseCase.autenticar(request);
        return ResponseEntity.ok(tokenDTO);
    }
}