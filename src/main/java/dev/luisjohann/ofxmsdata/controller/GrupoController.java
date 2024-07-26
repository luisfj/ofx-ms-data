package dev.luisjohann.ofxmsdata.controller;

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
import dev.luisjohann.ofxmsdata.dto.NovoGrupoDTO;
import dev.luisjohann.ofxmsdata.dto.NovoGrupoResponseDTO;
import dev.luisjohann.ofxmsdata.dto.OperacoesDTO;
import dev.luisjohann.ofxmsdata.service.GrupoService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

// @CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/grupo")
@RequiredArgsConstructor
public class GrupoController {

   final PermissionCheckerClient permissionChecker;
   final GrupoService service;

   @GetMapping("/{idUser}/{idUe}")
   @ResponseStatus(HttpStatus.OK)
   public Mono<List<OperacoesDTO>> buscarImportacoesPendentes(@PathVariable("idUser") Long idUser,
         @PathVariable("idUe") Long idUe) throws InterruptedException {

      permissionChecker.checkGetDataPermission(idUser, idUe);
      // Thread.sleep(RandomGenerator.getDefault().nextLong(1000, 3000));
      return service.findByIdImportacao(idUser, idUe, LocalDate.of(2000, 1, 1), LocalDate.of(2040, 1, 1));
   }

   @PostMapping("/{idUser}/{idUe}")
   @ResponseStatus(HttpStatus.OK)
   public Mono<ResponseEntity<NovoGrupoResponseDTO>> adicionarGrupo(@PathVariable("idUser") Long idUser,
         @PathVariable("idUe") Long idUe, @RequestBody NovoGrupoDTO novoGrupoDTO) {
      permissionChecker.checkPostDataOfx(idUser, idUe);

      var grupoResponseDTO = service.adicionarGrupo(idUser, idUe, novoGrupoDTO);

      return Mono.just(ResponseEntity.ok().body(grupoResponseDTO));
   }

   @PutMapping("/{idUser}/{idUe}/{idGrupo}")
   @ResponseStatus(HttpStatus.OK)
   public Mono<ResponseEntity<NovoGrupoResponseDTO>> atualizarGrupo(@PathVariable("idUser") Long idUser,
         @PathVariable("idUe") Long idUe, @PathVariable("idGrupo") Long idGrupo,
         @RequestBody NovoGrupoDTO novoGrupoDTO) {
      permissionChecker.checkPostDataOfx(idUser, idUe);

      var grupoResponseDTO = service.atualizarGrupo(idUser, idUe, idGrupo, novoGrupoDTO);

      return Mono.just(ResponseEntity.ok().body(grupoResponseDTO));
   }

   @DeleteMapping("/{idUser}/{idUe}/{idGrupo}")
   @ResponseStatus(HttpStatus.OK)
   public ResponseEntity<Void> excluirOperacaoDefinitivamente(@PathVariable("idUser") Long idUser,
         @PathVariable("idUe") Long idUe, @PathVariable("idGrupo") Long idGrupo) {
      permissionChecker.checkPostDataOfx(idUser, idUe);

      service.excluirGrupoDefinitivamente(idUser, idUe, idGrupo);

      return ResponseEntity.ok().build();
   }
}
