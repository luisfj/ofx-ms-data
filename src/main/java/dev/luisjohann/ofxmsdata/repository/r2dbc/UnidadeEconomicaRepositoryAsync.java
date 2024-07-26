package dev.luisjohann.ofxmsdata.repository.r2dbc;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import dev.luisjohann.ofxmsdata.model.UnidadeEconomica;

@Repository
// public interface UserRepository extends R2dbcRepository<User, Long> {
public interface UnidadeEconomicaRepositoryAsync extends R2dbcRepository<UnidadeEconomica, Long> {

}
