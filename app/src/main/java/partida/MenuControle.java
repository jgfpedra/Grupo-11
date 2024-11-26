package partida;

import javafx.stage.Stage;
import view.MenuView;
import view.TabuleiroView;

public class MenuControle {

    private TabuleiroView tabuleiroView;  // Referência ao TabuleiroView
    private Stage primaryStage;  // Stage principal para controle da janela

    public MenuControle(TabuleiroView tabuleiroView, Stage primaryStage) {
        this.tabuleiroView = tabuleiroView;
        this.primaryStage = primaryStage;
    }

    public void mostrarMenu(Partida partida) {
        Stage menuStage = new Stage();
        new MenuView(menuStage, partida, this);  // Passa o controle para o MenuView
        menuStage.setTitle("Menu");
        menuStage.initOwner(primaryStage);  // Relaciona o Stage do menu com o principal
        menuStage.setResizable(false);
        menuStage.show();
    }

    public void retornarAoTabuleiro() {
        tabuleiroView.atualizarEstado("Jogo Continuado");
        primaryStage.show();
    }

    public void sairAoInicio(){
        
    }
}