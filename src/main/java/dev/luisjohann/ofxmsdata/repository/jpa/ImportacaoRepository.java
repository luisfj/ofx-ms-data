package dev.luisjohann.ofxmsdata.repository.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import dev.luisjohann.ofxmsdata.model.jpa.ImportacaoEntity;

@Repository
public interface ImportacaoRepository extends CrudRepository<ImportacaoEntity, Long> {

}
