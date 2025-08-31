package br.com.nafer.gerenciadorcripto.ui.viewmodel

import br.com.nafer.gerenciadorcripto.domain.model.dtos.OperacoesDTO
import br.com.nafer.gerenciadorcripto.dtos.ListagemDeOperacoesDTO
import br.com.nafer.gerenciadorcripto.dtos.binance.ArquivoDTO
import br.com.nafer.gerenciadorcripto.service.OperacaoService
import org.springframework.stereotype.Component

@Component
class OperacaoViewModel(private val operacaoService: OperacaoService) {
    fun listarOperacoes(): List<ListagemDeOperacoesDTO> {
        return  operacaoService.listarOperacoes().map { converterParaDTO(it) }
    }

    private fun converterParaDTO(operacao: OperacoesDTO): ListagemDeOperacoesDTO {
        val dataOperacao = operacao.dataOperacaoEntrada ?: operacao.dataOperacaoSaida
        return ListagemDeOperacoesDTO(
            dataOperacao!!,
            operacao.moedaEntrada?.ticker,
            operacao.moedaEntrada?.icone,
            operacao.moedaEntrada?.nome,
            operacao.quantidadeEntrada,
            operacao.moedaSaida?.ticker,
            operacao.moedaSaida?.icone,
            operacao.moedaSaida?.nome,
            operacao.quantidadeSaida,
            operacao.valorBrl,
            operacao.lucroPrejuizo,
            operacao.tipoOperacao.descricao,
            operacao.finalidade.descricao,
            operacao.tipoOperacao
        )
    }
}