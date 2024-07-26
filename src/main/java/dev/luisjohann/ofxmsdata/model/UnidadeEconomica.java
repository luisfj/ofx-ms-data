package dev.luisjohann.ofxmsdata.model;

import org.springframework.data.annotation.Id;

import dev.luisjohann.ofxmsdata.enums.StatusCadastro;

public record UnidadeEconomica(
      @Id Long id,
      String name,
      StatusCadastro status) {
}