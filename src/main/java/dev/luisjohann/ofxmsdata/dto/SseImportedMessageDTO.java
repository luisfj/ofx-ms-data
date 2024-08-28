package dev.luisjohann.ofxmsdata.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record SseImportedMessageDTO(String userId, Long ueId, String fileName,
      LocalDateTime dateTime,
      List<Operation> operations) implements Serializable {

}
