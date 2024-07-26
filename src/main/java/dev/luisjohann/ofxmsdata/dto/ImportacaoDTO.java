package dev.luisjohann.ofxmsdata.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import dev.luisjohann.ofxmsdata.enums.StatusImportacao;

public record ImportacaoDTO(
            Long idUserImportador,
            String nomeUserImportador,
            Long id,
            Long idConta,
            String nomeConta,
            String nrConta,
            String corConta,
            String bancoConta,
            @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataImportacao,
            String nomeArquivo,
            Integer nrRegistros,
            StatusImportacao status,
            BigDecimal valorTotal) {

}
