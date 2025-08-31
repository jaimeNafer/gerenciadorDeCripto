package br.com.nafer.gerenciadorcripto.domain.model.enums

enum class TipoOperacaoEnum(val descricao: String) {
    DEPOSITO("Deposito"),
    COMPRA("Compra"),
    VENDA("Venda"),
    PERMUTA("Permuta"),
    SAQUE("Saque"),
    TRANSFERENCIA("Transferencia"),
    OUTROS("-")
}
