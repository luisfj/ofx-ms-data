package dev.luisjohann.ofxmsdata.repository.jpa;

import dev.luisjohann.ofxmsdata.model.jpa.OperacaoEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OperacaoRepository extends CrudRepository<OperacaoEntity, Long> {

    Optional<OperacaoEntity> findByIdAndIdUe(Long id, Long idUe);

    @Modifying
    @Query(value = "update operacao set id_grupo = null where id_grupo = :idGrupo", nativeQuery = true)
    void updateGrupoIdNullByGrupoId(@Param("idGrupo") Long idGrupo);

    List<OperacaoEntity> findAllByIdIn(List<Long> ids);
}
