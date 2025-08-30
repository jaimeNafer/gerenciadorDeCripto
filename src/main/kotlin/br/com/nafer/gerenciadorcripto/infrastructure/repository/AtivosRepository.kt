package br.com.nafer.gerenciadorcripto.infrastructure.repository

import br.com.nafer.gerenciadorcripto.domain.model.Ativos
import br.com.nafer.gerenciadorcripto.domain.model.Carteira
import br.com.nafer.gerenciadorcripto.domain.model.Moeda
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AtivosRepository : JpaRepository<Ativos, Int> {
    fun findByCarteiraAndMoeda(carteira: Carteira, moeda: Moeda): Ativos?
}
