package jogador;

import java.util.List;
import java.util.Random;

import partida.Cor;
import partida.EstadoJogo;
import partida.Tabuleiro;
import pecas.Peca;
import partida.Movimento;
import partida.Partida;
import partida.Posicao;

public class JogadorIA extends Jogador {
    private int nivelDificuldade;  // Difficulty level for the AI

    // Default constructor
    public JogadorIA() {
    }

    // Constructor that takes the difficulty level
    public JogadorIA(Cor cor, String nome, int nivelDificuldade) {
        super(cor, nome);
        this.nivelDificuldade = nivelDificuldade;
    }

    @Override
    public void escolherMovimento(Partida partida) {
        List<Movimento> possiveisMovimentos = partida.getTabuleiro().getPossiveisMovimentos(this); // Get all possible moves for the AI's pieces
        Movimento movimentoEscolhido = null;

        // Choose the move based on difficulty
        switch (nivelDificuldade) {
            case 1:
                movimentoEscolhido = escolherMovimentoAleatorio(possiveisMovimentos);
                break;
            case 2:
                movimentoEscolhido = escolherMovimentoMedio(possiveisMovimentos, partida.getTabuleiro());
                break;
            case 3:
                movimentoEscolhido = escolherMovimentoDificil(possiveisMovimentos, partida);
                break;
            default:
                throw new IllegalArgumentException("Dificuldade inválida");
        }

        if (movimentoEscolhido != null) {
            partida.jogar(movimentoEscolhido);
        }
    }

    @Override
    public void temPecas() {
    }

    private Movimento escolherMovimentoAleatorio(List<Movimento> possiveisMovimentos) {
        Random random = new Random();
        return possiveisMovimentos.get(random.nextInt(possiveisMovimentos.size()));
    }

    private Movimento escolherMovimentoMedio(List<Movimento> possiveisMovimentos, Tabuleiro tabuleiro) {
        for (Movimento movimento : possiveisMovimentos) {
            if (movimentoCapturaPecaValiosa(movimento)) {
                return movimento;
            }
        }
        return possiveisMovimentos.get(0);
    }
    private boolean movimentoCapturaPecaValiosa(Movimento movimento) {
            return false;
    }

    private Movimento escolherMovimentoDificil(List<Movimento> possiveisMovimentos, Partida partida) {
        Movimento melhorMovimento = null;
        int melhorValor = Integer.MIN_VALUE;
        
        for (Movimento movimento : possiveisMovimentos) {
            // Clonando a partida (não apenas o tabuleiro)
            Partida novaPartida = partida.clone();  // Clona a partida inteira
            novaPartida.jogar(movimento);  // Aplica o movimento na nova partida
    
            // Usando o algoritmo Minimax com poda alpha-beta
            int valor = minimax(novaPartida, 3, Integer.MIN_VALUE, Integer.MAX_VALUE, false, getCor()); 
            if (valor > melhorValor) {
                melhorValor = valor;
                melhorMovimento = movimento;
            }
        }
        
        return melhorMovimento != null ? melhorMovimento : possiveisMovimentos.get(0);  // Retorna o melhor movimento encontrado
    }
    
    private int minimax(Partida partida, int profundidade, int alpha, int beta, boolean maximizando, Cor corJogador) {
        if (profundidade == 0 || partida.getEstadoJogo() == EstadoJogo.FIM) {
            return avaliarTabuleiro(partida.getTabuleiro(), corJogador);
        }
    
        if (maximizando) {
            int melhorValor = Integer.MIN_VALUE;
            for (Movimento movimento : partida.getTabuleiro().getPossiveisMovimentos(this)) {
                Partida novaPartida = partida.clone();  // Clona a partida
                novaPartida.jogar(movimento);
                int valor = minimax(novaPartida, profundidade - 1, alpha, beta, false, corJogador);
                melhorValor = Math.max(melhorValor, valor);
                alpha = Math.max(alpha, valor);
                if (beta <= alpha) break;
            }
            return melhorValor;
        } else {
            int melhorValor = Integer.MAX_VALUE;
            // Simula os movimentos para o adversário
            for (Movimento movimento : partida.getTabuleiro().getPossiveisMovimentos(this)) {
                Partida novaPartida = partida.clone();  // Clona a partida
                novaPartida.jogar(movimento);  // Aplica o movimento na nova partida
                int valor = minimax(novaPartida, profundidade - 1, alpha, beta, true, corJogador);
                melhorValor = Math.min(melhorValor, valor);
                beta = Math.min(beta, valor);
                if (beta <= alpha) break;  // Poda alpha-beta
            }
            return melhorValor;
        }
    }    
    
    private int avaliarTabuleiro(Tabuleiro tabuleiro, Cor corJogador) {
        int pontuacao = 0;
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Posicao posicao = new Posicao(i, j);
                Peca peca = tabuleiro.obterPeca(posicao);
                if (peca != null) {
                    if (peca.getCor() == corJogador) {
                        pontuacao += obterValorPeca(peca);  // Adiciona o valor da peça se for do jogador
                    } else {
                        pontuacao -= obterValorPeca(peca);  // Subtrai o valor se for do oponente
                    }
                }
            }
        }
        
        return pontuacao;
    }
    
    private int obterValorPeca(Peca peca){
        return peca.getValor();
    }

    public int getNivelDificuldade() {
        return nivelDificuldade;
    }

    public void setNivelDificuldade(int nivelDificuldade) {
        this.nivelDificuldade = nivelDificuldade;
    }
}