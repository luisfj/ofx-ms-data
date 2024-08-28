package dev.luisjohann.ofxmsdata.service;

import dev.luisjohann.ofxmsdata.dto.*;
import dev.luisjohann.ofxmsdata.enums.StatusOperacao;
import dev.luisjohann.ofxmsdata.enums.TipoAtualizacaoDados;
import dev.luisjohann.ofxmsdata.enums.TipoOperacao;
import dev.luisjohann.ofxmsdata.model.jpa.ImportacaoEntity;
import dev.luisjohann.ofxmsdata.model.jpa.OperacaoEntity;
import dev.luisjohann.ofxmsdata.model.mapper.OperacaoMapper;
import dev.luisjohann.ofxmsdata.repository.jpa.OperacaoRepository;
import dev.luisjohann.ofxmsdata.repository.r2dbc.OperacaoRepositoryAsync;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OperacaoService {

    final OperacaoRepository repository;
    final OperacaoRepositoryAsync asyncRepository;

    public List<OperacaoEntity> importarOperacoes(Long idUe, ImportacaoEntity importacao,
                                                  List<Operation> operacoes) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        var operacoesPersist = operacoes
                .stream()
                .sorted(Comparator.comparing(Operation::dt))
                .map(op -> new OperacaoEntity(null, idUe, importacao.getId(),
                        TipoOperacao.valueOf(op.trnType()),
                        op.dt(), BigDecimal.valueOf(Double.parseDouble(op.value())),
                        op.fiTid(), op.refNum(), op.memo(), StatusOperacao.IMPORTADO,
                        atomicInteger.incrementAndGet(), null))
                .toList();

        return (List<OperacaoEntity>) repository.saveAll(operacoesPersist);
    }

    public Mono<List<OperacoesDTO>> findByIdImportacao(Long idUe, Long idImportacao) {
        return asyncRepository.findByIdImportacao(idUe, idImportacao).collectList()
                .map(this::organizaGruposEFilhos);
    }

    public Flux<OperacoesDTO> findOperacoesPendendesByDataBetween(Long idUe, LocalDateTime dtInicial,
                                                                  LocalDateTime dtFinal) {
        return asyncRepository.findOperacoesPendendesByDataBetween(idUe, dtInicial, dtFinal);
    }

    private List<OperacoesDTO> organizaGruposEFilhos(List<OperacoesDTO> operacoes) {
        var baseGrupos = operacoes
                .stream()
                .filter(op -> Objects.isNull(op.getIdGrupo())).toList();

        baseGrupos.forEach(op -> adicionaFilhos(op, operacoes));

        return baseGrupos;
    }

    private void adicionaFilhos(OperacoesDTO pai, List<OperacoesDTO> operacoes) {
        var filhos = operacoes.stream()
                .filter(op -> Objects.nonNull(op.getIdGrupo()) && op.getIdGrupo().equals(pai.getId()))
                .toList();

        filhos.forEach(op -> adicionaFilhos(op, operacoes));

        pai.setFilhos(filhos);
    }

    public void atualizarOperacoes(Long idUe, UpdateOperacoesDTO operacoesDTO) {
        var operacoesAtualizar = operacoesDTO.operacoes()
                .stream()
                .filter(o -> Objects.nonNull(o.getTipoAlteracao()))
                .collect(Collectors.groupingBy(OperacoesDTO::getTipoAlteracao));

        var dtosInseridos = operacoesAtualizar.getOrDefault(TipoAtualizacaoDados.INSERIDO, Collections.emptyList());
        var dtosAtualizar = operacoesAtualizar.getOrDefault(TipoAtualizacaoDados.ATUALIZADO,
                Collections.emptyList());
        var dtosRemovidos = operacoesAtualizar.getOrDefault(TipoAtualizacaoDados.DELETADO, Collections.emptyList());

        inserirOperacoes(OperacaoMapper.INSTANCE.dtoToEntity(dtosInseridos));
        removerOperacoes(dtosRemovidos);
        inserirOperacoes(OperacaoMapper.INSTANCE.dtoToEntity(dtosAtualizar));
    }

    private void atualizarOperacoes(List<OperacaoEntity> operacoes) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'atualizarOperacoes'");
    }

    private void removerOperacoes(List<OperacoesDTO> operacoes) {
        var idOperacoesRemover = operacoes.stream().map(OperacoesDTO::getId).toList();
        if (CollectionUtils.isEmpty(idOperacoesRemover))
            return;
        repository.deleteAllById(idOperacoesRemover);
    }

    private void inserirOperacoes(List<OperacaoEntity> operacoes) {
        repository.saveAll(operacoes);
    }

    public NovaOperacaoResponseDTO salvarOperacao(Long idUe, NovaOperacaoDTO novaOperacaoDTO) {
        var operacaoEntity = OperacaoMapper.INSTANCE.dtoToEntity(novaOperacaoDTO);
        operacaoEntity.setStatus(StatusOperacao.MANUAL);
        operacaoEntity.setTipo(BigDecimal.ZERO.compareTo(novaOperacaoDTO.valor()) >= 0 ? TipoOperacao.CREDIT
                : TipoOperacao.DEBIT);
        operacaoEntity.setIdUe(idUe);
        if (operacaoEntity.getIdGrupo() != null && operacaoEntity.getIdGrupo() == 0)
            operacaoEntity.setIdGrupo(null);

        var saved = repository.save(operacaoEntity);

        return OperacaoMapper.INSTANCE.entityToNovaOperacaoResponseDTo(saved);
    }

    private OperacaoEntity findOperacaoByIdAndIdUserAndIdUe(Long idUe, Long idOperacao) {
        return repository.findByIdAndIdUe(idOperacao, idUe)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Operação com id:%s não existe na base de dados", idOperacao)));

    }

    public NovaOperacaoResponseDTO atualizarOperacao(Long idUe, Long idOperacao,
                                                     NovaOperacaoDTO novaOperacaoDTO) {
        var operacaoEntity = findOperacaoByIdAndIdUserAndIdUe(idUe, idOperacao);

        if (TipoOperacao.GROUP.equals(operacaoEntity.getTipo()))
            throw new RuntimeException("Operação do tipo grupo não pode ser atualizada por esta função");

        operacaoEntity.setDataHora(novaOperacaoDTO.dataHora());
        operacaoEntity.setFitId(novaOperacaoDTO.fitId());
        if (StatusOperacao.MANUAL.equals(operacaoEntity.getStatus()))
            operacaoEntity.setRefNum(novaOperacaoDTO.refNum());
        operacaoEntity.setMemo(novaOperacaoDTO.memo());
        operacaoEntity.setOrdem(novaOperacaoDTO.ordem());
        operacaoEntity.setValor(novaOperacaoDTO.valor());
        if (novaOperacaoDTO.idGrupo() != null && novaOperacaoDTO.idGrupo() == 0)
            operacaoEntity.setIdGrupo(null);
        else
            operacaoEntity.setIdGrupo(novaOperacaoDTO.idGrupo());
        operacaoEntity.setTipo(BigDecimal.ZERO.compareTo(novaOperacaoDTO.valor()) >= 0 ? TipoOperacao.CREDIT
                : TipoOperacao.DEBIT);

        var saved = repository.save(operacaoEntity);
        return OperacaoMapper.INSTANCE.entityToNovaOperacaoResponseDTo(saved);
    }

    public Mono<NovaOperacaoResponseDTO> buscarOperacaoPorId(Long idUe, Long idOperacao) {
        return asyncRepository.findByIdUeAndId(idUe, idOperacao)
                .map(OperacaoMapper.INSTANCE::entityToNovaOperacaoResponseDTo);
    }

    public void excluirOperacaoDefinitivamente(Long idUe, Long idOperacao) {
        var operacaoEntity = findOperacaoByIdAndIdUserAndIdUe(idUe, idOperacao);
        repository.delete(operacaoEntity);
    }

    public void removerOperacaoDoGrupo(Long idUe, Long idOperacao) {
        var operacaoEntity = findOperacaoByIdAndIdUserAndIdUe(idUe, idOperacao);
        operacaoEntity.setIdGrupo(null);
        repository.save(operacaoEntity);
    }

    public void agruparOperacoes(Long idUe, Long idGrupo, List<Long> idOperacoes) {
        var operacoes = repository.findAllByIdIn(idOperacoes);
        if (operacoes.stream().anyMatch(op -> Objects.nonNull(op.getIdGrupo())
                || !idUe.equals(op.getIdUe())
                || TipoOperacao.GROUP.equals(op.getTipo()))) {
            throw new RuntimeException("Operação informada para agrupar é inválida ou não existe!");
        }

        operacoes.forEach(op -> op.setIdGrupo(idGrupo));
        repository.saveAll(operacoes);
    }

}
