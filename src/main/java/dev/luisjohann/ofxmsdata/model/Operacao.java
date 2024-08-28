package dev.luisjohann.ofxmsdata.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import dev.luisjohann.ofxmsdata.enums.StatusOperacao;
import dev.luisjohann.ofxmsdata.enums.TipoOperacao;

public record Operacao(
            @Id Long id,
            Long idUe,
            Long idImportacao,
            TipoOperacao tipo,
            LocalDateTime dataHora,
            BigDecimal valor,
            String fitId,
            String refNum,
            String memo,
            StatusOperacao status,
            Integer ordem,
            Long idGrupo) {

}
