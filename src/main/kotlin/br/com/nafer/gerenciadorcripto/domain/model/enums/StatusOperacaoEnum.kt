package br.com.nafer.gerenciadorcripto.domain.model.enums

enum class StatusOperacaoEnum(status: String) {
    PROCESSADA("Processada"),
    PENDENTE("Pendente"),
    ERRO("Erro"),
    NAO_MAPEADA("Não Mapeada"),
    REPROCESSAR("Reprocessar")
}
