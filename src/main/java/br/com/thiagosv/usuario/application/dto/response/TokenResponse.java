package br.com.thiagosv.usuario.application.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    private String token;
    private String tipo;
    private long expiracaoEmSegundos;
}