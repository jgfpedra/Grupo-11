package xadrez;

import UI.TabuleiroController;
import UI.TabuleiroView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import partida.Tabuleiro;

public class Main extends Application{
    public static void main(String[] args){
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        Tabuleiro tabuleiro = new Tabuleiro();
        TabuleiroView tabuleiroView = new TabuleiroView();
        new TabuleiroController(tabuleiro, tabuleiroView);

        primaryStage.setTitle("Chess Game");
        primaryStage.setScene(new Scene(tabuleiroView, 640, 640));
        primaryStage.show();
    }
}