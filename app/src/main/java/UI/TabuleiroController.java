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
        // Configura a ação de clicar nas casas do tabuleiro para a peça ou mover
        if (tabuleiroView.getOnMouseClicked() == null) {
            tabuleiroView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    int col = (int) event.getX() / TabuleiroView.TILE_SIZE;
                    int row = (int) event.getY() / TabuleiroView.TILE_SIZE;
                    col = Math.min(Math.max(col, 0), 7);
                    row = Math.min(Math.max(row, 0), 7);
                    Posicao posicaoClicada = new Posicao(row, col);

                    tabuleiroView.clearHighlights();
    
                    if (origemSelecionada == null) {
                        // Se ainda não há nenhuma peça selecionada
                        Peca pecaSelecionada = tabuleiroView.getTabuleiro().obterPeca(posicaoClicada);
                        if (pecaSelecionada != null && pecaSelecionada.getCor() == partida.getJogadorAtual().getCor()) {
                            origemSelecionada = posicaoClicada;

    
                            // Destacar os movimentos possíveis da nova peça
                            List<Posicao> possiveisMovimentos = criarMovimento(origemSelecionada);
                            if (possiveisMovimentos != null && !possiveisMovimentos.isEmpty()) {
                                System.out.println("Possiveis movimentos: " + possiveisMovimentos);
                                tabuleiroView.highlightPossibleMoves(possiveisMovimentos);
                            }
                        }
                    } else {
                        List<Posicao> movimentosPossiveis = criarMovimento(origemSelecionada);
                        if (movimentosPossiveis != null && movimentosPossiveis.contains(posicaoClicada)) {
                            Peca pecaOrigem = tabuleiroView.getTabuleiro().obterPeca(origemSelecionada);
                            Movimento movimento = new Movimento(origemSelecionada, posicaoClicada, pecaOrigem);
                            if (movimento.validarMovimento(partida.getTabuleiro())) {
                                System.out.println(movimento);
                                partida.jogar(movimento);
                                tabuleiroView.updateTabuleiro(partida.getTabuleiro());
                                origemSelecionada = null;  // Resetando a seleção após o movimento
                                tabuleiroView.clearSelection();  // Reseta visualmente a seleção
                                atualizarCapturas();  // Atualiza as peças capturadas
                            } else {
                                System.out.println("Movimento inválido! Clique em um destino válido.");
                                origemSelecionada = null;  // Resetando a seleção ao tentar movimento inválido
                                tabuleiroView.clearSelection();  // Reseta visualmente a seleção
                            }
                        } else {
                            origemSelecionada = null;  // Caso o clique não seja em um movimento válido, reseta a seleção
                            tabuleiroView.clearSelection();  // Reseta visualmente a seleção
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
        tabuleiroView.updateTabuleiro(partida.getTabuleiro());
        String estadoJogo = partida.getEstadoJogo().toString();
        tabuleiroView.updateEstadoJogo(estadoJogo);
        atualizarCapturas();
    }

    private void atualizarCapturas() {
        tabuleiroView.atualizarCapturas(
            partida.getTabuleiro().getCapturadasJogador1(),
            partida.getTabuleiro().getCapturadasJogador2()
        );
    }

}