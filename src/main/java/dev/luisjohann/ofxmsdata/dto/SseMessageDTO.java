package dev.luisjohann.ofxmsdata.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record SseMessageDTO(String userId, String title, String message, LocalDateTime dateTime) implements Serializable {

}
