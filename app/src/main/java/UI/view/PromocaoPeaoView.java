package UI.view;

import java.util.concurrent.atomic.AtomicReference;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import partida.Cor;
import pecas.Bispo;
import pecas.Cavalo;
import pecas.Peca;
import pecas.Rainha;
import pecas.Torre;

public class PromocaoPeaoView {
    /**
     * Exibe uma janela de escolha para o jogador ao atingir a última linha.
     * 
     * @param corDoPeao Cor do peão a ser promovido.
     * @return A peça escolhida pelo jogador para a promoção.
     */
    public Peca promoverPeao(Cor corDoPeao) {
        AtomicReference<Peca> pecaEscolhida = new AtomicReference<>(null);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Promoção de Peão");
        alert.setHeaderText("Escolha a peça para promover o seu peão:");
        ButtonType rainhaButton = new ButtonType("Rainha");
        ButtonType torreButton = new ButtonType("Torre");
        ButtonType bispoButton = new ButtonType("Bispo");
        ButtonType cavaloButton = new ButtonType("Cavalo");
        alert.getButtonTypes().setAll(rainhaButton, torreButton, bispoButton, cavaloButton);
        alert.showAndWait().ifPresent(response -> {
            if (response == rainhaButton) {
                pecaEscolhida.set(new Rainha(corDoPeao));
            } else if (response == torreButton) {
                pecaEscolhida.set(new Torre(corDoPeao));
            } else if (response == bispoButton) {
                pecaEscolhida.set(new Bispo(corDoPeao));
            } else if (response == cavaloButton) {
                pecaEscolhida.set(new Cavalo(corDoPeao));
            }
        });
        return pecaEscolhida.get();
    }
}
