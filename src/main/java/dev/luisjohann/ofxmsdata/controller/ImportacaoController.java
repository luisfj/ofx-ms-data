package dev.luisjohann.ofxmsdata.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.luisjohann.ofxmsdata.clients.PermissionCheckerClient;
import dev.luisjohann.ofxmsdata.dto.ImportacaoDTO;
import dev.luisjohann.ofxmsdata.service.ImportacaoService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

// @CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/importacao")
@RequiredArgsConstructor
public class ImportacaoController {

    final PermissionCheckerClient permissionChecker;
    final ImportacaoService service;

    @GetMapping("/pendencias/{idUser}/{idUe}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<ImportacaoDTO> buscarImportacoesPendentes(@PathVariable("idUser") Long idUser,
            @PathVariable("idUe") Long idUe) {
        // Authentication authentication =
        // SecurityContextHolder.getContext().getAuthentication();
        // System.out.println(getUserDetails(authentication));
        return permissionChecker.checkGetDataPermission(idUser, idUe)
                .thenMany(service.buscarImportacoesPendentesDoUsuarioEUe(idUser, idUe));

    }

    public String getUserDetails(Authentication authentication) {
        if (authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            // Acessar dados do JWT
            String username = jwt.getClaimAsString("preferred_username"); // Nome de usuário do JWT
            String email = jwt.getClaimAsString("email"); // Email do JWT
            String roles = jwt.getClaimAsString("realm_access"); // Roles (como exemplo)

            // Pode-se acessar outros claims conforme necessário
            return String.format("User: %s, Email: %s, Roles: %s", username, email, roles);
        } else {
            return "No JWT token found";
        }
    }
}
