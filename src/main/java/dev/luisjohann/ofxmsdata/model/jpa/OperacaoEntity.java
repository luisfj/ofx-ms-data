package dev.luisjohann.ofxmsdata.model.jpa;

import dev.luisjohann.ofxmsdata.enums.StatusOperacao;
import dev.luisjohann.ofxmsdata.enums.TipoOperacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "operacao")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperacaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "id_ue", nullable = false)
    Long idUe;
    @Column(name = "id_importacao")
    Long idImportacao;
    @Column(name = "tipo", nullable = false)
    @Enumerated(EnumType.STRING)
    TipoOperacao tipo;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_hora", nullable = false)
    LocalDateTime dataHora;
    @Column(name = "valor", nullable = false)
    BigDecimal valor;
    @Column(name = "fit_id", nullable = false)
    String fitId;
    @Column(name = "ref_num")
    String refNum;
    @Column(name = "memo", nullable = false)
    String memo;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    StatusOperacao status;
    @Column(name = "ordem", nullable = false)
    Integer ordem;
    @Column(name = "id_grupo")
    Long idGrupo;

}