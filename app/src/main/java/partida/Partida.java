package partida;

import java.util.List;
import java.io.IOException;
import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import jogador.Jogador;
import jogador.JogadorIA;
import pecas.Peca;
import pecas.Rei;
import view.*;

@XmlRootElement
public class Partida {
    private int turno;
    private EstadoJogo estadoJogo;
    private boolean check;
    private boolean checkMate;
    private boolean empate;
    private boolean partidaFinalizada;
    private Jogador jogadorPreto;
    private Jogador jogadorBranco;
    private Jogador jogadorAtual;
    private Tabuleiro tabuleiro;
    private HistoricoMovimentos historico;
    private LocalDateTime inicioPartida;
    private LocalDateTime fimPartida;

    public Partida(){

    }
    public Partida(Jogador jogadorPreto, Jogador jogadorBranco, Tabuleiro tabuleiro,
            HistoricoMovimentos historicoMovimentos) {
        this.turno = 0;
        this.estadoJogo = EstadoJogo.EM_ANDAMENTO;
        this.jogadorPreto = jogadorPreto;
        this.jogadorBranco = jogadorBranco;
        this.jogadorAtual = jogadorPreto;
        if (tabuleiro == null) {
            this.tabuleiro = new Tabuleiro();
        } else {
            this.tabuleiro = tabuleiro;
        }
        if (historicoMovimentos == null) {
            this.historico = new HistoricoMovimentos(tabuleiro, this, jogadorPreto, jogadorBranco);
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
            System.out.println("===CHECK MATE===");
            checkMate = true;
            estadoJogo = EstadoJogo.FIM;
            fimPartida = LocalDateTime.now();
            return;
        }
        if (verificaCheck()) {
            System.out.println("===CHECK===");
            check = true;
        } else {
            check = false;
        }
        if (verificaEmpate()) {
            System.out.println("===EMPATOU===");
            empate = true;
            estadoJogo = EstadoJogo.EMPATE;
            fimPartida = LocalDateTime.now();
            return;
        }
        mudarTurno();
    }

    public void voltaTurno() {
        if (historico.temMovimentos()) {
            Movimento ultimoMovimento = historico.obterUltimoMovimento();
            tabuleiro.desfazerMovimento(ultimoMovimento);
            historico.removerUltimoMovimento();
            turno--; // Decrementa o turno
            mudarTurno(); // Volta o turno para o jogador anterior
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
        jogadorAtual = (jogadorAtual.equals(jogadorPreto)) ? jogadorBranco : jogadorPreto;
        turno++; // Aumenta o turno após a jogada
    }

    private boolean verificaCheck() {
        Posicao posicaoRei = tabuleiro.getPosicaoRei(jogadorAtual.getCor());
        return tabuleiro.isReiEmCheck(posicaoRei, jogadorAtual.getCor());
    }

    private boolean verificaCheckMate() {
        if (verificaCheck()) {
            return tabuleiro.temMovimentosValidosParaSairDoCheck(jogadorAtual.getCor());
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

    public boolean isEmpate() {
        return empate;
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
        return 0; // Caso o jogo não tenha terminado ainda
    }

    public boolean verificaEmpate() {
        int contadorReis = 0;
        int outrasPecas = 0;

        // Percorre a matriz de casas
        for (List<Casa> linha : Tabuleiro.casas) {
            for (Casa casa : linha) {
                Peca peca = casa.getPeca(); // Obtém a peça da casa
                if (peca != null) {
                    if (peca instanceof Rei) {
                        contadorReis++;
                    } else {
                        outrasPecas++;
                    }
                }
            }
        }
        return contadorReis == 2 && outrasPecas == 0;
    }

    public boolean isJogadorBrancoIA() {
        return (jogadorBranco instanceof JogadorIA);
    }

    public HistoricoMovimentos getHistoricoMovimentos() {
        return historico;
    }

    public void terminar() {
        // Marcar a partida como terminada
        this.partidaFinalizada = true;
    }
    
    public boolean isFinalizada() {
        return partidaFinalizada;
    }
}