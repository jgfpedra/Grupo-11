package UI.view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.*;
import partida.Cor;

import java.io.File;
import UI.controle.PartidaOnlineControle;

public class PartidaOnlineCriarView {

    private PartidaOnlineControle partidaOnlineControle;
    private Image imagemJogador1;

    public PartidaOnlineCriarView(Stage primaryStage) {
        VBox menuLayout = new VBox(10);
        menuLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label titleLabel = new Label("Jogo de Xadrez - Criar Partida");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label nomeLabel = new Label("Digite seu nome:");
        TextField nomeTextField = new TextField();
        nomeTextField.setPromptText("Nome do jogador");

        Label imagemLabel = new Label("Escolha a imagem do jogador:");
        Button escolherImagemButton = new Button("Escolher Imagem");

        escolherImagemButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagem", "*.png", "*.jpg", "*.jpeg", "*.gif"));
            File arquivo = fileChooser.showOpenDialog(primaryStage);
            if (arquivo != null) {
                imagemJogador1 = new Image(arquivo.toURI().toString());
            }
        });

        Label portaServidorLabel = new Label("Porta do servidor:");
        TextField portaServidorTextField = new TextField();
        portaServidorTextField.setPromptText("Porta do servidor");

        partidaOnlineControle = new PartidaOnlineControle(primaryStage);

        Button criarPartidaButton = new Button("Criar Partida");
        criarPartidaButton.setOnAction(event -> {
            String nomeJogador1 = nomeTextField.getText();
            int porta = Integer.parseInt(portaServidorTextField.getText());
            if (!nomeJogador1.isEmpty() && imagemJogador1 != null && porta > 0) {
                if(partidaOnlineControle.criarPartida(nomeJogador1, imagemJogador1, Cor.BRANCO, porta)){
                    System.out.println("Jogo iniciado");
                }
            } else {
                // Mostrar alerta se nome ou imagem não forem preenchidos
                Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor, preencha seu nome e escolha uma imagem.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        Button voltarButton = new Button("Voltar");
        voltarButton.setStyle("-fx-font-size: 16px;");
        voltarButton.setOnAction(e -> {
            new PartidaOnlineMenuView(primaryStage);
        });

        menuLayout.getChildren().addAll(
                titleLabel,
                nomeLabel,
                nomeTextField,
                imagemLabel,
                escolherImagemButton,
                portaServidorLabel,
                portaServidorTextField,
                criarPartidaButton,
                voltarButton
        );

        Scene menuScene = new Scene(menuLayout, 800, 800);
        menuScene.getStylesheets().add(getClass().getResource("/style/menu.css").toExternalForm());
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }
}