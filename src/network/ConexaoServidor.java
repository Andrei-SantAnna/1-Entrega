package network;
import schema.Question;
import util.BancoPerguntas;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
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
            ObjectOutputStream saida = new ObjectOutputStream(socketServidorParceiro.getOutputStream());
            ObjectInputStream entrada = new ObjectInputStream(socketServidorParceiro.getInputStream())
        ) {
            // 1. Envia banner para o peer
            saida.writeObject("BANNER:MeuServidor");
            saida.flush();

            // 2. Aguarda comando do peer
            Object recebido = entrada.readObject();
            if (recebido instanceof String comando && "EXPORT".equalsIgnoreCase(comando)) {
                List<Question> todasAsQuestoes = new ArrayList<>(bancoPerguntas.obterTodasAsQuestoes());

                // 3. Envia lista de questões
                saida.writeObject(todasAsQuestoes);
                saida.flush();

                System.out.printf("[PEER] Enviadas %d questões para %s%n",
                        todasAsQuestoes.size(),
                        socketServidorParceiro.getRemoteSocketAddress());
            } else {
                System.out.printf("[PEER] Comando inesperado de %s: %s%n",
                        socketServidorParceiro.getRemoteSocketAddress(),
                        recebido);
            }
        } catch (Exception e) {
            System.err.println("[ERRO] Falha na conexão com peer: " + e.getMessage());
        } finally {
            try {
                socketServidorParceiro.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
