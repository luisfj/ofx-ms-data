package dev.luisjohann.ofxmsdata.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

public record NovoGrupoDTO(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora,
            String memo,
            Integer ordem) {

}
