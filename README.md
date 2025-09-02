# ProgressoFit - Backend
### Controle seu treino. Veja seu progresso. Treine com inteligência.

_**Projeto acadêmico em desenvolvimento*_

Nosso projeto é desenvolver um site que ofereça uma ferramenta simples e poderosa para quem deseja monitorar e melhorar seus treinos. A plataforma permitirá que os usuários registrem diariamente exercícios, cargas, tempos, sensações e outras métricas, transformando esses dados em relatórios e gráficos de evolução claros e fáceis de interpretar. Além do histórico detalhado, o site poderá, mediante integração com inteligência artificial (por exemplo, ChatGPT), gerar mensagens motivacionais e recomendações de treino personalizadas com base no perfil e no desempenho de cada usuário. Tudo pensado para tornar o acompanhamento mais objetivo, motivador e eficiente.

**Desenvolvedores**
- Vinícius Menezes Pontes
- Matheus Aquino de Andrade
- Isabela Soares dos Santos
- Juan Pablo Lima Rassi

## 🚀 Tecnologias Utilizadas

- **Backend**: Java 17 + Spring Boot
- **Banco de Dados**: PostgreSQL
- **Containerização**: Docker & Docker Compose
- **Build Tool**: Gradle

## 📋 Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

- ☕ **Java 17** ou superior
- 🐳 **Docker** e **Docker Compose**
- 🔧 **Git**

### Verificando as versões instaladas:

```bash
# Verificar versão do Java
java -version

# Verificar versão do Docker
docker --version

# Verificar versão do Docker Compose
docker-compose --version
```

## 🛠️ Como executar o projeto

### 1. Clone o repositório
```bash
gh repo clone ViniciusMPonte/ProgressoFit-Backend
cd ProgressoFit-Backend
```

### 2. Inicie os serviços com Docker
```bash
docker-compose up
```
Este comando irá:
- Configurar todas as dependências necessárias
- Inicializar o banco de dados PostgreSQL e deixar pronto para conexão

### 3. Execute a aplicação Spring Boot
```bash
./gradlew bootRun
```

### 4. Acesse a aplicação
A aplicação estará disponível em: `http://localhost:8090`
