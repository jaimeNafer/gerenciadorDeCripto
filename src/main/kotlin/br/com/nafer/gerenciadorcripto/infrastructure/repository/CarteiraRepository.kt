package br.com.nafer.gerenciadorcripto.infrastructure.repository

import br.com.nafer.gerenciadorcripto.domain.model.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CarteiraRepository : JpaRepository<Carteira, Int> {
    @Query("SELECT c FROM Carteira c WHERE c.usuario IN :usuarios AND c.corretora IN :corretoras")
    fun filtrarCarteiraPorUsuariosECorretoras(
        @Param("usuarios") usuarios: List<Usuario>?,
        @Param("corretoras") corretoras: List<Corretora>?
    ): List<Carteira>?

    @Query("SELECT c FROM Carteira c WHERE c.usuario = :usuario AND c.corretora = :corretora")
    fun filtrarCarteiraPorUsuarioECorretora(
        @Param("usuario") usuario: Usuario,
        @Param("corretora") corretora: Corretora
    ): Carteira?

    @Query("SELECT c FROM Carteira c WHERE c.usuario IN :usuarios")
    fun filtrarAtivosPorUsuarios(@Param("usuarios") usuarios: List<Usuario>?): List<Carteira>?

    @Query("SELECT c FROM Carteira c WHERE c.corretora IN :corretoras")
    fun filtrarAtivosPorCorretoras(@Param("corretoras") corretoras: List<Corretora>?): List<Carteira>?
    fun findByTicker(moeda: Moeda): Carteira?
}
