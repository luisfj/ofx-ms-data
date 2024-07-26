package dev.luisjohann.ofxmsdata.service;

import org.springframework.stereotype.Service;

import dev.luisjohann.ofxmsdata.model.ContaBancaria;
import dev.luisjohann.ofxmsdata.repository.r2dbc.ContaBancariaRepositoryAsync;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ContaBancariaService {

   final ContaBancariaRepositoryAsync repository;

   public Flux<ContaBancaria> findAll() {
      return repository.findAll();
   }

}
