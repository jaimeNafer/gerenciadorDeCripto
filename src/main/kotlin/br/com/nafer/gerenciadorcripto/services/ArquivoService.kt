package br.com.nafer.gerenciadorcripto.services

import br.com.nafer.gerenciadorcripto.controllers.dtos.ArquivoResponse
import br.com.nafer.gerenciadorcripto.controllers.dtos.ArquivoStatusResponse
import br.com.nafer.gerenciadorcripto.domain.mappers.ArquivoMapper
import br.com.nafer.gerenciadorcripto.domain.model.Arquivo
import br.com.nafer.gerenciadorcripto.domain.model.enums.StatusArquivoEnum
import br.com.nafer.gerenciadorcripto.exceptions.NotFoundException
import br.com.nafer.gerenciadorcripto.exceptions.UnprocessableEntityException
import br.com.nafer.gerenciadorcripto.infrastructure.repository.ArquivoRepository
import br.com.nafer.gerenciadorcripto.services.events.ArquivoCriadoEvent
import br.com.nafer.gerenciadorcripto.services.events.ArquivoExcluidoEvent
import br.com.nafer.gerenciadorcripto.utils.csvUtils.gerarHashUnico
import jakarta.transaction.Transactional
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ArquivoService(
    private val arquivoRepository: ArquivoRepository,
    private val operacaoService: OperacaoService,
    private val carteiraService: CarteiraService,
    private val arquivoMapper: ArquivoMapper,
    private val publisher: ApplicationEventPublisher
) {
    @Transactional
    fun salvar(idCarteira: Int, file: MultipartFile, separador: String): ArquivoResponse {
        validarArquivo(file)
        val hashArquivo = gerarHashUnico(file.bytes)
        validarSeArquivoJaFoiImportado(hashArquivo)
        val carteira = carteiraService.obterCarteiraOu404(idCarteira)
        val arquivo = Arquivo(carteira = carteira, nome = file.name, hashArquivo = hashArquivo, tamanhoBytes = file.size)
        val arquivoSalvo = arquivoRepository.save(arquivo)
        publisher.publishEvent(
            ArquivoCriadoEvent(
            arquivo.carteira.idCarteira!!,
            arquivo.idArquivo!!,
            file.name,
            file.bytes,
            arquivo.hashArquivo)
        )
        return arquivoMapper.toResponse(arquivoSalvo)
    }
    @Transactional
    fun processarExclusaoArquivo(idCarteira: Int, idArquivo: Int) {
        val arquivo = arquivoRepository.findByIdArquivoAndCarteiraIdCarteira(idArquivo, idCarteira)
            ?: run { throw NotFoundException("Arquivo $idArquivo não encontrado para carteira $idCarteira") }
        operacaoService.removerTodasOperacoesPorArquivo(arquivo)
        arquivoRepository.deleteById(idArquivo)
        publisher.publishEvent(ArquivoExcluidoEvent(arquivo.storageKey!!))

    }
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    fun atualizaStatus(idArquivo: Int, status: StatusArquivoEnum) {
        arquivoRepository.atualizaStatus(idArquivo, status)
    }
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    fun updateStorageKey(idArquivo: Int, storageKey: String) {
        arquivoRepository.updateStorageKey(idArquivo, storageKey)
    }
    fun obterArquivosPorIdCarteira(idCarteira: Int): List<ArquivoResponse> {
        return arquivoRepository.findByCarteiraIdCarteira(idCarteira).map { arquivoMapper.toResponse(it) }
    }
    fun obterStatus(idCarteira: Int, idArquivo: Int): ArquivoStatusResponse {
        return arquivoMapper.toStatusResponse(obterOu404(idArquivo))
    }
    fun obterOu404(idArquivo: Int): Arquivo {
        return arquivoRepository.findById(idArquivo).orElseThrow { NotFoundException("Arquivo $idArquivo não encontrado!") }
    }
    private fun validarSeArquivoJaFoiImportado(hashArquivo: String) {
        arquivoRepository.findByHashArquivo(hashArquivo)?.let {
            throw UnprocessableEntityException("Este Arquivo já foi importado!")
        }
    }
    private fun validarArquivo(file: MultipartFile) {
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
