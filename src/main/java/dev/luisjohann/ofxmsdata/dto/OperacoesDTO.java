package dev.luisjohann.ofxmsdata.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.luisjohann.ofxmsdata.enums.StatusOperacao;
import dev.luisjohann.ofxmsdata.enums.TipoAtualizacaoDados;
import dev.luisjohann.ofxmsdata.enums.TipoOperacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OperacoesDTO {
    Long id;
    Long idUe;
    String nomeUe;
    Long idConta;
    String nomeConta;
    String nrConta;
    String banco;
    String corConta;
    Long idImportacao;
    TipoOperacao tipo;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime dataHora;
    BigDecimal valor;
    String fitId;
    String refNum;
    String memo;
    StatusOperacao status;
    Integer ordem;
    Long idGrupo;
    List<OperacoesDTO> filhos;
    TipoAtualizacaoDados tipoAlteracao;

    public OperacoesDTO(Long id, Long idUe, String nomeUe, Long idConta, String nomeConta, String nrConta,
                        String banco, String corConta, Long idImportacao, TipoOperacao tipo, LocalDateTime dataHora, BigDecimal valor,
                        String fitId, String refNum, String memo, StatusOperacao status, Integer ordem, Long idGrupo) {
        this.id = id;
        this.idUe = idUe;
        this.nomeUe = nomeUe;
        this.idConta = idConta;
        this.nomeConta = nomeConta;
        this.nrConta = nrConta;
        this.banco = banco;
        this.corConta = corConta;
        this.idImportacao = idImportacao;
        this.tipo = tipo;
        this.dataHora = dataHora;
        this.valor = valor;
        this.fitId = fitId;
        this.refNum = refNum;
        this.memo = memo;
        this.status = status;
        this.ordem = ordem;
        this.idGrupo = idGrupo;
    }

    public void addFilho(OperacoesDTO filho) {
        if (filhos == null)
            filhos = new ArrayList<>();
        filhos.add(filho);
    }

}
