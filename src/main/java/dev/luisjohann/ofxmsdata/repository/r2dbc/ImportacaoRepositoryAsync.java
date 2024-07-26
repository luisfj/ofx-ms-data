package dev.luisjohann.ofxmsdata.repository.r2dbc;

import java.util.List;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.luisjohann.ofxmsdata.dto.ImportacaoDTO;
import dev.luisjohann.ofxmsdata.enums.StatusImportacao;
import dev.luisjohann.ofxmsdata.model.Importacao;
import reactor.core.publisher.Flux;

@Repository
public interface ImportacaoRepositoryAsync extends R2dbcRepository<Importacao, Long> {

	@Query("""
			         select
				i.id_user_importador,
				ou.name as nome_user_importador,
				i.id,
				i.id_conta_bancaria as id_conta,
				cb.nome	as nome_conta,
				cb.numero	as nr_conta,
				cb.cor as cor_conta,
				cb.banco as banco_conta,
				i.data_importacao,
				i.nome_arquivo,
				i.nr_registros,
				i.status,
				coalesce(sum(o.valor), 0) as valor_total
			from importacao i
				inner join ofx_user ou on ou.id = i.id_user_importador
				left join conta_bancaria cb on cb.id = i.id_conta_bancaria
				left join operacao o on o.id_user = ou.id
			where i.id_user = :idUser and i.id_ue = :idUe and i.status in (:status)
			group by 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12
			         """)
	Flux<ImportacaoDTO> findAllByIdUserAndIdUeAndStatusIn(@Param("idUser") Long idUser, @Param("idUe") Long idUe,
			@Param("status") List<StatusImportacao> status);
}
