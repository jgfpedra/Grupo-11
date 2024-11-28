package view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import controle.TabuleiroControle;
import jogador.*;
import partida.*;

public class PartidaLocalView {

    public PartidaLocalView(Stage primaryStage) {
        VBox menuLayout = new VBox(10);
        menuLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Título do jogo
        Label titleLabel = new Label("Jogo de Xadrez - Local");
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
        player2Local.setToggleGroup(player2Group);
        player2AI.setToggleGroup(player2Group);
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
            String player1AISelectedLevel = player1AILevel.getValue();
            String player2AISelectedLevel = player2AILevel.getValue();
            iniciarJogo(player1IsAI, player2IsAI, player1AISelectedLevel, player2AISelectedLevel, primaryStage);
        });
        
        Button voltarButton = new Button("Voltar");
        voltarButton.setStyle("-fx-font-size: 16px;");
        voltarButton.setOnAction(e -> {
            // Volta para a tela anterior (menu principal)
            new InicioView(primaryStage);  // Supondo que você tenha uma classe MenuView para o menu principal
        });

        // Adicionar todos os elementos no layout
        menuLayout.getChildren().addAll(
                titleLabel,
                player1Label, player1Local, player1AI, player1AILevel,
                player2Label, player2Local, player2AI, player2AILevel,
                startButton,
                voltarButton
        );

        // Criar a cena do menu
        Scene menuScene = new Scene(menuLayout, 1200, 900);
        menuScene.getStylesheets().add(getClass().getResource("/style/menu.css").toExternalForm());

        primaryStage.setTitle("Jogo de Xadrez - Local");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    private void iniciarJogo(boolean player1IsAI, boolean player2IsAI, String player1AISelectedLevel, String player2AISelectedLevel, Stage primaryStage) {
        Jogador player1 = player1IsAI ? new JogadorIA(Cor.BRANCO, "IA Preto", getAILevel(player1AISelectedLevel)) : new JogadorLocal(Cor.BRANCO, "Jogador 1");
        Jogador player2 = player2IsAI ? new JogadorIA(Cor.PRETO, "IA Branco", getAILevel(player2AISelectedLevel)) : new JogadorLocal(Cor.PRETO, "Jogador 2");

        Partida partida = new Partida(player1, player2, null); // Sem histórico

        // Criar a visualização do tabuleiro e o controlador
        TabuleiroView tabuleiroView = new TabuleiroView(partida);
        new TabuleiroControle(partida, tabuleiroView, primaryStage);

        // Exibir a cena do tabuleiro
        primaryStage.setTitle("Jogo de Xadrez");
        primaryStage.setScene(new Scene(tabuleiroView, 800, 800));
        primaryStage.getScene().getStylesheets().add(getClass().getResource("/style/tabuleiro.css").toExternalForm());
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
                return 2;
        }
    }
}