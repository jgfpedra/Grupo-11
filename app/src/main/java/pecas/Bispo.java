package pecas;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import partida.Cor;
import partida.Posicao;

public class Bispo extends Peca{

    public Bispo(Cor cor, Image imagem){
        super(cor, imagem);
    }
    @Override
    public List<Posicao> proxMovimento(Posicao origem) {
        List<Posicao> movimentosValidos = new ArrayList<>();
        // Diagonal movements (top-left, top-right, bottom-left, bottom-right)
        for (int i = 1; i < 8; i++) {
            // Top-left diagonal
            if (origem.getLinha() - i >= 0 && origem.getColuna() - i >= 0) {
                movimentosValidos.add(new Posicao(origem.getLinha() - i, origem.getColuna() - i));
            }
            // Top-right diagonal
            if (origem.getLinha() - i >= 0 && origem.getColuna() + i < 8) {
                movimentosValidos.add(new Posicao(origem.getLinha() - i, origem.getColuna() + i));
            }
            // Bottom-left diagonal
            if (origem.getLinha() + i < 8 && origem.getColuna() - i >= 0) {
                movimentosValidos.add(new Posicao(origem.getLinha() + i, origem.getColuna() - i));
            }
            // Bottom-right diagonal
            if (origem.getLinha() + i < 8 && origem.getColuna() + i < 8) {
                movimentosValidos.add(new Posicao(origem.getLinha() + i, origem.getColuna() + i));
            }
        }
        // Assuming Movimento can accept a list of valid positions
        return movimentosValidos;
    }
}
