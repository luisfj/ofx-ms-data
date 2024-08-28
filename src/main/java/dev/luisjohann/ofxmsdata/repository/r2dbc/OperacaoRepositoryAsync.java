package dev.luisjohann.ofxmsdata.repository.r2dbc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.luisjohann.ofxmsdata.dto.OperacoesDTO;
import dev.luisjohann.ofxmsdata.model.Operacao;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OperacaoRepositoryAsync extends R2dbcRepository<Operacao, Long> {

	@Query("""
			   select
				o.id,
				o.id_ue,
				ue.name as nome_ue,
				o.id_conta_bancaria as id_conta,
				cb.nome as nome_conta,
				cb.numero as nr_conta,
				cb.banco as banco,
				cb.cor as cor_conta,
				o.tipo,
				o.data_hora,
				o.valor,
				o.fit_id,
				o.ref_num,
				o.memo,
				o.status,
				o.ordem,
				o.id_grupo,
				false as changes
			from operacao o
				left join unidade_economica ue on ue.id = o.id_ue
				left join conta_bancaria cb on cb.id = o.id_conta_bancaria
			where o.id_importacao = :idImportacao and o.id_ue = :idUe
			   """)
	Flux<OperacoesDTO> findByIdImportacao(@Param("idUe") Long idUe,
			@Param("idImportacao") Long idImportacao);

	@Query("""
			  with cte_grupos as (
				select
					o.id,
					o.id_ue,
					ue.name as nome_ue,
					o.id_conta_bancaria as id_conta,
					cb.nome as nome_conta,
					cb.numero as nr_conta,
					cb.banco as banco,
					cb.cor as cor_conta,
					o.tipo,
					o.data_hora,
					o.valor,
					o.fit_id,
					o.ref_num,
					o.memo,
					o.status,
					o.ordem,
					o.id_grupo,
					false as changes
				from operacao o
					left join unidade_economica ue on ue.id = o.id_ue
					left join conta_bancaria cb on cb.id = o.id_conta_bancaria
				where o.id_ue = :idUe
					and o.tipo = 'GROUP'
					and o.data_hora between :dtInicial and :dtFinal
			),
			cte_childs as (
				select
					o.id,
					o.id_ue,
					ue.name as nome_ue,
					o.id_conta_bancaria as id_conta,
					cb.nome as nome_conta,
					cb.numero as nr_conta,
					cb.banco as banco,
					cb.cor as cor_conta,
					o.tipo,
					o.data_hora,
					o.valor,
					o.fit_id,
					o.ref_num,
					o.memo,
					o.status,
					o.ordem,
					o.id_grupo,
					false as changes
				from operacao o
					left join unidade_economica ue on ue.id = o.id_ue
					left join conta_bancaria cb on cb.id = o.id_conta_bancaria
				where o.id_grupo in (select id from cte_grupos)
			)
			select * from cte_grupos
			union all
			select * from cte_childs
			  """)
	Flux<OperacoesDTO> findGruposByIdImportacaoAndDataBetween(
			@Param("idUe") Long idUe,
			@Param("dtInicial") LocalDateTime dtInicial,
			@Param("dtFinal") LocalDateTime dtFinal);

	@Query("""
			select
				o.id,
				o.id_ue,
				ue.name as nome_ue,
				o.id_conta_bancaria as id_conta,
				cb.nome as nome_conta,
				cb.numero as nr_conta,
				cb.banco as banco,
				cb.cor as cor_conta,
				o.tipo,
				o.data_hora,
				o.valor,
				o.fit_id,
				o.ref_num,
				o.memo,
				o.status,
				o.ordem,
				o.id_grupo,
				false as changes
			from operacao o
				left join unidade_economica ue on ue.id = o.id_ue
				left join conta_bancaria cb on cb.id = o.id_conta_bancaria
			where o.id_ue = :idUe
				and o.tipo <> 'GROUP'
				and o.id_grupo is null
				and o.data_hora between :dtInicial and :dtFinal
			 """)
	Flux<OperacoesDTO> findOperacoesPendendesByDataBetween(
			@Param("idUe") Long idUe,
			@Param("dtInicial") LocalDateTime dtInicial,
			@Param("dtFinal") LocalDateTime dtFinal);

	Mono<Operacao> findByIdUeAndId(Long idUe, Long id);
}
