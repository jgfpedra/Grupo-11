package controle;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import partida.Movimento;
import partida.ObservadorTabuleiro;
import partida.Partida;
import partida.Posicao;
import pecas.Peca;
import view.InicioView;
import view.TabuleiroView;

public class TabuleiroControle implements ObservadorTabuleiro {
    private Partida partida;
    private TabuleiroView tabuleiroView;
    private BiConsumer<Integer, Integer> callback;
    private Posicao origemSelecionada;
    private Stage primaryStage;

    public TabuleiroControle(Partida partida, TabuleiroView tabuleiroView, Stage primaryStage) {
        this.partida = partida;
        this.tabuleiroView = tabuleiroView;
        this.partida.getTabuleiro().adicionarObservador(this);
        initialize();
        this.primaryStage = primaryStage;
    }

    private void initialize() {
        if (!partida.isEmpate() && !partida.isCheckMate()) {
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
                            System.out.println(possiveisMovimentos);
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
            tabuleiroView.reconfigurarEventosDeClique(callback);
        } else {
            return;
        }
    }

    private List<Posicao> criarMovimento(Posicao origem) {
        Peca pecaSelecionada = partida.getTabuleiro().obterPeca(origem);
        List<Posicao> movimentos = pecaSelecionada.possiveisMovimentos(origem);
        return movimentos != null ? movimentos : new ArrayList<>();
    }

    @Override
    public void atualizar() {
        // Atualiza o estado do tabuleiro
        tabuleiroView.updateTabuleiro(partida.getTabuleiro(), callback);
        atualizarCapturas();

        // Verifica se o jogo terminou
        if (partida.isEmpate()) {
            terminarPartida("Empate! Apenas os dois reis restam no tabuleiro.");
            tabuleiroView.atualizarEstado(partida.getEstadoJogo().name());
        } else if (partida.isCheckMate()) {
            terminarPartida("Checkmate! O vencedor é: " + partida.getJogadorAtual().getCor());
            tabuleiroView.atualizarEstado(partida.getEstadoJogo().name());
        }
    }

    private void atualizarCapturas() {
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

    public void terminarPartida(String mensagemFim) {
        // Exibe o alerta de fim da partida
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Fim da Partida");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagemFim);

        // Espera o jogador clicar em "OK"
        alerta.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                retornarAoInicio();
            }
        });
    }

    private void retornarAoInicio() {
        try {
            new InicioView(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
}