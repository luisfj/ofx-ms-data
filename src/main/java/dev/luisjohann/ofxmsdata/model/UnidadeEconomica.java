package dev.luisjohann.ofxmsdata.model;

import dev.luisjohann.ofxmsdata.enums.StatusCadastro;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UnidadeEconomica implements Persistable<Long> {
    @Id
    Long id;
    String name;
    StatusCadastro status;

    @Transient
    boolean isNew;

    @Override
    public boolean isNew() {
        return isNew;
    }
}