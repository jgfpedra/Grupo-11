package UI.view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

/**
 * A classe de visualização inicial do jogo de xadrez.
 * 
 * Esta classe cria a interface gráfica de início do jogo, onde o usuário pode escolher se deseja
 * jogar localmente ou online, ou então sair do aplicativo. Ela exibe os botões de opção correspondentes
 * e lida com os eventos gerados por esses botões.
 */
public class InicioView {

    /**
     * Constrói a tela inicial do jogo de xadrez.
     * 
     * Ao ser instanciada, esta classe cria uma janela com o título "Jogo de Xadrez", contendo botões
     * para jogar localmente, jogar online ou sair do jogo. Cada botão tem um comportamento associado
     * que determina qual ação será realizada quando clicado.
     * 
     * @param primaryStage A janela principal da aplicação.
     */
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