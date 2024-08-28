package dev.luisjohann.ofxmsdata.model.mapper;

import dev.luisjohann.ofxmsdata.dto.*;
import dev.luisjohann.ofxmsdata.model.Operacao;
import dev.luisjohann.ofxmsdata.model.jpa.OperacaoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OperacaoMapper {

    static OperacaoMapper INSTANCE = Mappers.getMapper(OperacaoMapper.class);

    // @Mapping(source = "", target = "")
    OperacaoEntity dtoToEntity(OperacoesDTO operacoesDTO);

    OperacaoEntity dtoToEntity(NovaOperacaoDTO novaOperacaoDTO);

    OperacaoEntity dtoToEntity(NovoGrupoDTO operacoesDTO);

    NovoGrupoResponseDTO entityToDTo(OperacaoEntity operacoesDTO);

    NovaOperacaoResponseDTO entityToNovaOperacaoResponseDTo(OperacaoEntity operacoesDTO);

    NovaOperacaoResponseDTO entityToNovaOperacaoResponseDTo(Operacao operacoesDTO);

    List<OperacaoEntity> dtoToEntity(Iterable<OperacoesDTO> operacoes);
}
