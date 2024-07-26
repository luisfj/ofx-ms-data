package dev.luisjohann.ofxmsdata.controller;

import org.springframework.http.HttpStatus;
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

      permissionChecker.checkGetDataPermission(idUser, idUe);

      return service.buscarImportacoesPendentesDoUsuarioEUe(idUser, idUe);
   }

}
