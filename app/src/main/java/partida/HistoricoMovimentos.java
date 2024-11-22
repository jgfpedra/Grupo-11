package partida;

import java.io.File;
import java.util.Stack;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import jogador.Jogador;

public class HistoricoMovimentos {

    private Stack<Movimento> movimentos;  // Stack to store the movements
    private Tabuleiro tabuleiro;
    private Partida partida;
    private Jogador jogador1;
    private Jogador jogador2;

    // Static file path for the XML file
    private static final String ARQUIVO_XML = "app/data/tabuleiro.xml";

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

    // Save the state of the history to an XML file
    private void salvarEstado() {
        try {
            // Create JAXBContext for the class HistoricoMovimentos
            JAXBContext context = JAXBContext.newInstance(HistoricoMovimentos.class);

            // Create the marshaller (responsible for converting to XML)
            Marshaller marshaller = context.createMarshaller();

            // Format the XML to be readable
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Specify the file to save the XML
            File arquivo = new File(ARQUIVO_XML);

            // Marshal the object to XML and write to the file
            marshaller.marshal(this, arquivo);

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
}