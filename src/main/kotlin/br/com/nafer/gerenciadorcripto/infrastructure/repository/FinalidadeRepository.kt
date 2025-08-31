package br.com.nafer.gerenciadorcripto.infrastructure.repository

import br.com.nafer.gerenciadorcripto.domain.model.Corretora
import br.com.nafer.gerenciadorcripto.domain.model.Finalidades
import br.com.nafer.gerenciadorcripto.domain.model.enums.FinalidadeOperacaoEnum
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FinalidadeRepository : JpaRepository<Finalidades, Int> {
    fun findByCorretoraAndNome(corretora: Corretora, nome: FinalidadeOperacaoEnum): Finalidades?
}
