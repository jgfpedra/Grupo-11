package partida;

import java.io.File;
import java.util.Stack;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class HistoricoMovimentos {

    private Stack<Movimento> movimentos;  // Pilha para armazenar os movimentos realizados
    private Tabuleiro tabuleiro;

    public HistoricoMovimentos(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
        this.movimentos = new Stack<>();
    }

    // Adiciona um movimento ao histórico
    public void adicionarMovimento(Movimento movimento) {
        movimentos.push(movimento);
        salvarEstado();  // Salva o estado após adicionar um movimento
    }

    // Remove o último movimento do histórico
    public void removerUltimoMovimento() {
        if (!movimentos.isEmpty()) {
            movimentos.pop();
            salvarEstado();  // Salva o estado após remover um movimento
        }
    }

    // Retorna o último movimento sem removê-lo
    public Movimento obterUltimoMovimento() {
        return movimentos.isEmpty() ? null : movimentos.peek();
    }

    // Verifica se há movimentos no histórico
    public boolean temMovimentos() {
        return !movimentos.isEmpty();
    }

    // Salva o estado do histórico de movimentos em um arquivo XML
    private void salvarEstado() {
        try {
            // Cria um contexto JAXB para a classe HistoricoMovimentos
            JAXBContext context = JAXBContext.newInstance(HistoricoMovimentos.class);

            // Cria o marshaller (responsável por converter para XML)
            Marshaller marshaller = context.createMarshaller();

            // Formata o XML de forma legível
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Especifica o arquivo onde o XML será salvo
            File arquivo = new File("historico_movimentos.xml");

            // Converte o objeto historico para XML e escreve no arquivo
            marshaller.marshal(this, arquivo);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    // Carrega o estado do histórico de movimentos a partir de um arquivo XML
    public void carregarEstado() {
        try {
            // Cria um contexto JAXB para a classe HistoricoMovimentos
            JAXBContext context = JAXBContext.newInstance(HistoricoMovimentos.class);

            // Cria o unmarshaller (responsável por converter de XML para objeto)
            Unmarshaller unmarshaller = context.createUnmarshaller();

            // Especifica o arquivo de onde o XML será lido
            File arquivo = new File("historico_movimentos.xml");

            // Lê o arquivo e converte de XML para o objeto HistoricoMovimentos
            HistoricoMovimentos historicoCarregado = (HistoricoMovimentos) unmarshaller.unmarshal(arquivo);
            this.movimentos = historicoCarregado.movimentos;
            this.tabuleiro = historicoCarregado.tabuleiro;

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}