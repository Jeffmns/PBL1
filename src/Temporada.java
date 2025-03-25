import java.util.Date;

public class Temporada {
    private int ano_lancamento;
    private int qtd_episodios;
    private int avaliacao;
    private Review review;

    public void avaliarTemporada (Date data_review, String review, int avaliacao) {
        Review r1 = new Review(avaliacao, data_review, review);
        this.review = r1;



    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public int getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(int avaliacao) {
        this.avaliacao = avaliacao;
    }

    public int getQtd_episodios() {
        return qtd_episodios;
    }

    public void setQtd_episodios(int qtd_episodios) {
        this.qtd_episodios = qtd_episodios;
    }

    public int getAno_lancamento() {
        return ano_lancamento;
    }

    public void setAno_lancamento(int ano_lancamento) {
        this.ano_lancamento = ano_lancamento;
    }
}

