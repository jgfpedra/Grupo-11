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

    public TabuleiroController(Partida partida, TabuleiroView tabuleiroView) {
        this.partida = partida;
        this.tabuleiroView = tabuleiroView;
        // Registra este controlador como observador do tabuleiro
        this.partida.getTabuleiro().adicionarObservador(this);
        initialize();
    }

    private void initialize() {
        // Configura a ação de clicar nas casas do tabuleiro
        tabuleiroView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int col = (int) event.getX() / TabuleiroView.TILE_SIZE;
                int row = (int) event.getY() / TabuleiroView.TILE_SIZE;
                
                Posicao origem = new Posicao(row, col);
                List<Posicao> possiveisMovimentos = criarMovimento(origem);

                if (possiveisMovimentos != null && !possiveisMovimentos.isEmpty()) {
                    // Exibe os possíveis movimentos para o usuário
                    tabuleiroView.highlightPossibleMoves(possiveisMovimentos);

                    // Agora, o usuário pode clicar na posição desejada para realizar o movimento
                    tabuleiroView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            int col = (int) event.getX() / TabuleiroView.TILE_SIZE;
                            int row = (int) event.getY() / TabuleiroView.TILE_SIZE;

                            Posicao destino = new Posicao(row, col);
                            if (possiveisMovimentos.contains(destino)) {
                                // Obter a peça selecionada para criar o movimento com ela
                                Peca pecaSelecionada = tabuleiroView.getTabuleiro().obterPeca(origem);
                                
                                // Criação do movimento com a peça, origem e destino
                                Movimento movimento = new Movimento(origem, destino, pecaSelecionada);
                                
                                // Realiza o movimento no jogo
                                partida.jogar(movimento);
                                // Atualiza a visualização do tabuleiro após o movimento
                                tabuleiroView.updateTabuleiro(partida.getTabuleiro());
                            }
                        }
                    });
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