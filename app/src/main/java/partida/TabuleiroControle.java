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
        // Configurar o evento de clique no tabuleiro
        tabuleiroView.setOnTileClicked((row, col) -> {
            Posicao posicaoClicada = new Posicao(row, col);

            if (origemSelecionada == null) {
                // Seleção de peça inicial
                Peca pecaSelecionada = partida.getTabuleiro().obterPeca(posicaoClicada);
                if (pecaSelecionada != null && pecaSelecionada.getCor() == partida.getJogadorAtual().getCor()) {
                    origemSelecionada = posicaoClicada;
                    List<Posicao> possiveisMovimentos = criarMovimento(origemSelecionada);
                    tabuleiroView.highlightPossibleMoves(possiveisMovimentos);
                    tabuleiroView.selecionarPeca(origemSelecionada);
                } else {
                    tabuleiroView.mostrarMensagem("Você não pode selecionar a peça do adversário!");
                }
            } else {
                // Tentativa de movimento
                List<Posicao> movimentosPossiveis = criarMovimento(origemSelecionada);
                if (movimentosPossiveis != null && movimentosPossiveis.contains(posicaoClicada)) {
                    Movimento movimento = new Movimento(origemSelecionada, posicaoClicada, partida.getTabuleiro().obterPeca(posicaoClicada));
                    partida.jogar(movimento);
                    tabuleiroView.moverPeca(origemSelecionada, posicaoClicada);
                    origemSelecionada = null;
                } else {
                    tabuleiroView.mostrarMensagem("Movimento inválido!");
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