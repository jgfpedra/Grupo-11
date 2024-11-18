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

    public Partida(){

    }
    public Partida(Jogador jogador1, Jogador jogador2){
        this.turno = 0;
        this.estadoJogo = EstadoJogo.EM_ANDAMENTO;
        this.jogador1 = jogador1;
        this.jogador2 = jogador2;
        this.tabuleiro = new Tabuleiro();
        this.inicioPartida = LocalDateTime.now();
    }
    public void jogar(){
        // Aplica o movimento no tabuleiro
        tabuleiro.aplicarMovimento(movimento);

        // Verifica se o movimento resultou em check ou checkmate
        if (verificaCheck(movimento)) {
            check = true;
            if (verificaFimJogo()) {
                checkMate = true;
                System.out.println("Checkmate! " + jogadorAtual.getNome() + " venceu!");
                return;
            }
        }

        // Alterna o turno
        mudarTurno();
    }
    public void salvarTabuleiro(){

    }
    public void carregarTabuleiro(){

    }
    public void voltaTurno(){
        // Aqui você pode implementar a lógica de reverter o último movimento, 
        // restaurando o estado anterior do tabuleiro e a contagem do turno.
        // Uma maneira simples seria armazenar o histórico de movimentos em um `HistoricoMovimentos`
        historico.desfazerUltimoMovimento();
        turno--;  // Decrementa o turno
        mudarTurno();  // Volta o turno para o jogador anterior
    }
    public Jogador getJogadorAtual(){
        return jogadorAtual;
    }
    public int getTurno(){
        return turno;
    }
    public Tabuleiro getTabuleiro(){
        return tabuleiro;
    }
    private void mudarTurno(){
    // Alterna entre os jogadores
        if (jogadorAtual.equals(jogador1)) {
            jogadorAtual = jogador2;
        } else {
            jogadorAtual = jogador1;
        }
        turno++;  // Aumenta o turno após a jogada
    }
    private boolean verificaFimJogo(){
        if (checkMate) {
            return true;  // O jogo terminou com checkmate
        }
        if (check) {
            // Verifique se o jogador em check tem movimentos válidos para sair do check
            // Se não, o jogo termina em checkmate
            return false;
        }
        // Verifique outras condições de empate (por exemplo, empate por insuficiência de material)
        return false;
    }
}
