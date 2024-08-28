package dev.luisjohann.ofxmsdata.dto;

import dev.luisjohann.ofxmsdata.enums.StatusCadastro;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

public record SseUeChangeMessageDTO(@Id Long id,
                                    String name,
                                    StatusCadastro status) implements Serializable {

}
