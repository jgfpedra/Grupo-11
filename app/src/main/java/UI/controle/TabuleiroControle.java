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
import jogador.JogadorOnline;
import partida.Cor;
import partida.Movimento;
import partida.ObservadorTabuleiro;
import partida.Partida;
import partida.Posicao;
import pecas.*;
import UI.view.InicioView;
import UI.view.TabuleiroView;

/**
 * Controlador responsável pela lógica do tabuleiro de xadrez, gerenciando os movimentos das peças,
 * a comunicação entre os jogadores e a atualização da interface gráfica.
 * 
 * Esta classe é responsável por controlar o andamento de uma partida, seja local ou online, gerenciando 
 * os eventos de clique nas casas do tabuleiro, fazendo as jogadas, atualizando o estado do jogo e 
 * exibindo a interface de acordo com o progresso da partida.
 */
public class TabuleiroControle implements ObservadorTabuleiro {
    private Partida partida;
    private TabuleiroView tabuleiroView;
    private BiConsumer<Integer, Integer> callback;
    private Posicao origemSelecionada;
    private Stage primaryStage;
    private Socket socket;
    private boolean isRunning = true;
    private boolean primeiroMovimento = false;

    /**
     * Constrói o controlador de tabuleiro para uma partida local.
     * 
     * @param partida A partida atual.
     * @param tabuleiroView A visualização do tabuleiro.
     * @param primaryStage A janela principal da aplicação.
     */
    public TabuleiroControle(Partida partida, TabuleiroView tabuleiroView, Stage primaryStage) {
        this.partida = partida;
        this.tabuleiroView = tabuleiroView;
        this.partida.getTabuleiro().adicionarObservador(this);
        initialize();
        this.primaryStage = primaryStage;
    }

    /**
     * Constrói o controlador de tabuleiro para uma partida online.
     * 
     * @param partida A partida atual.
     * @param tabuleiroView A visualização do tabuleiro.
     * @param primaryStage A janela principal da aplicação.
     * @param socket O socket para comunicação com o outro jogador.
     */
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
                    Thread.sleep(1000);
                } catch (IOException | InterruptedException e) {
                    Platform.runLater(() -> terminarPartida("Um dos jogadores desconectou. A partida foi finalizada."));
                    isRunning = false;
                }
            }
        }).start();             
    }

    /**
     * Inicializa os eventos e lógica do tabuleiro.
     * 
     * Este método configura o comportamento de clique nas casas do tabuleiro e realiza a lógica de movimentação
     * das peças. Ele verifica a cor do jogador atual e realiza a jogada, se possível.
     */
    private void initialize() {
        try{
            if (!partida.isEmpate() && !partida.isCheckMate()) {
                callback = (row, col) -> {
                    if (partida.getJogadorAtual() == null) {
                        return;
                    }
                    if ((socket == null && partida.getJogadorAtual().getCor() == Cor.PRETO && partida.getJogadorAtual() instanceof JogadorOnline) || 
                        (socket != null && partida.getJogadorAtual().getCor() != partida.getJogadorAtual().getCor() && partida.getJogadorAtual() instanceof JogadorOnline)) {
                        return;
                    }
                    Posicao posicaoClicada = new Posicao(row, col);
                    if (origemSelecionada != null) {
                        List<Posicao> movimentosPossiveis = criarMovimento(origemSelecionada);
                        if (movimentosPossiveis != null && movimentosPossiveis.contains(posicaoClicada)) {
                            Peca pecaMovida = partida.getTabuleiro().obterPeca(origemSelecionada);
                            if (pecaMovida != null) {        
                                Movimento movimento = new Movimento(origemSelecionada, posicaoClicada, pecaMovida);
                                try{
                                    partida.jogar(movimento);
                                } catch(Exception e){
                                    tabuleiroView.exibirPopupErro(e.getMessage());
                                }
                                tabuleiroView.moverPeca(origemSelecionada, posicaoClicada);
                                this.origemSelecionada = null;
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
        } catch (Exception e){
            tabuleiroView.exibirPopupErro(e.getMessage());
        }
    }

    /**
     * Seleciona uma nova peça para movimentação.
     * 
     * Este método verifica se a peça selecionada pertence ao jogador atual e, se for o caso,
     * exibe os movimentos possíveis para essa peça. Caso contrário, a seleção é descartada.
     * 
     * @param posicaoClicada A posição da peça que o jogador deseja mover.
     */
    private void selecionarNovaPeca(Posicao posicaoClicada) {
        Peca pecaSelecionada = partida.getTabuleiro().obterPeca(posicaoClicada);
        
        if (pecaSelecionada != null && pecaSelecionada.getCor() == partida.getJogadorAtual().getCor()) {
            origemSelecionada = posicaoClicada;
            if (pecaSelecionada instanceof Rei) {
                try {
                    List<Posicao> possiveisMovimentos = criarMovimento(origemSelecionada);
                    tabuleiroView.grifarMovimentosPossiveis(possiveisMovimentos);
                    tabuleiroView.selecionarPeca(origemSelecionada);
                } catch (Exception e) {
                    tabuleiroView.exibirPopupErro(e.getMessage());
                }
            } else {
                List<Posicao> possiveisMovimentos = criarMovimento(origemSelecionada);
                tabuleiroView.grifarMovimentosPossiveis(possiveisMovimentos);
                tabuleiroView.selecionarPeca(origemSelecionada);
            }
        } else {
            origemSelecionada = null;
            tabuleiroView.clearSelection();
        }
    }
    
    
    /**
     * Cria uma lista de movimentos possíveis para a peça selecionada.
     * 
     * @param origem A posição da peça que o jogador deseja mover.
     * @return Uma lista de posições para as quais a peça pode se mover.
     */
    private List<Posicao> criarMovimento(Posicao origem) {
        Peca pecaSelecionada = partida.getTabuleiro().obterPeca(origem);
        List<Posicao> movimentos = pecaSelecionada.possiveisMovimentos(partida.getTabuleiro(), origem);
        return movimentos != null ? movimentos : new ArrayList<>();
    }


    /**
     * Atualiza o estado do jogo e a interface gráfica.
     * 
     * Este método é chamado sempre que há uma mudança no estado da partida, como um movimento de peça,
     * empate ou checkmate. Ele atualiza a interface do tabuleiro e as informações de turno e capturas.
     */
    @Override
    public void atualizar() {
        tabuleiroView.updateTabuleiro(partida, callback);
        atualizarCapturas();
        if (!primeiroMovimento) {
            tabuleiroView.iniciarTimer();
            primeiroMovimento = true;
        }
        if (partida.isEmpate()) {
            terminarPartida("Empate! Apenas os dois reis restam no tabuleiro.");
            tabuleiroView.atualizarEstado(partida.getEstadoJogo().name());
            if (socket != null) {
                enviarEstadoPartida();
            }
        } else if (partida.isCheckMate()) {
            terminarPartida("Xeque-Mate! O vencedor é: " + partida.getJogadorAtual().getCor());
            tabuleiroView.atualizarEstado(partida.getEstadoJogo().name());
            if (socket != null) {
                enviarEstadoPartida();
            }
        } else if (partida.isCheck()){
            tabuleiroView.atualizarEstado(partida.getEstadoJogo().name());
            if(socket != null){
                enviarEstadoPartida();
            }
        } else {
            if (socket != null) {
                enviarEstadoPartida();
            }
            atualizarTurno();
        }
    }    

    /**
     * Atualiza as peças capturadas de ambos os jogadores.
     * 
     * Este método atualiza as peças capturadas no tabuleiro de acordo com os movimentos realizados.
     */
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

    /**
     * Finaliza a partida e exibe uma mensagem de vitória, empate ou erro.
     * 
     * @param mensagemFim A mensagem a ser exibida quando a partida terminar.
     */
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

    /**
     * Retorna à tela inicial após o término da partida.
     */
    private void retornarAoInicio() {
        try {
            new InicioView(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Envia o estado atual da partida para o outro jogador.
     * 
     * Este método envia o estado completo da partida (tabuleiro, jogadores, capturas, etc.)
     * para o outro jogador durante uma partida online.
     */
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

    /**
     * Recebe o estado atual da partida do outro jogador durante uma partida online.
     * 
     * Este método atualiza o estado do jogo com as informações enviadas pelo outro jogador, 
     * permitindo que ambos os jogadores vejam o mesmo tabuleiro e as mesmas informações.
     * 
     * @param socket O socket de comunicação com o outro jogador.
     * @throws IOException Se ocorrer um erro na leitura do estado da partida.
     */
    private void receberEstadoPartida(Socket socket) throws IOException {
        DataInputStream input = new DataInputStream(socket.getInputStream());
        String estadoCompleto = input.readUTF();
        if (estadoCompleto == null || estadoCompleto.isEmpty()) {
            throw new IOException("Jogador desconectado.");
        }
        partida.fromEstadoCompleto(estadoCompleto);
        atualizarCapturas();
        Platform.runLater(() -> {
            tabuleiroView.updateTabuleiro(partida, callback);
            atualizarTurno();
        });
    }    

    /**
     * Desconecta o jogador da partida online.
     * 
     * Este método fecha a conexão com o outro jogador.
     * 
     * @throws IOException Se ocorrer um erro ao tentar fechar a conexão.
     */
    public void desconectarJogador() throws IOException {
        socket.close();
    }
    
    /**
     * Atualiza o turno do jogador atual na interface.
     * 
     * Este método exibe o turno do jogador (se é a vez do jogador branco ou preto) na interface.
     */
    public void atualizarTurno() {
        String turno = partida.getJogadorAtual().getCor() == Cor.BRANCO ? "VEZ DO BRANCO" : "VEZ DO PRETO";
        tabuleiroView.atualizarTurno(turno);
    }
}