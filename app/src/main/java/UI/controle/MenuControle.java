package UI.controle;

import javafx.stage.Stage;
import partida.Partida;
import UI.view.InicioView;
import UI.view.MenuView;
import UI.view.TabuleiroView;

public class MenuControle {

    private TabuleiroView tabuleiroView;
    private Stage primaryStage;

    public MenuControle(TabuleiroView tabuleiroView, Stage primaryStage) {
        this.tabuleiroView = tabuleiroView;
        this.primaryStage = primaryStage;
    }

    public void mostrarMenu(Partida partida) {
        Stage menuStage = new Stage();
        new MenuView(menuStage, partida, this);
        menuStage.setTitle("Menu");
        menuStage.initOwner(primaryStage);
        menuStage.setResizable(false);
        menuStage.show();
    }

    public void retornarAoTabuleiro() {
        tabuleiroView.atualizarEstado("Jogo Continuado");
        primaryStage.show();
    }

    public void sairAoInicio(){
        primaryStage.close();
        new InicioView(primaryStage);
    }
}