package util;
import schema.Question;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Gerencia o banco de dados de questões em memória.
 */
public class BancoPerguntas {
    // Agora armazena objetos do tipo 'Question'
    private final Set<Question> questoes = new HashSet<>();

    /**
     * Carrega as questões de um arquivo de texto.
     */
    public synchronized void carregarDoArquivo(String caminhoArquivo) {
        try (BufferedReader leitor = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            int contador = 0;
            while ((linha = leitor.readLine()) != null) {
                if (linha.trim().isEmpty() || linha.startsWith("#")) {
                    continue;
                }
                String[] partes = linha.split(";", 4);
                if (partes.length < 4) continue;

                String tema = partes[0].trim();
                String textoPergunta = partes[1].trim();
                List<String> opcoes = Arrays.asList(partes[2].split(","));
                int indiceCorreto = Integer.parseInt(partes[3].trim());

               
                Question questao = new Question(tema, textoPergunta, opcoes, indiceCorreto);
                this.questoes.add(questao);
                contador++;
            }
            System.out.println(contador + " questões carregadas de " + caminhoArquivo);
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao carregar questões do arquivo: " + e.getMessage());
        }
    }

    /** Adiciona uma lista de novas questões (vindas de outro servidor). */
    public synchronized int adicionarQuestoes(List<Question> novasQuestoes) {
        int tamanhoInicial = this.questoes.size();
        this.questoes.addAll(novasQuestoes);
        return this.questoes.size() - tamanhoInicial;
    }

    /** Retorna um conjunto com todos os temas disponíveis. */
    public synchronized Set<String> obterTemas() {
        return questoes.stream()
                .map(q -> q.topic) // ATUALIZAÇÃO: Usa o campo 'topic'
                .collect(Collectors.toSet());
    }

    /** Retorna uma lista embaralhada de questões para um tema específico. */
    public synchronized List<Question> obterQuestoesPorTema(String tema) {
        List<Question> questoesFiltradas = questoes.stream()
                .filter(q -> q.topic.equalsIgnoreCase(tema)) // ATUALIZAÇÃO: Usa o campo 'topic'
                .collect(Collectors.toList());
        Collections.shuffle(questoesFiltradas);
        return questoesFiltradas;
    }

    /** Retorna todas as questões do banco. */
    public synchronized List<Question> obterTodasAsQuestoes() {
        return new ArrayList<>(this.questoes);
    }
}
