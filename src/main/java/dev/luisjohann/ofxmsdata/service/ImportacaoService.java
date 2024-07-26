package dev.luisjohann.ofxmsdata.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.luisjohann.ofxmsdata.configuration.TransactionManagerUtil;
import dev.luisjohann.ofxmsdata.dto.ImportacaoDTO;
import dev.luisjohann.ofxmsdata.dto.Operation;
import dev.luisjohann.ofxmsdata.enums.StatusImportacao;
import dev.luisjohann.ofxmsdata.model.jpa.ImportacaoEntity;
import dev.luisjohann.ofxmsdata.repository.jpa.ImportacaoRepository;
import dev.luisjohann.ofxmsdata.repository.r2dbc.ImportacaoRepositoryAsync;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ImportacaoService {

      final ImportacaoRepositoryAsync asyncRepository;
      final ImportacaoRepository repository;
      final OperacaoService operacaoService;

      @Async
      @Transactional(TransactionManagerUtil.JPA_TRANSACTION_MANAGER)
      public CompletableFuture<ImportacaoEntity> importarOperacoesAsync(Long idUser, Long idImportedUser, Long idUe,
                  String nomeArquivo,
                  List<Operation> operacoes) throws InterruptedException {
            try {
                  var novaImportacao = new ImportacaoEntity(null, idUser, idImportedUser, idUe, null,
                              LocalDateTime.now(),
                              nomeArquivo,
                              operacoes.size(),
                              StatusImportacao.IMPORTADO);

                  var saveImp = repository.save(novaImportacao);
                  operacaoService.importarOperacoes(idUser, idUe, novaImportacao, operacoes);

                  return CompletableFuture.completedFuture(saveImp);
            } catch (Exception ex) {
                  return CompletableFuture.failedFuture(ex);
            }
      }

      public Flux<ImportacaoDTO> buscarImportacoesPendentesDoUsuarioEUe(Long idUser, Long idUe) {
            return asyncRepository.findAllByIdUserAndIdUeAndStatusIn(idUser, idUe, List.of(StatusImportacao.IMPORTADO));
      }

}
