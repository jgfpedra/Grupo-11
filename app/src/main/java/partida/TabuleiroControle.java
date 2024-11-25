package partida;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import pecas.Peca;
import view.TabuleiroView;

public class TabuleiroControle implements ObservadorTabuleiro {
    private Partida partida;
    private TabuleiroView tabuleiroView;
    private BiConsumer<Integer, Integer> callback;
    private Posicao origemSelecionada; // Para armazenar a posição da peça selecionada

    public TabuleiroControle(Partida partida, TabuleiroView tabuleiroView) {
        this.partida = partida;
        this.tabuleiroView = tabuleiroView;
        this.partida.getTabuleiro().adicionarObservador(this);
        initialize();
    }

    private void initialize() {
        callback = (row, col) -> {
            Posicao posicaoClicada = new Posicao(row, col);
    
            // Se já existe uma origem selecionada
            if (origemSelecionada != null) {
                // Caso o jogador clique na peça de origem novamente, cancelamos a seleção
                if (origemSelecionada.equals(posicaoClicada)) {
                    origemSelecionada = null;
                    tabuleiroView.clearSelection();
                }
                
                // Caso contrário, tentamos mover a peça
                List<Posicao> movimentosPossiveis = criarMovimento(origemSelecionada);
                if (movimentosPossiveis != null && movimentosPossiveis.contains(posicaoClicada)) {
                    Peca pecaMovida = partida.getTabuleiro().obterPeca(origemSelecionada);
                    if (pecaMovida != null) {
                        Movimento movimento = new Movimento(origemSelecionada, posicaoClicada, pecaMovida);
                        partida.jogar(movimento);  // Executa o movimento no modelo de jogo
                        tabuleiroView.moverPeca(origemSelecionada, posicaoClicada); // Atualiza o tabuleiro
                        origemSelecionada = null;  // Reseta a origem após o movimento
                        atualizar();  // Atualiza o tabuleiro após o movimento
                    }
                } else {
                    origemSelecionada = null;  // Se o movimento não é válido, desmarca a origem
                    tabuleiroView.clearSelection();
                }
            } else {
                // Se não houver origem selecionada, tentamos selecionar uma peça
                Peca pecaSelecionada = partida.getTabuleiro().obterPeca(posicaoClicada);
                if (pecaSelecionada != null && pecaSelecionada.getCor() == partida.getJogadorAtual().getCor()) {
                    origemSelecionada = posicaoClicada; // Marca a nova origem
                    List<Posicao> possiveisMovimentos = criarMovimento(origemSelecionada);
                    tabuleiroView.highlightPossibleMoves(possiveisMovimentos); // Destaca os movimentos possíveis
                    tabuleiroView.selecionarPeca(origemSelecionada); // Destaca a peça selecionada
                } else {
                    origemSelecionada = null; // Se não houver peça válida ou de cor correta, desmarque a origem
                    tabuleiroView.clearSelection();
                }
            }
        };
    
        // Registra o callback de clique para o TabuleiroView
        tabuleiroView.reconfigurarEventosDeClique(callback);
    }    

    private List<Posicao> criarMovimento(Posicao origem) {
        Peca pecaSelecionada = partida.getTabuleiro().obterPeca(origem);
        if (pecaSelecionada != null) {
            List<Posicao> movimentos = pecaSelecionada.proxMovimento(origem);
            return movimentos != null ? movimentos : new ArrayList<>();
        }
        return new ArrayList<>();
    }    

    @Override
    public void atualizar() {
        // Passando o callback configurado para o método updateTabuleiro
        tabuleiroView.updateTabuleiro(partida.getTabuleiro(), callback);
        atualizarCapturas();
    }

    private void atualizarCapturas() {
        // Limpar as capturas antigas antes de adicionar as novas
        tabuleiroView.getCapturasJogadorPreto().getChildren().clear();
        tabuleiroView.getCapturasJogadorBranco().getChildren().clear();
        
        // Adicionar peças capturadas do jogador preto
        List<Peca> capturadasPreto = partida.getTabuleiro().getCapturadasJogadorPreto();
        for (Peca peca : capturadasPreto) {
            tabuleiroView.adicionarCapturaPreto(peca);
        }
    
        // Adicionar peças capturadas do jogador branco
        List<Peca> capturadasBranco = partida.getTabuleiro().getCapturadasJogadorBranco();
        for (Peca peca : capturadasBranco) {
            tabuleiroView.adicionarCapturaBranco(peca);
        }
    }    
}