package partida;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class HistoricoMovimentos {
    public void salvarEstado() {
        try {
            // Cria um contexto JAXB para a classe Tabuleiro
            JAXBContext context = JAXBContext.newInstance(Tabuleiro.class);
    
            // Cria o marshaller (responsável por converter para XML)
            Marshaller marshaller = context.createMarshaller();
    
            // Formata o XML de forma legível
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    
            // Especifica o arquivo onde o XML será salvo
            File arquivo = new File("tabuleiro.xml");
    
            // Converte o objeto tabuleiro para XML e escreve no arquivo
            marshaller.marshal(this.tabuleiro, arquivo);
    
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
