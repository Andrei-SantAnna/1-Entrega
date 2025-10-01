package jogo;

import network.ConexaoCliente;
import network.ConexaoServidor;
import schema.Question;
import util.BancoPerguntas;


import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.List;

public class Servidor {
    private final int portaCliente;
    private final int portaServidorParceiro;
    private final BancoPerguntas bancoPerguntas;

    public Servidor(int portaCliente, int portaServidor, String arquivoInicial) {
        this.portaCliente = portaCliente;
        this.portaServidorParceiro = portaServidor;
        this.bancoPerguntas = new BancoPerguntas();
        this.bancoPerguntas.carregarDoArquivo(arquivoInicial);
    }
    
    
    public void iniciar() {// Inicia as threads para escutar clientes e servidores parceiros
        new Thread(this::escutarClientes).start();
        new Thread(this::escutarServidoresParceiros).start();

        System.out.println("Servidor iniciado. Portas: Clientes=" + portaCliente + ", Parceiros=" + portaServidorParceiro);
        escutarComandosAdmin();
    }

    private void escutarClientes() {// Escuta conexões de clientes
        try (ServerSocket serverSocket = new ServerSocket(portaCliente)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ConexaoCliente(clientSocket, bancoPerguntas)).start();
            }
        } catch (IOException e) {
            System.err.println("Erro Crítico no Servidor de Clientes: " + e.getMessage());
        }
    }

    private void escutarServidoresParceiros() {// Escuta conexões de servidores parceiros
        try (ServerSocket serverSocket = new ServerSocket(portaServidorParceiro)) {
            while (true) {
                Socket peerSocket = serverSocket.accept();
                new Thread(new ConexaoServidor(peerSocket, bancoPerguntas)).start();
            }
        } catch (IOException e) {
            System.err.println("Erro Crítico no Servidor de Parceiros: " + e.getMessage());
        }
    }

    private void escutarComandosAdmin() {// Escuta comandos do administrador via console
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite 'PAREAR <host> <porta>' para sincronizar ou 'SAIR' para fechar.");
        while (true) {
            System.out.print("Comando do Servidor > ");
            String linha = scanner.nextLine();
            String[] partes = linha.split(" ");
            String comando = partes[0].toUpperCase();

            if ("PAREAR".equals(comando) && partes.length == 3) {
                String host = partes[1];
                try {
                    int porta = Integer.parseInt(partes[2]);
                    sincronizarComServidorParceiro(host, porta);
                } catch (NumberFormatException e) {
                    System.out.println("Erro: A porta deve ser um número.");
                }
            } else if ("SAIR".equals(comando)) {
                System.out.println("Desligando o servidor...");
                System.exit(0);
            } else {
                System.out.println("Comando inválido. Use: PAREAR <host> <porta> ou SAIR");
            }
        }
    }

    private void sincronizarComServidorParceiro(String host, int porta) {
    System.out.println("Tentando parear com " + host + ":" + porta);
    try (
        Socket socket = new Socket(host, porta);
        ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())
    ) {
        // 1. Recebe banner do peer
        Object banner = entrada.readObject();
        System.out.println("Recebido banner: " + banner);

        // 2. Solicita exportação
        saida.writeObject("EXPORT");
        saida.flush();

        // 3. Recebe lista de questões
        Object resp = entrada.readObject();
        if (resp instanceof List<?> lista) {
            int novas = 0;
            for (Object o : lista) {
                if (o instanceof Question q) {
                    novas += bancoPerguntas.adicionarQuestoes(List.of(q));
                }
            }
            System.out.println("Pareamento completo! " + novas + " novas questões adicionadas.");
            System.out.println("Temas atuais: " + bancoPerguntas.obterTemas());
        } else {
            System.err.println("Resposta inesperada do peer: " + resp);
        }
    } catch (Exception e) {
        System.err.println("Não foi possível conectar ao parceiro " + host + ":" + porta + " - " + e.getMessage());
    }
}


    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Uso: java jogo.Servidor <porta_cliente> <porta_parceiro> <arquivo_questoes.txt>");
            return;
        }
        int portaCliente = Integer.parseInt(args[0]);
        int portaParceiro = Integer.parseInt(args[1]);
        String arquivoQuestoes = args[2];

        Servidor servidor = new Servidor(portaCliente, portaParceiro, arquivoQuestoes);
        servidor.iniciar();
    }
}
