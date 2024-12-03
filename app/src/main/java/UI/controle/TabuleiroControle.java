package UI.controle;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import partida.Cor;
import partida.Movimento;
import partida.ObservadorTabuleiro;
import partida.Partida;
import partida.Posicao;
import pecas.Peca;
import UI.view.InicioView;
import UI.view.TabuleiroView;

public class TabuleiroControle implements ObservadorTabuleiro {
    private Partida partida;
    private TabuleiroView tabuleiroView;
    private BiConsumer<Integer, Integer> callback;
    private Posicao origemSelecionada;
    private Stage primaryStage;
    private Socket socket;
    private boolean isRunning = true;

    public TabuleiroControle(Partida partida, TabuleiroView tabuleiroView, Stage primaryStage) {
        this.partida = partida;
        this.tabuleiroView = tabuleiroView;
        this.partida.getTabuleiro().adicionarObservador(this);
        initialize();
        this.primaryStage = primaryStage;
    }

    public TabuleiroControle(Partida partida, TabuleiroView tabuleiroView, Stage primaryStage, Socket socket) {
        this.partida = partida;
        this.tabuleiroView = tabuleiroView;
        this.partida.getTabuleiro().adicionarObservador(this);
        initialize();
        this.primaryStage = primaryStage;
        this.socket = socket;
    
        new Thread(() -> {
            while (isRunning) {
                try {
                    receberEstadoPartida(socket);
                } catch (IOException e) {
                    Platform.runLater(() -> terminarPartida("Um dos jogadores desconectou. A partida foi finalizada."));
                    isRunning = false;
                }
            }
        }).start();
    }

    private void initialize() {
        if (!partida.isEmpate() && !partida.isCheckMate()) {
            callback = (row, col) -> {
                if (partida.getJogadorAtual() == null) {
                    return;
                }
                if ((socket == null && partida.getJogadorAtual().getCor() == Cor.PRETO) || 
                    (socket != null && partida.getJogadorAtual().getCor() != partida.getJogadorAtual().getCor())) {
                    return;
                }
                Posicao posicaoClicada = new Posicao(row, col);
    
                if (origemSelecionada != null) {
                    List<Posicao> movimentosPossiveis = criarMovimento(origemSelecionada);
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
                        selecionarNovaPeca(posicaoClicada);
                    }
                } else {
                    selecionarNovaPeca(posicaoClicada);
                }
            };
            tabuleiroView.reconfigurarEventosDeClique(partida, callback);
        } else {
            return;
        }
    }   

    private void selecionarNovaPeca(Posicao posicaoClicada) {
        Peca pecaSelecionada = partida.getTabuleiro().obterPeca(posicaoClicada);
        if (pecaSelecionada != null && pecaSelecionada.getCor() == partida.getJogadorAtual().getCor()) {
            origemSelecionada = posicaoClicada;
            List<Posicao> possiveisMovimentos = criarMovimento(origemSelecionada);
            tabuleiroView.grifarMovimentosPossiveis(possiveisMovimentos);
            tabuleiroView.selecionarPeca(origemSelecionada);
        } else {
            origemSelecionada = null;
            tabuleiroView.clearSelection();
        }
    }    
    
    private List<Posicao> criarMovimento(Posicao origem) {
        Peca pecaSelecionada = partida.getTabuleiro().obterPeca(origem);
        List<Posicao> movimentos = pecaSelecionada.possiveisMovimentos(partida.getTabuleiro(), origem);
        return movimentos != null ? movimentos : new ArrayList<>();
    }

    @Override
    public void atualizar() {
        tabuleiroView.updateTabuleiro(partida, callback);
        atualizarCapturas();
        
        if (partida.isEmpate()) {
            terminarPartida("Empate! Apenas os dois reis restam no tabuleiro.");
            tabuleiroView.atualizarEstado(partida.getEstadoJogo().name());
            if (socket != null) {
                enviarEstadoPartida();
            }
        } else if (partida.isCheckMate()) {
            terminarPartida("Checkmate! O vencedor é: " + partida.getJogadorAtual().getCor());
            tabuleiroView.atualizarEstado(partida.getEstadoJogo().name());
            if (socket != null) {
                enviarEstadoPartida();
            }
        } else {
            if (socket != null) {
                enviarEstadoPartida();
            }
            atualizarTurno();
        }
    }    
    
    private void atualizarCapturas() {
        Platform.runLater(() -> {
            tabuleiroView.getCapturasJogadorPreto().getChildren().clear();
            tabuleiroView.getCapturasJogadorBranco().getChildren().clear();
            List<Peca> capturadasPreto = partida.getTabuleiro().getCapturadasJogadorPreto();
            for (Peca peca : capturadasPreto) {
                tabuleiroView.adicionarCapturaPreto(peca);
            }
            List<Peca> capturadasBranco = partida.getTabuleiro().getCapturadasJogadorBranco();
            for (Peca peca : capturadasBranco) {
                tabuleiroView.adicionarCapturaBranco(peca);
            }
        });
    }    

    public void terminarPartida(String mensagemFim) {
        tabuleiroView.pararTimer();
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Fim da Partida");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagemFim);
        alerta.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                retornarAoInicio();
            }
        });    
        if (socket != null) {
            try {
                desconectarJogador();
            } catch (IOException e) {
                System.out.println("Erro ao desconectar o jogador: " + e.getMessage());
            }
        }
    }

    private void retornarAoInicio() {
        try {
            new InicioView(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enviarEstadoPartida() {
        if (socket != null) {
            try {
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                output.writeUTF(partida.getEstadoCompleto());
                output.flush();
            } catch (IOException e) {
                System.out.println("Erro ao enviar estado da partida: " + e.getMessage());
            }
        }
    }    

    private void receberEstadoPartida(Socket socket) throws IOException {
        DataInputStream input = new DataInputStream(socket.getInputStream());
        String estadoCompleto = input.readUTF();
    
        if (estadoCompleto == null || estadoCompleto.isEmpty()) {
            throw new IOException("Jogador desconectado.");
        }
        partida.fromEstadoCompleto(estadoCompleto);
        Platform.runLater(() -> {
            tabuleiroView.updateTabuleiro(partida, callback);
        });
    }    

    public void desconectarJogador() throws IOException {
        socket.close();
    }

    public void atualizarTurno() {
        String turno = partida.getJogadorAtual().getCor() == Cor.BRANCO ? "VEZ DO BRANCO" : "VEZ DO PRETO";
        tabuleiroView.atualizarTurno(turno);
    }
}