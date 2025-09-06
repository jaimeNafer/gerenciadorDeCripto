package br.com.nafer.gerenciadorcripto.infrastructure.repository

import br.com.nafer.gerenciadorcripto.domain.model.Arquivo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ArquivoRepository : JpaRepository<Arquivo, Int> {
    fun findByHashArquivo(hashArquivo: String): Arquivo?
    fun findByCarteiraIdCarteira(idCarteira: Int): List<Arquivo>
    fun findByIdArquivoAndCarteiraIdCarteira(idArquivo: Int, idCarteira: Int): Arquivo?
}
