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
        // Configura a ação de clicar nas casas do tabuleiro para selecionar a peça ou
        // mover
        tabuleiroView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            int col = (int) event.getX() / TabuleiroView.TILE_SIZE;
            int row = (int) event.getY() / TabuleiroView.TILE_SIZE;
            Posicao posicaoClicada = new Posicao(row, col);

            // Obter a peça na posição clicada
            Peca pecaClicada = partida.getTabuleiro().obterPeca(posicaoClicada);
            System.out.println("Peca clicada: " + pecaClicada);

            if (origemSelecionada == null) { 
                System.out.println("Sem origem selecionada");
                // Nenhuma origem foi selecionada: seleciona a peça
                if (pecaClicada != null && pecaClicada.getCor() == partida.getJogadorAtual().getCor()) {
                    System.out.println("Peca do jogador da vez");
                    origemSelecionada = posicaoClicada;
                    criarMovimento(posicaoClicada);
                    List<Posicao> movimentosPossiveis = pecaClicada.proxMovimento(origemSelecionada);
                    tabuleiroView.highlightPossibleMoves(movimentosPossiveis);
                    System.out.println("Peça selecionada: " + pecaClicada + " na posição " + origemSelecionada);
                    System.out.println("Destino clicado: " + posicaoClicada);
                } else {
                    System.out.println("Seleção inválida. Escolha uma peça válida.");
                }
            } else {
                System.out.println("Origem selecionada");
                // Uma peça foi selecionada: tenta mover
                if (pecaClicada == null || pecaClicada.getCor() != partida.getJogadorAtual().getCor()) {
                    try {
                        Movimento movimento = new Movimento(origemSelecionada, posicaoClicada, 
                                partida.getTabuleiro().obterPeca(origemSelecionada));
                        partida.jogar(movimento);
                        tabuleiroView.updateTabuleiro(partida.getTabuleiro());
                        System.out.println("Movimento realizado de " + origemSelecionada + " para " + posicaoClicada);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Movimento inválido: " + e.getMessage());
                    }
                    origemSelecionada = null; // Limpa a seleção após o movimento
                } else {
                    // Seleciona outra peça do mesmo jogador
                    origemSelecionada = posicaoClicada;
                    System.out.println("Nova peça selecionada: " + pecaClicada + " na posição " + origemSelecionada);
                }
            }
            
        }
    });
    }

    private List<Posicao> criarMovimento(Posicao origem) {
        // Aqui, você precisa determinar qual peça foi selecionada
        Peca pecaSelecionada = tabuleiroView.getTabuleiro().obterPeca(origem); // Obtém a peça na posição clicada
        if (pecaSelecionada != null) {
            // Obtém os próximos movimentos possíveis para a peça selecionada
            return pecaSelecionada.proxMovimento(origem); // Método a ser implementado nas classes de Peca
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