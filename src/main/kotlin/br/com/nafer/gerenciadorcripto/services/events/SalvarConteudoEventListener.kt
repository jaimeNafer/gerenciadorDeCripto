package br.com.nafer.gerenciadorcripto.services.events

import br.com.nafer.gerenciadorcripto.services.ArquivoService
import br.com.nafer.gerenciadorcripto.services.FileSystemStorageService
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class SalvarConteudoEventListener(
    private val storage: FileSystemStorageService,
    private val arquivoService: ArquivoService
) {
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onSalvarConteudoArquivo(evento: ArquivoCriadoEvent) {
        val key = storage.saveCsvGz(evento.idCarteira, evento.idArquivo, evento.nome, evento.conteudo, evento.hashArquivo)
        arquivoService.updateStorageKey(evento.idArquivo, key)
    }
}