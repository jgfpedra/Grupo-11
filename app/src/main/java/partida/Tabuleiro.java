package partida;

import java.util.ArrayList;
import java.util.List;

import pecas.Peca;

public class Tabuleiro {
    private List<List<Casa>> casas;
    private ArrayList<ObservadorTabuleiro> observadores;

    public Tabuleiro() {
        casas = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            List<Casa> row = new ArrayList<>();
            for (int j = 0; j < 8; j++) {
                // Assign color to each tile
                Cor cor = (i + j) % 2 == 0 ? Cor.BRANCO : Cor.PRETO; // Example: White and Black squares
                Posicao posicao = new Posicao(i, j);  // Position based on row and column
                row.add(new Casa(cor, posicao));  // Create a new Casa for each tile
            }
            casas.add(row);
        }
    }

    public Peca obterPeca(Posicao posicao){
        return casas.get(posicao.getLinha()).get(posicao.getColuna()).getPeca();
    }

    public void aplicarMovimento(Movimento movimento){
        Posicao origem = movimento.getOrigem();
        Posicao destino = movimento.getDestino();
        Peca pecaMovida = movimento.getPecaMovida();
    
        // Remove the piece from the origem (source) position
        Casa casaOrigem = getCasa(origem);
        casaOrigem.setPeca(null);  // Set the piece on the origin tile to null
    
        // Place the piece on the destino (destination) position
        Casa casaDestino = getCasa(destino);
        casaDestino.setPeca(pecaMovida);  // Set the moved piece to the destination tile
    
        // After the move, notify observers
        notificarObservador();
    }
    // Get a specific Casa
    public Casa getCasa(Posicao posicao) {
        return casas.get(posicao.getLinha()).get(posicao.getColuna());
    }
    // Set a specific Casa (if needed)
    public void setCasa(int linha, int coluna, Casa casa) {
        casas.get(linha).set(coluna, casa);
    }
    public void notificarObservador(){
        for (ObservadorTabuleiro observador : observadores) {
            observador.atualizar();  // Assuming 'atualizarTabuleiro' is a method to update the observers
        }
    }
    private void promovePeao(Posicao posicao){
        Casa casa = getCasa(posicao);
        Peca peca = casa.getPeca();
    
        if (peca instanceof Peao) {
            if (peca.getCor() == Cor.BRANCO && posicao.getLinha() == 7 || peca.getCor() == Cor.PRETO && posicao.getLinha() == 0) {
                // For simplicity, promote to a Queen for now
                Peca novaPeca = new Rainha(peca.getCor(), peca.getImagem());  // Assuming a 'Rainha' class for the Queen piece
                casa.setPeca(novaPeca);
            }
        }
    }
    private void roque(Posicao origemRei, Posicao destinoRei, Posicao origemTorre, Posicao destinoTorre){
        // 1. O rei e a torre não se moveram
        if (origemRei.getPeca().getMovimentos() > 0 || origemTorre.getPeca().getMovimentos() > 0) {
            throw new IllegalStateException("O rei ou a torre já se moveram.");
        }

        // 2. As casas entre o rei e a torre estão desocupadas
        int linha = origemRei.getLinha();  // Linha do rei e da torre (mesma linha para o roque)
        if (origemRei.getColuna() < origemTorre.getColuna()) {
            // Verifique as casas entre o rei e a torre (para roque pequeno)
            for (int col = origemRei.getColuna() + 1; col < origemTorre.getColuna(); col++) {
                if (getCasa(new Posicao(linha, col)).getPeca() != null) {
                    throw new IllegalStateException("Há peças entre o rei e a torre.");
                }
            }
        } else {
            // Verifique as casas entre o rei e a torre (para roque grande)
            for (int col = origemTorre.getColuna() + 1; col < origemRei.getColuna(); col++) {
                if (getCasa(new Posicao(linha, col)).getPeca() != null) {
                    throw new IllegalStateException("Há peças entre o rei e a torre.");
                }
            }
        }

        // 3. Verifique se o rei não está em xeque e não passará por casas atacadas
        if (estáEmXeque(origemRei.getPeca().getCor()) || estáEmXeque(destinoRei.getPeca().getCor())) {
            throw new IllegalStateException("O rei está em xeque.");
        }

        // 4. Realize o movimento do roque
        // Mova o rei
        Casa casaReiOrigem = getCasa(origemRei);
        casaReiOrigem.setPeca(null);  // Remove o rei da sua casa original
        Casa casaReiDestino = getCasa(destinoRei);
        casaReiDestino.setPeca(casaReiOrigem.getPeca());  // Coloca o rei na nova casa

        // Mova a torre
        Casa casaTorreOrigem = getCasa(origemTorre);
        casaTorreOrigem.setPeca(null);  // Remove a torre da sua casa original
        Casa casaTorreDestino = getCasa(destinoTorre);
        casaTorreDestino.setPeca(casaTorreOrigem.getPeca());  // Coloca a torre na nova casa

    }

    private void capturaPeca(Posicao origem, Posicao destino){
        Casa casaDestino = getCasa(destino);
        Peca pecaCapturada = casaDestino.getPeca();
        
        if (pecaCapturada != null) {
            // Capture the piece
            casaDestino.setPeca(null);
            // You could update the player's captured pieces list or take other actions
        }
    }
}