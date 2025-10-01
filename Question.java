import java.io.Serializable;
import java.util.List;

/**
 * Representa uma única questão, seguindo o contrato obrigatório do manual.
 * Isso garante a interoperabilidade entre diferentes servidores.
 */
public class Question implements Serializable {
    private static final long serialVersionUID = 1L;

    // Campos obrigatórios conforme o manual
    public final String id;       // gerado de forma determinística
    public final String topic;    // tema da pergunta (ex: "Java")
    public final String text;     // enunciado da pergunta
    public final List<String> options; // alternativas (ordem importa)
    public final int correct;     // índice da alternativa correta (0-based)

    /**
     * Construtor principal que também gera o ID determinístico.
     */
    public Question(String topic, String text, List<String> options, int correct) {
        // Validação básica para evitar erros
        if (topic == null || text == null || options == null) {
            throw new IllegalArgumentException("Argumentos não podem ser nulos.");
        }
        this.topic = topic;
        this.text = text;
        this.options = List.copyOf(options); // Cria uma lista imutável
        this.correct = correct;
        this.id = generateId(topic, text, options, correct);
    }

    /**
     * Gera um ID único e determinístico baseado no conteúdo da questão.
     * Duas questões com o mesmo conteúdo terão o mesmo ID.
     */
    private static String generateId(String topic, String text, List<String> options, int correct) {
        String key = topic + "|" + text + "|" + String.join(";", options) + "|" + correct;
        return Integer.toHexString(key.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return id.equals(question.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}