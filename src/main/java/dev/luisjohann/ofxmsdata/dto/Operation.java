package dev.luisjohann.ofxmsdata.dto;

import java.time.LocalDateTime;

public record Operation(String trnType, LocalDateTime dt, String value, String fiTid, String refNum, String memo) {
}