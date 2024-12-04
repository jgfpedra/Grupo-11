package UI.view;

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

        Button criarPartidaButton = new Button("Criar Partida");
        criarPartidaButton.setStyle("-fx-font-size: 16px;");
        criarPartidaButton.setOnAction(event -> {
            new PartidaOnlineCriarView(primaryStage);
        });

        Button entrarPartidaButton = new Button("Entrar em Partida");
        entrarPartidaButton.setStyle("-fx-font-size: 16px;");
        entrarPartidaButton.setOnAction(event -> {
            new PartidaOnlineEntrarView(primaryStage);
        });

        Button voltarButton = new Button("Voltar");
        voltarButton.setStyle("-fx-font-size: 16px;");
        voltarButton.setOnAction(e -> {
            new InicioView(primaryStage);
        });

        menuLayout.getChildren().addAll(
                titleLabel,
                criarPartidaButton,
                entrarPartidaButton,
                voltarButton
        );

        Scene menuScene = new Scene(menuLayout, 800, 800);
        menuScene.getStylesheets().add(getClass().getResource("/style/menu.css").toExternalForm());

        primaryStage.setTitle("Jogo de Xadrez - Online");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }
}