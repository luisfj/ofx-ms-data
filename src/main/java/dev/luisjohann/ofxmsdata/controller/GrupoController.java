package dev.luisjohann.ofxmsdata.controller;

import dev.luisjohann.ofxmsdata.clients.PermissionCheckerClient;
import dev.luisjohann.ofxmsdata.dto.NovoGrupoDTO;
import dev.luisjohann.ofxmsdata.dto.NovoGrupoResponseDTO;
import dev.luisjohann.ofxmsdata.dto.OperacoesDTO;
import dev.luisjohann.ofxmsdata.service.GrupoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

// @CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/grupo")
@RequiredArgsConstructor
@Slf4j
public class GrupoController {

    final PermissionCheckerClient permissionChecker;
    final GrupoService service;

    @GetMapping("/{idUe}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<List<OperacoesDTO>> buscarImportacoesPendentes(
            @PathVariable("idUe") Long idUe) throws InterruptedException {

        permissionChecker.checkGetDataPermission(idUe);
        // Thread.sleep(RandomGenerator.getDefault().nextLong(1000, 3000));
        return service.findByIdImportacao(idUe, LocalDate.of(2000, 1, 1), LocalDate.of(2040, 1, 1));
    }

    @PostMapping("/{idUe}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<NovoGrupoResponseDTO>> adicionarGrupo(
            @PathVariable("idUe") Long idUe, @RequestBody NovoGrupoDTO novoGrupoDTO) {
        permissionChecker.checkPostDataOfx(idUe);

        var grupoResponseDTO = service.adicionarGrupo(idUe, novoGrupoDTO);

        return Mono.just(ResponseEntity.ok().body(grupoResponseDTO));
    }

    @PutMapping("/{idUe}/{idGrupo}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<NovoGrupoResponseDTO>> atualizarGrupo(
            @PathVariable("idUe") Long idUe, @PathVariable("idGrupo") Long idGrupo,
            @RequestBody NovoGrupoDTO novoGrupoDTO) {
        log.info("Atualizar Grupo id: {} e ue id: {}", idGrupo, idUe);
        permissionChecker.checkPostDataOfx(idUe);

        var grupoResponseDTO = service.atualizarGrupo(idUe, idGrupo, novoGrupoDTO);

        return Mono.just(ResponseEntity.ok().body(grupoResponseDTO));
    }

    @DeleteMapping("/{idUe}/{idGrupo}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> excluirOperacaoDefinitivamente(
            @PathVariable("idUe") Long idUe, @PathVariable("idGrupo") Long idGrupo) {
        permissionChecker.checkPostDataOfx(idUe);

        service.excluirGrupoDefinitivamente(idUe, idGrupo);

        return ResponseEntity.ok().build();
    }
}
