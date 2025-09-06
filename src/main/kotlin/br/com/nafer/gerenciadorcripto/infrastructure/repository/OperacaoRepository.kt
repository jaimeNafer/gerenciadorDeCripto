package br.com.nafer.gerenciadorcripto.infrastructure.repository

import br.com.nafer.gerenciadorcripto.domain.model.Operacoes
import br.com.nafer.gerenciadorcripto.domain.model.enums.StatusOperacaoEnum
import br.com.nafer.gerenciadorcripto.domain.model.enums.TipoOperacaoEnum
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface OperacaoRepository : JpaRepository<Operacoes, Int> {
    fun deleteAllByArquivoIdArquivo(idArquivo: Int)
    @Query("""
        select distinct o.moedaEntrada.ticker from Operacoes o where o.arquivo.idArquivo = :idArquivo 
    """)
    fun obterMoedaEntradaTickersDistintosPorIdArquivo(idArquivo: Int): List<String>
    @Query("""
        select distinct o.moedaSaida.ticker from Operacoes o where o.arquivo.idArquivo = :idArquivo 
    """)
    fun obterMoedaSaidaTickersDistintosPorIdArquivo(idArquivo: Int): List<String>
    @Modifying
    @Query("""
        update Operacoes o 
            set o.statusOperacao = :status 
        where o.arquivo.carteira.idCarteira = :idCarteira 
            and (o.moedaEntrada.ticker in :tickersAfetados or o.moedaSaida.ticker in : tickersAfetados)
    """)
    fun atualizarStatusPorCarteiraETickers(
        @Param(value = "idCarteira") idCarteira: Int,
        @Param(value = "tickersAfetados") tickersAfetados: List<String>,
        @Param(value = "status") status: StatusOperacaoEnum
    )

    fun findAllByStatusOperacaoNotAndArquivoIdArquivoAndTipoOperacaoIn(statusOperacao: StatusOperacaoEnum, idArquivo: Int, tipoOperacao: List<TipoOperacaoEnum>): List<Operacoes>
    abstract fun findAllByArquivoCarteiraIdCarteira(idCarteira: Int): List<Operacoes>
}
