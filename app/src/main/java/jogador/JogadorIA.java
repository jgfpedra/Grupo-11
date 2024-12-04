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

/**
 * Representa um jogador controlado por inteligência artificial (IA) em um jogo de xadrez.
 * A IA pode ter diferentes níveis de dificuldade, variando entre movimentos aleatórios, movimentos estratégicos médios e
 * movimentos baseados em uma árvore de decisão utilizando o algoritmo minimax.
 * 
 * O nível de dificuldade influencia o comportamento da IA, podendo ser:
 * - Nível 1: Movimentos aleatórios.
 * - Nível 2: Movimentos baseados em estratégias simples, como captura de peças valiosas.
 * - Nível 3: Movimentos baseados no algoritmo minimax, considerando várias jogadas futuras para maximizar a pontuação.
 */
public class JogadorIA extends Jogador {
    private int nivelDificuldade;

    /**
     * Construtor padrão para o JogadorIA.
     * Inicializa o jogador com a cor e nome fornecidos, mas sem imagem associada.
     */
    public JogadorIA() {
    }

    /**
     * Construtor que inicializa o jogador de IA com cor, nome e nível de dificuldade.
     *
     * @param cor             A cor do jogador (branco ou preto).
     * @param nome            O nome do jogador.
     * @param nivelDificuldade O nível de dificuldade da IA (1, 2 ou 3).
     */
    public JogadorIA(Cor cor, String nome, int nivelDificuldade) {
        super(cor, nome, null);
        this.nivelDificuldade = nivelDificuldade;
    }

    /**
     * Escolhe o movimento para a IA realizar com base no nível de dificuldade.
     * A IA pode escolher entre três abordagens diferentes dependendo do nível:
     * - Nível 1: Movimentos aleatórios.
     * - Nível 2: Movimentos que capturam peças valiosas.
     * - Nível 3: Movimentos baseados no algoritmo minimax.
     *
     * @param partida A partida em andamento.
     */
    public void escolherMovimento(Partida partida) {
        List<Movimento> possiveisMovimentos = partida.getTabuleiro().getPossiveisMovimentos(this);
        Movimento movimentoEscolhido = null;
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

    /**
     * Escolhe um movimento aleatório entre os movimentos possíveis.
     *
     * @param possiveisMovimentos A lista de movimentos possíveis.
     * @return O movimento escolhido aleatoriamente.
     */
    private Movimento escolherMovimentoAleatorio(List<Movimento> possiveisMovimentos) {
        Random random = new Random();
        return possiveisMovimentos.get(random.nextInt(possiveisMovimentos.size()));
    }

    /**
     * Escolhe um movimento médio, dando preferência à captura de peças valiosas.
     * Se não houver nenhuma captura valiosa, escolhe o primeiro movimento da lista.
     *
     * @param possiveisMovimentos A lista de movimentos possíveis.
     * @param tabuleiro O tabuleiro da partida.
     * @return O movimento escolhido com base na estratégia média.
     */
    private Movimento escolherMovimentoMedio(List<Movimento> possiveisMovimentos, Tabuleiro tabuleiro) {
        for (Movimento movimento : possiveisMovimentos) {
            if (movimentoCapturaPecaValiosa(movimento)) {
                return movimento;
            }
        }
        return possiveisMovimentos.get(0);
    }

    /**
     * Verifica se o movimento captura uma peça valiosa.
     *
     * @param movimento O movimento a ser analisado.
     * @return True se o movimento captura uma peça valiosa, False caso contrário.
     */
    private boolean movimentoCapturaPecaValiosa(Movimento movimento) {
        Peca pecaCapturada = movimento.getPecaCapturada();
        if (pecaCapturada != null) {
            int valorPecaCapturada = obterValorPeca(pecaCapturada);
            return valorPecaCapturada >= 5;
        }
        return false;
    }

    /**
     * Escolhe um movimento difícil, utilizando o algoritmo minimax para maximizar a pontuação do jogador.
     * A IA simula diferentes jogadas e escolhe a melhor possível considerando uma profundidade de 3 movimentos futuros.
     *
     * @param possiveisMovimentos A lista de movimentos possíveis.
     * @param partida A partida atual em andamento.
     * @return O movimento escolhido com base na análise do algoritmo minimax.
     */
    private Movimento escolherMovimentoDificil(List<Movimento> possiveisMovimentos, Partida partida) {
        Movimento melhorMovimento = null;
        int melhorValor = Integer.MIN_VALUE;
        for (Movimento movimento : possiveisMovimentos) {
            Partida novaPartida = partida.clone();
            novaPartida.jogar(movimento);
            int valor = minimax(novaPartida, 3, Integer.MIN_VALUE, Integer.MAX_VALUE, false, getCor()); 
            if (valor > melhorValor) {
                melhorValor = valor;
                melhorMovimento = movimento;
            }
        }
        return melhorMovimento != null ? melhorMovimento : possiveisMovimentos.get(0);
    }
    
    /**
     * Implementa o algoritmo Minimax com poda Alpha-Beta para determinar o valor de um movimento,
     * maximizando ou minimizando a pontuação do jogador dependendo da perspectiva.
     *
     * @param partida A partida simulada após a jogada.
     * @param profundidade A profundidade da árvore de decisão.
     * @param alpha O valor atual de alpha (poda).
     * @param beta O valor atual de beta (poda).
     * @param maximizando True se for a vez do jogador maximizar, False se for a vez de minimizar.
     * @param corJogador A cor do jogador atual.
     * @return O valor do tabuleiro após a jogada simulada.
     */
    private int minimax(Partida partida, int profundidade, int alpha, int beta, boolean maximizando, Cor corJogador) {
        if (profundidade == 0 || partida.getEstadoJogo() == EstadoJogo.FIM) {
            return avaliarTabuleiro(partida.getTabuleiro(), corJogador);
        }
        if (maximizando) {
            int melhorValor = Integer.MIN_VALUE;
            for (Movimento movimento : partida.getTabuleiro().getPossiveisMovimentos(this)) {
                Partida novaPartida = partida.clone();
                novaPartida.jogar(movimento);
                int valor = minimax(novaPartida, profundidade - 1, alpha, beta, false, corJogador);
                melhorValor = Math.max(melhorValor, valor);
                alpha = Math.max(alpha, valor);
                if (beta <= alpha) break;
            }
            return melhorValor;
        } else {
            int melhorValor = Integer.MAX_VALUE;
            for (Movimento movimento : partida.getTabuleiro().getPossiveisMovimentos(this)) {
                Partida novaPartida = partida.clone();
                novaPartida.jogar(movimento);
                int valor = minimax(novaPartida, profundidade - 1, alpha, beta, true, corJogador);
                melhorValor = Math.min(melhorValor, valor);
                beta = Math.min(beta, valor);
                if (beta <= alpha) break;
            }
            return melhorValor;
        }
    }

    /**
     * Avalia o tabuleiro para determinar a pontuação de um jogador, somando os valores das peças
     * do jogador e subtraindo os valores das peças do adversário.
     *
     * @param tabuleiro O tabuleiro atual do jogo.
     * @param corJogador A cor do jogador para o qual a avaliação está sendo realizada.
     * @return A pontuação total do jogador considerando todas as peças no tabuleiro.
     */
    private int avaliarTabuleiro(Tabuleiro tabuleiro, Cor corJogador) {
        int pontuacao = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Posicao posicao = new Posicao(i, j);
                Peca peca = tabuleiro.obterPeca(posicao);
                if (peca != null) {
                    if (peca.getCor() == corJogador) {
                        pontuacao += obterValorPeca(peca);
                    } else {
                        pontuacao -= obterValorPeca(peca);
                    }
                }
            }
        }
        return pontuacao;
    }

    /**
     * Retorna o valor da peça, utilizado na avaliação do tabuleiro.
     * O valor das peças pode ser ajustado conforme a necessidade da lógica do jogo.
     *
     * @param peca A peça para a qual se deseja obter o valor.
     * @return O valor da peça.
     */
    private int obterValorPeca(Peca peca){
        return peca.getValor();
    }
}