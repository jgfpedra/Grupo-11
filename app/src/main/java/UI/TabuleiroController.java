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
    private Posicao origemSelecionada;  // Para armazenar a posição da peça selecionada

    public TabuleiroController(Partida partida, TabuleiroView tabuleiroView) {
        this.partida = partida;
        this.tabuleiroView = tabuleiroView;
        // Registra este controlador como observador do tabuleiro
        this.partida.getTabuleiro().adicionarObservador(this);
        initialize();
    }

    private void initialize() {
        // Configura a ação de clicar nas casas do tabuleiro para selecionar a peça ou mover
        tabuleiroView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int col = (int) event.getX() / TabuleiroView.TILE_SIZE;
                int row = (int) event.getY() / TabuleiroView.TILE_SIZE;
                Posicao posicaoClicada = new Posicao(row, col);
    
                // Verifica se a peça clicada é da cor do jogador atual
                Peca pecaSelecionada = tabuleiroView.getTabuleiro().obterPeca(posicaoClicada);
    
                if (pecaSelecionada != null && pecaSelecionada.getCor() == partida.getJogadorAtual().getCor()) {
                    // Se a peça for da cor do jogador, procede com a seleção e exibição dos movimentos possíveis
                    origemSelecionada = posicaoClicada;
                    List<Posicao> possiveisMovimentos = criarMovimento(origemSelecionada);
    
                    // Exibe os possíveis movimentos
                    if (possiveisMovimentos != null && !possiveisMovimentos.isEmpty()) {
                        tabuleiroView.highlightPossibleMoves(possiveisMovimentos);
                    }
                } else if (origemSelecionada != null) {
                    // Caso a peça não seja da cor do jogador, e a origem foi selecionada, tentamos mover
                    List<Posicao> movimentosPossiveis = criarMovimento(origemSelecionada);
                    
                    if (movimentosPossiveis != null && movimentosPossiveis.contains(posicaoClicada)) {
                        // Obtém a peça selecionada da posição origem
                        Peca pecaOrigem = tabuleiroView.getTabuleiro().obterPeca(origemSelecionada);
                        Movimento movimento = new Movimento(origemSelecionada, posicaoClicada, pecaOrigem);
                        
                        // Realiza o movimento
                        partida.jogar(movimento);
    
                        // Atualiza a visualização do tabuleiro após o movimento
                        tabuleiroView.updateTabuleiro(partida.getTabuleiro());
                        origemSelecionada = null; // Limpa a seleção após o movimento
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
            return pecaSelecionada.proxMovimento(origem);  // Método a ser implementado nas classes de Peca
        }
        return null;
    }

    @Override
    public void atualizar() {
        // Esse método é chamado quando há uma atualização no tabuleiro
        // Aqui, você pode atualizar a interface visual, como exibir o estado do jogo
        tabuleiroView.updateTabuleiro(partida.getTabuleiro());
        // Aqui você pode também atualizar outros componentes de UI, como um rótulo que mostra "Check" ou "Checkmate"
        String estadoJogo = partida.getEstadoJogo().toString();
        tabuleiroView.updateEstadoJogo(estadoJogo);  // Supondo que você tenha um método que atualiza o estado do jogo na UI
    }
}