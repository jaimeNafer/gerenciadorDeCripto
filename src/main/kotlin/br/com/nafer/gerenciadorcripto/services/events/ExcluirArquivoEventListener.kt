package br.com.nafer.gerenciadorcripto.services.events

import br.com.nafer.gerenciadorcripto.services.ArquivoService
import br.com.nafer.gerenciadorcripto.services.FileSystemStorageService
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class ExcluirArquivoEventListener(
    private val storage: FileSystemStorageService,
    private val arquivoService: ArquivoService
) {
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onExcluirArquivo(evento: ArquivoExcluidoEvent) {
        val key = storage.deleteByKey(evento.storageKey)
    }
}