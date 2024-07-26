package dev.luisjohann.ofxmsdata.service;

import org.springframework.stereotype.Service;

import dev.luisjohann.ofxmsdata.model.UnidadeEconomica;
import dev.luisjohann.ofxmsdata.repository.r2dbc.UnidadeEconomicaRepositoryAsync;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class UnidadeEconomicaService {

   final UnidadeEconomicaRepositoryAsync repository;

   public Flux<UnidadeEconomica> findAll() {
      return repository.findAll();
   }

}
