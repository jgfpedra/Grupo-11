package partida;

import java.time.LocalDateTime;
import jogador.Jogador;

public class Partida {
    private int turno;
    private EstadoJogo estadoJogo;
    private boolean check;
    private boolean checkMate;
    private Jogador jogador1;
    private Jogador jogador2;
    private Jogador jogadorAtual;
    private Tabuleiro tabuleiro;
    private HistoricoMovimentos historico;
    private LocalDateTime inicioPartida;
    private LocalDateTime fimPartida;

    public Partida() {
        // Construtor padrão sem o início da partida
        this.turno = 0;
        this.estadoJogo = EstadoJogo.EM_ANDAMENTO;
        this.jogador1 = null;
        this.jogador2 = null;
        this.tabuleiro = new Tabuleiro();
        this.historico = new HistoricoMovimentos();
    }

    public Partida(Jogador jogador1, Jogador jogador2) {
        this();
        this.jogador1 = jogador1;
        this.jogador2 = jogador2;
        this.jogadorAtual = jogador1;  // Começa com o primeiro jogador
    }

    public void jogar(Movimento movimento) {
        // Registra o início da partida apenas no primeiro movimento
        if (inicioPartida == null) {
            inicioPartida = LocalDateTime.now();  // Registra o tempo de início
        }

        // Aplica o movimento no tabuleiro
        tabuleiro.aplicarMovimento(movimento);

        // Verifica se o movimento resultou em check ou checkmate
        if (verificaCheckMate()) {
            checkMate = true;
            System.out.println("Checkmate! " + jogadorAtual.getNome() + " venceu!");
            estadoJogo = EstadoJogo.FIM;
            fimPartida = LocalDateTime.now();  // Registra o fim da partida
            return;
        }

        // Verifica se houve um check (mas não checkmate)
        if (verificaCheck()) {
            check = true;
            System.out.println("Check! " + jogadorAtual.getNome() + " está em check!");
        } else {
            check = false;
        }

        // Alterna o turno para o próximo jogador
        mudarTurno();
    }

    public void salvarTabuleiro() {
        // Salvar o estado do tabuleiro, se necessário
    }

    public void carregarTabuleiro() {
        // Carregar o estado do tabuleiro, se necessário
    }

    public void voltaTurno() {
        // Desfaz o último movimento
        historico.desfazerUltimoMovimento();
        turno--;  // Decrementa o turno
        mudarTurno();  // Volta o turno para o jogador anterior
    }

    public Jogador getJogadorAtual() {
        return jogadorAtual;
    }

    public int getTurno() {
        return turno;
    }

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    private void mudarTurno() {
        // Alterna entre os jogadores
        if (jogadorAtual.equals(jogador1)) {
            jogadorAtual = jogador2;
        } else {
            jogadorAtual = jogador1;
        }
        turno++;  // Aumenta o turno após a jogada
    }

    private boolean verificaCheck() {
        // Verifica se o jogador atual está em check (o rei está ameaçado)
        Posicao posicaoRei = tabuleiro.getPosicaoRei(jogadorAtual.getCor());
        return tabuleiro.isReiEmCheck(posicaoRei, jogadorAtual.getCor());
    }

    private boolean verificaCheckMate() {
        // Verifica se o jogador atual está em checkmate
        if (verificaCheck()) {
            // Verifique se o jogador tem movimentos válidos para sair do check
            return !tabuleiro.temMovimentosValidosParaSairDoCheck(jogadorAtual.getCor());
        }
        return false;
    }

    public EstadoJogo getEstadoJogo() {
        return estadoJogo;
    }

    public boolean isCheck() {
        return check;
    }

    public boolean isCheckMate() {
        return checkMate;
    }

    // Métodos para retornar o tempo de jogo (caso queira exibir para os jogadores)
    public LocalDateTime getInicioPartida() {
        return inicioPartida;
    }

    public LocalDateTime getFimPartida() {
        return fimPartida;
    }

    // Método adicional para calcular a duração da partida
    public long getDuracaoPartidaEmMinutos() {
        if (fimPartida != null) {
            return java.time.Duration.between(inicioPartida, fimPartida).toMinutes();
        }
        return 0;  // Caso o jogo não tenha terminado ainda
    }
}