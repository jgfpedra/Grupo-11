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
import pecas.Peao;
import pecas.Rei;
import pecas.Torre;

public class TesteErros {
    @Test
    public void testMovimentoInvalido() {
        JogadorLocal jogador1 = new JogadorLocal(Cor.BRANCO, "jogador1");
        JogadorLocal jogador2 = new JogadorLocal(Cor.PRETO, "jogador2");
        Partida partida = new Partida(jogador1, jogador2, null);
        Posicao origem = new Posicao(1, 0);
        Posicao destino = new Posicao(3, 0);
        Movimento movimento = new Movimento(origem, destino, partida.getTabuleiro().obterPeca(origem));
        assertThrows(MovimentoInvalidoException.class, () -> {
            partida.jogar(movimento);
        });
    }

    @Test
    public void testReiEmCheck() {
        JogadorLocal jogador1 = new JogadorLocal(Cor.BRANCO, "jogador1");
        JogadorLocal jogador2 = new JogadorLocal(Cor.PRETO, "jogador2");
        Partida partida = new Partida(jogador1, jogador2, null);
        partida.getTabuleiro().limparTabuleiro();;
    
        Posicao origemRei = new Posicao(0, 4);
        partida.getTabuleiro().colocarPeca(new Rei(Cor.PRETO), origemRei);
        partida.getTabuleiro().colocarPeca(new Peao(Cor.BRANCO), new Posicao(0, 5));
    
        Posicao destinoRei = new Posicao(0, 5);
        Movimento movimento = new Movimento(origemRei, destinoRei, partida.getTabuleiro().obterPeca(origemRei));
    
        assertThrows(ReiEmCheckException.class, () -> {
            partida.jogar(movimento);
        });
    }

    @Test
    public void testCaminhoBloqueado() {
        JogadorLocal jogador1 = new JogadorLocal(Cor.BRANCO, "jogador1");
        JogadorLocal jogador2 = new JogadorLocal(Cor.PRETO, "jogador2");
        Partida partida = new Partida(jogador1, jogador2, null);
        partida.getTabuleiro().limparTabuleiro();
    
        Torre torre = (Torre) partida.getTabuleiro().obterPeca(new Posicao(0, 0));
        Posicao destinoTorre = new Posicao(0, 3);
        Posicao posicaoBloqueio = new Posicao(0, 1);
    
        partida.getTabuleiro().colocarPeca(new Torre(Cor.BRANCO), posicaoBloqueio);
    
        Movimento movimento = new Movimento(new Posicao(0, 0), destinoTorre, torre);
    
        assertThrows(CaminhoBloqueadoException.class, () -> {
            partida.jogar(movimento);
        });
    }

    @Test
    public void testRoqueInvalido() {
        JogadorLocal jogador1 = new JogadorLocal(Cor.BRANCO, "jogador1");
        JogadorLocal jogador2 = new JogadorLocal(Cor.PRETO, "jogador2");
        Partida partida = new Partida(jogador1, jogador2, null);
        partida.getTabuleiro().limparTabuleiro();
    
        Rei rei = new Rei(Cor.BRANCO);
        Torre torre = new Torre(Cor.BRANCO);
    
        Posicao origemRei = new Posicao(0, 4);
        Posicao origemTorre = new Posicao(0, 7);
    
        partida.getTabuleiro().colocarPeca(rei, origemRei);
        partida.getTabuleiro().colocarPeca(torre, origemTorre);
    
        Posicao destinoRei = new Posicao(0, 6);
        Movimento movimento = new Movimento(origemRei, destinoRei, rei);
    
        assertThrows(RoqueInvalidoException.class, () -> {
            partida.jogar(movimento);
        });
    }  
}