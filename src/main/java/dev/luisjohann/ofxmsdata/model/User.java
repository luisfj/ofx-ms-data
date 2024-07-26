package dev.luisjohann.ofxmsdata.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("OFX_USER")
public record User(
      @Id Long id,
      String name) {
}
