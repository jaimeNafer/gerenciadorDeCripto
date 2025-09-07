package br.com.nafer.gerenciadorcripto.configs

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Gerenciador de Criptomoedas API")
                    .description("API REST para gerenciamento de carteiras de criptomoedas")
                    .version("1.0.0")
                    .contact(
                        Contact()
                            .name("Equipe de Desenvolvimento")
                            .email("dev@nafer.com.br")
                    )
                    .license(
                        License()
                            .name("MIT License")
                            .url("https://opensource.org/licenses/MIT")
                    )
            )
            .addServersItem(
                Server()
                    .url("http://localhost:8080")
                    .description("Servidor de Desenvolvimento")
            )
    }
}
