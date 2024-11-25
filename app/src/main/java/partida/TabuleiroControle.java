package partida;

import java.util.List;
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
        tabuleiroView.setOnTileClicked((row, col) -> {
            Posicao posicaoClicada = new Posicao(row, col);
            System.out.println("Célula clicada: Linha " + row + ", Coluna " + col);
    
            // Caso o jogador já tenha uma peça selecionada
            if (origemSelecionada != null) {
                // Se a peça clicada for um movimento válido
                List<Posicao> movimentosPossiveis = criarMovimento(origemSelecionada);
                if (movimentosPossiveis != null && movimentosPossiveis.contains(posicaoClicada)) {
                    // Se o movimento for válido, efetua o movimento
                    Movimento movimento = new Movimento(origemSelecionada, posicaoClicada, partida.getTabuleiro().obterPeca(posicaoClicada));
                    System.out.println("a");
                    partida.jogar(movimento);
                    tabuleiroView.moverPeca(origemSelecionada, posicaoClicada);
                    origemSelecionada = null;
                    tabuleiroView.clearSelection();
                } else {
                    origemSelecionada = null; // Limpa a seleção
                    tabuleiroView.clearSelection(); // Limpa o destaque de seleção
                }
                origemSelecionada = posicaoClicada;
                List<Posicao> possiveisMovimentos = criarMovimento(origemSelecionada);
                tabuleiroView.highlightPossibleMoves(possiveisMovimentos); // Destaca os movimentos possíveis
                tabuleiroView.selecionarPeca(origemSelecionada); // Marca a peça como selecionada
            } else {
                // Caso não tenha nenhuma peça selecionada, tenta selecionar uma nova peça
                Peca pecaSelecionada = partida.getTabuleiro().obterPeca(posicaoClicada);
                if (pecaSelecionada != null && pecaSelecionada.getCor() == partida.getJogadorAtual().getCor()) {
                    // Se uma peça for clicada, não precisamos limpar nada, apenas selecionamos ela
                    origemSelecionada = posicaoClicada;
                    List<Posicao> possiveisMovimentos = criarMovimento(origemSelecionada);
                    tabuleiroView.highlightPossibleMoves(possiveisMovimentos); // Destaca os movimentos possíveis
                    tabuleiroView.selecionarPeca(origemSelecionada); // Marca a peça como selecionada
                }
            }
        });
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