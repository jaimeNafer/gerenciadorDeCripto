package br.com.nafer.gerenciadorcripto.infrastructure.repository

import br.com.nafer.gerenciadorcripto.domain.model.Arquivo
import br.com.nafer.gerenciadorcripto.domain.model.Carteira
import br.com.nafer.gerenciadorcripto.domain.model.Operacoes
import br.com.nafer.gerenciadorcripto.domain.model.enums.StatusOperacaoEnum
import br.com.nafer.gerenciadorcripto.domain.model.enums.TipoOperacaoEnum
import org.jetbrains.skia.Region
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface OperacaoRepository : JpaRepository<Operacoes, Int> {
    fun deleteAllByArquivo(arquivo: Arquivo)
    fun findAllByStatusOperacao(status: StatusOperacaoEnum): List<Operacoes>
    fun findAllByCarteiraAndTipoOperacaoIn(
        carteira: Carteira,
        tipoOperacao: List<TipoOperacaoEnum>
    ): List<Operacoes>
}
