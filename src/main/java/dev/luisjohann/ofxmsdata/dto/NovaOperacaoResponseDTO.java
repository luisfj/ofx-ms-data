package dev.luisjohann.ofxmsdata.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import dev.luisjohann.ofxmsdata.enums.StatusOperacao;
import dev.luisjohann.ofxmsdata.enums.TipoOperacao;

public record NovaOperacaoResponseDTO(
            Long id,
            TipoOperacao tipo,
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora,
            BigDecimal valor,
            String fitId,
            String refNum,
            String memo,
            StatusOperacao status,
            Integer ordem,
            Long idGrupo) {
}