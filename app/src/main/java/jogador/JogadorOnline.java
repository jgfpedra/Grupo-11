package jogador;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import partida.Cor;

/**
 * Representa um jogador que participa de uma partida de xadrez online.
 * 
 * A classe {@code JogadorOnline} é responsável por estabelecer e gerenciar a comunicação em rede entre dois jogadores
 * durante uma partida online. Ela permite que o jogador crie um servidor para aguardar a conexão de um adversário, 
 * ou que se conecte a um servidor de outro jogador.
 * 
 * O jogador online pode enviar e receber informações sobre sua identidade (nome, cor e imagem) para garantir que a
 * partida seja configurada corretamente entre os dois jogadores.
 */
public class JogadorOnline extends Jogador {
    private Socket socket;
    private ServerSocket serverSocket;
    
    /**
     * Construtor padrão para o JogadorOnline.
     * Inicializa o jogador online sem definir uma cor, nome ou imagem.
     */
    public JogadorOnline() {
    }

    /**
     * Construtor que inicializa o jogador online com cor, nome e imagem fornecidos.
     * 
     * @param cor A cor do jogador (branco ou preto).
     * @param nome O nome do jogador.
     * @param imagem A imagem do jogador para representar sua identidade visual.
     */
    public JogadorOnline(Cor cor, String nome, Image imagem) {
        super(cor, nome, imagem);
    }
    
    /**
     * Cria um servidor para permitir que outro jogador se conecte a este jogador online.
     * 
     * O método cria um servidor na porta especificada, aguarda a conexão de um jogador adversário e, ao estabelecer 
     * a conexão, retorna o socket de comunicação.
     * 
     * @param porta A porta na qual o servidor ficará aguardando conexões.
     * @return O socket de comunicação do servidor, ou {@code null} em caso de falha.
     */
    public Socket criarServidor(int porta) {
        try {
            System.out.println("Iniciando servidor...");
            serverSocket = new ServerSocket(porta);
            InetAddress localHost = InetAddress.getLocalHost();
            String ipServidor = localHost.getHostAddress();
            System.out.println("Servidor criado. Aguardando conexão...");
            System.out.println("IP do servidor: " + ipServidor + " Porta: " + porta);
            mostrarAlertServidor(ipServidor, porta);
            socket = serverSocket.accept();
            System.out.println("Conexão estabelecida com: " + socket.getInetAddress());
            return socket;
        } catch (IOException e) {
            System.out.println("Erro ao criar servidor: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Conecta este jogador a um servidor remoto, especificado pelo endereço IP e porta.
     * 
     * O método tenta estabelecer uma conexão com o servidor na URL e porta fornecidas.
     * Se a conexão for bem-sucedida, envia os dados do jogador (nome, cor e imagem) para o servidor.
     * 
     * @param enderecoServidor O endereço IP ou nome do servidor ao qual o jogador deseja se conectar.
     * @param porta A porta de comunicação do servidor.
     * @return {@code true} se a conexão foi bem-sucedida, {@code false} caso contrário.
     */
    public boolean conectar(String enderecoServidor, int porta) {
        try {
            socket = new Socket(enderecoServidor, porta);
            System.out.println("Conectado ao servidor!");
            System.out.println("Socket: " + socket);
            enviarDadosParaServidor(socket);
            return true;
        } catch (IOException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Envia os dados do jogador para o servidor após a conexão ser estabelecida.
     * 
     * Os dados enviados incluem o nome do jogador, sua cor (branco ou preto) e a URL da sua imagem de representação.
     * 
     * @param socket O socket utilizado para enviar os dados ao servidor.
     */
    private void enviarDadosParaServidor(Socket socket) {
        try {
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            output.writeUTF(this.getNome());
            output.writeUTF(this.getCor().toString());
            output.writeUTF(this.getImagem().getUrl());
            output.flush();
        } catch (IOException e) {
            System.out.println("Erro ao enviar dados para o servidor: " + e.getMessage());
        }
    }
    
    /**
     * Exibe um alerta informando o IP e a porta do servidor.
     * 
     * Este alerta é exibido para o jogador que criou o servidor, mostrando a ele as informações necessárias
     * para que um adversário possa se conectar ao servidor.
     * 
     * @param ip O endereço IP do servidor.
     * @param porta A porta do servidor.
     */
    private void mostrarAlertServidor(String ip, int porta) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Informação do Servidor");
        alert.setHeaderText("Servidor Criado");
        alert.setContentText("IP do Servidor: " + ip + "\nPorta: " + porta);
        alert.showAndWait();
    }

    /**
     * Retorna o socket de comunicação do jogador online.
     * 
     * @return O socket de comunicação.
     */
    public Socket getSocket() {
        return socket;
    }
}