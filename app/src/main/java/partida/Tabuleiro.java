package partida;

import java.util.ArrayList;
import java.util.List;

public class Tabuleiro {
    private List<List<Casa>> casas;
    private ArrayList<ObservadorTabuleiro> observadores;

    public Tabuleiro() {
        // Initialize the board as a list of lists (8x8 board)
        casas = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            casas.add(new ArrayList<>());  // Add a new row (List<Casa>) for each row of the board
            for (int j = 0; j < 8; j++) {
                casas.get(i).add(new Casa(i, j));  // Add a new Casa object to each row
            }
        }
    }

    // Getters, setters, and game logic methods can be added here

    public Casa getCasa(int linha, int coluna) {
        return casas.get(linha).get(coluna);
    }

    public void setCasa(Casa casa, int linha, int coluna) {
        casas.get(linha).set(coluna, casa);
    }
}