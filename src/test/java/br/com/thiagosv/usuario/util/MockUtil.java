package br.com.thiagosv.usuario.util;

import br.com.thiagosv.usuario.application.dto.request.AtualizarUsuarioRequest;
import br.com.thiagosv.usuario.application.dto.request.CriarUsuarioRequest;
import br.com.thiagosv.usuario.application.dto.response.UsuarioResponse;

import java.util.Arrays;
import java.util.List;

public class MockUtil {

    public static UsuarioResponse criarUsuarioResponse() {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(ConstantUtil.ID_USUARIO);
        response.setNome(ConstantUtil.NOME_USUARIO);
        response.setEmail(ConstantUtil.EMAIL_VALIDO);
        return response;
    }

    public static List<UsuarioResponse> criarListaUsuariosResponse() {
        return Arrays.asList(
                criarUsuarioResponse(),
                criarOutroUsuarioResponse()
        );
    }

    public static UsuarioResponse criarOutroUsuarioResponse() {
        UsuarioResponse response = new UsuarioResponse();
        response.setId("456");
        response.setNome("Outro Usuário");
        response.setEmail(ConstantUtil.OUTRO_EMAIL_VALIDO);
        return response;
    }

    public static CriarUsuarioRequest criarUsuarioRequest() {
        CriarUsuarioRequest request = new CriarUsuarioRequest();
        request.setNome(ConstantUtil.NOME_USUARIO);
        request.setEmail(ConstantUtil.EMAIL_VALIDO);
        request.setSenha(ConstantUtil.SENHA);
        return request;
    }

    public static AtualizarUsuarioRequest criarAtualizarUsuarioRequest() {
        AtualizarUsuarioRequest request = new AtualizarUsuarioRequest();
        request.setNome(ConstantUtil.NOME_USUARIO_ATUALIZADO);
        request.setEmail(ConstantUtil.EMAIL_VALIDO);
        return request;
    }
}