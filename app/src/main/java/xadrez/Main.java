package xadrez;

import javafx.application.Application;
import javafx.stage.Stage;
import view.InicioView;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Criação da interface do menu através da classe MenuView
        new InicioView(primaryStage);
    }
}