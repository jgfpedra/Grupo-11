package jogador;

import java.net.Socket;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import partida.Cor;
import partida.Tabuleiro;

@XmlRootElement
public class JogadorOnline extends Jogador{
    private Socket socket;

    public JogadorOnline(){

    }
    public JogadorOnline(Cor cor, String nome, Socket socket){
        super(cor, nome);
        this.socket = socket;
    }
    @Override
    public void escolherMovimento(Tabuleiro tabuleiro) {
    }
    @Override
    public void temPecas() {
    }
    @XmlElement
    public Socket getSocket(){
        return socket;
    }
}
