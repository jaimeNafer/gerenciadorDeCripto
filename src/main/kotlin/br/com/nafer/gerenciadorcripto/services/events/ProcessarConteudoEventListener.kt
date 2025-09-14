package br.com.nafer.gerenciadorcripto.services.events

import br.com.nafer.gerenciadorcripto.domain.model.enums.StatusArquivoEnum
import br.com.nafer.gerenciadorcripto.exceptions.UnprocessableEntityException
import br.com.nafer.gerenciadorcripto.services.ArquivoService
import br.com.nafer.gerenciadorcripto.services.OperacaoService
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class ProcessarConteudoEventListener(
    private val operacaoService: OperacaoService,
    private val arquivoService: ArquivoService
) {
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onProcessarConteudoArquivo(evento: ArquivoCriadoEvent) {
        try {
            val arquivo = arquivoService.obterOu404(evento.idArquivo)
            validarStatus(arquivo.status)
            arquivoService.atualizaStatus(evento.idArquivo, StatusArquivoEnum.PROCESSANDO)
            operacaoService.criarOperacoesPorArquivo(arquivo, evento.conteudo)
            arquivoService.atualizaStatus(evento.idArquivo, StatusArquivoEnum.PROCESSADO)
        } catch (ex: Exception){
            arquivoService.atualizaStatus(evento.idArquivo, StatusArquivoEnum.ERRO)
            ex.printStackTrace()
        }
    }

    fun validarStatus(statusAtual: StatusArquivoEnum){
        if(statusAtual == StatusArquivoEnum.PROCESSANDO) {
            throw UnprocessableEntityException("Arquivo já está em proessamento")
        }
    }
}