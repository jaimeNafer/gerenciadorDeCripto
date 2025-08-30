package br.com.nafer.gerenciadorcripto.dtos.binance

import com.opencsv.bean.CsvBindByName
import com.opencsv.bean.CsvDate
import java.math.BigDecimal
import java.time.LocalDateTime

open class CsvBinanceDTO {
    @CsvBindByName(column = "User_ID")
    lateinit var userId: String

    @CsvDate("yyyy-MM-dd HH:mm:ss")
    @CsvBindByName(column = "UTC_Time")
    lateinit var utcTime: LocalDateTime

    @CsvBindByName(column = "Account")
    lateinit var account: String

    @CsvBindByName(column = "Operation")
    lateinit var operation: String

    @CsvBindByName(column = "Coin")
    lateinit var coin: String

    @CsvBindByName(column = "Change")
    lateinit var change: BigDecimal

    @CsvBindByName(column = "Remark")
    lateinit var remark: String
}
