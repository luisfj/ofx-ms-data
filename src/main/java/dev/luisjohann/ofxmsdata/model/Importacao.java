package dev.luisjohann.ofxmsdata.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import dev.luisjohann.ofxmsdata.enums.StatusImportacao;
import jakarta.persistence.Table;

@Table
public record Importacao(
      @Id Long id,
      Long idUser,
      Long idUserImportador,
      Long idUe,
      Long idConta,
      LocalDateTime dataImportacao,
      String nomeArquivo,
      Integer nrRegistros,
      StatusImportacao status) {

}