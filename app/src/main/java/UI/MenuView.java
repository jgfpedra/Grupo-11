package UI;

import java.net.Socket;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import jogador.Jogador;
import jogador.JogadorIA;
import jogador.JogadorLocal;
import jogador.JogadorOnline;
import partida.Cor;
import partida.Partida;

public class MenuView {

    private VBox menuLayout;
    
    public MenuView(Stage primaryStage) {
        // Criação do layout do menu
        menuLayout = new VBox(10);
        menuLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Título do jogo
        Label titleLabel = new Label("Jogo de Xadrez");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Player 1 selection
        Label player1Label = new Label("Selecione o Jogador 1: ");
        ToggleGroup player1Group = new ToggleGroup();
        RadioButton player1Local = new RadioButton("Local");
        RadioButton player1AI = new RadioButton("Inteligência Artificial");
        player1Local.setToggleGroup(player1Group);
        player1AI.setToggleGroup(player1Group);
        player1Local.setSelected(true);

        // AI level selection for Player 1
        ComboBox<String> player1AILevel = new ComboBox<>();
        player1AILevel.getItems().addAll("Fácil", "Médio", "Difícil");
        player1AILevel.setValue("Médio");

        // Player 2 selection
        Label player2Label = new Label("Selecione o Jogador 2: ");
        ToggleGroup player2Group = new ToggleGroup();
        RadioButton player2Local = new RadioButton("Local");
        RadioButton player2AI = new RadioButton("Inteligência Artificial");
        RadioButton player2Online = new RadioButton("Online");
        player2Local.setToggleGroup(player2Group);
        player2AI.setToggleGroup(player2Group);
        player2Online.setToggleGroup(player2Group);
        player2Local.setSelected(true);

        // AI level selection for Player 2
        ComboBox<String> player2AILevel = new ComboBox<>();
        player2AILevel.getItems().addAll("Fácil", "Médio", "Difícil");
        player2AILevel.setValue("Médio");

        // Start button
        Button startButton = new Button("Iniciar Jogo");
        startButton.setOnAction(event -> {
            boolean player1IsAI = player1AI.isSelected();
            boolean player2IsAI = player2AI.isSelected();
            boolean player2IsOnline = player2Online.isSelected();
            String player1AISelectedLevel = player1AILevel.getValue();
            String player2AISelectedLevel = player2AILevel.getValue();
            iniciarJogo(player1IsAI, player2IsAI, player2IsOnline, player1AISelectedLevel, player2AISelectedLevel, primaryStage);
        });

        // Adicionar todos os elementos no layout
        menuLayout.getChildren().addAll(
                titleLabel,
                player1Label, player1Local, player1AI, player1AILevel,
                player2Label, player2Local, player2AI, player2Online, player2AILevel,
                startButton
        );

        // Criar a cena do menu
        Scene menuScene = new Scene(menuLayout, 400, 300);

        // Aplicar o CSS na cena
        menuScene.getStylesheets().add(getClass().getResource("/css/menu.css").toExternalForm());

        primaryStage.setTitle("Jogo de Xadrez - Seleção de Jogadores");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    // Método para iniciar o jogo com os jogadores selecionados
    private void iniciarJogo(boolean player1IsAI, boolean player2IsAI, boolean player2IsOnline,
                             String player1AISelectedLevel, String player2AISelectedLevel, Stage primaryStage) {
        Jogador player1 = player1IsAI ? new JogadorIA(Cor.PRETO, "IA Preto", getAILevel(player1AISelectedLevel)) : new JogadorLocal(Cor.PRETO, "Jogador 1");
        Jogador player2;

        if (player2IsAI) {
            player2 = new JogadorIA(Cor.BRANCO, "IA Branco", getAILevel(player2AISelectedLevel));
        } else if (player2IsOnline) {
            // Placeholder para a conexão online
            Socket socket = new Socket();
            player2 = new JogadorOnline(Cor.BRANCO, "Jogador Online 2", socket);
        } else {
            player2 = new JogadorLocal(Cor.BRANCO, "Jogador 2");
        }

        Partida partida = new Partida(player1, player2);

        // Criar a visualização do tabuleiro e o controlador
        TabuleiroView tabuleiroView = new TabuleiroView(partida);
        new TabuleiroController(partida, tabuleiroView);

        // Exibir a cena do tabuleiro
        primaryStage.setTitle("Jogo de Xadrez");
        primaryStage.setScene(new Scene(tabuleiroView, 1024, 1024));
        primaryStage.show();
    }

    // Método para converter o nível de dificuldade da IA
    private int getAILevel(String level) {
        switch (level) {
            case "Fácil":
                return 1;
            case "Médio":
                return 2;
            case "Difícil":
                return 3;
            default:
                return 2; // Default level
        }
    }
}