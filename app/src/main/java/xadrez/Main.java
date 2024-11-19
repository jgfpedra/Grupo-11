package xadrez;

import UI.TabuleiroController;
import UI.TabuleiroView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jogador.JogadorIA;
import partida.Partida;
import partida.Tabuleiro;

public class Main extends Application{
    public static void main(String[] args){
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        VBox menuLayout = new VBox(10);
        menuLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label player1Label = new Label("Selecione o Jogador 1: ");
        ToggleGroup player1Group = new ToggleGroup();
        RadioButton player1Local = new RadioButton("Local");
        RadioButton player1AI = new RadioButton("Inteligencia Artificial");
        player1Local.setToggleGroup(player1Group);
        player1AI.setToggleGroup(player1Group);
        player1Local.setSelected(true);

        Label player2Label = new Label("Selecione o Jogador 1: ");
        ToggleGroup player2Group = new ToggleGroup();
        RadioButton player2Local = new RadioButton("Local");
        RadioButton player2AI = new RadioButton("Inteligencia Artificial");
        RadioButton player2Online = new RadioButton("Online");
        player2Local.setToggleGroup(player2Group);
        player2AI.setToggleGroup(player2Group);
        player2Online.setToggleGroup(player2Group);
        player2Local.setSelected(true);

        Button botaoIniciar = new Button("Iniciar Jogo");
        botaoIniciar.setOnAction(event -> {
            boolean player1IsAI = player1AI.isSelected();
            boolean player2IsAI = player1AI.isSelected();
            iniciarJogo(player1IsAI, player2IsAI, primaryStage);
        });
    }
    private void startGame(boolean player1IsAI, boolean player2IsAI, Stage primaryStage) {
        // Create the partida (game) and set the players

        
        // Initialize players based on the selections
        JogadorIA player1 = player1IsAI ? new AIPlayer() : new HumanPlayer();
        JogadorIA player2 = player2IsAI ? new AIPlayer() : new HumanPlayer();
        
        Partida partida = new Partida();
        
        // Create the board view and controller
        TabuleiroView tabuleiroView = new TabuleiroView();
        new TabuleiroController(partida, tabuleiroView); // Assuming partida.getTabuleiro() returns the board
        
        // Show the chessboard scene
        primaryStage.setTitle("Jogo de Xadrez");
        primaryStage.setScene(new Scene(tabuleiroView, 640, 640));
        primaryStage.show();
    }
}