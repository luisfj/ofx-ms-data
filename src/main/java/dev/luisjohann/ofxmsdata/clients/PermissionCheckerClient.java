package dev.luisjohann.ofxmsdata.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PermissionCheckerClient {
    private final WebClient webClient;

    public PermissionCheckerClient(WebClient.Builder webClientBuilder,
                                   @Value("${conf.permission-checker.url}") String permissionCheckerUrl) {
        this.webClient = webClientBuilder.baseUrl(permissionCheckerUrl).build();
    }

    public Mono<Void> checkPostDataOfx(@PathVariable("ue_id") Long ueId) {
        String uri = String.format("/check-post-data/%s", ueId);

        return ReactiveSecurityContextHolder.getContext()
                .map(this::extractToken)
                .flatMap(token -> webClient.get()
                        .uri(uri)
                        .header("Authorization", "Bearer " + token)
                        .retrieve()
                        .bodyToMono(Void.class));
    }

    public Mono<Void> checkGetDataPermission(@PathVariable("ue_id") Long ueId) {
        String uri = String.format("/check-get-data/%s", ueId);

        return ReactiveSecurityContextHolder.getContext()
                .map(this::extractToken)
                .flatMap(token -> webClient.get()
                        .uri(uri)
                        .header("Authorization", "Bearer " + token)
                        .retrieve()
                        .bodyToMono(Void.class));
    }

    private String extractToken(SecurityContext context) {
        Authentication authentication = context.getAuthentication();
        if (authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            // Acessar dados do JWT
            return jwt.getTokenValue();
        }
        throw new RuntimeException("Token não encontrado");

    }
}
