package dev.luisjohann.ofxmsdata.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public record NovoGrupoResponseDTO(
      Long id,
      @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora,
      String memo,
      Integer ordem) {

}
