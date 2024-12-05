package partida;

import java.util.List;
import java.time.LocalDateTime;

import jogador.Jogador;
import jogador.JogadorIA;
import jogador.JogadorOnline;
import pecas.Bispo;
import pecas.Cavalo;
import pecas.Peao;
import pecas.Peca;
import pecas.Rainha;
import pecas.Rei;
import pecas.Torre;

/**
 * A classe Partida representa uma partida de xadrez. 
 * Ela gerencia o estado do jogo, o turno dos jogadores, os movimentos realizados, 
 * as condições de vitória ou empate, e a interação entre os jogadores, 
 * além de permitir a reversão de movimentos e a conclusão da partida.
 */
public class Partida implements Cloneable {

    private int turno;
    private EstadoJogo estadoJogo;
    private boolean check;
    private boolean checkMate;
    private boolean empate;
    private boolean partidaFinalizada;
    private boolean isOnline;
    private Jogador jogadorPreto;
    private Jogador jogadorBranco;
    private Jogador jogadorAtual;
    private Tabuleiro tabuleiro;
    private HistoricoMovimentos historico;
    private LocalDateTime inicioPartida;
    private LocalDateTime fimPartida;

    /**
     * Construtor para iniciar uma nova partida de xadrez.
     * Inicializa os jogadores, o tabuleiro, o histórico de movimentos e configura a partida.
     * Verifica se a partida é online (caso ambos os jogadores sejam online).
     * 
     * @param jogadorBranco Jogador que irá jogar com as peças brancas.
     * @param jogadorPreto Jogador que irá jogar com as peças pretas.
     * @param historicoMovimentos Histórico de movimentos da partida (caso exista).
     */
    public Partida(Jogador jogadorBranco, Jogador jogadorPreto, HistoricoMovimentos historicoMovimentos) {
        this.turno = 0;
        this.estadoJogo = EstadoJogo.EM_ANDAMENTO;
        this.jogadorBranco = jogadorBranco;
        this.jogadorPreto = jogadorPreto;
        this.jogadorAtual = jogadorBranco;
        if(jogadorBranco instanceof JogadorOnline & jogadorPreto instanceof JogadorOnline){
            isOnline = true;
        } else {
            isOnline = false;
        }
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

    /**
     * Realiza um movimento na partida e atualiza o estado do jogo.
     * Aplica o movimento no tabuleiro e atualiza o histórico.
     * Verifica condições como xeque, xeque-mate e empate após o movimento.
     * 
     * @param movimento O movimento a ser realizado na partida.
     */
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

    /**
     * Reverte o último movimento realizado na partida.
     * Atualiza o tabuleiro e o histórico de movimentos.
     * O turno também é decrementado e o jogador atual é alterado de volta.
     */
    public void voltaTurno() {
        if (historico.temMovimentos()) {
            Movimento ultimoMovimento = historico.obterUltimoMovimento();
            tabuleiro.desfazerMovimento(ultimoMovimento);
            historico.removerUltimoMovimento();
            turno--; 
            mudarTurno(); 
        }
    }

    /**
     * Obtém o jogador atual da partida (aquele que deve fazer o próximo movimento).
     * 
     * @return O jogador atual da partida.
     */
    public Jogador getJogadorAtual() {
        return jogadorAtual;
    }

    /**
     * Obtém o número do turno atual da partida.
     * 
     * @return O número do turno atual.
     */
    public int getTurno() {
        return turno;
    }

    /**
     * Obtém o tabuleiro atual da partida.
     * 
     * @return O tabuleiro da partida.
     */
    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    /**
     * Altera o turno para o próximo jogador e verifica se é o turno de um jogador IA.
     */
    public void mudarTurno() {
        jogadorAtual = (jogadorAtual.equals(jogadorPreto)) ? jogadorBranco : jogadorPreto;
        turno++;
    
        if (jogadorAtual instanceof JogadorIA) {
            JogadorIA jogadorIA = (JogadorIA) jogadorAtual;
            jogadorIA.escolherMovimento(this);
        }
    }

    /**
     * Verifica se o jogador atual está em check (xeque).
     * 
     * @return Verdadeiro se o jogador atual está em check, falso caso contrário.
     */
    private boolean verificaCheck() {
        Posicao posicaoRei = tabuleiro.getPosicaoRei(jogadorAtual.getCor());
        return tabuleiro.isReiEmCheck(posicaoRei, jogadorAtual.getCor());
    }

    /**
     * Verifica se a partida terminou com um xeque-mate.
     * 
     * @return Verdadeiro se a partida terminou com um xeque-mate, falso caso contrário.
     */
    private boolean verificaCheckMate() {
        if (verificaCheck()) {
            return tabuleiro.temMovimentosValidosParaSairDoCheck(jogadorAtual.getCor());
        }
        return false;
    }

    /**
     * Obtém o estado atual do jogo (em andamento, xeque, empate, etc).
     * 
     * @return O estado atual do jogo.
     */
    public EstadoJogo getEstadoJogo() {
        return estadoJogo;
    }

    /**
     * Verifica se o rei está em xeque na partida.
     * 
     * O método retorna verdadeiro se o rei estiver em posição de xeque, ou seja, se houver
     * uma ameaça de captura do rei pelo adversário na próxima jogada. Caso contrário, retorna falso.
     * 
     * @return Verdadeiro se o rei estiver em xeque, falso caso contrário.
     */
    public boolean isCheck() {
        return check;
    }

    /**
     * Verifica se a partida chegou a um xeque-mate.
     * 
     * O método retorna verdadeiro se o rei do jogador em turno estiver em xeque e não houver
     * nenhuma jogada legal possível para escapar do xeque, resultando no fim da partida. Caso contrário,
     * retorna falso.
     * 
     * @return Verdadeiro se a partida estiver em xeque-mate, falso caso contrário.
     */
    public boolean isCheckMate() {
        return checkMate;
    }

    /**
     * Verifica se a partida terminou em empate.
     * 
     * @return Verdadeiro se a partida terminou em empate, falso caso contrário.
     */
    public boolean isEmpate() {
        return empate;
    }

    /**
     * Obtém a data e hora de início da partida.
     * 
     * @return A data e hora de início da partida.
     */
    public LocalDateTime getInicioPartida() {
        return LocalDateTime.now();
    }

    /**
     * Obtém a data e hora de término da partida (se a partida foi finalizada).
     * 
     * @return A data e hora de término da partida.
     */
    public LocalDateTime getFimPartida() {
        return fimPartida;
    }

    /**
     * Calcula a duração da partida em minutos.
     * 
     * @return A duração da partida em minutos.
     */
    public long getDuracaoPartidaEmMinutos() {
        if (fimPartida != null) {
            return java.time.Duration.between(inicioPartida, fimPartida).toMinutes();
        }
        return 0;
    }

    /**
     * Verifica se a partida terminou em empate, com base nas condições de empate do xadrez.
     * 
     * @return Verdadeiro se a partida terminou em empate, falso caso contrário.
     */
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

    /**
     * Verifica se o jogador branco está jogando contra uma IA.
     * 
     * @return Verdadeiro se o jogador branco for uma IA, falso caso contrário.
     */
    public boolean isJogadorBrancoIA() {
        return (jogadorBranco instanceof JogadorIA);
    }

    /**
     * Obtém o histórico de movimentos da partida.
     * 
     * @return O histórico de movimentos da partida.
     */
    public HistoricoMovimentos getHistoricoMovimentos() {
        return historico;
    }

    /**
     * Termina a partida, marcando-a como finalizada.
     */
    public void terminar() {
        this.partidaFinalizada = true;
    }
    
    /**
     * Verifica se a partida foi finalizada.
     * 
     * @return Verdadeiro se a partida foi finalizada, falso caso contrário.
     */
    public boolean isFinalizada() {
        return partidaFinalizada;
    }

    /**
     * Verifica se a partida está sendo jogada online.
     * 
     * @return Verdadeiro se a partida for online, falso caso contrário.
     */
    public boolean getIsOnline(){
        return isOnline;
    }

    /**
     * Obtém o jogador que está jogando com as peças brancas.
     * 
     * @return O jogador que está jogando com as peças brancas.
     */
    public Jogador getJogadorBranco(){
        return jogadorBranco;
    }

    /**
     * Obtém o jogador que está jogando com as peças pretas.
     * 
     * @return O jogador que está jogando com as peças pretas.
     */
    public Jogador getJogadorPreto(){
        return jogadorPreto;
    }

    /**
     * Altera o jogador que está jogando com as peças pretas.
     * 
     * @param jogador2 O novo jogador que jogará com as peças pretas.
     */
    public void setJogador2(Jogador jogador2){
        this.jogadorPreto = jogador2;
    }

    /**
     * Altera o tabuleiro da partida.
     * 
     * @param tabuleiro O novo tabuleiro a ser configurado na partida.
     */
    public void setTabuleiro(Tabuleiro tabuleiro){
        this.tabuleiro = tabuleiro;
    }

    /**
     * Verifica se uma peça foi capturada anteriormente no histórico de capturas.
     * 
     * @param peca A peça a ser verificada.
     * @param capturadas A lista de peças capturadas.
     * @return Verdadeiro se a peça foi capturada, falso caso contrário.
     */
    public boolean jaFoiCapturada(Peca peca, List<Peca> capturadas) {
        for (Peca capturada : capturadas) {
            if (capturada.getCor() == peca.getCor() && capturada.getClass().equals(peca.getClass())) {
                return true;
            }
        }
        return false;
    }    

    /**
     * Verifica se é o turno de um jogador específico em uma partida online.
     * 
     * @param isJogador2 Flag que indica se é o turno do jogador 2 (preto).
     * @return Verdadeiro se for o turno do jogador especificado, falso caso contrário.
     */
    public boolean ehTurnoDoJogador(boolean isJogador2) {
        if(jogadorAtual instanceof JogadorOnline){
            if (isJogador2) {
                return jogadorAtual.equals(jogadorPreto);
            } else {
                return jogadorAtual.equals(jogadorBranco);
            }
        }
        return false;
    }
    
    /**
     * Cria uma nova peça de xadrez com base no tipo e cor fornecidos.
     * 
     * @param tipo O tipo da peça (Peao, Torre, Cavalo, Bispo, Rainha, Rei).
     * @param cor A cor da peça (Branca ou Preta).
     * @return A peça criada ou null se o tipo não for reconhecido.
     */
    private Peca criarPeca(String tipo, Cor cor) {
        switch (tipo) {
            case "Peao": return new Peao(cor);
            case "Torre": return new Torre(cor);
            case "Cavalo": return new Cavalo(cor);
            case "Bispo": return new Bispo(cor);
            case "Rainha": return new Rainha(cor);
            case "Rei": return new Rei(cor);
            default: return null;
        }
    }

    /**
     * Converte uma string para o estado do jogo correspondente.
     * 
     * @param texto O estado do jogo como string (exemplo: "EM_ANDAMENTO", "FIM").
     * @return O estado correspondente do jogo.
     * @throws IllegalArgumentException Se o estado fornecido não for válido.
     */
    public static EstadoJogo fromString(String texto) {
        for (EstadoJogo estado : EstadoJogo.values()) {
            if (estado.toString().equalsIgnoreCase(texto)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado de jogo inválido: " + texto);
    }

    /**
     * Retorna o estado completo da partida, incluindo o estado do jogo, turno, jogador atual,
     * peças capturadas e posição das peças no tabuleiro.
     * 
     * @return Uma string representando o estado completo da partida.
     */
    public String getEstadoCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append("EstadoJogo:").append(estadoJogo.toString()).append(";");
        sb.append("Turno:").append(turno).append(";");
        sb.append("JogadorAtual:").append(jogadorAtual.getCor()).append(";");
        sb.append("PecasCapturadasBranco:");
        if (tabuleiro.getCapturadasJogadorBranco() != null && !tabuleiro.getCapturadasJogadorBranco().isEmpty()) {
            for (Peca p : tabuleiro.getCapturadasJogadorBranco()) {
                sb.append(p.getClass().getSimpleName()).append(",").append(p.getCor()).append(";");
            }
        } else {
            sb.append(";");
        }
        sb.append("PecasCapturadasPreto:");
        if (tabuleiro.getCapturadasJogadorPreto() != null && !tabuleiro.getCapturadasJogadorPreto().isEmpty()) {
            for (Peca p : tabuleiro.getCapturadasJogadorPreto()) {
                sb.append(p.getClass().getSimpleName()).append(",").append(p.getCor()).append(";");
            }
        } else {
            sb.append(";");
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Casa casa = tabuleiro.getCasas().get(i).get(j);
                Peca peca = casa.getPeca();
                if (peca != null) {
                    sb.append(i).append(",").append(j).append(",").append(peca.getCor()).append(",").append(peca.getMovCount()).append(",").append(peca.getClass().getSimpleName()).append(";");
                }
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * Restaura o estado completo da partida a partir de uma string que contém o estado da partida.
     * Isso inclui a posição das peças, capturas e o estado do jogo.
     * 
     * @param estadoCompleto A string representando o estado completo da partida.
     */
    public void fromEstadoCompleto(String estadoCompleto) {
        String[] partes = estadoCompleto.split(";");
        tabuleiro.limparTabuleiro();
        tabuleiro.limparCapturadasJogadorBranco();
        tabuleiro.limparCapturadasJogadorPreto();
        for (String parte : partes) {
            if (parte.startsWith("EstadoJogo:")) {
                estadoJogo = fromString(parte.split(":")[1]);
            } else if (parte.startsWith("Turno:")) {
                turno = Integer.parseInt(parte.split(":")[1]);
            } else if (parte.startsWith("JogadorAtual:")) {
                Cor cor = Cor.valueOf(parte.split(":")[1]);
                jogadorAtual = (cor == Cor.BRANCO) ? jogadorBranco : jogadorPreto;
            } else if (parte.startsWith("PecasCapturadasBranco:")) {
                String capturadasStr = parte.split(":").length > 1 ? parte.split(":")[1] : "";
                if (!capturadasStr.isEmpty()) {
                    String[] capturadas = capturadasStr.split(";");
                    for (String capturada : capturadas) {
                        if (!capturada.isEmpty()) {
                            String[] dados = capturada.split(",");
                            if (dados.length == 2) {
                                String tipoPeca = dados[0];
                                Cor cor = Cor.valueOf(dados[1]);
                                Peca peca = criarPeca(tipoPeca, cor);
                                if (!jaFoiCapturada(peca, tabuleiro.getCapturadasJogadorBranco())) {
                                    tabuleiro.addCapturadasJogadorBranco(peca);
                                }
                            }
                        }
                    }
                }
            } else if (parte.startsWith("PecasCapturadasPreto:")) {
                String capturadasStr = parte.contains(":") ? parte.substring(parte.indexOf(":") + 1) : "";
                if (!capturadasStr.isEmpty()) {
                    String[] capturadas = capturadasStr.split(";");
                    for (String capturada : capturadas) {
                        if (!capturada.isEmpty()) {
                            String[] dados = capturada.split(",");
                            if (dados.length == 2) {
                                String tipoPeca = dados[0];
                                Cor cor = Cor.valueOf(dados[1]);
                                Peca peca = criarPeca(tipoPeca, cor);
                                if (!jaFoiCapturada(peca, tabuleiro.getCapturadasJogadorPreto())) {
                                    tabuleiro.addCapturadasJogadorPreto(peca);
                                }
                            }
                        }
                    }
                }
            } else if (parte.contains(",")) {
                String[] dados = parte.split(",");
                if (dados.length == 5) {
                    int linha = Integer.parseInt(dados[0]);
                    int coluna = Integer.parseInt(dados[1]);
                    Cor cor = Cor.valueOf(dados[2]);
                    int movCount = Integer.parseInt(dados[3]);
                    String tipoPeca = dados[4];
                    Peca peca = criarPeca(tipoPeca, cor);
                    if (peca != null) {
                        peca.setMovCount(movCount);
                        tabuleiro.getCasa(new Posicao(linha, coluna)).setPeca(peca);
                    }
                }
            }
        }
    }

    /**
     * Cria uma cópia da partida atual.
     * 
     * @return Uma nova instância da partida com o mesmo estado da partida original.
     */
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
}