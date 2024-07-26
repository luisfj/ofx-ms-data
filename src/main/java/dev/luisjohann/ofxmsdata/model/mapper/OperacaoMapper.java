package dev.luisjohann.ofxmsdata.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import dev.luisjohann.ofxmsdata.dto.NovaOperacaoDTO;
import dev.luisjohann.ofxmsdata.dto.NovaOperacaoResponseDTO;
import dev.luisjohann.ofxmsdata.dto.NovoGrupoDTO;
import dev.luisjohann.ofxmsdata.dto.NovoGrupoResponseDTO;
import dev.luisjohann.ofxmsdata.dto.OperacoesDTO;
import dev.luisjohann.ofxmsdata.model.Operacao;
import dev.luisjohann.ofxmsdata.model.jpa.OperacaoEntity;

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
