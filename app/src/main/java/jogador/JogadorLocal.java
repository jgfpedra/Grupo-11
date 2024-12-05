package jogador;

import partida.Cor;

/**
 * Representa um jogador humano que participa localmente de uma partida de xadrez.
 * Um jogador local é controlado diretamente pelo usuário, em vez de ser controlado por uma inteligência artificial.
 * 
 * A classe é uma subclasse de {@link Jogador}, utilizando a cor e o nome fornecidos para inicializar o jogador.
 * O jogador local não possui funcionalidades avançadas como a inteligência artificial (IA), sendo responsável por realizar
 * jogadas manuais no tabuleiro durante a partida.
 */
public class JogadorLocal extends Jogador {

    /**
     * Construtor padrão para o JogadorLocal.
     * Inicializa o jogador local sem a definição de cor ou nome.
     */
    public JogadorLocal(){
    }

    /**
     * Construtor para inicializar um jogador local com uma cor e nome específicos.
     *
     * @param cor  A cor do jogador (branco ou preto).
     * @param nome O nome do jogador.
     */
    public JogadorLocal(Cor cor, String nome){
        super(cor, nome, null);
    }
}