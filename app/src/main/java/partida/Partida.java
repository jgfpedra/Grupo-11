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
    public void jogar(Movimento movimento){
        // Aplica o movimento no tabuleiro
        tabuleiro.aplicarMovimento(movimento);

        // Verifica se o movimento resultou em check ou checkmate
        if (verificaCheckMate()) {
            checkMate = true;
            System.out.println("Checkmate! " + jogadorAtual.getNome() + " venceu!");
            estadoJogo = EstadoJogo.FIM;
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
    private boolean verificaCheck() {
        // Verifica se o jogador atual está em check (o rei está ameaçado)
        Posicao posicaoRei = tabuleiro.getPosicaoRei(jogadorAtual.getCor()); // Método hipotético que retorna a posição do rei do jogador
        return tabuleiro.isReiEmCheck(posicaoRei, jogadorAtual.getCor());
    }

    private boolean verificaCheckMate() {
        // Verifica se o jogador atual está em checkmate
        if (verificaCheck()) {
            // Verifique se o jogador tem movimentos válidos para sair do check
            // Se o jogador não tem movimentos válidos para sair do check, é checkmate
            return !temMovimentosValidosParaSairDoCheck();
        }
        return false;
    }

    private boolean temMovimentosValidosParaSairDoCheck() {
        // Verifica se o jogador atual tem movimentos válidos que o tirem do check
        for (Posicao posicaoOrigem : tabuleiro.getPosicoesComPecas(jogadorAtual.getCor())) {
            for (Posicao destino : tabuleiro.getPossiveisDestinos(posicaoOrigem)) {
                if (tabuleiro.isMovimentoSeguro(posicaoOrigem, destino, jogadorAtual.getCor())) {
                    return true;  // Se encontrar um movimento que não resulte em check, retorna true
                }
            }
        }
        return false;  // Se não encontrar nenhum movimento seguro, é checkmate
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
}
