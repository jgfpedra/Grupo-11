package xadrez;

import java.net.Socket;

import UI.TabuleiroController;
import UI.TabuleiroView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jogador.Jogador;
import jogador.JogadorIA;
import jogador.JogadorLocal;
import jogador.JogadorOnline;
import partida.Cor;
import partida.Partida;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox menuLayout = new VBox(10);
        menuLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Player 1 selection
        Label player1Label = new Label("Selecione o Jogador 1: ");
        ToggleGroup player1Group = new ToggleGroup();
        RadioButton player1Local = new RadioButton("Local");
        RadioButton player1AI = new RadioButton("Inteligencia Artificial");
        player1Local.setToggleGroup(player1Group);
        player1AI.setToggleGroup(player1Group);
        player1Local.setSelected(true);

        // AI level selection for Player 1
        ComboBox<String> player1AILevel = new ComboBox<>();
        player1AILevel.getItems().addAll("Fácil", "Médio", "Difícil");
        player1AILevel.setValue("Médio"); // Default value

        // Player 2 selection
        Label player2Label = new Label("Selecione o Jogador 2: ");
        ToggleGroup player2Group = new ToggleGroup();
        RadioButton player2Local = new RadioButton("Local");
        RadioButton player2AI = new RadioButton("Inteligencia Artificial");
        RadioButton player2Online = new RadioButton("Online");
        player2Local.setToggleGroup(player2Group);
        player2AI.setToggleGroup(player2Group);
        player2Online.setToggleGroup(player2Group);
        player2Local.setSelected(true);

        // AI level selection for Player 2
        ComboBox<String> player2AILevel = new ComboBox<>();
        player2AILevel.getItems().addAll("Fácil", "Médio", "Difícil");
        player2AILevel.setValue("Médio"); // Default value

        Button botaoIniciar = new Button("Iniciar Jogo");
        botaoIniciar.setOnAction(event -> {
            boolean player1IsAI = player1AI.isSelected();
            boolean player2IsAI = player2AI.isSelected();
            boolean player2IsOnline = player2Online.isSelected();
            String player1AISelectedLevel = player1AILevel.getValue();
            String player2AISelectedLevel = player2AILevel.getValue();
            iniciarJogo(player1IsAI, player2IsAI, player2IsOnline, player1AISelectedLevel, player2AISelectedLevel, primaryStage);
        });

        menuLayout.getChildren().addAll(
                player1Label, player1Local, player1AI, player1AILevel,
                player2Label, player2Local, player2AI, player2Online, player2AILevel,
                botaoIniciar
        );

        // Set up the scene for the player selection menu
        Scene menuScene = new Scene(menuLayout, 400, 300);
        primaryStage.setTitle("Jogo de Xadrez - Seleção de Jogadores");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    private void iniciarJogo(boolean player1IsAI, boolean player2IsAI, boolean player2IsOnline,
                             String player1AISelectedLevel, String player2AISelectedLevel, Stage primaryStage) {

        Jogador player1 = player1IsAI ? new JogadorIA(Cor.PRETO, "IA Preto", getAILevel(player1AISelectedLevel)) : new JogadorLocal(Cor.PRETO, "Jogador 1");
        Jogador player2;

        if (player2IsAI) {
            player2 = new JogadorIA(Cor.BRANCO, "IA Branco", getAILevel(player2AISelectedLevel));
        } else if (player2IsOnline) {
            // Connect to the online server (this is a placeholder for now)
            Socket socket = new Socket(); // Replace with actual connection code
            player2 = new JogadorOnline(Cor.BRANCO, "Jogador Online 2", socket);
        } else {
            player2 = new JogadorLocal(Cor.BRANCO, "Jogador 2");
        }

        Partida partida = new Partida(player1, player2);

        // Criar a visualização do tabuleiro e o controlador
        TabuleiroView tabuleiroView = new TabuleiroView(partida);
        new TabuleiroController(partida, tabuleiroView); // Controller atualiza a visualização

        // Exibir a cena do tabuleiro
        primaryStage.setTitle("Jogo de Xadrez");
        primaryStage.setScene(new Scene(tabuleiroView, 1024, 1024));
        primaryStage.show();
    }

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