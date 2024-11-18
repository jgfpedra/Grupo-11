package UI;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TabuleiroView extends GridPane {
    protected static final int TILE_SIZE = 100;
    private static final int ROWS = 8;
    private static final int COLS = 8;

    public TabuleiroView() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                tile.setFill((row + col) % 2 == 0 ? Color.BEIGE : Color.BROWN);
                add(tile, col, row); // Add tile to the grid
            }
        }
    }
}
