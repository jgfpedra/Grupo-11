package controle;

import jogador.JogadorOnline;
import partida.Cor;
import partida.Partida;
import view.TabuleiroView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class PartidaOnlineControle {

    private JogadorOnline jogador1;
    private JogadorOnline jogador2;
    private String codigoSala;
    private Partida partida;
    private Stage stage;

    public PartidaOnlineControle(Stage primaryStage) {
        this.stage = primaryStage;
    }

    public String criarPartida(String nomeJogador1, Image imagemJogador1, Cor corJogador1, int porta) {
        jogador1 = new JogadorOnline(corJogador1, nomeJogador1, imagemJogador1);
        codigoSala = gerarCodigoSala();
        if (jogador1.criarServidor(porta, codigoSala)) {
            new Thread(() -> {
                aceitarConexaoJogador2(codigoSala);
            }).start();
            String ipJogador1 = jogador1.getSocket().getInetAddress().getHostAddress();
            return "Código da sala: " + codigoSala + " IP do servidor: " + ipJogador1 + ", Porta: " + porta;
        }
        return null;
    }

    private String gerarCodigoSala() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";  // Letras maiúsculas e números
        Random random = new Random();
        StringBuilder codigo = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(caracteres.length());
            codigo.append(caracteres.charAt(index));
        }
        
        return codigo.toString();  // Retorna o código gerado
    }

    public void aceitarConexaoJogador2(String codigoSala) {
        try {
            DataInputStream input = new DataInputStream(jogador2.getSocket().getInputStream());
            String nomeJogador2 = input.readUTF();
            Cor corJogador2 = Cor.valueOf(input.readUTF());
            Image imagemJogador2 = new Image(input.readUTF());
            String codigoSalaJogador2 = input.readUTF();
            if(codigoSalaJogador2.equals(codigoSala)){
                System.out.println("Jogador 2: " + nomeJogador2 + " (Cor: " + corJogador2 + ")");
                JogadorOnline jogador2 = new JogadorOnline(corJogador2, nomeJogador2, imagemJogador2);
                partida = new Partida(jogador2, jogador2, null);
                iniciarPartida();
            } else {
                System.out.println("Codigo de sala errado");
            }
        } catch (IOException e) {
            System.out.println("Erro ao receber dados do Jogador 2: " + e.getMessage());
        }
    }

    public boolean entrarPartida(String nomeJogador2, Image imagemJogador2, String ipServidor, int portaServidor, String codigoSala) {
        try {
            jogador2 = new JogadorOnline(Cor.PRETO, nomeJogador2, imagemJogador2);
            if (jogador2.conectar(ipServidor, portaServidor)) {
                DataOutputStream output = new DataOutputStream(jogador2.getSocket().getOutputStream());
                output.writeUTF(nomeJogador2);
                output.writeUTF(jogador2.getCor().name());
                output.writeUTF(jogador2.getImagem().getUrl());
                output.writeUTF(codigoSala);
                output.flush();
                return true;
            }
        } catch (IOException e) {
            System.out.println("Erro ao conectar com o servidor: " + e.getMessage());
        }
        return false;
    }

    public void iniciarPartida() {
        if (partida != null) {
            TabuleiroView tabuleiroView = new TabuleiroView(partida);
            new TabuleiroControle(partida, tabuleiroView, stage);  // Inicializa o controlador do tabuleiro

            stage.setTitle("Jogo de Xadrez Online");
            stage.setScene(new Scene(tabuleiroView, 800, 800));  // Define o tamanho da cena
            stage.getScene().getStylesheets().add(getClass().getResource("/style/tabuleiro.css").toExternalForm());  // Adiciona o estilo CSS
            stage.show();
        }
    }
}