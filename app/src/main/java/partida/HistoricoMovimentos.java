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

    private Stack<Movimento> movimentos;

    public HistoricoMovimentos() {
        this.movimentos = new Stack<>();
    }

    public void adicionarMovimento(Movimento movimento) {
        if (!movimentos.contains(movimento)) {
            movimentos.push(movimento);
            salvarEstado();
        } else {
            System.out.println("Movimento duplicado não adicionado.");
        }
    }

    public void removerUltimoMovimento() {
        if (!movimentos.isEmpty()) {
            movimentos.pop();
            salvarEstado();
        }
    }

    public Movimento obterUltimoMovimento() {
        return movimentos.isEmpty() ? null : movimentos.peek();
    }

    public boolean temMovimentos() {
        return !movimentos.isEmpty();
    }

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