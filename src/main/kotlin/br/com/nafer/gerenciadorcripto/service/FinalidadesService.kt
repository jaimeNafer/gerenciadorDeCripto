package br.com.nafer.gerenciadorcripto.service

import br.com.nafer.gerenciadorcripto.domain.model.Corretora
import br.com.nafer.gerenciadorcripto.domain.model.Finalidades
import br.com.nafer.gerenciadorcripto.domain.model.dtos.FinalidadesDTO
import br.com.nafer.gerenciadorcripto.domain.model.enums.FinalidadeOperacaoEnum
import br.com.nafer.gerenciadorcripto.infrastructure.repository.FinalidadeRepository
import org.springframework.stereotype.Service
import java.lang.RuntimeException

@Service
class FinalidadesService(val finalidadeRepository: FinalidadeRepository) {
    fun obterFinalidadePorCorretoraENome(corretora: Corretora, nome: FinalidadeOperacaoEnum): Finalidades {
        return finalidadeRepository.findByCorretoraAndNome(corretora, nome) ?: throw RuntimeException("")
    }
}
