package UI.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import partida.Partida;
import partida.Posicao;
import partida.Tabuleiro;
import pecas.Peca;
import java.util.function.BiConsumer;

import UI.controle.MenuControle;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A classe `TabuleiroView` é responsável por exibir e gerenciar a interface gráfica
 * do tabuleiro de xadrez em uma aplicação JavaFX. Ela lida com a representação visual
 * do tabuleiro, as peças, o temporizador da partida e a interação com o jogador.
 * A classe também permite a captura de peças, exibição de mensagens e gerenciamento
 * do fluxo de turno entre os jogadores.
 *
 * <p>Ela é projetada para ser usada em um jogo de xadrez com múltiplos modos de partida,
 * incluindo partidas locais e online, e com suporte a partidas contra IA. A interface
 * permite que os jogadores selecionem e movam peças no tabuleiro, visualizem as capturas
 * e acompanhem o tempo de jogo.</p>
 *
 * A classe faz uso de componentes JavaFX como `GridPane`, `HBox`, `VBox`, `Label`, `ImageView`,
 * `Button`, e `Timeline` para compor a interface do tabuleiro e suas funcionalidades.
 *
 * <p>Principais responsabilidades:</p>
 * <ul>
 *   <li>Exibição do tabuleiro e peças.</li>
 *   <li>Gerenciamento de capturas de peças.</li>
 *   <li>Exibição do estado do jogo e do turno do jogador.</li>
 *   <li>Controle do temporizador de jogo.</li>
 *   <li>Interação com o jogador para selecionar e mover peças.</li>
 *   <li>Comunicação com a lógica da partida (clase `Partida`).</li>
 * </ul>
 */
public class TabuleiroView extends VBox {
    private static final int TILE_SIZE = 70;
    private Label estadoJogoLabel;
    private Label turnoJogoLabel;
    private HBox capturasJogadorBranco;
    private HBox capturasJogadorPreto;
    private ImageView imagemJogadorBranco;
    private ImageView imagemJogadorPreto;
    private Rectangle[][] tiles;
    private GridPane tabuleiroGrid;
    private Map<Posicao, ImageView> mapaImagemView;
    private Button voltarTurnoButton;
    private Button menuButton;
    private Label timerLabel;
    private LocalDateTime inicioPartida;
    private boolean isJogador2;
    
    private Timeline timeline;

    /**
     * Construtor que inicializa a interface do tabuleiro de xadrez.
     * @param partida A partida que contém as informações do jogo.
     * @param isJogador2 Flag que indica se o jogador atual é o jogador 2 (adversário).
     */
    public TabuleiroView(Partida partida, boolean isJogador2) {
        this.isJogador2 = isJogador2;
        tiles = new Rectangle[8][8];
        partida.getTabuleiro();
        mapaImagemView = new HashMap<>();
        inicioPartida = partida.getInicioPartida();
        this.setSpacing(10);
        this.getStyleClass().add("tabuleiro-container");
        this.setAlignment(Pos.CENTER);
        menuButton = new Button("Menu");
        menuButton.getStyleClass().add("button");
        menuButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
        menuButton.setOnAction(event -> eventoMostrarMenu(partida));
        HBox jogadorBrancoBox = new HBox(10);
        imagemJogadorBranco = new ImageView(partida.getJogadorBranco().getImagem());
        imagemJogadorBranco.setFitHeight(50);
        imagemJogadorBranco.setFitWidth(50);
        Label nomeJogadorBranco = new Label("Jogador Branco");
        capturasJogadorBranco = new HBox(5);
        capturasJogadorBranco.setStyle("-fx-alignment: center;");
        if(isJogador2){
            jogadorBrancoBox.getChildren().addAll(menuButton, imagemJogadorBranco, nomeJogadorBranco, capturasJogadorBranco);
        } else {
            jogadorBrancoBox.getChildren().addAll(imagemJogadorBranco, nomeJogadorBranco, capturasJogadorBranco);
        }
        jogadorBrancoBox.getStyleClass().add("jogador-box");
        timerLabel = new Label("00:00");
        timerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        tabuleiroGrid = new GridPane();
        tabuleiroGrid.getStyleClass().add("tabuleiro-grid");
        construirTabuleiro(partida.getTabuleiro(), tabuleiroGrid);
        HBox estadoTurnoBox = new HBox(10);
        estadoTurnoBox.setAlignment(Pos.CENTER);
        estadoJogoLabel = new Label("EM ANDAMENTO");
        estadoJogoLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Label separadorHifen = new Label(" - ");
        separadorHifen.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        turnoJogoLabel = new Label("VEZ JOGADOR BRANCO");
        turnoJogoLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        estadoTurnoBox.getChildren().addAll(estadoJogoLabel, separadorHifen, turnoJogoLabel);
        voltarTurnoButton = new Button("Voltar Turno");
        voltarTurnoButton.getStyleClass().add("button");
        voltarTurnoButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
        eventoVoltarTurno(partida);
        HBox jogadorPretoBox = new HBox(10);
        imagemJogadorPreto = new ImageView(partida.getJogadorPreto().getImagem());
        imagemJogadorPreto.setFitHeight(50);
        imagemJogadorPreto.setFitWidth(50);
        Label nomeJogadorPreto = new Label("Jogador Preto");
        capturasJogadorPreto = new HBox(5);
        capturasJogadorPreto.setStyle("-fx-alignment: center;");
        if(isJogador2){
            jogadorPretoBox.getChildren().addAll(imagemJogadorPreto, nomeJogadorPreto, capturasJogadorPreto);
        } else {
            jogadorPretoBox.getChildren().addAll(menuButton, imagemJogadorPreto, nomeJogadorPreto, capturasJogadorPreto);
        }
        jogadorPretoBox.getStyleClass().add("jogador-box");
        if (partida.isJogadorBrancoIA()) {
            this.getChildren().addAll(jogadorPretoBox, tabuleiroGrid, estadoTurnoBox, timerLabel, voltarTurnoButton, jogadorBrancoBox);
        } else if(isJogador2) {
            this.getChildren().addAll(jogadorBrancoBox, tabuleiroGrid, estadoTurnoBox, timerLabel, jogadorPretoBox);
        } else {
            this.getChildren().addAll(jogadorPretoBox, tabuleiroGrid, estadoTurnoBox, timerLabel, jogadorBrancoBox);
        }
    }

    /**
     * Constrói a representação visual do tabuleiro (as casas).
     * @param tabuleiro O objeto `Tabuleiro` que contém as peças e seu estado.
     * @param tabuleiroGrid A grade do tabuleiro onde as casas serão colocadas.
     */
    private void construirTabuleiro(Tabuleiro tabuleiro, GridPane tabuleiroGrid) {
        tabuleiroGrid.setGridLinesVisible(true);
        tabuleiroGrid.setStyle("-fx-alignment: center;");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Rectangle casa = new Rectangle(TILE_SIZE, TILE_SIZE);
                casa.setFill((i + j) % 2 == 0 ? Color.BEIGE : Color.BROWN);
                tiles[i][j] = casa;
                casa.getStyleClass().add("casa");
                tabuleiroGrid.add(casa, j, i);
            }
        }
        adicionarPecasTabuleiro(tabuleiro);
    }   

    /**
     * Atualiza o estado do jogo exibido na interface.
     * @param estado O novo estado do jogo (ex: "EM ANDAMENTO", "FINALIZADO").
     */
    public void atualizarEstado(String estado) {
        estadoJogoLabel.setText(estado);
    }

    /**
     * Adiciona uma peça capturada pelo jogador branco à sua área de capturas.
     * @param peca A peça capturada.
     */
    public void adicionarCapturaBranco(Peca peca) {
        ImageView pecaCapturada = new ImageView(peca.getImagem());
        pecaCapturada.setFitHeight(30);
        pecaCapturada.setFitWidth(30);
        capturasJogadorBranco.getChildren().add(pecaCapturada);
    }

    /**
     * Adiciona uma peça capturada pelo jogador preto à sua área de capturas.
     * @param peca A peça capturada.
     */
    public void adicionarCapturaPreto(Peca peca) {
        ImageView pecaCapturada = new ImageView(peca.getImagem());
        pecaCapturada.setFitHeight(30);
        pecaCapturada.setFitWidth(30);
        capturasJogadorPreto.getChildren().add(pecaCapturada);
    }

    /**
     * Inicia o temporizador da partida.
     */
    public void iniciarTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> atualizarTimer()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Atualiza o timer a cada segundo.
     */
    private void atualizarTimer() {
        LocalDateTime agora = LocalDateTime.now();
        long inicioMillis = inicioPartida.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long agoraMillis = agora.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long duracaoMillis = agoraMillis - inicioMillis;
        long minutos = duracaoMillis / 60000;
        long segundos = (duracaoMillis % 60000) / 1000;
        timerLabel.setText(String.format("%02d:%02d", minutos, segundos));
    }
    
    /**
     * Destaca as casas no tabuleiro onde o jogador pode mover a peça selecionada.
     * @param moves Lista de posições possíveis onde o jogador pode mover a peça.
     */
    public void grifarMovimentosPossiveis(List<Posicao> moves) {
        clearHighlights();
        for (Posicao pos : moves) {
            int row = isJogador2 ? 7 - pos.getLinha() : pos.getLinha();
            int col = isJogador2 ? 7 - pos.getColuna() : pos.getColuna();
            tiles[row][col].setFill(Color.LIGHTGREEN);
        }
    }
    
    /**
     * Destaca a casa onde a peça foi selecionada.
     * @param origem A posição da peça selecionada.
     */
    public void selecionarPeca(Posicao origem) {
        int row = isJogador2 ? 7 - origem.getLinha() : origem.getLinha();
        int col = isJogador2 ? 7 - origem.getColuna() : origem.getColuna();
        tiles[row][col].setFill(Color.LIGHTBLUE);
    }

    /**
     * Move a peça de uma posição para outra no tabuleiro.
     * @param origem A posição de origem da peça.
     * @param destino A posição de destino da peça.
     */
    public void moverPeca(Posicao origem, Posicao destino) {
        int rowD = isJogador2 ? 7 - destino.getLinha() : destino.getLinha();
        int colD = isJogador2 ? 7 - destino.getColuna() : destino.getColuna();
        ImageView pecaView = obterImageViewDaPosicao(origem.getLinha(), origem.getColuna());
        if (pecaView == null) {
            return;
        }
        tabuleiroGrid.getChildren().remove(pecaView);
        tabuleiroGrid.add(pecaView, rowD, colD);
        mapaImagemView.remove(origem);
        mapaImagemView.put(new Posicao(rowD, colD), pecaView);
    }

    /**
     * Limpa qualquer seleção ou destaque no tabuleiro.
     */
    public void clearSelection() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (tiles[row][col] != null) {
                    tiles[row][col].setFill((row + col) % 2 == 0 ? Color.BEIGE : Color.BROWN);
                }
            }
        }
        clearHighlights();
    }
    
    /**
     * Obtém o `ImageView` da peça presente na posição fornecida.
     * @param linha A linha da posição.
     * @param coluna A coluna da posição.
     * @return O `ImageView` da peça na posição especificada.
     */
    public ImageView obterImageViewDaPosicao(int linha, int coluna) {
        Posicao posicao = new Posicao(linha, coluna);
        return mapaImagemView.get(posicao);
    }
    
    /**
     * Exibe uma mensagem de informação para o jogador.
     * @param mensagem A mensagem a ser exibida.
     */
    public void mostrarMensagem(String mensagem) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    /**
     * Atualiza o tabuleiro com base no estado atual da partida.
     * @param partida A partida atual.
     * @param callback A função a ser chamada após a atualização do tabuleiro.
     */
    public void updateTabuleiro(Partida partida, BiConsumer<Integer, Integer> callback) {
        Platform.runLater(() -> {
            clearHighlights();
            tabuleiroGrid.getChildren().clear();
            construirTabuleiro(partida.getTabuleiro(), tabuleiroGrid);
            reconfigurarEventosDeClique(partida, callback);
        });
    }    

    /**
     * Reconfigura os eventos de clique nas casas e peças do tabuleiro.
     * @param partida A partida atual.
     * @param callback A função de callback que será chamada quando um evento de clique ocorrer.
     */
    public void reconfigurarEventosDeClique(Partida partida, BiConsumer<Integer, Integer> callback) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ImageView pecaView = obterImageViewDaPosicao(row, col);
                Rectangle casa = tiles[row][col];
                final int rowF = row;
                final int colF = col;
                casa.setOnMouseClicked(event -> {
                    if (partida.ehTurnoDoJogador(isJogador2) && partida.getIsOnline()) {
                        int rowFR = rowF;
                        int colFR = colF;
                        if (partida.getIsOnline() && isJogador2) {
                            rowFR = 7 - rowF;
                            colFR = 7 - colF;
                        }
                        callback.accept(rowFR, colFR);
                    } else if(!partida.getIsOnline()) {
                        callback.accept(rowF, colF);
                    }
                });
                if (pecaView != null) {
                    pecaView.setOnMouseClicked(event -> {
                        if (partida.ehTurnoDoJogador(isJogador2) && partida.getIsOnline()) {
                            callback.accept(rowF, colF);
                        } else if(!partida.getIsOnline()) {
                            callback.accept(rowF, colF);
                        }
                    });
                }
            }
        }
    }

    /**
     * Adiciona as peças no tabuleiro, baseando-se nas informações do tabuleiro da partida.
     * @param tabuleiro O tabuleiro da partida, contendo as peças em suas posições.
     */
    private void adicionarPecasTabuleiro(Tabuleiro tabuleiro) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Posicao posicao = new Posicao(row, col);
                Peca peca = tabuleiro.obterPeca(posicao);
                if (peca != null) {
                    int displayRow = isJogador2 ? 7 - row : row;
                    int displayCol = isJogador2 ? 7 - col : col;
                    Image img = peca.getImagem();
                    if (img != null) {
                        ImageView pecaView = new ImageView(img);
                        pecaView.setFitWidth(TILE_SIZE);
                        pecaView.setFitHeight(TILE_SIZE);
                        pecaView.setPreserveRatio(true);
                        tabuleiroGrid.add(pecaView, displayCol, displayRow);
                        mapaImagemView.put(new Posicao(row, col), pecaView);
                    }
                }
            }
        }
    }     

    /**
     * Limpa os destaques de todas as casas do tabuleiro.
     */
    private void clearHighlights() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (tiles[row][col] != null) {
                    tiles[row][col].setFill((row + col) % 2 == 0 ? Color.BEIGE : Color.BROWN);
                }
            }
        }
    }

    /**
     * Obtém a área de capturas do jogador preto.
     * @return O `HBox` contendo as capturas do jogador preto.
     */
    public HBox getCapturasJogadorPreto(){
        return capturasJogadorPreto;
    }
    
    /**
     * Obtém a área de capturas do jogador branco.
     * @return O `HBox` contendo as capturas do jogador branco.
     */
    public HBox getCapturasJogadorBranco(){
        return capturasJogadorBranco;
    }

    /**
     * Configura o evento de voltar o turno para a partida.
     * @param partida A partida atual.
     */
    private void eventoVoltarTurno(Partida partida) {
        voltarTurnoButton.setOnAction(event -> {
            partida.voltaTurno();
        });
    }

    /**
     * Método responsável por mostrar o menu de controle da partida.
     * 
     * @param partida A partida atual que será usada para exibir as opções no menu.
     */
    private void eventoMostrarMenu(Partida partida) {
        MenuControle menuControle = new MenuControle(this, (Stage) this.getScene().getWindow());
        menuControle.mostrarMenu(partida);
    }

    /**
     * Método responsável por retornar o estado de "Jogador 2".
     * 
     * @return Retorna um valor booleano indicando se o jogador atual é o Jogador 2.
     */
    public boolean getIsJogador2(){
        return isJogador2;
    }

    /**
     * Método responsável por parar o temporizador, se ele estiver em execução.
     * 
     * Este método verifica se a variável de controle de tempo (timeline) não é nula
     * e, em seguida, interrompe o temporizador, caso esteja em execução.
     */
    public void pararTimer() {
        if (timeline != null) {
            timeline.stop();
        }
    }
    
    /**
     * Método responsável por atualizar o texto que exibe o turno atual do jogo.
     * 
     * @param turno O turno a ser exibido, representado como uma string.
     */
    public void atualizarTurno(String turno) {
        turnoJogoLabel.setText(turno);
    }
}