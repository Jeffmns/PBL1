import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Temporada {
    private int numero; // Número da temporada
    private int ano_lancamento;
    private int qtd_episodios;
    private boolean assistido;
    private List<Review> avaliacoes = new ArrayList<>();

    public Temporada(int numero, int ano_lancamento, int qtd_episodios) {
        this.numero = numero;
        this.ano_lancamento = ano_lancamento;
        this.qtd_episodios = qtd_episodios;
        this.assistido = false;
    }

    public void  avaliarTemporada(int avaliacao, Date data_review, String comentario) {
        Review novaReview = new Review(avaliacao, data_review, comentario);
        avaliacoes.add(novaReview);
        this.assistido = true;
    }

    public double getMediaAvaliacoes() {
        if (avaliacoes.isEmpty()) return 0.0;

        int soma = 0;
        for (Review r : avaliacoes) {
            soma += r.getAvaliacao();
        }
        return (double) soma / avaliacoes.size();
    }

    public List<Review> getAvaliacao() {
        return avaliacoes;
    }

    public void setAvaliacao(int avaliacao) {
        this.avaliacoes = avaliacoes;
    }

    public boolean isAssistido() {
        return assistido;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(){
        this.numero = numero;
    }

    public int getAno_lancamento() {
        return ano_lancamento;
    }

    public void setAno_lancamento(int ano_lancamento) {
        this.ano_lancamento = ano_lancamento;
    }

    public int getQtd_episodios() {
        return qtd_episodios;
    }

    public void setQtd_episodios(int qtd_episodios) {
        this.qtd_episodios = qtd_episodios;
    }

    public boolean getAssistido() {
        return assistido;
    }

    public void setAssistido(boolean assistido) {
        this.assistido = assistido;
    }

}

