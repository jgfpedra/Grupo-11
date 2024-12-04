# Projeto Final MC322 - Jogo de Xadrez
# Grupo-11

## Descrição
O **Projeto Final MC322** é um jogo de **xadrez** desenvolvido como parte de um projeto acadêmico para a disciplina de MC322. O jogo foi projetado para permitir partidas **online** e **locais**, com mecânicas que imitam as regras clássicas do xadrez, mas com uma interface moderna e funcional. O sistema oferece suporte tanto para partidas entre dois jogadores locais quanto para partidas online, onde as coordenadas do tabuleiro podem ser invertidas, proporcionando uma dinâmica diferenciada para cada situação.

### Funcionalidades:
- **Partidas Locais e Online**: O jogo oferece suporte para partidas entre dois jogadores na mesma máquina, bem como para partidas online.
- **Inversão de Coordenadas**: Em partidas online, as coordenadas do tabuleiro são invertidas, permitindo que o jogador tenha sempre a visão de sua perspectiva.
- **Tabuleiro Interativo**: O tabuleiro é composto por casas que contêm as peças, em vez de associar as peças a posições fixas. Isso oferece maior flexibilidade no controle do estado do jogo.
- **Controle de Tempo**: O jogo conta com um temporizador para cada jogador, que pode ser pausado ou interrompido conforme necessário.
- **Interface Gráfica**: Desenvolvido com JavaFX, o jogo oferece uma interface gráfica amigável e interativa, com a possibilidade de exibir o estado do jogo, o turno atual e interagir com as peças.

### Estrutura do Jogo:
- **Tabuleiro**: O tabuleiro é uma matriz de casas, cada uma contendo uma peça (ou estando vazia). A movimentação das peças é gerida de forma intuitiva, com regras que respeitam as normas tradicionais do xadrez.
- **Partida**: O modelo de partida inclui um controle de turnos e de tempo, além de fornecer a lógica para determinar se a partida terminou (por xeque-mate, empate, etc.).
- **Menu de Controle**: O jogo possui menus interativos que permitem ao jogador pausar a partida, exibir opções e configurar preferências, como a inversão das coordenadas.
- **Jogador 2**: A funcionalidade `getIsJogador2()` identifica qual jogador está atualmente realizando a jogada, especialmente útil para determinar o controle do jogo.

### Tecnologias Utilizadas:
- **Java**: Linguagem de programação principal para o desenvolvimento do jogo.
- **JavaFX**: Framework gráfica utilizada para criar a interface de usuário interativa.
- **MVC (Model-View-Controller)**: Arquitetura de design aplicada para separar a lógica de negócios (modelo), a interface de usuário (visão) e a manipulação dos eventos (controle), permitindo uma manutenção mais fácil e escalabilidade.

### Como Jogar:
1. **Iniciar o Jogo**: Ao iniciar a aplicação, a tela inicial do jogo será exibida. A partir daí, você pode escolher entre uma partida local ou online.
2. **Movimentação das Peças**: Cada jogador pode mover suas peças no tabuleiro conforme as regras tradicionais de xadrez.
3. **Controle de Tempo**: Cada jogador possui um temporizador que é iniciado ao fazer sua jogada. O tempo é contado para cada jogador individualmente.
4. **Final do Jogo**: O jogo termina quando um dos jogadores sofre xeque-mate ou o jogo é declarado empatado por outras razões.

### Exemplo de Código:
O código foi desenvolvido utilizando uma abordagem modular, com classes para gerenciar as peças, o tabuleiro, os jogadores, o controle de tempo e a interface gráfica.

```java
// Exemplo de código para controle de tempo
public void pararTimer() {
    if (timeline != null) {
        timeline.stop();
    }
}

```markdown
## Como Executar o Jogo:

```markdown
1. **Clone o Repositório**:  
   Faça o clone deste repositório para o seu ambiente local. Você pode fazer isso usando o seguinte comando no terminal:
   ```bash
   git clone https://github.com//projeto-final-mc322.git

```markdown
2. **Compile o Código**:  
   Para compilar o código, você pode usar uma IDE Java, como **IntelliJ IDEA** ou **Eclipse**, que irão gerenciar a compilação automaticamente. Caso prefira compilar manualmente via linha de comando, siga os passos abaixo:

   - Abra o terminal ou prompt de comando.
   - Navegue até o diretório raiz do projeto (onde está o arquivo `Main.java`).
   - Execute o seguinte comando para compilar o código:
   
   ```bash
   javac -d bin src/xadrez/Main.java

```markdown
3. **Execute a Aplicação**:  
   Após a compilação, você pode executar a aplicação para iniciar o jogo. Se estiver utilizando uma IDE como **IntelliJ IDEA** ou **Eclipse**, basta executar a classe principal diretamente pela interface da IDE.

   Caso prefira executar pela linha de comando, faça o seguinte:

   - Abra o terminal ou prompt de comando.
   - Navegue até o diretório onde os arquivos compilados estão localizados (a pasta `bin`).
   - Execute o seguinte comando para rodar o jogo:

   ```bash
   java -cp bin xadrez.Main

```markdown
4. **Interface Gráfica**"
   Ao executar a aplicação, a interface gráfica será carregada automaticamente. Na tela inicial, você poderá escolher entre as opções de **partida local** ou **online**. 

   A partir daí, o jogo estará pronto para ser jogado, com controle de tempo para cada jogador, tabuleiro interativo e todas as funcionalidades que o projeto oferece.

   Caso queira sair do jogo ou acessar o menu de controle, você pode usar os botões e menus disponíveis na interface gráfica.

## Conclusão

Este projeto oferece uma experiência de jogo de xadrez com uma interface gráfica interativa e moderna, utilizando **Java** e **JavaFX**. O jogo é projetado para suportar tanto partidas locais quanto online, com funcionalidades como controle de tempo e inversão de coordenadas em partidas online.

A arquitetura foi pensada para garantir uma boa organização e escalabilidade, utilizando o padrão **MVC (Model-View-Controller)**

