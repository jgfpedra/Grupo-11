package controle;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jogador.JogadorOnline;
import partida.Cor;
import partida.Partida;
import view.TabuleiroView;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class PartidaOnlineControle {

    private Stage primaryStage;
    private ServerSocket serverSocket;
    private JogadorOnline jogador1;  // Jogador 1 (host)
    private Partida partida;         // A partida vai ser criada após o segundo jogador entrar

    public PartidaOnlineControle(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Criar partida e gerenciar o servidor
    public String criarPartida(String nomeJogador1, Image imagemJogador1) {
        try {
            // Gerar código único para a sala (5 caracteres alfanuméricos)
            String codigoSala = gerarCodigoSala();  // Geração do código da sala
            System.out.println("Código da sala: " + codigoSala);
        
            // Criar o servidor para escutar as conexões
            serverSocket = new ServerSocket(6969);
            System.out.println("Servidor esperando jogadores. Código da sala: " + codigoSala);
        
            // Criar o primeiro jogador (host)
            jogador1 = new JogadorOnline(Cor.PRETO, nomeJogador1, imagemJogador1, null);  // Jogador 1 ainda sem socket
            System.out.println("Jogador 1 (host) criado: " + nomeJogador1);
        
            // Aguardar a conexão do segundo jogador
            Socket socketJogador2 = serverSocket.accept();  // Espera pelo jogador 2
            System.out.println("Jogador 2 conectado.");
        
            // Criar o segundo jogador com o socket do jogador 2
            JogadorOnline jogador2 = new JogadorOnline(Cor.BRANCO, "", null, socketJogador2);  // Passe o nome e imagem do jogador 2 depois
        
            // Criar a partida apenas após o Jogador 2 se conectar
            partida = new Partida(jogador1, jogador2, null);  // Passa ambos os jogadores para a partida
            TabuleiroView tabuleiroView = new TabuleiroView(partida);
            new TabuleiroControle(partida, tabuleiroView, primaryStage);
        
            primaryStage.setTitle("Jogo de Xadrez");
            primaryStage.setScene(new Scene(tabuleiroView, 800, 800));
            primaryStage.show();
        
            // Agora que o servidor está ativo, exibe o IP e o código da sala para o jogador 1
            String ipServidor = obterIpServidor();
            System.out.println("IP Servidor: " + ipServidor);
        
            return codigoSala;  // Retorna o código da sala para que possa ser exibido para o jogador 1
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;  // Caso haja algum erro, retorna null
    }    

    private String obterIpServidor() {
        try {
            // Obtém o endereço IP da máquina local
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();  // Retorna o endereço IP
        } catch (Exception e) {
            e.printStackTrace();
            return "Desconhecido";  // Se não conseguir obter o IP, retorna um valor padrão
        }
    }

    private String gerarCodigoSala() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder codigo = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            codigo.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return codigo.toString();
    }
    
    public void entrarPartida(String nomeJogador2, Image imagemJogador2, String ipServidor, String codigoSala) {
        try {
            System.out.println("Tentando conectar à sala: " + codigoSala);
    
            // Criar o socket para conectar ao servidor no IP do host e na porta 6969
            Socket socketJogador2 = new Socket(ipServidor, 6969);  // Conectar no servidor usando o IP do host
            System.out.println("Jogador 2 conectado ao servidor.");
    
            // Criar o segundo jogador
            JogadorOnline jogador2 = new JogadorOnline(Cor.BRANCO, nomeJogador2, imagemJogador2, socketJogador2);
            System.out.println("Jogador 2 criado: " + nomeJogador2);
    
            // Aguarda o Jogador 1 configurar a partida antes de continuar
            while (partida == null) {
                Thread.sleep(100);  // Aguardar até que a partida seja inicializada no lado do Jogador 1
            }
    
            // Atribuir o Jogador 2 à partida (Jogador 1 deve já ter chamado setJogador2)
            partida.setJogador2(jogador2);  // Completa a partida com o jogador 2
            TabuleiroView tabuleiroView = new TabuleiroView(partida);
            new TabuleiroControle(partida, tabuleiroView, primaryStage);
    
            primaryStage.setTitle("Jogo de Xadrez");
            primaryStage.setScene(new Scene(tabuleiroView, 800, 800));
            primaryStage.show();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }      

    public void fecharServidor() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Servidor fechado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}