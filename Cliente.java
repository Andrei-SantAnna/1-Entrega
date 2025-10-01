import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Uso: java com.seuprojeto.game.Cliente <host_do_servidor> <porta>");
            return;
        }
        String host = args[0];
        int porta = Integer.parseInt(args[1]);

        try (
            Socket socket = new Socket(host, porta);
            PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scannerDoConsole = new Scanner(System.in)
        ) {
            System.out.println("Conectado ao servidor de jogo!");
            System.out.println("Comandos: listar_temas, jogar <tema>, sair");

            while (true) {
                System.out.print("> ");
                String comandoUsuario = scannerDoConsole.nextLine();
                String[] partes = comandoUsuario.split(" ", 2);
                String comando = partes[0].toLowerCase();

                if ("sair".equals(comando)) {
                    saida.println("SAIR");
                    break;
                } else if ("listar_temas".equals(comando)) {
                    saida.println("LISTAR_TEMAS");
                    System.out.println("Temas disponíveis: " + entrada.readLine());
                } else if ("jogar".equals(comando) && partes.length > 1) {
                    saida.println("JOGAR " + partes[1]);
                    iniciarJogo(entrada, saida, scannerDoConsole);
                } else {
                    System.out.println("Comando inválido.");
                }
            }
            System.out.println(entrada.readLine());
        } catch (UnknownHostException e) {
            System.err.println("Host desconhecido: " + host);
        } catch (IOException e) {
            System.err.println("Não foi possível conectar ao servidor: " + e.getMessage());
        }
    }

    private static void iniciarJogo(BufferedReader entrada, PrintWriter saida, Scanner scanner) throws IOException {
        String respostaServidor;
        while ((respostaServidor = entrada.readLine()) != null) {
            if (respostaServidor.startsWith("QUESTAO")) {
                exibirQuestao(respostaServidor);
                
                System.out.print("Sua resposta (letra): ");
                String respostaLetra = scanner.nextLine().trim().toLowerCase();
                
                int indiceResposta = -1;
                if (respostaLetra.length() == 1) {
                    char letra = respostaLetra.charAt(0);
                    if (letra >= 'a' && letra <= 'z') {
                        indiceResposta = letra - 'a';
                    }
                }
                
                saida.println("RESPOSTA " + indiceResposta);

            } else if (respostaServidor.startsWith("CORRETO") || respostaServidor.startsWith("INCORRETO")) {
                System.out.println(">> " + respostaServidor);
            } else if (respostaServidor.startsWith("FIM_DE_JOGO")) {
                System.out.println(">> Fim de Jogo! " + respostaServidor.split(";", 2)[1]);
                break;
            } else if (respostaServidor.startsWith("ERRO")) {
                System.err.println("Erro do servidor: " + respostaServidor.split(";", 2)[1]);
                break;
            }
        }
    }

    private static void exibirQuestao(String dadosQuestao) {
        String[] partes = dadosQuestao.split(";");
        System.out.println("\n---------------------------------");
        System.out.println("Pergunta: " + partes[1]);
        for (int i = 2; i < partes.length; i++) {
            char letraOpcao = (char) ('a' + (i - 2));
            System.out.println(letraOpcao + ") " + partes[i]);
        }
        System.out.println("---------------------------------");
    }
}