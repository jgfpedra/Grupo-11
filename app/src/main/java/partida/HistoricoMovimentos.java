package partida;

import java.io.File;
import java.util.List;
import java.util.Stack;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "historicoMovimentos")
public class HistoricoMovimentos {

    private Stack<Movimento> movimentos;  // Stack to store the movements

    public HistoricoMovimentos() {
        this.movimentos = new Stack<>();
    }

    // Add a movement to the history
    public void adicionarMovimento(Movimento movimento) {
        // Verifica se o movimento já existe no histórico
        if (!movimentos.contains(movimento)) {
            movimentos.push(movimento);
            salvarEstado();  // Save state after adding a move
        } else {
            System.out.println("Movimento duplicado não adicionado.");
        }
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
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void carregarEstadoDeArquivo(File arquivo) {
        try {
            if (arquivo.exists() && arquivo.isFile()) {
                JAXBContext context = JAXBContext.newInstance(HistoricoMovimentos.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
    
                // Deserializa o arquivo XML no objeto HistoricoMovimentos
                HistoricoMovimentos historicoCarregado = (HistoricoMovimentos) unmarshaller.unmarshal(arquivo);
                
                // Atualiza o campo 'movimentos' com os dados carregados
                this.movimentos = historicoCarregado.getMovimentos();

                inicializarImagensMovimentos();

            } else {
                System.out.println("Arquivo inválido ou não encontrado.");
            }
        } catch (JAXBException e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar o arquivo XML.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro desconhecido ao carregar o arquivo.");
        }
    }
    
    public void inicializarImagensMovimentos(){
        List<Movimento> movimentosPecas = movimentos;
        for(Movimento movimento : movimentosPecas){
            movimento.getPecaMovida().inicializarImagem();
        }
    }

    @XmlElement(name = "movimento")
    public Stack<Movimento> getMovimentos() {
        return movimentos;
    }
}