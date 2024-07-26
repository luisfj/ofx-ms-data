package dev.luisjohann.ofxmsdata.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.luisjohann.ofxmsdata.clients.PermissionCheckerClient;
import dev.luisjohann.ofxmsdata.dto.NovaOperacaoDTO;
import dev.luisjohann.ofxmsdata.dto.NovaOperacaoResponseDTO;
import dev.luisjohann.ofxmsdata.dto.OperacoesDTO;
import dev.luisjohann.ofxmsdata.dto.UpdateOperacoesDTO;
import dev.luisjohann.ofxmsdata.service.OperacaoService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// @CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/operacao")
@RequiredArgsConstructor
public class OperacaoController {

   final PermissionCheckerClient permissionChecker;
   final OperacaoService service;

   @GetMapping("/{idUser}/{idUe}/{idImportacao}")
   @ResponseStatus(HttpStatus.OK)
   public Mono<List<OperacoesDTO>> buscarImportacoesPendentes(@PathVariable("idUser") Long idUser,
         @PathVariable("idUe") Long idUe, @PathVariable("idImportacao") Long idImportacao) throws InterruptedException {

      permissionChecker.checkGetDataPermission(idUser, idUe);
      // Thread.sleep(RandomGenerator.getDefault().nextLong(1000, 3000));
      return service.findByIdImportacao(idUser, idUe, idImportacao);
   }

   @GetMapping("/pendentes/{idUser}/{idUe}")
   @ResponseStatus(HttpStatus.OK)
   public Flux<OperacoesDTO> buscarOperacoesPendentes(@PathVariable("idUser") Long idUser,
         @PathVariable("idUe") Long idUe) throws InterruptedException {

      permissionChecker.checkGetDataPermission(idUser, idUe);
      // Thread.sleep(RandomGenerator.getDefault().nextLong(1000, 3000));
      return service.findOperacoesPendendesByDataBetween(idUser, idUe, LocalDate.of(2000, 1, 1),
            LocalDate.of(2040, 1, 1));
   }

   @PutMapping("/{idUser}/{idUe}")
   @ResponseStatus(HttpStatus.OK)
   public void atualizarOperacoes(@PathVariable("idUser") Long idUser,
         @PathVariable("idUe") Long idUe, @RequestBody UpdateOperacoesDTO operacoesDTO) {
      permissionChecker.checkPostDataOfx(idUser, idUe);

      service.atualizarOperacoes(idUser, idUe, operacoesDTO);
   }

   @GetMapping("/single/{idUser}/{idUe}/{idOperacao}")
   @ResponseStatus(HttpStatus.OK)
   public Mono<NovaOperacaoResponseDTO> buscarOperacaoPorId(@PathVariable("idUser") Long idUser,
         @PathVariable("idUe") Long idUe, @PathVariable("idOperacao") Long idOperacao) {
      permissionChecker.checkGetDataPermission(idUser, idUe);

      var response = service.buscarOperacaoPorId(idUser, idUe, idOperacao);

      return response;
   }

   @PostMapping("/single/{idUser}/{idUe}")
   @ResponseStatus(HttpStatus.CREATED)
   public ResponseEntity<NovaOperacaoResponseDTO> salvarOperacao(@PathVariable("idUser") Long idUser,
         @PathVariable("idUe") Long idUe, @RequestBody NovaOperacaoDTO novaOperacaoDTO) {
      permissionChecker.checkPostDataOfx(idUser, idUe);

      var response = service.salvarOperacao(idUser, idUe, novaOperacaoDTO);

      return ResponseEntity.created(URI.create("" + response.id()))
            .body(response);
   }

   @PutMapping("/single/{idUser}/{idUe}/{idOperacao}")
   @ResponseStatus(HttpStatus.OK)
   public ResponseEntity<NovaOperacaoResponseDTO> atualizarOperacao(@PathVariable("idUser") Long idUser,
         @PathVariable("idUe") Long idUe, @PathVariable("idOperacao") Long idOperacao,
         @RequestBody NovaOperacaoDTO novaOperacaoDTO) {
      permissionChecker.checkPostDataOfx(idUser, idUe);

      var response = service.atualizarOperacao(idUser, idUe, idOperacao, novaOperacaoDTO);

      return ResponseEntity.ok(response);
   }

   @DeleteMapping("/single/{idUser}/{idUe}/{idOperacao}")
   @ResponseStatus(HttpStatus.OK)
   public ResponseEntity<Void> excluirOperacaoDefinitivamente(@PathVariable("idUser") Long idUser,
         @PathVariable("idUe") Long idUe, @PathVariable("idOperacao") Long idOperacao) {
      permissionChecker.checkPostDataOfx(idUser, idUe);

      service.excluirOperacaoDefinitivamente(idUser, idUe, idOperacao);

      return ResponseEntity.ok().build();
   }

   @PutMapping("/remover-grupo/{idUser}/{idUe}/{idOperacao}")
   @ResponseStatus(HttpStatus.OK)
   public ResponseEntity<Void> removerOperacaoDoGrupo(@PathVariable("idUser") Long idUser,
         @PathVariable("idUe") Long idUe, @PathVariable("idOperacao") Long idOperacao) {
      permissionChecker.checkPostDataOfx(idUser, idUe);

      service.removerOperacaoDoGrupo(idUser, idUe, idOperacao);

      return ResponseEntity.ok().build();
   }

   @PostMapping("/agrupar/{idUser}/{idUe}/{idGrupo}")
   @ResponseStatus(HttpStatus.OK)
   public Mono<ResponseEntity<Void>> agruparOperacoes(@PathVariable("idUser") Long idUser,
         @PathVariable("idUe") Long idUe, @PathVariable("idGrupo") Long idGrupo,
         @RequestBody List<Long> idOperacoes) throws InterruptedException {

      permissionChecker.checkPostDataOfx(idUser, idUe);
      // Thread.sleep(RandomGenerator.getDefault().nextLong(1000, 3000));
      service.agruparOperacoes(idUser, idUe, idGrupo, idOperacoes);

      return Mono.just(ResponseEntity.ok().build());
   }
}
