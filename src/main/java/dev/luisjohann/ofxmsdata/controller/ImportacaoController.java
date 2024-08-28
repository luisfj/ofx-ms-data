package dev.luisjohann.ofxmsdata.controller;

import dev.luisjohann.ofxmsdata.clients.PermissionCheckerClient;
import dev.luisjohann.ofxmsdata.dto.ImportacaoDTO;
import dev.luisjohann.ofxmsdata.service.ImportacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

// @CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/importacao")
@RequiredArgsConstructor
public class ImportacaoController {

    final PermissionCheckerClient permissionChecker;
    final ImportacaoService service;

    @GetMapping("/pendencias/{idUe}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<ImportacaoDTO> buscarImportacoesPendentes(
            @PathVariable("idUe") Long idUe) {
        return permissionChecker.checkGetDataPermission(idUe)
                .thenMany(service.buscarImportacoesPendentesDaUe(idUe));

    }

}
