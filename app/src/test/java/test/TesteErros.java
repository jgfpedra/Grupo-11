package test;

import org.junit.jupiter.api.Test;

import exception.CaminhoBloqueadoException;
import exception.MovimentoInvalidoException;
import exception.ReiEmCheckException;
import exception.RoqueInvalidoException;
import jogador.JogadorLocal;

import static org.junit.jupiter.api.Assertions.assertThrows;

import partida.Cor;
import partida.Movimento;
import partida.Partida;
import partida.Posicao;
import pecas.Rainha;
import pecas.Rei;

public class TesteErros {
        
    /**
     * Testa o lançamento de uma exceção MovimentoInvalidoException quando um movimento inválido é tentado.
     * Neste caso, o jogador tenta mover uma peça para uma posição inválida (uma casa fora dos movimentos permitidos).
     */
    @Test
    public void testMovimentoInvalido() {
        JogadorLocal jogador1 = new JogadorLocal(Cor.BRANCO, "jogador1");
        JogadorLocal jogador2 = new JogadorLocal(Cor.PRETO, "jogador2");
        Partida partida = new Partida(jogador1, jogador2, null);
        Posicao origem = new Posicao(1, 0);
        Posicao destino = new Posicao(4, 0);
        Movimento movimento = new Movimento(origem, destino, partida.getTabuleiro().obterPeca(origem));
        assertThrows(MovimentoInvalidoException.class, () -> {
            partida.jogar(movimento);
        });
    }

    /**
     * Testa o lançamento de uma exceção ReiEmCheckException quando o Rei é movido para uma posição 
     * onde ele ficaria em cheque.
     * Neste caso, o movimento do Rei é bloqueado por um Peão adversário, deixando o Rei em cheque.
     */
    @Test
    public void testReiEmCheck() {
        JogadorLocal jogador1 = new JogadorLocal(Cor.BRANCO, "jogador1");
        JogadorLocal jogador2 = new JogadorLocal(Cor.PRETO, "jogador2");
        Partida partida = new Partida(jogador1, jogador2, null);
        partida.getTabuleiro().limparTabuleiro();
        Posicao origemRei = new Posicao(0, 0);
        partida.getTabuleiro().colocarPeca(new Rei(Cor.PRETO), origemRei);
        partida.getTabuleiro().colocarPeca(new Rainha(Cor.BRANCO), new Posicao(2, 0));
        Posicao destinoRei = new Posicao(1, 0);
        Movimento movimento = new Movimento(origemRei, destinoRei, partida.getTabuleiro().obterPeca(origemRei));
        assertThrows(ReiEmCheckException.class, () -> {
            partida.jogar(movimento);
        });
    }    

    /**
     * Testa o lançamento de uma exceção CaminhoBloqueadoException quando uma peça tenta mover-se
     * através de um caminho bloqueado por outra peça.
     * Neste caso, uma Torre tenta mover-se, mas encontra uma peça bloqueando seu caminho.
     */
    @Test
    public void testCaminhoBloqueado() {
        JogadorLocal jogador1 = new JogadorLocal(Cor.BRANCO, "jogador1");
        JogadorLocal jogador2 = new JogadorLocal(Cor.PRETO, "jogador2");
        Partida partida = new Partida(jogador1, jogador2, null);
        Movimento movimento = new Movimento(new Posicao(0, 0), new Posicao(0, 3), partida.getTabuleiro().obterPeca(new Posicao(0, 0)));
        assertThrows(CaminhoBloqueadoException.class, () -> {
            partida.jogar(movimento);
        });
    }

    /**
     * Testa o lançamento de uma exceção RoqueInvalidoException quando um roque é tentado, mas não é válido.
     * Neste caso, o Rei tenta se mover durante o roque, mas não é possível devido a peças bloqueando o caminho.
     */
    @Test
    public void testRoqueInvalido() {
        JogadorLocal jogador1 = new JogadorLocal(Cor.BRANCO, "jogador1");
        JogadorLocal jogador2 = new JogadorLocal(Cor.PRETO, "jogador2");
        Partida partida = new Partida(jogador1, jogador2, null);
        Movimento movimento = new Movimento(new Posicao(0, 4), new Posicao(0, 7), partida.getTabuleiro().obterPeca(new Posicao(0, 4)));
        assertThrows(RoqueInvalidoException.class, () -> {
            partida.jogar(movimento);
        });
    }  
}