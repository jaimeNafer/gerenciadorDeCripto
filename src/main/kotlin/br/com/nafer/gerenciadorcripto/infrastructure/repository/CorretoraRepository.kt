package br.com.nafer.gerenciadorcripto.infrastructure.repository

import br.com.nafer.gerenciadorcripto.domain.model.Corretora
import br.com.nafer.gerenciadorcripto.domain.model.enums.ExchangesEnum
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CorretoraRepository : JpaRepository<Corretora, Int> {
    fun findByNome(exchange: ExchangesEnum): Optional<Corretora?>
}
