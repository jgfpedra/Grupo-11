package controle;

import jogador.JogadorOnline;
import partida.Cor;
import partida.Partida;
import view.TabuleiroView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class PartidaOnlineControle {

    private JogadorOnline jogador1;
    private JogadorOnline jogador2;
    private Partida partida;
    private Stage stage;

    public PartidaOnlineControle(Stage primaryStage) {
        this.stage = primaryStage;
    }

    public boolean criarPartida(String nomeJogador1, Image imagemJogador1, Cor corJogador1, int porta) {
        jogador1 = new JogadorOnline(corJogador1, nomeJogador1, imagemJogador1);
        Socket socket = jogador1.criarServidor(porta);
        if (socket != null) {
            new Thread(() -> {
                aceitarConexaoJogador2(socket);
            }).start();
            return true;
        }
        return false;
    }

    public void aceitarConexaoJogador2(Socket socket) {
        try {
            // Inicializando o fluxo de entrada para receber dados do Jogador 2
            DataInputStream input = new DataInputStream(socket.getInputStream());
            String nomeJogador2 = input.readUTF();
            Cor corJogador2 = Cor.valueOf(input.readUTF());
            Image imagemJogador2 = new Image(input.readUTF());
            System.out.println("Jogador 2: " + nomeJogador2 + " (Cor: " + corJogador2 + ")");
            
            // Criando o Jogador 2 e a partida
            jogador2 = new JogadorOnline(corJogador2, nomeJogador2, imagemJogador2);
            partida = new Partida(jogador1, jogador2, null);
            enviarEstadoPartida(socket);
            enviarEstadoParaJogador1();
            iniciarPartida();
        
        } catch (IOException e) {
            System.out.println("Erro ao receber dados do Jogador 2: " + e.getMessage());
        }
    }      

    public boolean entrarPartida(Cor corJogador2, String nomeJogador2, Image imagemJogador2, String ipServidor, int portaServidor) {
        jogador2 = new JogadorOnline(corJogador2, nomeJogador2, imagemJogador2);
        if (jogador2.conectar(ipServidor, portaServidor)) {
            // Receber o estado da partida do servidor
            String estadoTabuleiro = receberEstadoPartida(jogador2.getSocket());
    
            // Criar a partida no lado do cliente e atualizar o tabuleiro
            Platform.runLater(() -> {
                partida = new Partida(jogador1, jogador2, null);  // Criando a partida
                partida.fromEstadoTabuleiro(estadoTabuleiro);  // Atualizando o tabuleiro com o estado recebido
                iniciarPartida();  // Atualizando a interface do cliente
            });
            return true;
        }
        return false;
    }
    
    private String receberEstadoPartida(Socket socket) {
        try {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            return input.readUTF();  // Supondo que você tenha uma representação textual do estado do tabuleiro
        } catch (IOException e) {
            System.out.println("Erro ao receber estado da partida: " + e.getMessage());
            return null;
        }
    }    
    
    public void iniciarPartida() {
        if (partida != null) {
            // Cria a interface do tabuleiro
            TabuleiroView tabuleiroView = new TabuleiroView(partida);
            new TabuleiroControle(partida, tabuleiroView, stage);  // Inicializa o controlador do tabuleiro
    
            // Usar Platform.runLater() para garantir que as alterações de UI ocorram no thread de eventos
            Platform.runLater(() -> {
                stage.setTitle("Jogo de Xadrez Online");  // Atualizar título
                stage.setScene(new Scene(tabuleiroView, 800, 800));  // Definir a cena com o tabuleiro
                stage.getScene().getStylesheets().add(getClass().getResource("/style/tabuleiro.css").toExternalForm());  // Adicionar o estilo CSS
                stage.show();  // Exibir a janela
            });
        }
    }

    private void enviarEstadoPartida(Socket socket) {
        try {
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            // Enviar informações sobre o estado do jogo
            // Exemplo: informações sobre as peças no tabuleiro, turno atual, etc.
            output.writeUTF(partida.getEstadoTabuleiro());  // Apenas um exemplo; você precisa implementar o método getEstadoTabuleiro
            output.flush();
        } catch (IOException e) {
            System.out.println("Erro ao enviar estado da partida: " + e.getMessage());
        }
    }

    private void enviarEstadoParaJogador1() {
        try {
            // Verifique se o socket do Jogador 1 está disponível
            Socket socketJogador1 = jogador1.getSocket();  // Assume que você tenha um método para pegar o socket
            if (socketJogador1 != null) {
                // Enviar o estado do tabuleiro para o Jogador 1
                DataOutputStream outputJogador1 = new DataOutputStream(socketJogador1.getOutputStream());
                outputJogador1.writeUTF(partida.getEstadoTabuleiro());  // Enviar o estado do tabuleiro
                outputJogador1.flush();
            } else {
                System.out.println("Erro: O socket do Jogador 1 não está disponível.");
            }
        } catch (IOException e) {
            System.out.println("Erro ao enviar estado para o Jogador 1: " + e.getMessage());
        }
    }    

}