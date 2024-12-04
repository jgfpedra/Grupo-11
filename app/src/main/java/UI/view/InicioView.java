package UI.view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class InicioView {

    public InicioView(Stage primaryStage) {
        VBox inicioLayout = new VBox(10);
        inicioLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        Label titleLabel = new Label("Jogo de Xadrez");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Button jogarLocalButton = new Button("Jogar Local");
        jogarLocalButton.setOnAction(event -> {
            new PartidaLocalView(primaryStage);
        });
        Button jogarOnlineButton = new Button("Jogar Online");
        jogarOnlineButton.setOnAction(event -> {
            new PartidaOnlineMenuView(primaryStage);
        });
        Button sairButton = new Button("Sair");
        sairButton.setOnAction(event -> {
            primaryStage.close();
        });
        inicioLayout.getChildren().addAll(
                titleLabel,
                jogarLocalButton,
                jogarOnlineButton,
                sairButton
        );
        Scene inicioScene = new Scene(inicioLayout, 800, 600);
        inicioScene.getStylesheets().add(getClass().getResource("/style/menu.css").toExternalForm());
        primaryStage.setTitle("Jogo de Xadrez - Início");
        primaryStage.setScene(inicioScene);
        primaryStage.show();
    }
}