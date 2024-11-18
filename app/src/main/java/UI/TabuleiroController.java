package UI;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import partida.Movimento;
import partida.ObservadorTabuleiro;
import partida.Partida;
import partida.Posicao;
import pecas.Peca;

public class TabuleiroController implements ObservadorTabuleiro{
    private Partida partida;
    private TabuleiroView tabuleiroView;

    public TabuleiroController(Partida partida, TabuleiroView tabuleiroView) {
        this.partida = partida;
        this.tabuleiroView = tabuleiroView;
        initialize();
    }

    private void initialize() {
        tabuleiroView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int col = (int) event.getX() / TabuleiroView.TILE_SIZE;
                int row = (int) event.getY() / TabuleiroView.TILE_SIZE;
                
                Posicao origem = new Posicao(row, col);
                Movimento movimento = criarMovimento(origem);

                if(movimento != null){

                }
            }
        });
    }

    private Movimento criarMovimento(Posicao origem) {
        // Implementação de como criar um movimento a partir de uma posição clicada
        Peca pecaSelecionada = tabuleiroView.getTabuleiro().obterPeca(origem); // Obtem a peça na posição clicada
        if (pecaSelecionada != null) {
            // Gerar o movimento de acordo com a peça selecionada
            return pecaSelecionada.proxMovimento(origem); // Método a ser implementado na classe Peca
        }
        return null;
    }

    @Override
    public void atualizar() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'atualizar'");
    }
}