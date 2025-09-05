package br.com.nafer.gerenciadorcripto.domain.mappers

import br.com.nafer.gerenciadorcripto.domain.model.Arquivo
import br.com.nafer.gerenciadorcripto.domain.model.Corretora
import br.com.nafer.gerenciadorcripto.domain.model.Usuario
import br.com.nafer.gerenciadorcripto.dtos.binance.ArquivoDTO
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface ArquivoMapper {

    fun toEntity(usuario: Usuario, corretora: Corretora, arquivo: ArquivoDTO, hashArquivo: String): Arquivo

    fun toDTO(arquivo: Arquivo): ArquivoDTO
}