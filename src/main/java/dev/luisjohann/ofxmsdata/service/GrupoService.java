package dev.luisjohann.ofxmsdata.service;

import dev.luisjohann.ofxmsdata.configuration.TransactionManagerUtil;
import dev.luisjohann.ofxmsdata.dto.NovoGrupoDTO;
import dev.luisjohann.ofxmsdata.dto.NovoGrupoResponseDTO;
import dev.luisjohann.ofxmsdata.dto.OperacoesDTO;
import dev.luisjohann.ofxmsdata.enums.StatusOperacao;
import dev.luisjohann.ofxmsdata.enums.TipoOperacao;
import dev.luisjohann.ofxmsdata.model.mapper.OperacaoMapper;
import dev.luisjohann.ofxmsdata.repository.jpa.OperacaoRepository;
import dev.luisjohann.ofxmsdata.repository.r2dbc.OperacaoRepositoryAsync;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GrupoService {

    final OperacaoRepository repository;
    final OperacaoRepositoryAsync asyncRepository;

    public Mono<List<OperacoesDTO>> findByIdImportacao(Long idUe, LocalDateTime dtInicial,
                                                       LocalDateTime dtFinal) {
        return asyncRepository
                .findGruposByIdImportacaoAndDataBetween(idUe, dtInicial, dtFinal)
                .collectList()
                .map(this::organizaGruposEFilhos);

    }

    private List<OperacoesDTO> organizaGruposEFilhos(List<OperacoesDTO> operacoes) {

        var baseGrupos = operacoes
                .stream()
                .filter(op -> TipoOperacao.GROUP.equals(op.getTipo())
                        && Objects.isNull(op.getIdGrupo()))
                .toList();

        baseGrupos.forEach(op -> adicionaFilhos(op, operacoes));
        return baseGrupos;
    }

    private void adicionaFilhos(OperacoesDTO pai, List<OperacoesDTO> operacoes) {
        var filhos = operacoes.stream()
                .filter(op -> Objects.nonNull(op.getIdGrupo()) && op.getIdGrupo().equals(pai.getId()))
                .toList();

        filhos.forEach(op -> adicionaFilhos(op, operacoes));

        pai.setFilhos(filhos);
        if (TipoOperacao.GROUP.equals(pai.getTipo())) {
            pai.setValor(filhos.stream().map(OperacoesDTO::getValor).reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO));
            pai.setTipo(BigDecimal.ZERO.compareTo(pai.getValor()) <= 0 ? TipoOperacao.CREDIT
                    : TipoOperacao.DEBIT);
        }
    }

    public NovoGrupoResponseDTO adicionarGrupo(Long idUe, NovoGrupoDTO novoGrupoDTO) {
        var grupo = OperacaoMapper.INSTANCE.dtoToEntity(novoGrupoDTO);

        grupo.setIdUe(idUe);
        grupo.setFitId(novoGrupoDTO.memo());
        grupo.setDataHora(novoGrupoDTO.dataHora());
        grupo.setOrdem(novoGrupoDTO.ordem());
        grupo.setStatus(StatusOperacao.CONFIRMADO);
        grupo.setTipo(TipoOperacao.GROUP);
        grupo.setValor(BigDecimal.ZERO);

        return OperacaoMapper.INSTANCE.entityToDTo(repository.save(grupo));
    }

    public NovoGrupoResponseDTO atualizarGrupo(Long idUe, Long idGrupo, NovoGrupoDTO novoGrupoDTO) {
        var grupo = repository.findByIdAndIdUe(idGrupo, idUe)
                .orElseThrow(() -> new RuntimeException(
                        "Nenhum grupo com este id para o usuário e ue encontrado"));

        if (!TipoOperacao.GROUP.equals(grupo.getTipo()))
            throw new RuntimeException("Id informado não é de um grupo válido");

        grupo.setFitId(novoGrupoDTO.memo());
        grupo.setMemo(novoGrupoDTO.memo());
        grupo.setDataHora(novoGrupoDTO.dataHora());
        grupo.setOrdem(novoGrupoDTO.ordem());

        return OperacaoMapper.INSTANCE.entityToDTo(repository.save(grupo));
    }

    @Transactional(TransactionManagerUtil.JPA_TRANSACTION_MANAGER)
    public void excluirGrupoDefinitivamente(Long idUe, Long idGrupo) {
        var grupoEntity = repository.findByIdAndIdUe(idGrupo, idUe)
                .orElseThrow(() -> new RuntimeException(
                        "Nenhum grupo com este id para a ue encontrado"));
        repository.updateGrupoIdNullByGrupoId(idGrupo);
        repository.delete(grupoEntity);
    }

}
