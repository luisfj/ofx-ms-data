package dev.luisjohann.ofxmsdata.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table("OFX_USER")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class User implements Persistable<String> {
    @Id
    String id;
    String email;
    String name;

    @Transient
    boolean isNew;

    @Override
    public boolean isNew() {
        return isNew;
    }
}
