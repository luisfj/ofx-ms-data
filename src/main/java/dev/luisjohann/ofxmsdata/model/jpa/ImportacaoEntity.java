package dev.luisjohann.ofxmsdata.model.jpa;

import java.time.LocalDateTime;

import dev.luisjohann.ofxmsdata.enums.StatusImportacao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "importacao")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportacaoEntity {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      Long id;
      @Column(name = "id_user", nullable = false)
      Long idUser;
      @Column(name = "id_user_importador", nullable = false)
      Long idUserImportador;
      @Column(name = "id_ue", nullable = false)
      Long idUe;
      @Column(name = "id_conta_bancaria")
      Long idContaBancaria;
      @Column(name = "data_importacao", nullable = false)
      @Temporal(TemporalType.TIMESTAMP)
      LocalDateTime dataImportacao;
      @Column(name = "nome_arquivo", nullable = false)
      String nomeArquivo;
      @Column(name = "nr_registros", nullable = false)
      Integer nrRegistros;
      @Column(name = "status", nullable = false)
      @Enumerated(EnumType.STRING)
      StatusImportacao status;

}