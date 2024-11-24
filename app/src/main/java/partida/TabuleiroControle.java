package partida;

import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import pecas.Peca;
import view.TabuleiroView;

public class TabuleiroControle implements ObservadorTabuleiro {
    private Partida partida;
    private TabuleiroView tabuleiroView;
    private Posicao origemSelecionada; // Para armazenar a posição da peça selecionada

    public TabuleiroControle(Partida partida, TabuleiroView tabuleiroView) {
        this.partida = partida;
        this.tabuleiroView = tabuleiroView;
        this.partida.getTabuleiro().adicionarObservador(this);
        initialize();
    }

    private void initialize() {
        if (tabuleiroView.getOnMouseClicked() == null) {
            tabuleiroView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    int col = (int) event.getX() / TabuleiroView.TILE_SIZE;
                    int row = (int) event.getY() / TabuleiroView.TILE_SIZE;
                    col = Math.min(Math.max(col, 0), 7);
                    row = Math.min(Math.max(row, 0), 7);
                    Posicao posicaoClicada = new Posicao(row, col);
    
                    // Verifica se a peça clicada é do jogador atual
                    if (origemSelecionada == null) {
                        Peca pecaSelecionada = partida.getTabuleiro().obterPeca(posicaoClicada);
                        if (pecaSelecionada != null && pecaSelecionada.getCor() == partida.getJogadorAtual().getCor()) {
                            origemSelecionada = posicaoClicada;
                            List<Posicao> possiveisMovimentos = criarMovimento(origemSelecionada);
                            if (possiveisMovimentos != null && !possiveisMovimentos.isEmpty()) {
                                tabuleiroView.highlightPossibleMoves(possiveisMovimentos);
                                tabuleiroView.selecionarPeca(origemSelecionada);
                            }
                        } else {
                            System.out.println("Você não pode selecionar a peça do adversário!");
                        }
                    } else {
                        List<Posicao> movimentosPossiveis = criarMovimento(origemSelecionada);
                        if (movimentosPossiveis != null && movimentosPossiveis.contains(posicaoClicada)) {
                            tabuleiroView.moverPeca(posicaoClicada);
                            origemSelecionada = null;
                        } else {
                            origemSelecionada = posicaoClicada;
                            Peca novaPecaSelecionada = partida.getTabuleiro().obterPeca(origemSelecionada);
                            if (novaPecaSelecionada != null && novaPecaSelecionada.getCor() == partida.getJogadorAtual().getCor()) {
                                List<Posicao> possiveisMovimentos = criarMovimento(origemSelecionada);
                                if (possiveisMovimentos != null && !possiveisMovimentos.isEmpty()) {
                                    tabuleiroView.highlightPossibleMoves(possiveisMovimentos);
                                    tabuleiroView.selecionarPeca(origemSelecionada);
                                }
                            } else {
                                origemSelecionada = null;  // Se a peça não for do jogador atual, desmarque a origem
                                System.out.println("Você não pode selecionar a peça do adversário!");
                            }
                        }
                    }
                }
            });
        }
    }

    private List<Posicao> criarMovimento(Posicao origem) {
        Peca pecaSelecionada = partida.getTabuleiro().obterPeca(origem);
        if (pecaSelecionada != null) {
            return pecaSelecionada.proxMovimento(origem);
        }
        return null;
    }  

    @Override
    public void atualizar() {
        tabuleiroView.updateTabuleiro(partida.getTabuleiro());
    }
}