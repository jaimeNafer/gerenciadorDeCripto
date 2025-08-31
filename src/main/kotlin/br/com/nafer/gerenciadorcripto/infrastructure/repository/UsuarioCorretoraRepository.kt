package br.com.nafer.gerenciadorcripto.infrastructure.repository

import br.com.nafer.gerenciadorcripto.domain.model.UsuarioCorretora
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UsuarioCorretoraRepository : JpaRepository<UsuarioCorretora, Int>
