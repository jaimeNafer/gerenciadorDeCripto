package br.com.nafer.gerenciadorcripto.infrastructure.repository

import br.com.nafer.gerenciadorcripto.domain.model.Arquivo
import br.com.nafer.gerenciadorcripto.domain.model.enums.StatusArquivoEnum
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ArquivoRepository : JpaRepository<Arquivo, Int> {
    fun findByHashArquivo(hashArquivo: String): Arquivo?
    fun findByCarteiraIdCarteira(idCarteira: Int): List<Arquivo>
    fun findByIdArquivoAndCarteiraIdCarteira(idArquivo: Int, idCarteira: Int): Arquivo?
    @Modifying
    @Query("""update Arquivo a set a.storageKey = :storageKey where a.idArquivo = :idArquivo""")
    fun updateStorageKey(@Param(value = "idArquivo") idArquivo: Int,@Param(value = "storageKey") storageKey: String)
    @Modifying
    @Query("""update Arquivo a set a.status = :status where a.idArquivo = :idArquivo""")
    abstract fun atualizaStatus(@Param(value = "idArquivo") idArquivo: Int, @Param(value = "status") status: StatusArquivoEnum)
}
