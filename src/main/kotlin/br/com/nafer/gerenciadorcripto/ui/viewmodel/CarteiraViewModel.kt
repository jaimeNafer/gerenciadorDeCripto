package br.com.nafer.gerenciadorcripto.ui.viewmodel

import br.com.nafer.gerenciadorcripto.domain.model.dtos.OperacoesDTO
import br.com.nafer.gerenciadorcripto.dtos.ListagemDeOperacoesDTO
import br.com.nafer.gerenciadorcripto.service.CarteiraService
import org.springframework.stereotype.Component

@Component
class CarteiraViewModel(private val carteiraService: CarteiraService) {
    fun obterCarteira(): CarteiraDTO {
        return  carteiraService.obterCarteira().map { converterParaDTO(it) }
    }

    private fun converterParaDTO(operacao: OperacoesDTO): ListagemDeOperacoesDTO {
        val dataOperacao = operacao.dataOperacaoEntrada ?: operacao.dataOperacaoSaida
        return ListagemDeOperacoesDTO(
            dataOperacao!!,
            operacao.moedaEntrada?.ticker,
            operacao.moedaEntrada?.icone,
            operacao.quantidadeEntrada,
            operacao.moedaSaida?.ticker,
            operacao.moedaSaida?.icone,
            operacao.quantidadeSaida,
            operacao.valorBrl,
            operacao.lucroPrejuizo,
            operacao.finalidade.descricao,
            operacao.tipoOperacao
        )
    }
}