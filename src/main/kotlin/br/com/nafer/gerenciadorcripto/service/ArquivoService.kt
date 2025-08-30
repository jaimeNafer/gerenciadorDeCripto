package br.com.nafer.gerenciadorcripto.service

import br.com.nafer.gerenciadorcripto.domain.mappers.ArquivoMapper
import br.com.nafer.gerenciadorcripto.domain.model.Arquivo
import br.com.nafer.gerenciadorcripto.domain.model.Corretora
import br.com.nafer.gerenciadorcripto.domain.model.Usuario
import br.com.nafer.gerenciadorcripto.dtos.binance.ArquivoDTO
import br.com.nafer.gerenciadorcripto.infrastructure.repository.ArquivoRepository
import br.com.nafer.gerenciadorcripto.utils.csvUtils.gerarHasUnico
import org.springframework.stereotype.Service

@Service
class ArquivoService(
    private val arquivoRepository: ArquivoRepository,
    private val arquivoMapper: ArquivoMapper
) {
    fun salvar(usuario: Usuario, corretora: Corretora, arquivoDTO: ArquivoDTO): Arquivo {
        val hashArquivo = gerarHasUnico(arquivoDTO.conteudo)
        val arquivoSalvo = arquivoRepository.findByHashArquivo(hashArquivo)
        validarSeArquivoExiste(arquivoSalvo)
        val arquivoNovo = arquivoMapper.toEntity(usuario, corretora, arquivoDTO, hashArquivo)
        return arquivoRepository.save(arquivoNovo)
    }

    fun listar(): List<ArquivoDTO> {
        return arquivoRepository.findAll().map { arquivoMapper.toDTO(it) }
    }

    fun obter(arquivo: ArquivoDTO): Arquivo {
        if (arquivo.idArquivo != null){
            return arquivoRepository.findById(arquivo.idArquivo).orElseThrow { RuntimeException("Arquivo não encontrado!") }
        }
        throw RuntimeException("idArquivo não pode ser nulo!")
    }

    fun deletar(arquivo: Arquivo) {
        arquivoRepository.delete(arquivo)
    }

    private fun validarSeArquivoExiste(arquivo: Arquivo?) {
        if(arquivo != null) {
            throw RuntimeException("Este Arquivo já foi importado!")
        }
    }
}
