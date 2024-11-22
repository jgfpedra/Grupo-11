package jogador;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import partida.Cor;
import partida.Tabuleiro;

@XmlRootElement
public class JogadorIA extends Jogador{
    private int nivelDificuldade;
    public JogadorIA(){
    }
    public JogadorIA(Cor cor, String nome, int nivelDificuldade){
        super(cor, nome);
        this.nivelDificuldade = nivelDificuldade;
    }

    @Override
    public void escolherMovimento(Tabuleiro tabuleiro) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'escolherMovimento'");
    }

    @Override
    public void temPecas() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'temPecas'");
    }
    
    @XmlElement
    public int getNivelDificuldade(){
        return nivelDificuldade;
    }
}
