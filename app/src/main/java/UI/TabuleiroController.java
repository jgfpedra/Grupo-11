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
        if (tabuleiroView.getOnMouseClicked() == null) {  // Verifica se já há um listener registrado
            tabuleiroView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    // Calcula a posição clicada
                    int col = (int) event.getX() / TabuleiroView.TILE_SIZE;
                    int row = (int) event.getY() / TabuleiroView.TILE_SIZE;
                    Posicao posicaoClicada = new Posicao(row, col);
    
                    // Debug: Mostra a posição clicada
                    System.out.println("Posição clicada: " + posicaoClicada.getLinha() + ", " + posicaoClicada.getColuna());
                    System.out.println("Origem selecionada: " + (origemSelecionada != null ? origemSelecionada.getLinha() + ", " + origemSelecionada.getColuna() : "Nenhuma"));
    
                    // Se a origem ainda não foi selecionada
                    if (origemSelecionada == null) {
                        // Verifica se a peça clicada é da cor do jogador atual
                        Peca pecaSelecionada = tabuleiroView.getTabuleiro().obterPeca(posicaoClicada);
    
                        // Debug: Verifica a peça clicada
                        System.out.println("Peça selecionada na posição: " + (pecaSelecionada != null ? pecaSelecionada.getCor() : "Nenhuma"));
    
                        // Se a peça foi selecionada e for da cor do jogador atual
                        if (pecaSelecionada != null && pecaSelecionada.getCor() == partida.getJogadorAtual().getCor()) {
                            // Marca a origem como a posição da peça clicada
                            origemSelecionada = posicaoClicada;
                            System.out.println("Origem selecionada em: " + origemSelecionada.getLinha() + ", " + origemSelecionada.getColuna());
    
                            List<Posicao> possiveisMovimentos = criarMovimento(origemSelecionada);
    
                            // Exibe os possíveis movimentos
                            if (possiveisMovimentos != null && !possiveisMovimentos.isEmpty()) {
                                System.out.println("Movimentos possíveis para a peça selecionada: " + possiveisMovimentos);
                                tabuleiroView.highlightPossibleMoves(possiveisMovimentos); // Marca os movimentos possíveis no tabuleiro
                            } else {
                                System.out.println("Não há movimentos possíveis para esta peça.");
                            }
                        } else {
                            // Caso a peça não seja válida, ou não seja da cor do jogador
                            System.out.println("Selecione uma peça válida para mover.");
                        }
                    } else {
                        // Se a origem foi selecionada, tenta mover
                        List<Posicao> movimentosPossiveis = criarMovimento(origemSelecionada);
                        System.out.println("Movimentos possíveis para a origem: " + movimentosPossiveis);
    
                        // Verifica se o destino clicado é válido
                        if (movimentosPossiveis != null && movimentosPossiveis.contains(posicaoClicada)) {
                            Peca pecaOrigem = tabuleiroView.getTabuleiro().obterPeca(origemSelecionada);
                            Movimento movimento = new Movimento(origemSelecionada, posicaoClicada, pecaOrigem);
    
                            // Realiza o movimento
                            partida.jogar(movimento);
                            System.out.println("Movimento realizado de " + origemSelecionada.getLinha() + ", " + origemSelecionada.getColuna() +
                                    " para " + posicaoClicada.getLinha() + ", " + posicaoClicada.getColuna());
    
                            // Atualiza a visualização do tabuleiro
                            tabuleiroView.updateTabuleiro(partida.getTabuleiro());
    
                            // Limpa a origem após o movimento
                            origemSelecionada = null;
                        } else {
                            // Caso o destino clicado não seja válido
                            System.out.println("Movimento inválido! Clique em um destino válido.");
                            origemSelecionada = null; // Limpa a origem, pois o clique foi inválido
                        }
                    }
                }
            });
        }
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