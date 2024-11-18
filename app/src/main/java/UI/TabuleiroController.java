package UI;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import partida.Tabuleiro;

public class TabuleiroController {
    private Tabuleiro tabuleiro;
    private TabuleiroView tabuleiroView;

    public TabuleiroController(Tabuleiro tabuleiro, TabuleiroView tabuleiroView) {
        this.tabuleiro = tabuleiro;
        this.tabuleiroView = tabuleiroView;
        initialize();
    }

    private void initialize() {
        tabuleiroView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int col = (int) event.getX() / TabuleiroView.TILE_SIZE;
                int row = (int) event.getY() / TabuleiroView.TILE_SIZE;

                // Handle click (e.g., select a piece or move)
                System.out.println("Clicked on: (" + row + ", " + col + ")");
            }
        });
    }
}