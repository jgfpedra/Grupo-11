package partida;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import jogador.Jogador;

@XmlRootElement
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

    public Partida(){

    }
    public Partida(Jogador jogador1, Jogador jogador2, Tabuleiro tabuleiro, HistoricoMovimentos historicoMovimentos) {
        this.turno = 0;
        this.estadoJogo = EstadoJogo.EM_ANDAMENTO;
        this.jogador1 = jogador1;
        this.jogador2 = jogador2;
        this.jogadorAtual = jogador1;  // Começa com o primeiro jogador
        if(tabuleiro == null){
            this.tabuleiro = new Tabuleiro();
        } else {
            this.tabuleiro = tabuleiro;
        }
        if(historicoMovimentos == null){
            this.historico = new HistoricoMovimentos(tabuleiro, this, jogador1, jogador2);
        } else {
            this.historico = historicoMovimentos;
        }
    }

    public void jogar(Movimento movimento) {        
        if (inicioPartida == null) {
            inicioPartida = LocalDateTime.now();
        }
        
        tabuleiro.aplicarMovimento(movimento);
        historico.adicionarMovimento(movimento);
    
        if (verificaCheckMate()) {
            checkMate = true;
            estadoJogo = EstadoJogo.FIM;
            fimPartida = LocalDateTime.now();
            return;
        }
        if (verificaCheck()) {
            check = true;
        } else {
            check = false;
        }
        mudarTurno();
    }
    
    // Método para desfazer o último movimento
    public void voltaTurno() {
        // Se houver um movimento para desfazer
        if (historico.temMovimentos()) {
            // Recupera o último movimento
            Movimento ultimoMovimento = historico.obterUltimoMovimento();
    
            // Desfaz o movimento no tabuleiro
            tabuleiro.desfazerMovimento(ultimoMovimento);
    
            // Remove o último movimento do histórico
            historico.removerUltimoMovimento();
    
            turno--;  // Decrementa o turno
            mudarTurno();  // Volta o turno para o jogador anterior
        }
    }

    public Jogador getJogadorAtual() {
        return jogadorAtual;
    }

    @XmlElement
    public int getTurno() {
        return turno;
    }

    @XmlElement
    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    private void mudarTurno() {
        // Alterna entre os jogadores
        jogadorAtual = (jogadorAtual.equals(jogador1)) ? jogador2 : jogador1;
        turno++;  // Aumenta o turno após a jogada
    }
    
    private boolean verificaCheck() {
        Posicao posicaoRei = tabuleiro.getPosicaoRei(jogadorAtual.getCor());
        return tabuleiro.isReiEmCheck(posicaoRei, jogadorAtual.getCor());
    }

    private boolean verificaCheckMate() {
        if (verificaCheck()) {
            return !tabuleiro.temMovimentosValidosParaSairDoCheck(jogadorAtual.getCor());
        }
        return false;
    }

    @XmlElement
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
    @XmlElement
    public LocalDateTime getInicioPartida() {
        return inicioPartida;
    }

    @XmlElement
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