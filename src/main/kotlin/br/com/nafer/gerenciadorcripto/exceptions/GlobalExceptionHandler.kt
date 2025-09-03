package br.com.nafer.gerenciadorcripto.exceptions

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import java.net.URI
import java.time.Instant

@ControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(
        ex: NotFoundException,
        request: WebRequest
    ): ResponseEntity<ProblemDetail> {
        logger.error("Entidade não encontrada: ${ex.message}", ex)
        
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.message ?: "Entidade não encontrada")
        problemDetail.type = URI.create("/errors/not-found")
        problemDetail.title = "Entidade Não Encontrada"
        problemDetail.setProperty("timestamp", Instant.now())
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(
        ex: MethodArgumentNotValidException,
        request: WebRequest
    ): ResponseEntity<ProblemDetail> {
        logger.warn("Erro de validação: ${ex.message}")
        
        val errors = ex.bindingResult.fieldErrors.associate { 
            it.field to (it.defaultMessage ?: "Valor inválido")
        }
        
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Dados de entrada inválidos")
        problemDetail.type = URI.create("/errors/validation-error")
        problemDetail.title = "Erro de Validação"
        problemDetail.setProperty("timestamp", Instant.now())
        problemDetail.setProperty("errors", errors)
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(
        ex: Exception,
        request: WebRequest
    ): ResponseEntity<ProblemDetail> {
        logger.error("Erro interno do servidor: ${ex.message}", ex)
        
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor")
        problemDetail.type = URI.create("/errors/internal-server-error")
        problemDetail.title = "Erro Interno"
        problemDetail.setProperty("timestamp", Instant.now())
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail)
    }
}