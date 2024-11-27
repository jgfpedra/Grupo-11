package test;

public class ErroPartida {
    /*

    @Test
    public void testMovimentoInvalido() {
        // Criação de um tabuleiro e peças para o teste
        Tabuleiro tabuleiro = new Tabuleiro();
        Peca peao = new Peao("branco");
        Posicao origem = new Posicao(1, 0);  // Exemplo de posição do peão
        Posicao destino = new Posicao(3, 0); // Movimento inválido para peão (não pode pular duas casas, por exemplo)

        tabuleiro.colocarPeca(peao, origem);
        
        Movimento movimento = new Movimento(origem, destino, peao);
        
        // Verifica se a exceção MovimentoInvalidoException é lançada
        assertThrows(MovimentoInvalidoException.class, () -> {
            movimento.aplicar(tabuleiro);  // Tentando aplicar um movimento inválido
        });
    }

    @Test
    public void testReiEmCheck() {
        // Criar um tabuleiro com o Rei em check
        Tabuleiro tabuleiro = new Tabuleiro();
        Peca rei = new Rei("branco");
        Peca torre = new Torre("preto");
        
        Posicao origemRei = new Posicao(0, 4);
        Posicao destinoRei = new Posicao(0, 5);  // Rei tenta se mover para uma casa que o deixa em check
        
        Posicao origemTorre = new Posicao(1, 4);
        
        // Coloca o rei e a torre no tabuleiro
        tabuleiro.colocarPeca(rei, origemRei);
        tabuleiro.colocarPeca(torre, origemTorre);
        
        Movimento movimento = new Movimento(origemRei, destinoRei, rei);
        
        // Verifica se a exceção ReiEmCheckException é lançada quando o rei se move para uma posição em check
        assertThrows(ReiEmCheckException.class, () -> {
            movimento.aplicar(tabuleiro);  // Tentando aplicar movimento que coloca o Rei em check
        });
    }

    @Test
    public void testCaminhoBloqueado() {
        // Criação de um tabuleiro e peças para testar um caminho bloqueado
        Tabuleiro tabuleiro = new Tabuleiro();
        Peca torre = new Torre("branco");
        Peca peao = new Peao("preto");
        
        Posicao origemTorre = new Posicao(0, 0);
        Posicao destinoTorre = new Posicao(0, 3);  // Torre tenta se mover para uma casa bloqueada por uma peça
        
        tabuleiro.colocarPeca(torre, origemTorre);
        tabuleiro.colocarPeca(peao, new Posicao(0, 2));  // Bloqueando o caminho da torre
        
        Movimento movimento = new Movimento(origemTorre, destinoTorre, torre);
        
        // Verifica se a exceção CaminhoBloqueadoException é lançada quando o caminho está bloqueado
        assertThrows(CaminhoBloqueadoException.class, () -> {
            movimento.aplicar(tabuleiro);  // Tentando mover a torre para uma casa bloqueada
        });
    }

    @Test
    public void testRoqueInvalido() {
        // Criação de um tabuleiro e peças para testar o roque inválido
        Tabuleiro tabuleiro = new Tabuleiro();
        Peca rei = new Rei(Cor.BRANCO);
        Peca torre = new Torre(Cor.BRANCO);
        
        Posicao origemRei = new Posicao(0, 4);
        Posicao destinoRei = new Posicao(0, 6);  // Tentando realizar um roque pequeno
        
        tabuleiro.colocarPeca(rei, origemRei);
        tabuleiro.colocarPeca(torre, new Posicao(0, 7));  // A torre precisa estar na posição correta
        
        Movimento movimento = new Movimento(origemRei, destinoRei, rei);
        
        // Verifica se a exceção RoqueInvalidoException é lançada quando o roque é inválido
        assertThrows(RoqueInvalidoException.class, () -> {
            movimento.aplicar(tabuleiro);  // Tentando fazer o roque com as peças de maneira inválida
        });
    }*/
}