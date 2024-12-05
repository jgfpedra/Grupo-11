package UI.controle;

import jogador.JogadorOnline;
import partida.Cor;
import partida.Partida;
import UI.view.TabuleiroView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Controlador responsável pela gestão de partidas online de xadrez.
 * 
 * Esta classe gerencia a lógica de criação de uma partida online, incluindo a comunicação entre
 * os jogadores através de um servidor e cliente, o gerenciamento da partida e a configuração da
 * interface gráfica. Ela permite que um jogador crie uma partida como servidor e outro entre como
 * cliente, ou que ambos entrem em uma partida existente. Após a conexão, o jogo é iniciado e o
 * tabuleiro é exibido na interface.
 */
public class PartidaOnlineControle {

    private JogadorOnline jogador1;
    private JogadorOnline jogador2;
    private Partida partida;
    private Stage stage;

    /**
     * Constrói o controlador da partida online, associando-o à janela principal.
     * 
     * @param primaryStage A janela principal da aplicação.
     */
    public PartidaOnlineControle(Stage primaryStage) {
        this.stage = primaryStage;
    }

    /**
     * Cria uma nova partida online, onde o jogador 1 atua como servidor.
     * 
     * Este método configura o jogador 1 como servidor e aguarda a conexão do jogador 2. Quando
     * a conexão é estabelecida, ele envia as informações do jogador 1 (nome, cor e imagem) ao jogador 2.
     * 
     * @param nomeJogador1 Nome do jogador 1.
     * @param imagemJogador1 Imagem do jogador 1.
     * @param corJogador1 Cor das peças do jogador 1.
     * @param porta Porta do servidor para a conexão.
     * @return Um valor booleano indicando se a partida foi criada com sucesso.
     */
    public boolean criarPartida(String nomeJogador1, Image imagemJogador1, Cor corJogador1, int porta) {
        jogador1 = new JogadorOnline(corJogador1, nomeJogador1, imagemJogador1);
        Socket socket = jogador1.criarServidor(porta);
        if (socket != null) {
            new Thread(() -> {
                aceitarConexaoJogador2(socket);
            }).start();
            try {
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                output.writeUTF(jogador1.getNome());
                output.writeUTF(jogador1.getCor().name());
                output.writeUTF(jogador1.getImagem().getUrl());
            } catch (IOException e) {
                System.out.println("Erro ao enviar dados do Jogador 1: " + e.getMessage());
            }
            
            return true;
        }
        return false;
    }   

    /**
     * Aceita a conexão do jogador 2, após ele se conectar ao servidor do jogador 1.
     * 
     * Este método recebe os dados do jogador 2, cria o jogador 2 e inicializa a partida entre
     * os dois jogadores. Em seguida, inicia a partida.
     * 
     * @param socket O socket de comunicação entre o jogador 1 e o jogador 2.
     */
    public void aceitarConexaoJogador2(Socket socket) {
        try {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            String nomeJogador2 = input.readUTF();
            Cor corJogador2 = Cor.valueOf(input.readUTF());
            Image imagemJogador2 = new Image(input.readUTF());
            jogador2 = new JogadorOnline(corJogador2, nomeJogador2, imagemJogador2);
            partida = new Partida(jogador1, jogador2, null);
            iniciarPartida(socket, false);
        } catch (IOException e) {
            System.out.println("Erro ao receber dados do Jogador 2: " + e.getMessage());
        }
    }      

    /**
     * Permite que o jogador 2 entre em uma partida online existente, conectando-se ao servidor
     * do jogador 1.
     * 
     * Este método permite que o jogador 2 se conecte a uma partida já criada pelo jogador 1,
     * recebendo as informações do jogador 1, como nome, cor e imagem, e então iniciando a partida.
     * 
     * @param corJogador2 Cor das peças do jogador 2.
     * @param nomeJogador2 Nome do jogador 2.
     * @param imagemJogador2 Imagem do jogador 2.
     * @param ipServidor Endereço IP do servidor do jogador 1.
     * @param portaServidor Porta do servidor do jogador 1.
     * @return Um valor booleano indicando se o jogador 2 conseguiu entrar na partida.
     */
    public boolean entrarPartida(Cor corJogador2, String nomeJogador2, Image imagemJogador2, String ipServidor, int portaServidor) {
        jogador2 = new JogadorOnline(corJogador2, nomeJogador2, imagemJogador2);
        if (jogador2.conectar(ipServidor, portaServidor)) {
            try {
                DataInputStream input = new DataInputStream(jogador2.getSocket().getInputStream());
                String nomeJogador1 = input.readUTF();
                Cor corJogador1 = Cor.valueOf(input.readUTF());
                Image imagemJogador1 = new Image(input.readUTF());
                jogador1 = new JogadorOnline(corJogador1, nomeJogador1, imagemJogador1);
                Platform.runLater(() -> {
                    partida = new Partida(jogador1, jogador2, null);
                    iniciarPartida(jogador2.getSocket(), true);
                });
                return true;
            } catch (IOException e) {
                System.out.println("Erro ao receber dados do Jogador 1: " + e.getMessage());
            }
        }
        return false;
    }

    /**
     * Inicia a partida após a conexão entre os jogadores, configurando o tabuleiro e exibindo a interface gráfica.
     * 
     * Este método inicializa a partida, criando a visualização do tabuleiro de xadrez e configurando
     * a interface gráfica para a exibição do jogo. A visualização é configurada de acordo com o jogador 2
     * (se for o servidor ou o cliente).
     * 
     * @param socket O socket de comunicação entre os jogadores.
     * @param isJogador2 Indica se o jogador 2 está jogando como cliente.
     */
    public void iniciarPartida(Socket socket, boolean isJogador2) {
        if (partida != null) {
            TabuleiroView tabuleiroView = new TabuleiroView(partida, isJogador2);
            new TabuleiroControle(partida, tabuleiroView, stage, socket);
            Platform.runLater(() -> {
                stage.setTitle("Jogo de Xadrez Online");
                stage.setScene(new Scene(tabuleiroView, 800, 800));
                stage.getScene().getStylesheets().add(getClass().getResource("/style/tabuleiro.css").toExternalForm());
                stage.show();
            });
        }
    }
}