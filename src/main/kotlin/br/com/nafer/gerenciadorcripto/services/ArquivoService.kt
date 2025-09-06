package br.com.nafer.gerenciadorcripto.services

import br.com.nafer.gerenciadorcripto.controllers.dtos.ArquivoResponse
import br.com.nafer.gerenciadorcripto.domain.mappers.ArquivoMapper
import br.com.nafer.gerenciadorcripto.domain.model.Arquivo
import br.com.nafer.gerenciadorcripto.dtos.binance.ArquivoDTO
import br.com.nafer.gerenciadorcripto.exceptions.NotFoundException
import br.com.nafer.gerenciadorcripto.exceptions.UnprocessableEntityException
import br.com.nafer.gerenciadorcripto.infrastructure.repository.ArquivoRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ArquivoService(
    private val arquivoRepository: ArquivoRepository,
    private val operacaoService: OperacaoService,
    private val carteiraService: CarteiraService,
    private val arquivoMapper: ArquivoMapper
) {
    @Transactional
    fun salvar(idCarteira: Int, file: MultipartFile, separador: String) {
        validar(file)
        val hashArquivo = Arquivo.gerarHashUnico(file.bytes)
        validarSeArquivoJaFoiImportado(hashArquivo)
        val carteira = carteiraService.obterCarteiraOu404(idCarteira)
        val arquivo = Arquivo(carteira = carteira, nome = file.name, hashArquivo = hashArquivo)
        val arquivoSalvo = arquivoRepository.save(arquivo)
        operacaoService.criarOperacoesPorArquivo(arquivoSalvo, file)
    }
    @Transactional
    fun processarExclusaoArquivo(idCarteira: Int, idArquivo: Int) {
        val arquivo = arquivoRepository.findByIdArquivoAndCarteiraIdCarteira(idArquivo, idCarteira)
            ?: run { throw NotFoundException("Arquivo $idArquivo não encontrado para carteira $idCarteira") }
        operacaoService.removerTodasOperacoesPorArquivo(arquivo)
        arquivoRepository.deleteById(idArquivo)
    }
    fun obterArquivosPorIdCarteira(idCarteira: Int): List<ArquivoResponse> {
        return arquivoRepository.findByCarteiraIdCarteira(idCarteira).map { arquivoMapper.toResponse(it) }
    }
    fun obter(arquivo: ArquivoDTO): Arquivo {
        return arquivoRepository.findById(arquivo.idArquivo).orElseThrow { RuntimeException("Arquivo não encontrado!") }
    }

    private fun validarSeArquivoJaFoiImportado(hashArquivo: String) {
        arquivoRepository.findByHashArquivo(hashArquivo)?.let {
            throw UnprocessableEntityException("Este Arquivo já foi importado!")
        }
    }

    fun validar(file: MultipartFile) {
        if(file.isEmpty){
            throw UnprocessableEntityException("Arquivo está vazio")
        }
        val contentTypeOk = when (file.contentType?.lowercase()) {
            "text/csv", "application/csv", "text/plain", "application/vnd.ms-excel" -> true
            else -> file.originalFilename?.endsWith(".csv", ignoreCase = true) == true
        }
        if (!contentTypeOk) {
            throw UnprocessableEntityException("Apenas arquivos .csv são aceitos")
        }
    }

}
