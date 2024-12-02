package UI.view;

import java.io.File;

import UI.controle.PartidaLocalControle;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import partida.Cor;

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

        // Player 1 color selection
        Label player1ColorLabel = new Label("Selecione a cor do Jogador 1: ");
        ComboBox<String> player1Color = new ComboBox<>();
        player1Color.getItems().addAll("Branco", "Preto");
        player1Color.setValue("Branco");

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

        // Player 2 color selection
        Label player2ColorLabel = new Label("Selecione a cor do Jogador 2: ");
        ComboBox<String> player2Color = new ComboBox<>();
        player2Color.getItems().addAll("Branco", "Preto");
        player2Color.setValue("Preto");

        // Start button
        Button startButton = new Button("Iniciar Jogo");
        startButton.setOnAction(event -> {
            boolean player1IsAI = player1AI.isSelected();
            boolean player2IsAI = player2AI.isSelected();
            String player1AISelectedLevel = player1AILevel.getValue();
            String player2AISelectedLevel = player2AILevel.getValue();
            Cor corJogador1 = player1Color.getValue().equals("Branco") ? Cor.BRANCO : Cor.PRETO;
            Cor corJogador2 = player2Color.getValue().equals("Branco") ? Cor.BRANCO : Cor.PRETO;

            PartidaLocalControle iniciarJogoController = new PartidaLocalControle();
            iniciarJogoController.iniciarJogo(player1IsAI, player2IsAI, player1AISelectedLevel, player2AISelectedLevel, corJogador1, corJogador2, primaryStage);
        });

        // Button to load the saved game
        Button loadGameButton = new Button("Carregar Jogo");
        loadGameButton.setOnAction(event -> {
            File arquivoHistorico = new File("data/tabuleiro.xml");
            boolean player1IsAI = player1AI.isSelected();
            boolean player2IsAI = player2AI.isSelected();
            String player1AISelectedLevel = player1AILevel.getValue();
            String player2AISelectedLevel = player2AILevel.getValue();
            Cor corJogador1 = player1Color.getValue().equals("Branco") ? Cor.BRANCO : Cor.PRETO;
            Cor corJogador2 = player2Color.getValue().equals("Branco") ? Cor.BRANCO : Cor.PRETO;
            if (arquivoHistorico.exists()) {
                PartidaLocalControle carregarJogoController = new PartidaLocalControle();
                carregarJogoController.carregarJogo(player1IsAI, player2IsAI, player1AISelectedLevel, player2AISelectedLevel, corJogador1, corJogador2, arquivoHistorico, primaryStage);
            } else {
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Erro");
                alerta.setHeaderText(null);
                alerta.setContentText("Nenhuma partida salva encontrada.");
                alerta.showAndWait();
            }
        });

        Button voltarButton = new Button("Voltar");
        voltarButton.setStyle("-fx-font-size: 16px;");
        voltarButton.setOnAction(e -> {
            new InicioView(primaryStage);  
        });

        menuLayout.getChildren().addAll(
                titleLabel,
                player1Label, player1Local, player1AI, player1AILevel, player1ColorLabel, player1Color,
                player2Label, player2Local, player2AI, player2AILevel, player2ColorLabel, player2Color,
                startButton, loadGameButton,
                voltarButton
        );

        Scene menuScene = new Scene(menuLayout, 1200, 900);
        menuScene.getStylesheets().add(getClass().getResource("/style/menu.css").toExternalForm());

        primaryStage.setTitle("Jogo de Xadrez - Local");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }
}