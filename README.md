# 💻 Gerenciador de Cripto — Desktop (Compose + Spring Boot)

Projeto **desktop multiplataforma** construído com **Kotlin (JVM 21)**, **Compose Multiplatform (Desktop)** para UI e **Spring Boot 3** para *IoC*, serviços e persistência (**JPA + MySQL**).  
O objetivo deste README é **acelerar o onboarding** de quem chega agora: entender a arquitetura, onde ficam as coisas e como evoluir a aplicação com segurança.

---

## ⚙️ Stack Técnica (do `build.gradle.kts`)

**Linguagem e Build**
- Kotlin 1.9.22 (`jvmToolchain(21)`)
- Gradle + Kotlin DSL
- Kapt para _annotation processing_ (MapStruct)

**UI (Desktop)**
- **JetBrains Compose Multiplatform (Desktop)** — `org.jetbrains.compose:1.6.10`
- `compose.desktop.currentOs`
- `compose.materialIconsExtended`
- **Charts**: `com.netguru.multiplatform-charts:multiplatform-charts-desktop:1.0.0`

**Backend/Infra (no mesmo processo Desktop)**
- Spring Boot 3.2.5
   - `spring-boot-starter`
   - `spring-boot-starter-web`
   - `spring-boot-starter-data-jpa`
   - `spring-boot-starter-cache` + **Caffeine**
- DB: `mysql:mysql-connector-java:8.0.33`
- **Jackson Kotlin**
- **MapStruct 1.5.5.Final**
- **OpenCSV 5.11**

**Distribuição**
- `compose.desktop.application.nativeDistributions` → gera **.dmg**, **.msi**, **.deb**

> **mainClass**: `MainKt`

---

## 🏗️ Arquitetura em Camadas

```
[ UI (Compose) ]
      ↓
[ ViewModel / State ]
      ↓
[ Services (Spring) ]
      ↓
[ Domain (Entities / DTOs / Mappers) ]
      ↓
[ Repositories (Spring Data JPA) ]
      ↓
[ Database (MySQL) ]
```

- **UI (Compose)**: telas, componentes visuais, ícones, temas.
- **ViewModel**: orquestra interação da UI com serviços.
- **Service (Spring)**: regras de negócio.
- **Domain**: entidades JPA, DTOs, enums e mappers.
- **Repository**: persistência com Spring Data JPA.

---

## 🗂️ Estrutura de Pastas

```
br/com/nafer/gerenciadorcripto/
├── clients/                 # Clientes HTTP ou integrações externas
├── configs/                 # Beans Spring, configs (cache, jackson, etc.)
├── domain/
│   ├── mappers/             # MapStruct mappers
│   └── model/
│       ├── dtos/            # DTOs de entrada/saída
│       └── enums/           # Enumerações de domínio
├── dtos/
│   └── binance/             # DTOs específicos da Binance
├── infrastructure/
│   └── repository/          # Repositórios JPA
├── navigation/              # Navegação entre telas (Compose)
├── service/                 # Regras de negócio (Spring @Service)
├── ui/
│   ├── components/          # Componentes reutilizáveis
│   ├── screens/             # Telas (Dashboard, Carteiras, etc.)
│   └── viewmodel/           # ViewModels/States
└── utils/
    └── csvUtils/            # Parsing e exportação de CSV
```

---

## 🗺️ Arquitetura Visual

![Arquitetura](..\gerenciadorDeCripto\architecture_diagram.png)

---

## 🧭 Onboarding Rápido

1. **Clonar repositório**
2. **Abrir no IntelliJ IDEA** (Gradle Kotlin DSL).
3. **Configurar DB MySQL** e `application.yml`.
4. **Rodar** com `./gradlew run` (UI+Spring) ou `MainKt`.
5. **Build instaladores** com `./gradlew createDistributable`.

---

## 🧰 Boas Práticas

- ViewModel expõe `State` imutável.
- Sem lógica de negócio na UI.
- MapStruct para conversões.
- Spring Cache (Caffeine) para leituras pesadas.
- Centralizar CSV em `utils/csvUtils`.

---

## 🛠️ Tarefas Gradle

```bash
./gradlew run                # Rodar UI+Spring
./gradlew bootRun            # Rodar backend isolado
./gradlew createDistributable # Gerar .dmg/.msi/.deb
./gradlew test               # Rodar testes
./gradlew clean              # Limpar build
```

---

## 🧭 Roadmap de docs

- `docs/architecture.md`
- `docs/data-model.md`
- `docs/import-csv.md`
- `docs/caching.md`
- `docs/release.md`