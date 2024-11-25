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
            if (origemSelecionada != null) {
                List<Posicao> movimentosPossiveis = criarMovimento(origemSelecionada);
                // Verificar se o movimento clicado está dentro dos movimentos possíveis
                if (movimentosPossiveis != null && movimentosPossiveis.contains(posicaoClicada)) {
                    Peca pecaMovida = partida.getTabuleiro().obterPeca(origemSelecionada);
                    if (pecaMovida != null) {
                        Movimento movimento = new Movimento(origemSelecionada, posicaoClicada, pecaMovida);
                        partida.jogar(movimento);
                        tabuleiroView.moverPeca(origemSelecionada, posicaoClicada);
                        origemSelecionada = null;  
                        tabuleiroView.clearSelection();
                        atualizar();
                    }
                } else {
                    Peca pecaSelecionada = partida.getTabuleiro().obterPeca(posicaoClicada);
                    if (pecaSelecionada != null && pecaSelecionada.getCor() == partida.getJogadorAtual().getCor()) {
                        origemSelecionada = posicaoClicada;
                        List<Posicao> possiveisMovimentos = criarMovimento(origemSelecionada);
                        tabuleiroView.highlightPossibleMoves(possiveisMovimentos);
                        tabuleiroView.selecionarPeca(origemSelecionada);
                    } else {
                        origemSelecionada = null;
                        tabuleiroView.clearSelection();
                    }
                }
            } else {
                Peca pecaSelecionada = partida.getTabuleiro().obterPeca(posicaoClicada);
                if (pecaSelecionada != null && pecaSelecionada.getCor() == partida.getJogadorAtual().getCor()) {
                    origemSelecionada = posicaoClicada;
                    List<Posicao> possiveisMovimentos = criarMovimento(origemSelecionada);
                    tabuleiroView.highlightPossibleMoves(possiveisMovimentos);
                    tabuleiroView.selecionarPeca(origemSelecionada);
                } else {
                    origemSelecionada = null;
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