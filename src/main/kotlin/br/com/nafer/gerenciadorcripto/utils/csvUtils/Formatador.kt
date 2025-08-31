import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object Formatador {

    private val simbolos = DecimalFormatSymbols(Locale("pt", "BR")).apply {
        decimalSeparator = ','
        groupingSeparator = '.'
    }

    private val formatoMoeda = DecimalFormat("¤ #,##0.00", simbolos).apply {
        currency = Currency.getInstance("BRL")
    }

    private val formatoQuantidade = DecimalFormat("#,##0.########", simbolos)

    private val formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

    fun formatarData(data: LocalDateTime?): String {
        return data?.format(formatterData) ?: ""
    }

    fun formatarMoeda(valor: BigDecimal?): String {
        if (valor == null) return ""
        return if (valor.abs() < BigDecimal("0.01") && valor.signum() != 0) {
            "R$ " + valor.setScale(10, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString().replace('.', ',')
        } else {
            formatoMoeda.format(valor)
        }
    }

    fun formatarQuantidade(valor: BigDecimal?): String {
        if (valor == null) return "-"
        return formatoQuantidade.format(valor.stripTrailingZeros())
    }
}
