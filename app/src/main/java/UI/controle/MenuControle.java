package UI.controle;

import javafx.stage.Stage;
import partida.Partida;
import UI.view.InicioView;
import UI.view.MenuView;
import UI.view.TabuleiroView;

/**
 * Controlador responsável pela interação com o menu do jogo.
 * 
 * Esta classe gerencia as ações no menu de pausa do jogo, como exibir o menu, 
 * retornar ao tabuleiro e sair para a tela inicial.
 * 
 * As funcionalidades incluem:
 * - Mostrar o menu de pausa durante uma partida.
 * - Retornar ao tabuleiro de xadrez após o jogo ser continuado.
 * - Sair para a tela inicial, fechando a partida atual.
 * 
 * As interações com o menu são feitas através deste controlador,
 * que utiliza as views correspondentes para manipular a interface do usuário.
 */
public class MenuControle {

    private TabuleiroView tabuleiroView;
    private Stage primaryStage;

    /**
     * Construtor da classe MenuControle.
     * 
     * Inicializa o controlador com a referência da visualização do tabuleiro
     * e a janela principal da aplicação (primaryStage).
     *
     * @param tabuleiroView A view do tabuleiro de xadrez.
     * @param primaryStage A janela principal da aplicação.
     */
    public MenuControle(TabuleiroView tabuleiroView, Stage primaryStage) {
        this.tabuleiroView = tabuleiroView;
        this.primaryStage = primaryStage;
    }

    /**
     * Exibe a janela de menu de pausa.
     * 
     * Abre uma nova janela de menu, permitindo ao usuário escolher entre
     * continuar a partida ou sair para a tela inicial.
     *
     * @param partida A partida em andamento, que é passada para a view do menu.
     */
    public void mostrarMenu(Partida partida) {
        Stage menuStage = new Stage();
        new MenuView(menuStage, partida, this);
        menuStage.setTitle("Menu");
        menuStage.initOwner(primaryStage);
        menuStage.setResizable(false);
        menuStage.show();
    }

    /**
     * Retorna ao tabuleiro de xadrez, continuando o jogo.
     * 
     * Este método fecha o menu atual e exibe o tabuleiro, atualizando seu estado
     * para refletir que o jogo foi continuado.
     */
    public void retornarAoTabuleiro() {
        tabuleiroView.atualizarEstado("Jogo Continuado");
        primaryStage.show();
    }


    /**
     * Encerra a partida e retorna à tela inicial.
     * 
     * Este método fecha a janela atual e exibe a tela inicial do jogo,
     * permitindo que o usuário inicie uma nova partida.
     */
    public void sairAoInicio(){
        primaryStage.close();
        new InicioView(primaryStage);
    }
}