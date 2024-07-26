package dev.luisjohann.ofxmsdata.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

public record NovaOperacaoDTO(
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora,
      BigDecimal valor,
      String fitId,
      String refNum,
      String memo,
      Integer ordem,
      Long idGrupo) {
}