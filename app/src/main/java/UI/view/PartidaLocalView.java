package UI.view;

import java.io.File;

import UI.controle.PartidaLocalControle;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import partida.Cor;

/**
 * Classe responsável pela criação da interface gráfica para iniciar ou carregar uma partida local de xadrez.
 * 
 * Oferece opções para o jogador escolher entre jogar contra outro jogador local ou contra a Inteligência Artificial (IA).
 * Permite também selecionar o nível de dificuldade da IA, a cor das peças de cada jogador e carregar um jogo salvo.
 */
public class PartidaLocalView {

    /**
     * Constrói a interface do jogo de xadrez local, permitindo ao jogador selecionar as opções para o início do jogo.
     * 
     * @param primaryStage A janela principal do jogo.
     */
    public PartidaLocalView(Stage primaryStage) {
        VBox menuLayout = new VBox(10);
        menuLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        Label titleLabel = new Label("Jogo de Xadrez - Local");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Label player1Label = new Label("Selecione o Jogador 1: ");
        ToggleGroup player1Group = new ToggleGroup();
        RadioButton player1Local = new RadioButton("Local");
        RadioButton player1AI = new RadioButton("Inteligência Artificial");
        player1Local.setToggleGroup(player1Group);
        player1AI.setToggleGroup(player1Group);
        player1Local.setSelected(true);
        ComboBox<String> player1AILevel = new ComboBox<>();
        player1AILevel.getItems().addAll("Fácil", "Médio", "Difícil");
        player1AILevel.setValue("Médio");
        Label player1ColorLabel = new Label("Selecione a cor do Jogador 1: ");
        ComboBox<String> player1Color = new ComboBox<>();
        player1Color.getItems().addAll("Branco", "Preto");
        player1Color.setValue("Branco");
        Label player2Label = new Label("Selecione o Jogador 2: ");
        ToggleGroup player2Group = new ToggleGroup();
        RadioButton player2Local = new RadioButton("Local");
        RadioButton player2AI = new RadioButton("Inteligência Artificial");
        player2Local.setToggleGroup(player2Group);
        player2AI.setToggleGroup(player2Group);
        player2Local.setSelected(true);
        ComboBox<String> player2AILevel = new ComboBox<>();
        player2AILevel.getItems().addAll("Fácil", "Médio", "Difícil");
        player2AILevel.setValue("Médio");
        Label player2ColorLabel = new Label("Selecione a cor do Jogador 2: ");
        ComboBox<String> player2Color = new ComboBox<>();
        player2Color.getItems().addAll("Branco", "Preto");
        player2Color.setValue("Preto");
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
        Button loadGameButton = new Button("Carregar Jogo");
        loadGameButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter xmlFilter = new FileChooser.ExtensionFilter("Arquivos XML", "*.xml");
            fileChooser.getExtensionFilters().add(xmlFilter);
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                boolean player1IsAI = player1AI.isSelected();
                boolean player2IsAI = player2AI.isSelected();
                String player1AISelectedLevel = player1AILevel.getValue();
                String player2AISelectedLevel = player2AILevel.getValue();
                Cor corJogador1 = player1Color.getValue().equals("Branco") ? Cor.BRANCO : Cor.PRETO;
                Cor corJogador2 = player2Color.getValue().equals("Branco") ? Cor.BRANCO : Cor.PRETO;
                PartidaLocalControle carregarJogoController = new PartidaLocalControle();
                carregarJogoController.carregarJogo(player1IsAI, player2IsAI, player1AISelectedLevel, player2AISelectedLevel, corJogador1, corJogador2, selectedFile, primaryStage);
            } else {
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Erro");
                alerta.setHeaderText(null);
                alerta.setContentText("Nenhum arquivo foi selecionado.");
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