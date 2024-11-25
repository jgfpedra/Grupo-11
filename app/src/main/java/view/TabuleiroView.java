package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import partida.Partida;
import partida.Posicao;
import partida.Tabuleiro;
import pecas.Peca;
import java.util.function.BiConsumer;
import java.util.HashMap;
import java.util.List;

public class TabuleiroView extends VBox {
    private Label estadoJogoLabel;
    private HBox capturasJogadorBranco;
    private HBox capturasJogadorPreto;
    private ImageView imagemJogadorBranco;
    private ImageView imagemJogadorPreto;
    private static final int TILE_SIZE = 50; // Tamanho padrão de cada casa do tabuleiro
    private Rectangle[][] tiles; // Representação gráfica das casas do tabuleiro
    private GridPane tabuleiroGrid; // Definimos como um atributo da classe

    public TabuleiroView(Partida partida) {
        tiles = new Rectangle[8][8];
        partida.getTabuleiro();
        new HashMap<>();
    
        // Configuração geral
        this.setSpacing(10);
    
        // Seção do jogador branco
        HBox jogadorBrancoBox = new HBox(10);
        imagemJogadorBranco = new ImageView(new Image(getClass().getResourceAsStream("/images/jogadores/jogadorlocal.png")));
        imagemJogadorBranco.setFitHeight(50);
        imagemJogadorBranco.setFitWidth(50);
        Label nomeJogadorBranco = new Label("Jogador Branco");
        capturasJogadorBranco = new HBox(5); // Para peças capturadas
        jogadorBrancoBox.getChildren().addAll(imagemJogadorBranco, nomeJogadorBranco, capturasJogadorBranco);
    
        // Tabuleiro
        tabuleiroGrid = new GridPane();
        construirTabuleiro(tabuleiroGrid);
    
        // Estado do jogo
        estadoJogoLabel = new Label("EM_ANDAMENTO");
        estadoJogoLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    
        // Seção do jogador preto
        HBox jogadorPretoBox = new HBox(10);
        imagemJogadorPreto = new ImageView(new Image(getClass().getResourceAsStream("/images/jogadores/jogadorlocal.png")));
        imagemJogadorPreto.setFitHeight(50);
        imagemJogadorPreto.setFitWidth(50);
        Label nomeJogadorPreto = new Label("Jogador Preto");
        capturasJogadorPreto = new HBox(5); // Para peças capturadas
        jogadorPretoBox.getChildren().addAll(imagemJogadorPreto, nomeJogadorPreto, capturasJogadorPreto);
    
        // Adiciona tudo à estrutura principal
        this.getChildren().addAll(jogadorBrancoBox, tabuleiroGrid, estadoJogoLabel, jogadorPretoBox);
    }

    private void construirTabuleiro(GridPane tabuleiroGrid) {
        tabuleiroGrid.setGridLinesVisible(true);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Rectangle casa = new Rectangle(TILE_SIZE, TILE_SIZE);
                casa.setFill((i + j) % 2 == 0 ? Color.BEIGE : Color.BROWN);
                tiles[i][j] = casa; // Inicializa corretamente o array tiles
                tabuleiroGrid.add(casa, j, i);
            }
        }
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

    public void setOnTileClicked(BiConsumer<Integer, Integer> callback) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                tiles[row][col].setOnMouseClicked(event -> {
                    int x = Math.min(Math.max((int) (event.getX() / TILE_SIZE), 0), 7);
                    int y = Math.min(Math.max((int) (event.getY() / TILE_SIZE), 0), 7);
                    callback.accept(y, x); // Note que "row" e "col" se tornam "y" e "x"
                });
            }
        }
    }
    
    public void highlightPossibleMoves(List<Posicao> moves) {
        clearHighlights();
        for (Posicao pos : moves) {
            int row = pos.getLinha();
            int col = pos.getColuna();
            tiles[row][col].setFill(Color.LIGHTGREEN); // Destaca possíveis movimentos
        }
    }
    
    public void selecionarPeca(Posicao origem) {
        clearHighlights();
        int row = origem.getLinha();
        int col = origem.getColuna();
        tiles[row][col].setFill(Color.LIGHTBLUE); // Destaca a peça selecionada
    }
    
    public void moverPeca(Posicao origem, Posicao destino) {
        int origemRow = origem.getLinha();
        int origemCol = origem.getColuna();
        int destinoRow = destino.getLinha();
        int destinoCol = destino.getColuna();

        // Aqui, você pode implementar a lógica para atualizar as imagens das peças
        tiles[origemRow][origemCol].setFill((origemRow + origemCol) % 2 == 0 ? Color.BEIGE : Color.GRAY); // Limpa casa de origem
        tiles[destinoRow][destinoCol].setFill(Color.LIGHTBLUE); // Destaca a peça movida
    }
    
    public void mostrarMensagem(String mensagem) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    public void updateTabuleiro(Tabuleiro tabuleiro) {
        // Limpa destaques e remove elementos antigos
        clearHighlights();
        tabuleiroGrid.getChildren().clear();
    
        // Reconstrói o tabuleiro base
        construirTabuleiro(tabuleiroGrid);
    
        // Adiciona peças ao tabuleiro
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Posicao posicao = new Posicao(row, col);
                Peca peca = tabuleiro.obterPeca(posicao);
                System.out.println(tabuleiro);
                if (peca != null) {
                    // Cria um ImageView para a peça
                    ImageView pecaView = new ImageView(peca.getImagem()); // Certifique-se de que getImagem retorna a imagem correta
                    pecaView.setFitWidth(TILE_SIZE);
                    pecaView.setFitHeight(TILE_SIZE);
                    pecaView.setPreserveRatio(true);
    
                    // Adiciona a peça ao GridPane na posição correta
                    tabuleiroGrid.add(pecaView, col, row);
                }
            }
        }
    }    

    private void clearHighlights() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (tiles[row][col] != null) {
                    tiles[row][col].setFill((row + col) % 2 == 0 ? Color.BEIGE : Color.GRAY);
                }
            }
        }
    }    
}