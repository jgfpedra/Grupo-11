package view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class PartidaOnlineMenuView {

    public PartidaOnlineMenuView(Stage primaryStage) {
        VBox menuLayout = new VBox(10);
        menuLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Título do jogo
        Label titleLabel = new Label("Jogo de Xadrez - Online");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Botão para criar partida
        Button criarPartidaButton = new Button("Criar Partida");
        criarPartidaButton.setStyle("-fx-font-size: 16px;");
        criarPartidaButton.setOnAction(event -> {
            // Quando clicar no botão "Criar Partida", exibe a tela de criação de partida
            new PartidaOnlineCriarView(primaryStage);
        });

        // Botão para entrar em uma partida
        Button entrarPartidaButton = new Button("Entrar em Partida");
        entrarPartidaButton.setStyle("-fx-font-size: 16px;");
        entrarPartidaButton.setOnAction(event -> {
            // Quando clicar no botão "Entrar em Partida", exibe a tela de entrar em uma partida
            new PartidaOnlineEntrarView(primaryStage);
        });

        Button voltarButton = new Button("Voltar");
        voltarButton.setStyle("-fx-font-size: 16px;");
        voltarButton.setOnAction(e -> {
            new InicioView(primaryStage);  // Supondo que você tenha uma classe MenuView para o menu principal
        });

        // Adicionar os botões ao layout
        menuLayout.getChildren().addAll(
                titleLabel,
                criarPartidaButton,
                entrarPartidaButton,
                voltarButton
        );

        // Criar a cena do menu
        Scene menuScene = new Scene(menuLayout, 1200, 900);
        menuScene.getStylesheets().add(getClass().getResource("/style/menu.css").toExternalForm());

        primaryStage.setTitle("Jogo de Xadrez - Online");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }
}
