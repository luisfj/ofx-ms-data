package dev.luisjohann.ofxmsdata.controller;

import dev.luisjohann.ofxmsdata.clients.PermissionCheckerClient;
import dev.luisjohann.ofxmsdata.dto.*;
import dev.luisjohann.ofxmsdata.service.OperacaoService;
import jakarta.ws.rs.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

// @CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/operacao")
@RequiredArgsConstructor
@Slf4j
public class OperacaoController {

    final PermissionCheckerClient permissionChecker;
    final OperacaoService service;

    @GetMapping("/{idUe}/{idImportacao}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<List<OperacoesDTO>> buscarImportacoesPendentes(
            @PathVariable("idUe") Long idUe, @PathVariable("idImportacao") Long idImportacao) throws InterruptedException {

        permissionChecker.checkGetDataPermission(idUe);
        // Thread.sleep(RandomGenerator.getDefault().nextLong(1000, 3000));
        return service.findByIdImportacao(idUe, idImportacao);
    }

    @GetMapping("/pendentes/{idUe}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<OperacoesDTO> buscarOperacoesPendentes(
            @PathVariable("idUe") Long idUe,
            @PathParam("dataInicial") LocalDate dataInicial,
            @PathParam("dataFinal") LocalDate dataFinal
    ) {
        log.info("buscarOperacoesPendentes ue:{} dataInicial: {} dataFinal: {}",
                idUe, dataInicial.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                dataFinal.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        permissionChecker.checkGetDataPermission(idUe);
        return service.findOperacoesPendendesByDataBetween(idUe, dataInicial.atStartOfDay(),
                dataFinal.atTime(23, 59, 59));
    }

    @GetMapping("/processadas/{idUe}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<OperacoesProcessadasDTO> buscarOperacoesProcessadas(
            @PathVariable("idUe") Long idUe,
            @PathParam("dataInicial") LocalDate dataInicial,
            @PathParam("dataFinal") LocalDate dataFinal
    ) {
        log.info("buscarOperacoesProcessadas ue:{} dataInicial: {} dataFinal: {}",
                idUe, dataInicial.atStartOfDay().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                dataFinal.atTime(23, 59, 59).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        permissionChecker.checkGetDataPermission(idUe);
        return service.findOperacoesProcessadasByDataBetween(idUe, dataInicial.atStartOfDay(),
                dataFinal.atTime(23, 59, 59));
    }

    @PutMapping("/{idUe}")
    @ResponseStatus(HttpStatus.OK)
    public void atualizarOperacoes(
            @PathVariable("idUe") Long idUe, @RequestBody UpdateOperacoesDTO operacoesDTO) {
        permissionChecker.checkPostDataOfx(idUe);

        service.atualizarOperacoes(idUe, operacoesDTO);
    }

    @GetMapping("/single/{idUe}/{idOperacao}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<NovaOperacaoResponseDTO> buscarOperacaoPorId(
            @PathVariable("idUe") Long idUe, @PathVariable("idOperacao") Long idOperacao) {
        permissionChecker.checkGetDataPermission(idUe);

        var response = service.buscarOperacaoPorId(idUe, idOperacao);

        return response;
    }

    @PostMapping("/single/{idUe}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<NovaOperacaoResponseDTO> salvarOperacao(
            @PathVariable("idUe") Long idUe, @RequestBody NovaOperacaoDTO novaOperacaoDTO) {
        permissionChecker.checkPostDataOfx(idUe);

        var response = service.salvarOperacao(idUe, novaOperacaoDTO);

        return ResponseEntity.created(URI.create("" + response.id()))
                .body(response);
    }

    @PutMapping("/single/{idUe}/{idOperacao}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<NovaOperacaoResponseDTO> atualizarOperacao(
            @PathVariable("idUe") Long idUe, @PathVariable("idOperacao") Long idOperacao,
            @RequestBody NovaOperacaoDTO novaOperacaoDTO) {
        permissionChecker.checkPostDataOfx(idUe);

        var response = service.atualizarOperacao(idUe, idOperacao, novaOperacaoDTO);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/single/{idUe}/{idOperacao}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> excluirOperacaoDefinitivamente(
            @PathVariable("idUe") Long idUe, @PathVariable("idOperacao") Long idOperacao) {
        permissionChecker.checkPostDataOfx(idUe);

        service.excluirOperacaoDefinitivamente(idUe, idOperacao);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/remover-grupo/{idUe}/{idOperacao}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> removerOperacaoDoGrupo(
            @PathVariable("idUe") Long idUe, @PathVariable("idOperacao") Long idOperacao) {
        permissionChecker.checkPostDataOfx(idUe);

        service.removerOperacaoDoGrupo(idUe, idOperacao);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/agrupar/{idUe}/{idGrupo}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<Void>> agruparOperacoes(
            @PathVariable("idUe") Long idUe, @PathVariable("idGrupo") Long idGrupo,
            @RequestBody List<Long> idOperacoes) throws InterruptedException {

        permissionChecker.checkPostDataOfx(idUe);
        // Thread.sleep(RandomGenerator.getDefault().nextLong(1000, 3000));
        service.agruparOperacoes(idUe, idGrupo, idOperacoes);

        return Mono.just(ResponseEntity.ok().build());
    }
}
