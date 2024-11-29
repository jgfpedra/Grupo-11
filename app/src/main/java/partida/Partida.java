package partida;

import java.util.List;
import java.time.LocalDateTime;

import jogador.Jogador;
import jogador.JogadorIA;
import pecas.Bispo;
import pecas.Cavalo;
import pecas.Peao;
import pecas.Peca;
import pecas.Rainha;
import pecas.Rei;
import pecas.Torre;

public class Partida implements Cloneable {
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

    public Partida(Jogador jogadorBranco, Jogador jogadorPreto, HistoricoMovimentos historicoMovimentos) {
        this.turno = 0;
        this.estadoJogo = EstadoJogo.EM_ANDAMENTO;
        this.jogadorBranco = jogadorBranco;
        this.jogadorPreto = jogadorPreto;
        this.jogadorAtual = jogadorBranco;
        this.tabuleiro = new Tabuleiro();   
        if(historicoMovimentos == null){
            this.historico = new HistoricoMovimentos();
        } else {
            this.historico = historicoMovimentos;
            List<Movimento> movimentos = historicoMovimentos.getMovimentos();
            for(Movimento movimento : movimentos){
                jogar(movimento);
            }
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
            estadoJogo = EstadoJogo.XEQUE;
        } else {
            check = false;
            estadoJogo = EstadoJogo.EM_ANDAMENTO;
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
            turno--; 
            mudarTurno(); 
        }
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

    public void mudarTurno() {
        jogadorAtual = (jogadorAtual.equals(jogadorPreto)) ? jogadorBranco : jogadorPreto;
        turno++;
    
        if (jogadorAtual instanceof JogadorIA) {
            JogadorIA jogadorIA = (JogadorIA) jogadorAtual;
            jogadorIA.escolherMovimento(this);
        }
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

    public LocalDateTime getInicioPartida() {
        return LocalDateTime.now();
    }

    public LocalDateTime getFimPartida() {
        return fimPartida;
    }

    public long getDuracaoPartidaEmMinutos() {
        if (fimPartida != null) {
            return java.time.Duration.between(inicioPartida, fimPartida).toMinutes();
        }
        return 0;
    }

    public boolean verificaEmpate() {
        int contadorReis = 0;
        int outrasPecas = 0;

        for (List<Casa> linha : tabuleiro.getCasas()) {
            for (Casa casa : linha) {
                Peca peca = casa.getPeca();
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
        this.partidaFinalizada = true;
    }
    
    public boolean isFinalizada() {
        return partidaFinalizada;
    }

    @Override
    public Partida clone() {
        try {
            Partida novaPartida = (Partida) super.clone();
            novaPartida.tabuleiro = this.tabuleiro.clone();
            return novaPartida;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public void setJogador2(Jogador jogador2){
        this.jogadorPreto = jogador2;
    }

    public void setTabuleiro(Tabuleiro tabuleiro){
        this.tabuleiro = tabuleiro;
    }

    public String getEstadoTabuleiro() {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Casa casa = tabuleiro.getCasas().get(i).get(j);
                Peca peca = casa.getPeca();
                if (peca != null) {
                    sb.append(peca.getIdentificador());
                } else {
                    sb.append("0");
                }
            }
        }
        return sb.toString();
    }

    public void fromEstadoTabuleiro(String estadoTabuleiro) {
        Tabuleiro tabuleiro = new Tabuleiro();
        String[] dadosPecas = estadoTabuleiro.split(";");

        for (String dados : dadosPecas) {
            String[] partes = dados.split(",");
            int linha = Integer.parseInt(partes[0]);
            int coluna = Integer.parseInt(partes[1]);
            Cor cor = Cor.valueOf(partes[2]);
            String tipoPeca = partes[3];
            Peca peca = null;
            switch (tipoPeca) {
                case "PEAO":
                    peca = new Peao(cor);
                    break;
                case "TORRE":
                    peca = new Torre(cor);
                    break;
                case "CAVALO":
                    peca = new Cavalo(cor);
                    break;
                case "BISPO":
                    peca = new Bispo(cor);
                    break;
                case "RAINHA":
                    peca = new Rainha(cor);
                    break;
                case "REI":
                    peca = new Rei(cor);
                    break;
            }
            if (peca != null) {
                tabuleiro.getCasa(new Posicao(linha, coluna)).setPeca(peca);
            }
        }
        this.tabuleiro = tabuleiro;
    }
}