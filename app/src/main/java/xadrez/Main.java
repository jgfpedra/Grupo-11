package xadrez;

import javafx.application.Application;
import javafx.stage.Stage;
import UI.view.InicioView;

/**
 * Classe principal que inicia a aplicação do jogo de Xadrez.
 * 
 * Esta classe herda de `Application` e é responsável por inicializar a aplicação JavaFX.
 * O método `main` chama o método `launch`, que, por sua vez, chama o método `start` para iniciar a interface gráfica.
 */
public class Main extends Application {

    /**
     * Método principal que inicia a aplicação.
     * 
     * @param args Argumentos de linha de comando passados para a aplicação. Este método chama `launch` para iniciar a execução da aplicação JavaFX.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Método de inicialização da interface gráfica da aplicação.
     * 
     * Este método é chamado automaticamente pela framework JavaFX quando a aplicação é iniciada. Ele cria a primeira view da aplicação, que é a tela inicial do jogo.
     * 
     * @param primaryStage O palco principal da aplicação, onde a interface gráfica será exibida.
     */
    @Override
    public void start(Stage primaryStage) {
       new InicioView(primaryStage);
    }
}