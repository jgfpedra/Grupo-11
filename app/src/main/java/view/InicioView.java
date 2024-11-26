package view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class InicioView {

    public InicioView(Stage primaryStage) {
        // Criação do layout do menu
        VBox inicioLayout = new VBox(10);
        inicioLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Título do jogo
        Label titleLabel = new Label("Jogo de Xadrez");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Botões de seleção
        Button jogarLocalButton = new Button("Jogar Local");
        jogarLocalButton.setOnAction(event -> {
            new PartidaLocalView(primaryStage);
        });

        Button jogarOnlineButton = new Button("Jogar Online");
        jogarOnlineButton.setOnAction(event -> {
            new PartidaOnlineView(primaryStage);
        });

        // Adicionar botões no layout
        inicioLayout.getChildren().addAll(
                titleLabel,
                jogarLocalButton,
                jogarOnlineButton
        );

        // Criar a cena do menu
        Scene menuScene = new Scene(inicioLayout, 800, 600);

        // Aplicar o CSS na cena
        menuScene.getStylesheets().add(getClass().getResource("/style/menu.css").toExternalForm());

        primaryStage.setTitle("Jogo de Xadrez - Início");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }
}