package br.com.nafer.gerenciadorcripto.ui.viewmodel

import br.com.nafer.gerenciadorcripto.domain.model.dtos.OperacoesDTO
import br.com.nafer.gerenciadorcripto.dtos.ListagemDeOperacoesDTO
import br.com.nafer.gerenciadorcripto.dtos.binance.ArquivoDTO
import br.com.nafer.gerenciadorcripto.service.ImportacaoService
import org.springframework.stereotype.Component

@Component
class ImportacaoViewModel(private val importacaoService: ImportacaoService) {
    fun processarImportacaoArquivo(arquivo: ArquivoDTO, idCorretora: Int, idUsuario: Int) {
        importacaoService.processarImportacao(arquivo, idCorretora, idUsuario)
    }

    fun processarExclusaoArquivo(arquivo: ArquivoDTO) {
        importacaoService.processarExclusaoArquivo(arquivo)
    }
}