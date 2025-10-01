import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.StringJoiner;

public class ConexaoCliente implements Runnable {
    private final Socket socketCliente;
    private final BancoPerguntas bancoPerguntas;

    public ConexaoCliente(Socket socket, BancoPerguntas banco) {
        this.socketCliente = socket;
        this.bancoPerguntas = banco;
    }
    
    // O método run() permanece o mesmo

    @Override
    public void run() {
        try (
            PrintWriter saida = new PrintWriter(socketCliente.getOutputStream(), true);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()))
        ) {
            String linhaRecebida;
            while ((linhaRecebida = entrada.readLine()) != null) {
                String[] partes = linhaRecebida.split(" ", 2);
                String comando = partes[0].toUpperCase();

                switch (comando) {
                    case "LISTAR_TEMAS":
                        processarListarTemas(saida);
                        break;
                    case "JOGAR":
                        if (partes.length > 1) {
                            processarJogo(saida, entrada, partes[1]);
                        } else {
                            saida.println("ERRO;Comando JOGAR requer um tema.");
                        }
                        break;
                    case "SAIR":
                        saida.println("ADEUS");
                        return;
                    default:
                        saida.println("ERRO;Comando desconhecido.");
                }
            }
        } catch (IOException e) {
            // Silencioso
        } finally {
            try {
                socketCliente.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processarListarTemas(PrintWriter saida) {
        String temas = String.join(",", bancoPerguntas.obterTemas());
        saida.println(temas);
    }

    private void processarJogo(PrintWriter saida, BufferedReader entrada, String tema) throws IOException {
        List<Question> questoes = bancoPerguntas.obterQuestoesPorTema(tema);
        if (questoes.isEmpty()) {
            saida.println("ERRO;Tema nao encontrado ou sem questoes.");
            return;
        }

        int pontuacao = 0;
        for (Question q : questoes) {
            StringJoiner sj = new StringJoiner(";");
            sj.add("QUESTAO");
            sj.add(q.text); // ATUALIZAÇÃO: Usa o campo 'text'
            q.options.forEach(sj::add); // ATUALIZAÇÃO: Usa o campo 'options'
            saida.println(sj.toString());

            String linhaResposta = entrada.readLine();
            if (linhaResposta == null) return;

            String[] partesResposta = linhaResposta.split(" ");
            
            if (partesResposta.length < 2 || !partesResposta[0].equalsIgnoreCase("RESPOSTA")) {
                saida.println("INCORRETO (comando inválido)");
                continue; 
            }

            try {
                int opcaoSelecionada = Integer.parseInt(partesResposta[1]);

                System.out.println("[DEBUG] Checando Resposta: Recebido=" + opcaoSelecionada + ", Correto=" + q.correct);

                if (opcaoSelecionada == q.correct) { // ATUALIZAÇÃO: Usa o campo 'correct'
                    pontuacao++;
                    saida.println("CORRETO");
                } else {
                    saida.println("INCORRETO");
                }
            } catch (NumberFormatException e) {
                saida.println("INCORRETO (resposta não numérica)");
            }
        }

        saida.println("FIM_DE_JOGO;Voce acertou " + pontuacao + " de " + questoes.size() + " perguntas.");
    }
}