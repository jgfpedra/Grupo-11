package UI;

import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import partida.Movimento;
import partida.ObservadorTabuleiro;
import partida.Partida;
import partida.Posicao;
import pecas.Peca;

public class TabuleiroController implements ObservadorTabuleiro {
    private Partida partida;
    private TabuleiroView tabuleiroView;
    private Posicao origemSelecionada; // Para armazenar a posição da peça selecionada

    public TabuleiroController(Partida partida, TabuleiroView tabuleiroView) {
        this.partida = partida;
        this.tabuleiroView = tabuleiroView;
        // Registra este controlador como observador do tabuleiro
        this.partida.getTabuleiro().adicionarObservador(this);
        initialize();
    }

    private void initialize() {
        // Configura a ação de clicar nas casas do tabuleiro para selecionar a peça ou mover
        if (tabuleiroView.getOnMouseClicked() == null) {
            tabuleiroView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    int col = (int) event.getX() / TabuleiroView.TILE_SIZE;
                    int row = (int) event.getY() / TabuleiroView.TILE_SIZE;
                    Posicao posicaoClicada = new Posicao(row, col);

                    if (origemSelecionada == null) {
                        Peca pecaSelecionada = tabuleiroView.getTabuleiro().obterPeca(posicaoClicada);
                        if (pecaSelecionada != null && pecaSelecionada.getCor() == partida.getJogadorAtual().getCor()) {
                            origemSelecionada = posicaoClicada;
                            List<Posicao> possiveisMovimentos = criarMovimento(origemSelecionada);

                            if (possiveisMovimentos != null && !possiveisMovimentos.isEmpty()) {
                                tabuleiroView.highlightPossibleMoves(possiveisMovimentos);
                            }
                        }
                    } else {
                        List<Posicao> movimentosPossiveis = criarMovimento(origemSelecionada);
                        if (movimentosPossiveis != null && movimentosPossiveis.contains(posicaoClicada)) {
                            Peca pecaOrigem = tabuleiroView.getTabuleiro().obterPeca(origemSelecionada);
                            Movimento movimento = new Movimento(origemSelecionada, posicaoClicada, pecaOrigem);
                            if (movimento.validarMovimento(partida.getTabuleiro())) {
                                partida.jogar(movimento);
                                tabuleiroView.updateTabuleiro(partida.getTabuleiro());
                                origemSelecionada = null;
                            } else {
                                System.out.println("Movimento inválido! Clique em um destino válido.");
                                origemSelecionada = null;
                            }
                        }
                    }
                }
            });
        }
    }

    private List<Posicao> criarMovimento(Posicao origem) {
        Peca pecaSelecionada = tabuleiroView.getTabuleiro().obterPeca(origem);
        if (pecaSelecionada != null) {
            return pecaSelecionada.proxMovimento(origem);
        }
        return null;
    }  

    @Override
    public void atualizar() {
        // Esse método é chamado quando há uma atualização no tabuleiro
        // Aqui, você pode atualizar a interface visual, como exibir o estado do jogo
        tabuleiroView.updateTabuleiro(partida.getTabuleiro());
        // Aqui você pode também atualizar outros componentes de UI, como um rótulo que
        // mostra "Check" ou "Checkmate"
        String estadoJogo = partida.getEstadoJogo().toString();
        tabuleiroView.updateEstadoJogo(estadoJogo); // Supondo que você tenha um método que atualiza o estado do jogo na
    }

}