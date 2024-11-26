package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
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

import controle.MenuControle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabuleiroView extends VBox {
    private static final int TILE_SIZE = 70;
    private Label estadoJogoLabel;
    private HBox capturasJogadorBranco;
    private HBox capturasJogadorPreto;
    private HBox menuButtonBox;
    private ImageView imagemJogadorBranco;
    private ImageView imagemJogadorPreto;
    private Rectangle[][] tiles;
    private GridPane tabuleiroGrid;
    private Map<Posicao, ImageView> mapaImagemView;
    private Button voltarTurnoButton;
    private Button menuButton; // Novo botão para voltar ao menu

    public TabuleiroView(Partida partida) {
        tiles = new Rectangle[8][8];
        partida.getTabuleiro();
        mapaImagemView = new HashMap<>();
    
        // Configuração geral
        this.setSpacing(10);
        this.getStyleClass().add("tabuleiro-container"); // Apply main container style
        this.setAlignment(Pos.CENTER);

        // Seção do jogador branco
        HBox jogadorBrancoBox = new HBox(10);
        imagemJogadorBranco = new ImageView(new Image(getClass().getResourceAsStream("/images/jogadores/jogadorlocal.png")));
        imagemJogadorBranco.setFitHeight(50);
        imagemJogadorBranco.setFitWidth(50);
        Label nomeJogadorBranco = new Label("Jogador Branco");
        capturasJogadorBranco = new HBox(5); // Para peças capturadas
        capturasJogadorBranco.setStyle("-fx-alignment: center;");
        jogadorBrancoBox.getChildren().addAll(imagemJogadorBranco, nomeJogadorBranco, capturasJogadorBranco);
        jogadorBrancoBox.getStyleClass().add("jogador-box");

        // Tabuleiro
        tabuleiroGrid = new GridPane();
        tabuleiroGrid.getStyleClass().add("tabuleiro-grid");
        construirTabuleiro(partida.getTabuleiro(), tabuleiroGrid);
    
        // Estado do jogo
        estadoJogoLabel = new Label("EM_ANDAMENTO");  // Você precisa inicializar o estado do jogo
        estadoJogoLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        voltarTurnoButton = new Button("Voltar Turno");
        voltarTurnoButton.getStyleClass().add("button");
        voltarTurnoButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");

        eventoVoltarTurno(partida);

        menuButton = new Button("Menu");
        menuButton.getStyleClass().add("button");
        menuButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
        menuButton.setOnAction(event -> eventoMostrarMenu(partida));
        menuButtonBox = new HBox();
        menuButtonBox.setAlignment(Pos.CENTER_RIGHT);
        menuButtonBox.getChildren().add(menuButton);

        // Seção do jogador preto
        HBox jogadorPretoBox = new HBox(10);
        imagemJogadorPreto = new ImageView(new Image(getClass().getResourceAsStream("/images/jogadores/jogadorlocal.png")));
        imagemJogadorPreto.setFitHeight(50);
        imagemJogadorPreto.setFitWidth(50);
        Label nomeJogadorPreto = new Label("Jogador Preto");
        capturasJogadorPreto = new HBox(5); // Para peças capturadas
        capturasJogadorPreto.setStyle("-fx-alignment: center;");
        jogadorPretoBox.getChildren().addAll(imagemJogadorPreto, nomeJogadorPreto, capturasJogadorPreto);
        jogadorPretoBox.getStyleClass().add("jogador-box");
    
        if (partida.isJogadorBrancoIA()) {
            this.getChildren().addAll(menuButtonBox, jogadorBrancoBox, tabuleiroGrid, estadoJogoLabel, voltarTurnoButton, jogadorPretoBox);
        } else {
            this.getChildren().addAll(menuButtonBox,jogadorBrancoBox, tabuleiroGrid, estadoJogoLabel, jogadorPretoBox);
        }
    }

    private void construirTabuleiro(Tabuleiro tabuleiro, GridPane tabuleiroGrid) {
        tabuleiroGrid.setGridLinesVisible(true);
    
        // Centraliza o GridPane dentro do VBox ou HBox
        tabuleiroGrid.setStyle("-fx-alignment: center;");  // Garante que o conteúdo será centralizado
    
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Rectangle casa = new Rectangle(TILE_SIZE, TILE_SIZE);
                casa.setFill((i + j) % 2 == 0 ? Color.BEIGE : Color.BROWN);  // A cor das casas alternadas
                tiles[i][j] = casa; // Inicializa corretamente o array tiles
                casa.getStyleClass().add("casa");  // Aplica o estilo da classe "casa"
                tabuleiroGrid.add(casa, j, i);  // Adiciona a casa no GridPane
            }
        }
        adicionarPecasTabuleiro(tabuleiro);
    }

    // Métodos para atualizar capturas, peças no tabuleiro e estado do jogo
    public void atualizarEstado(String estado) {
        estadoJogoLabel.setText(estado);
    }

    public void adicionarCapturaBranco(Peca peca) {
        ImageView pecaCapturada = new ImageView(peca.getImagem()); // Exemplo
        pecaCapturada.setFitHeight(30);
        pecaCapturada.setFitWidth(30);
        capturasJogadorBranco.getChildren().add(pecaCapturada);
    }

    public void adicionarCapturaPreto(Peca peca) {
        ImageView pecaCapturada = new ImageView(peca.getImagem()); // Exemplo
        pecaCapturada.setFitHeight(30);
        pecaCapturada.setFitWidth(30);
        capturasJogadorPreto.getChildren().add(pecaCapturada);
    }
    
    public void highlightPossibleMoves(List<Posicao> moves) {
        clearHighlights();
        System.out.println(moves);
        for (Posicao pos : moves) {
            int row = pos.getLinha();
            int col = pos.getColuna();
            tiles[row][col].setFill(Color.LIGHTGREEN); // Destaca possíveis movimentos
        }
    }
    
    public void selecionarPeca(Posicao origem) {
        int row = origem.getLinha();
        int col = origem.getColuna();
        tiles[row][col].setFill(Color.LIGHTBLUE); // Destaca a peça selecionada
    }
    
    public void moverPeca(Posicao origem, Posicao destino) {
        ImageView pecaView = obterImageViewDaPosicao(origem.getLinha(), origem.getColuna());
        if (pecaView == null) {
            return;
        }
        tabuleiroGrid.getChildren().remove(pecaView);
        tabuleiroGrid.add(pecaView, destino.getColuna(), destino.getLinha());
        mapaImagemView.remove(origem); // Remove a entrada da posição de origem
        mapaImagemView.put(destino, pecaView); // Adiciona a entrada na nova posição
    }    

    public void clearSelection() {
        // Remove qualquer destaque de seleção da casa no tabuleiro
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (tiles[row][col] != null) {
                    // Restaura a cor original da casa (bege ou marrom)
                    tiles[row][col].setFill((row + col) % 2 == 0 ? Color.BEIGE : Color.BROWN);
                }
            }
        }
        clearHighlights();
    }
    
    public ImageView obterImageViewDaPosicao(int linha, int coluna) {
        Posicao posicao = new Posicao(linha, coluna);
        return mapaImagemView.get(posicao); // Retorna a ImageView da posição, se existir
    }
    
    public void mostrarMensagem(String mensagem) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    public void updateTabuleiro(Tabuleiro tabuleiro, BiConsumer<Integer, Integer> callback) {
        clearHighlights();
        tabuleiroGrid.getChildren().clear();
        construirTabuleiro(tabuleiro, tabuleiroGrid);
        reconfigurarEventosDeClique(callback);
    }

    public void reconfigurarEventosDeClique(BiConsumer<Integer, Integer> callback) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ImageView pecaView = obterImageViewDaPosicao(row, col);
                Rectangle casa = tiles[row][col];
                final int rowF = row;
                final int colF = col;
                if (pecaView != null) {
                    pecaView.setOnMouseClicked(event -> callback.accept(rowF, colF));
                }
                casa.setOnMouseClicked(event -> callback.accept(rowF, colF));
            }
        }
    }        

    private void adicionarPecasTabuleiro(Tabuleiro tabuleiro) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Posicao posicao = new Posicao(row, col);
                Peca peca = tabuleiro.obterPeca(posicao);
                if (peca != null) {
                    // Cria um ImageView para a peça
                    Image img = peca.getImagem();
                    if (img != null) {
                        ImageView pecaView = new ImageView(img);
                        pecaView.setFitWidth(TILE_SIZE);
                        pecaView.setFitHeight(TILE_SIZE);
                        pecaView.setPreserveRatio(true);
                        tabuleiroGrid.add(pecaView, col, row);
                        mapaImagemView.put(posicao, pecaView);
                    }
                }
            }
        }
    }

    private void clearHighlights() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (tiles[row][col] != null) {
                    tiles[row][col].setFill((row + col) % 2 == 0 ? Color.BEIGE : Color.BROWN);
                }
            }
        }
    }

    public HBox getCapturasJogadorPreto(){
        return capturasJogadorPreto;
    }
    
    public HBox getCapturasJogadorBranco(){
        return capturasJogadorBranco;
    }

    private void eventoVoltarTurno(Partida partida) {
        voltarTurnoButton.setOnAction(event -> {
            partida.voltaTurno();
        });
    }

    private void eventoMostrarMenu(Partida partida) {
        MenuControle menuControle = new MenuControle(this, (Stage) this.getScene().getWindow());
        menuControle.mostrarMenu(partida);  // Chama o método do MenuControle para abrir o menu
    }
    
}