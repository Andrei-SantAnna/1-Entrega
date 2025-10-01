import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

/**
 * Lida com a conexão de um outro servidor (peer/parceiro)
 * que quer obter a base de perguntas.
 */
public class ConexaoServidor implements Runnable {
    private final Socket socketServidorParceiro;
    private final BancoPerguntas bancoPerguntas;

    public ConexaoServidor(Socket socket, BancoPerguntas banco) {
        this.socketServidorParceiro = socket;
        this.bancoPerguntas = banco;
    }

    @Override
    public void run() {
        try (
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socketServidorParceiro.getInputStream()))
        ) {
            String comando = entrada.readLine();
            
            if ("GET_QUESTIONS".equalsIgnoreCase(comando)) {
                ObjectOutputStream saidaDeObjeto = new ObjectOutputStream(socketServidorParceiro.getOutputStream());
                List<Questao> todasAsQuestoes = bancoPerguntas.obterTodasAsQuestoes();
                saidaDeObjeto.writeObject(todasAsQuestoes);
                saidaDeObjeto.flush();
            }
        } catch (IOException e) {
            // Silencioso
        } finally {
            try {
                socketServidorParceiro.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}