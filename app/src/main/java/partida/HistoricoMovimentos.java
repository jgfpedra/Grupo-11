package partida;

import java.io.File;
import java.util.Stack;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import jogador.Jogador;

@XmlRootElement  // Indica que esta classe será a raiz no arquivo XML
public class HistoricoMovimentos {

    private Stack<Movimento> movimentos;  // Stack to store the movements
    private Tabuleiro tabuleiro;
    private Partida partida;
    private Jogador jogador1;
    private Jogador jogador2;

    // Static file path for the XML file
    private static final String ARQUIVO_XML = "app/data/tabuleiro.xml";

    public HistoricoMovimentos(){

    }
    public HistoricoMovimentos(Tabuleiro tabuleiro, Partida partida, Jogador jogador1, Jogador jogador2) {
        this.tabuleiro = tabuleiro;
        this.partida = partida;
        this.jogador1 = jogador1;
        this.jogador2 = jogador2;
        this.movimentos = new Stack<>();
    }

    // Add a movement to the history
    public void adicionarMovimento(Movimento movimento) {
        movimentos.push(movimento);
        salvarEstado();  // Save state after adding a move
    }

    // Remove the last movement from history
    public void removerUltimoMovimento() {
        if (!movimentos.isEmpty()) {
            movimentos.pop();
            salvarEstado();  // Save state after removing a move
        }
    }

    // Get the last movement without removing it
    public Movimento obterUltimoMovimento() {
        return movimentos.isEmpty() ? null : movimentos.peek();
    }

    // Check if there are any movements in history
    public boolean temMovimentos() {
        return !movimentos.isEmpty();
    }

    private void salvarEstado() {
        try {
            // Obtém o diretório de trabalho atual
            String caminhoProjeto = System.getProperty("user.dir");

            // Define o caminho completo para o arquivo XML dentro de 'app/data'
            File diretorio = new File(caminhoProjeto + "/data");

            // Verifica se o diretório existe, caso contrário, cria o diretório
            if (!diretorio.exists()) {
                boolean sucesso = diretorio.mkdirs();  // Cria o diretório, incluindo subdiretórios
                if (!sucesso) {
                    System.out.println("Falha ao criar o diretório!");
                    return;
                }
            }

            // Cria o arquivo XML
            File arquivo = new File(diretorio, "tabuleiro.xml");

            // Cria JAXBContext para a classe HistoricoMovimentos
            JAXBContext context = JAXBContext.newInstance(HistoricoMovimentos.class);

            // Cria o marshaller
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // Para formatação legível

            // Salva o objeto em um arquivo XML
            marshaller.marshal(this, arquivo);
            
            System.out.println("Estado salvo com sucesso no arquivo: " + arquivo.getAbsolutePath());

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }


    // Load the state of the history from an XML file
    public void carregarEstado() {
        try {
            // Create JAXBContext for the class HistoricoMovimentos
            JAXBContext context = JAXBContext.newInstance(HistoricoMovimentos.class);

            // Create the unmarshaller (responsible for converting from XML to object)
            Unmarshaller unmarshaller = context.createUnmarshaller();

            // Specify the file to read from
            File arquivo = new File(ARQUIVO_XML);

            // Unmarshal the file into a HistoricoMovimentos object
            HistoricoMovimentos historicoCarregado = (HistoricoMovimentos) unmarshaller.unmarshal(arquivo);
            this.movimentos = historicoCarregado.movimentos;
            this.tabuleiro = historicoCarregado.tabuleiro;
            this.partida = historicoCarregado.partida;
            this.jogador1 = historicoCarregado.jogador1;
            this.jogador2 = historicoCarregado.jogador2;

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    
    @XmlElement(name = "movimentos")
    public Stack<Movimento> getMovimentos() {
        return movimentos;
    }

    @XmlElement(name = "partida")
    public Partida getPartida() {
        return partida;
    }

    @XmlElement(name = "jogador1")
    public Jogador getJogador1() {
        return jogador1;
    }

    @XmlElement(name = "jogador2")
    public Jogador getJogador2() {
        return jogador2;
    }
}
