package br.com.nafer.gerenciadorcripto.domain.mappers

import br.com.nafer.gerenciadorcripto.domain.model.Arquivo
import br.com.nafer.gerenciadorcripto.domain.model.Corretora
import br.com.nafer.gerenciadorcripto.domain.model.Usuario
import br.com.nafer.gerenciadorcripto.dtos.binance.ArquivoDTO
import br.com.nafer.gerenciadorcripto.dtos.binance.CorretoraDTO
import br.com.nafer.gerenciadorcripto.dtos.binance.UsuarioDTO
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface ArquivoMapper {

    @Mapping(target = "idArquivo", ignore = true)
    @Mapping(target = "nome", source = "arquivo.nome")
    @Mapping(target = "dataCriacao", expression = "java(java.time.LocalDateTime.now())")
    fun toEntity(usuario: Usuario, corretora: Corretora, arquivo: ArquivoDTO, hashArquivo: String): Arquivo

    fun toDTO(arquivo: Arquivo): ArquivoDTO
}