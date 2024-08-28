package dev.luisjohann.ofxmsdata.dto;

import java.io.Serializable;

public record SseUserChangeMessageDTO(String userId, String email, String name) implements Serializable {

}
