package dev.luisjohann.ofxmsdata.repository.r2dbc;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import dev.luisjohann.ofxmsdata.model.ContaBancaria;

@Repository
public interface ContaBancariaRepositoryAsync extends R2dbcRepository<ContaBancaria, Long> {

}
