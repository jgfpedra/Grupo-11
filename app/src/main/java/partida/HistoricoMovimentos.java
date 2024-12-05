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

/**
 * Representa o histórico de movimentos de uma partida de xadrez.
 * 
 * A classe {@code HistoricoMovimentos} armazena uma pilha de movimentos realizados durante a partida. 
 * Ela permite adicionar e remover movimentos do histórico, além de persistir o estado do histórico 
 * em um arquivo XML e carregar o histórico a partir de um arquivo XML.
 * 
 * A classe também lida com a inicialização de imagens das peças associadas aos movimentos.
 */
@XmlRootElement(name = "historicoMovimentos")
public class HistoricoMovimentos {

    private Stack<Movimento> movimentos;

    /**
     * Construtor padrão que inicializa o histórico de movimentos.
     * O histórico é representado por uma pilha (stack), onde os movimentos são armazenados 
     * na ordem em que ocorreram na partida.
     */
    public HistoricoMovimentos() {
        this.movimentos = new Stack<>();
    }

    /**
     * Adiciona um movimento ao histórico.
     * 
     * Se o movimento não for duplicado, ele será adicionado ao histórico e o estado será salvo em um arquivo XML.
     * 
     * @param movimento O movimento a ser adicionado ao histórico.
     */
    public void adicionarMovimento(Movimento movimento) {
        if (!movimentos.contains(movimento)) {
            movimentos.push(movimento);
            salvarEstado();
        } else {
            System.out.println("Movimento duplicado não adicionado.");
        }
    }

    /**
     * Remove o último movimento do histórico.
     * 
     * O último movimento da pilha é removido e o estado do histórico é salvo em um arquivo XML.
     */
    public void removerUltimoMovimento() {
        if (!movimentos.isEmpty()) {
            movimentos.pop();
            salvarEstado();
        }
    }

    /**
     * Obtém o último movimento do histórico.
     * 
     * @return O último movimento, ou {@code null} se o histórico estiver vazio.
     */
    public Movimento obterUltimoMovimento() {
        return movimentos.isEmpty() ? null : movimentos.peek();
    }

    /**
     * Verifica se existem movimentos no histórico.
     * 
     * @return {@code true} se houver movimentos no histórico, {@code false} caso contrário.
     */
    public boolean temMovimentos() {
        return !movimentos.isEmpty();
    }

    /**
     * Salva o estado atual do histórico de movimentos em um arquivo XML.
     * 
     * O estado do histórico é persistido em um arquivo XML no diretório "data" do projeto, 
     * com o nome "tabuleiro.xml".
     */
    private void salvarEstado() {
        try {
            String caminhoProjeto = System.getProperty("user.dir");
            File diretorio = new File(caminhoProjeto + "/data");
            if (!diretorio.exists()) {
                boolean sucesso = diretorio.mkdirs();
                if (!sucesso) {
                    return;
                }
            }
            File arquivo = new File(diretorio, "tabuleiro.xml");
            JAXBContext context = JAXBContext.newInstance(HistoricoMovimentos.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(this, arquivo);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carrega o estado do histórico de movimentos a partir de um arquivo XML.
     * 
     * O arquivo XML deve estar no formato correto, contendo informações sobre os movimentos realizados.
     * Se o arquivo for válido, o histórico será carregado e as imagens das peças associadas aos movimentos serão inicializadas.
     * 
     * @param arquivo O arquivo XML contendo o histórico de movimentos.
     */
    public void carregarEstadoDeArquivo(File arquivo) {
        try {
            if (arquivo.exists() && arquivo.isFile()) {
                JAXBContext context = JAXBContext.newInstance(HistoricoMovimentos.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                HistoricoMovimentos historicoCarregado = (HistoricoMovimentos) unmarshaller.unmarshal(arquivo);
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
    
    /**
     * Inicializa as imagens das peças associadas aos movimentos no histórico.
     * Para cada movimento no histórico, a imagem da peça movida é inicializada.
     */
    public void inicializarImagensMovimentos(){
        List<Movimento> movimentosPecas = movimentos;
        for(Movimento movimento : movimentosPecas){
            movimento.getPecaMovida().inicializarImagem();
        }
    }

    /**
     * Obtém a lista de movimentos do histórico.
     * 
     * @return A pilha de movimentos registrados no histórico.
     */
    @XmlElement(name = "movimento")
    public Stack<Movimento> getMovimentos() {
        return movimentos;
    }
}