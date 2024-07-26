package dev.luisjohann.ofxmsdata.model;

import org.springframework.data.annotation.Id;

import dev.luisjohann.ofxmsdata.enums.StatusCadastro;

public record ContaBancaria(
            @Id Long id,
            String nome,
            String numero,
            String banco,
            String cor,
            Long idUser,
            Long idUe,
            StatusCadastro status) {

}