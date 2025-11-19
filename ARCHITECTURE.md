# Arquitetura do Projeto - Pong Game

## 📋 Visão Geral

Jogo Pong implementado em **Java 17** com **Swing** (interface gráfica) e **PostgreSQL** (persistência de jogos salvos).

O projeto está organizado em pacotes com responsabilidades bem definidas, separando lógica do jogo, interface, persistência e entrada de dados.

---

## 📂 Estrutura e Responsabilidades

### **`Main.java`**

Ponto de entrada da aplicação. Carrega a fonte customizada e inicia a janela principal (`GameFrame`).

---

### **`application/`** - Camada de Serviços

#### `services/GameStateService.java`

Gerencia o salvamento e carregamento de jogos.

- Converte entidades do jogo em modelos de banco de dados
- Salva estado completo (raquetes, bola, pontuação) no PostgreSQL
- Carrega jogos salvos e restaura o estado

---

### **`constants/`** - Constantes do Projeto

Centraliza valores fixos usados em todo o projeto:

- **`GameConstants.java`**: Velocidades, tamanhos, FPS, pontuação máxima
- **`GameState.java`**: Estados do jogo (PLAYING, PAUSED, GAME_OVER, STOPPED)
- **`InputConstants.java`**: Mapeamento de teclas (W/S para jogador 1, setas para jogador 2)
- **`UIConstants.java`**: Cores, fontes, tamanhos de botões

---

### **`core/`** - Motor do Jogo

#### `engine/GameLoop.java`

Thread que executa o loop principal do jogo.

- Atualiza estado das entidades (60x por segundo)
- Solicita redesenho da tela
- Gerencia timing com delta time

#### `collision/CollisionDetector.java`

Detecta colisões entre objetos do jogo.

- Verifica intersecção entre retângulos
- Notifica objetos quando colidem

#### `collision/CollisionObserver.java`

Interface para objetos que precisam reagir a colisões (ex: bola).

---

### **`domain/`** - Regras de Negócio

#### `entities/` - Objetos do Jogo

- **`GameObject.java`**: Classe base abstrata com posição, tamanho e métodos `update()` e `render()`
- **`Ball.java`**: Bola que se move automaticamente, rebate nas bordas e aumenta velocidade a cada colisão
- **`Paddle.java`**: Raquete controlada pelo jogador, responde a input do teclado
- **`FieldLine.java`**: Linha pontilhada central decorativa

#### `managers/ScoreManager.java`

Gerencia pontuação dos dois jogadores.

- Incrementa pontos quando bola sai pela lateral
- Verifica condição de vitória
- Renderiza placar na tela

---

### **`infrastructure/`** - Infraestrutura

#### `ui/` - Interface Gráfica

**`GameFrame.java`**: Janela principal do jogo

- Alterna entre menu e tela de jogo
- Gerencia ciclo de vida dos painéis

**`panels/MenuPanel.java`**: Tela inicial

- Botões: JOGAR, CARREGAR, SAIR
- Lista jogos salvos disponíveis

**`panels/GamePanel.java`**: Tela onde o jogo acontece

- Inicializa e desenha todas as entidades
- Gerencia botões in-game (PAUSE, SAVE, LOAD, MENU)
- Controla estados do jogo

**`panels/SaveGameDialog.java`**: Dialog para selecionar jogo salvo

**`factories/ButtonFactory.java`**: Cria botões estilizados com aparência consistente

**`utils/FontUtils.java`**: Carrega fonte customizada do projeto

#### `persistence/` - Persistência de Dados

**`models/`**: Classes que representam as tabelas do banco

- `BallModel`, `PaddleModel`, `ScoreManagerModel`, `MatchModel`
- Anotadas com `@Table`, `@Column`, `@ForeignKey`

**`repositories/CRUDRepository.java`**: Realiza operações no banco

- `create()`, `read()`, `findAll()` para qualquer modelo
- Gera SQL dinamicamente baseado nas anotações

**`repositories/annotations/`**: Anotações customizadas para mapear classes → tabelas

**`repositories/connection/`**: Gerencia conexão com PostgreSQL

**`repositories/mapper/`**: Converte ResultSet (resultado de query) → objeto Java

**`repositories/metadata/`**: Extrai informações das classes anotadas (nome da tabela, campos, chaves)

**`repositories/schema/`**: Cria/deleta estrutura do banco

- `SchemaManager`: Executa criação de todas as tabelas
- `DependencyAnalyzer`: Ordena tabelas por dependência (foreign keys)

**`repositories/sql/`**: Gera comandos SQL

- `DDLGenerator`: CREATE/DROP TABLE
- `DMLGenerator`: INSERT/SELECT/UPDATE/DELETE

---

### **`input/`** - Entrada de Dados

#### `InputObserver.java`

Interface para objetos que respondem a teclas pressionadas.

#### `handlers/KeyboardHandler.java`

Captura eventos de teclado e notifica os observers (raquetes).

#### `handlers/GameShortcuts.java`

Processa atalhos globais (tecla ESC para sair).

---

## 💡 Lógica Principal

### Como o Jogo Funciona

**1. Loop do Jogo** (`core/engine/GameLoop.java`)

- Thread separada que roda continuamente em 60 FPS
- A cada frame: atualiza posições → detecta colisões → redesenha tela

**2. Detecção de Colisão** (`core/collision/CollisionDetector.java`)

- Verifica se a bola tocou nas raquetes usando intersecção de retângulos
- Quando há colisão, inverte a direção da bola e aumenta sua velocidade

**3. Entrada do Usuário** (`input/handlers/KeyboardHandler.java`)

- Captura teclas pressionadas (W/S e setas)
- Notifica as raquetes para moverem para cima ou para baixo

**4. Sistema de Pontuação** (`domain/managers/ScoreManager.java`)

- Incrementa pontos quando a bola sai pela lateral
- Reseta a bola no centro após cada ponto
- Verifica se algum jogador atingiu a pontuação máxima (vitória)

**5. Salvamento de Jogo** (`application/services/GameStateService.java`)

- Converte estado atual (posições, velocidades, pontos) em modelos de banco
- Salva em 4 tabelas relacionadas: `paddles`, `balls`, `score_manager`, `matches`
- Carrega jogos salvos e restaura exatamente o estado anterior

---

## 🗂️ Estrutura do Banco de Dados

O projeto usa um mini-ORM customizado (similar ao Hibernate) que cria automaticamente as tabelas baseado em anotações nas classes.

### Tabelas Criadas

```sql
paddles (id, x, y, up_key, down_key)
    ↑                           ↑
    │                           │
matches (id, left_paddle_id, right_paddle_id, ball_id, score_manager_id)
    │                           │
    ↓                           ↓
balls (id, x, y, velocity_x, velocity_y)
score_manager (id, left_score, right_score, winning_score)
```

**Como funciona:**

- Classes anotadas com `@Table`, `@Column`, `@ForeignKey`

- `SchemaManager` lê as anotações e gera SQL automaticamente
- `DependencyAnalyzer` ordena criação de tabelas (foreign keys primeiro)
- `CRUDRepository` realiza operações no banco sem escrever SQL manualmente

---
