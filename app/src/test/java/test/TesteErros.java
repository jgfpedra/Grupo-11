package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import partida.Cor;
import partida.Movimento;
import partida.Posicao;
import partida.Tabuleiro;
import pecas.Peao;
import pecas.Rei;
import pecas.Torre;

public class TesteErros {
    @Test
    public void testMovimentoInvalido() {
        Tabuleiro tabuleiro = new Tabuleiro();
        Peao peao = new Peao(Cor.BRANCO);
        Posicao origem = new Posicao(1, 0);
        Posicao destino = new Posicao(3, 0);
        
        Movimento movimento = new Movimento(origem, destino, peao);
        assertThrows(MovimentoInvalidoException.class, () -> {
            movimento.aplicar(tabuleiro);  // Tentando aplicar um movimento inválido
        });
    }

    @Test
    public void testReiEmCheck() {
        Tabuleiro tabuleiro = new Tabuleiro();
        Rei rei = new Rei(Cor.BRANCO);
        Posicao origemRei = new Posicao(0, 4);
        Posicao destinoRei = new Posicao(0, 5);
        Movimento movimento = new Movimento(origemRei, destinoRei, rei);
        assertThrows(ReiEmCheckException.class, () -> {
            movimento.aplicar(tabuleiro);  // Tentando aplicar movimento que coloca o Rei em check
        });
    }

    @Test
    public void testCaminhoBloqueado() {
        // Criação de um tabuleiro e peças para testar um caminho bloqueado
        Tabuleiro tabuleiro = new Tabuleiro();
        Torre torre = new Torre(Cor.BRANCO);
        
        Posicao origemTorre = new Posicao(0, 0);
        Posicao destinoTorre = new Posicao(0, 3);
        Movimento movimento = new Movimento(origemTorre, destinoTorre, torre);
        
        // Verifica se a exceção CaminhoBloqueadoException é lançada quando o caminho está bloqueado
        assertThrows(CaminhoBloqueadoException.class, () -> {
            movimento.aplicar(tabuleiro);  // Tentando mover a torre para uma casa bloqueada
        });
    }

    @Test
    public void testRoqueInvalido() {
        Tabuleiro tabuleiro = new Tabuleiro();
        Rei rei = new Rei(Cor.BRANCO);
        Posicao origemRei = new Posicao(0, 4);
        Posicao destinoRei = new Posicao(0, 6);
        Movimento movimento = new Movimento(origemRei, destinoRei, rei);
        assertThrows(RoqueInvalidoException.class, () -> {
            movimento.aplicar(tabuleiro);
        });
    }
}