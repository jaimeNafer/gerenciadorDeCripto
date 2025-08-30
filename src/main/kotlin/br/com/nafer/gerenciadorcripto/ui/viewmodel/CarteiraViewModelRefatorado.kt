package br.com.nafer.gerenciadorcripto.ui.viewmodel

import br.com.nafer.gerenciadorcripto.domain.model.Ativos
import br.com.nafer.gerenciadorcripto.domain.model.Carteira
import br.com.nafer.gerenciadorcripto.service.CarteiraService
import br.com.nafer.gerenciadorcripto.ui.base.BaseViewModel
import br.com.nafer.gerenciadorcripto.ui.base.ServiceAdapter
import br.com.nafer.gerenciadorcripto.ui.base.UiState
import br.com.nafer.gerenciadorcripto.ui.screens.AtivoCarteiraDTO
import br.com.nafer.gerenciadorcripto.ui.screens.HistoricoMensalDTO
import br.com.nafer.gerenciadorcripto.ui.screens.PatrimonioDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Eventos que podem ser disparados na tela de carteira
 */
sealed class CarteiraEvent {
    /** Carrega os dados da carteira */
    object LoadCarteira : CarteiraEvent()
    
    /** Filtra os ativos da carteira */
    data class FilterAtivos(val query: String) : CarteiraEvent()
    
    /** Navega para a tela de nova operação para um ativo específico */
    data class NavigateToNovaOperacao(val ativo: AtivoCarteiraDTO) : CarteiraEvent()
    
    /** Navega para a tela de visualização de operações de um ativo específico */
    data class NavigateToVerOperacoes(val ativo: AtivoCarteiraDTO) : CarteiraEvent()
}

/**
 * Estado da UI da tela de carteira
 */
data class CarteiraUiState(
    val patrimonio: PatrimonioDTO = PatrimonioDTO(
        valorInvestido = BigDecimal.ZERO,
        valorAtual = BigDecimal.ZERO,
        lucroPrejuizo = BigDecimal.ZERO,
        percentualVariacao = BigDecimal.ZERO
    ),
    val historico: List<HistoricoMensalDTO> = emptyList(),
    val ativos: List<AtivoCarteiraDTO> = emptyList(),
    val filtroAtivos: String = ""
)

/**
 * ViewModel para a tela de carteira
 */
@Component
class CarteiraViewModel(
    private val carteiraService: CarteiraService
) : BaseViewModel<CarteiraUiState, CarteiraEvent>() {
    
    // Estado local da carteira
    private val _carteiraState = MutableStateFlow(CarteiraUiState())
    val carteiraState: StateFlow<CarteiraUiState> = _carteiraState.asStateFlow()
    
    // Estado filtrado dos ativos
    private val _filteredAtivos = MutableStateFlow<UiState<List<AtivoCarteiraDTO>>>(UiState.Loading)
    val filteredAtivos: StateFlow<UiState<List<AtivoCarteiraDTO>>> = _filteredAtivos.asStateFlow()
    
    // Callbacks para navegação
    var onNavigateToNovaOperacao: ((ticker: String) -> Unit)? = null
    var onNavigateToVerOperacoes: ((ticker: String) -> Unit)? = null
    
    /**
     * Processa eventos da tela de carteira
     */
    override fun processEvent(event: CarteiraEvent) {
        when (event) {
            is CarteiraEvent.LoadCarteira -> loadCarteira()
            is CarteiraEvent.FilterAtivos -> filterAtivos(event.query)
            is CarteiraEvent.NavigateToNovaOperacao -> {
                onNavigateToNovaOperacao?.invoke(event.ativo.ticker)
            }
            is CarteiraEvent.NavigateToVerOperacoes -> {
                onNavigateToVerOperacoes?.invoke(event.ativo.ticker)
            }
        }
    }
    
    /**
     * Carrega os dados da carteira
     */
    private fun loadCarteira() {
        launch {
            // Adapta a chamada do serviço para Flow<UiState>
            ServiceAdapter.adapt {
                carteiraService.obterCarteira()
            }.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        val carteiras = state.data
                        processCarteiras(carteiras)
                    }
                    is UiState.Loading -> {
                        // Mantém o estado de loading
                    }
                    is UiState.Error -> {
                        // Atualiza o estado com erro
                    }
                    is UiState.Empty -> {
                        // Atualiza o estado com vazio
                    }
                }
            }
        }
    }
    
    /**
     * Processa as carteiras retornadas pelo serviço
     */
    private fun processCarteiras(carteiras: List<Carteira>?) {
        if (carteiras.isNullOrEmpty()) return
        
        // Aqui seria necessário processar os dados das carteiras para gerar
        // o patrimônio, histórico e ativos
        // Este é um exemplo simplificado
        
        val ativos = mutableListOf<AtivoCarteiraDTO>()
        val historico = mutableListOf<HistoricoMensalDTO>()
        var valorInvestidoTotal = BigDecimal.ZERO
        var valorAtualTotal = BigDecimal.ZERO
        
        // Processa os ativos de todas as carteiras
        carteiras.forEach { carteira ->
            val ativosCarteira = carteiraService.obterAtivosPorCarteira(carteira)
            ativosCarteira.forEach { ativo ->
                ativos.add(converterParaAtivoCarteiraDTO(ativo))
                valorInvestidoTotal = valorInvestidoTotal.add(ativo.valorInvestido)
                valorAtualTotal = valorAtualTotal.add(calcularValorAtual(ativo))
            }
        }
        
        // Calcula o lucro/prejuízo e percentual de variação
        val lucroPrejuizo = valorAtualTotal.subtract(valorInvestidoTotal)
        val percentualVariacao = if (valorInvestidoTotal > BigDecimal.ZERO) {
            lucroPrejuizo.divide(valorInvestidoTotal, 4, RoundingMode.HALF_UP).multiply(BigDecimal("100"))
        } else {
            BigDecimal.ZERO
        }
        
        // Atualiza o estado da UI
        _carteiraState.update { currentState ->
            currentState.copy(
                patrimonio = PatrimonioDTO(
                    valorInvestido = valorInvestidoTotal,
                    valorAtual = valorAtualTotal,
                    lucroPrejuizo = lucroPrejuizo,
                    percentualVariacao = percentualVariacao
                ),
                historico = historico,
                ativos = ativos
            )
        }
        
        // Atualiza os ativos filtrados
        filterAtivos(_carteiraState.value.filtroAtivos)
    }
    
    /**
     * Filtra os ativos da carteira
     */
    private fun filterAtivos(query: String) {
        _carteiraState.update { it.copy(filtroAtivos = query) }
        
        val filteredList = if (query.isBlank()) {
            _carteiraState.value.ativos
        } else {
            _carteiraState.value.ativos.filter { ativo ->
                ativo.ticker.contains(query, ignoreCase = true) ||
                ativo.nome.contains(query, ignoreCase = true)
            }
        }
        
        _filteredAtivos.value = if (filteredList.isEmpty()) {
            UiState.Empty
        } else {
            UiState.Success(filteredList)
        }
    }
    
    /**
     * Converte um ativo do modelo de domínio para DTO da UI
     */
    private fun converterParaAtivoCarteiraDTO(ativo: Ativos): AtivoCarteiraDTO {
        return AtivoCarteiraDTO(
            ticker = ativo.moeda.ticker,
            nome = ativo.moeda.nome!!,
            icone = ativo.moeda.icone ?: "",
            quantidade = ativo.quantidade,
            valorAtual = calcularValorAtual(ativo),
            valorInvestido = ativo.valorInvestido,
            precoMedio = ativo.precoMedio,
            lucroPrejuizo = ativo.lucroPrejuizo
        )
    }
    
    /**
     * Calcula o valor atual de um ativo
     * Em uma implementação real, isso poderia consultar uma API de preços
     */
    private fun calcularValorAtual(ativo: Ativos): BigDecimal {
        // Aqui seria necessário obter o preço atual da moeda
        // Este é um exemplo simplificado que usa o preço médio
        return ativo.quantidade.multiply(ativo.precoMedio)
    }
}