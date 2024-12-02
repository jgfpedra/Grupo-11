package UI.view;

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
            new PartidaOnlineMenuView(primaryStage);
        });

        // Botão para sair do jogo
        Button sairButton = new Button("Sair");
        sairButton.setOnAction(event -> {
            // Fecha o aplicativo
            primaryStage.close();
        });

        // Adicionar botões no layout
        inicioLayout.getChildren().addAll(
                titleLabel,
                jogarLocalButton,
                jogarOnlineButton,
                sairButton
        );

        // Criar a cena do menu
        Scene inicioScene = new Scene(inicioLayout, 800, 600);

        // Aplicar o CSS na cena
        inicioScene.getStylesheets().add(getClass().getResource("/style/menu.css").toExternalForm());

        primaryStage.setTitle("Jogo de Xadrez - Início");
        primaryStage.setScene(inicioScene);
        primaryStage.show();
    }
}