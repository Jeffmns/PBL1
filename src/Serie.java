import java.util.ArrayList;
import java.util.List;

public class Serie {
    private String titulo;
    private String genero;
    private int ano_lancamento;
    private String elenco;
    private String onde_assistir;
    private List<Temporada> temporadas = new ArrayList<>();
    private List<Review> avaliacoes = new ArrayList<>();

    public Serie(String titulo, String genero, int ano_lancamento, String elenco, String onde_assistir) {
        this.titulo = titulo;
        this.genero = genero;
        this.ano_lancamento = ano_lancamento;
        this.elenco = elenco;
        this.onde_assistir = onde_assistir;

    }

    public void adicionarTemporada(Temporada temporada) {
        temporadas.add(temporada);
    }


    public double getMediaAvaliacoes() {
        if (temporadas.isEmpty()) return 0.0;
        double soma = 0;
        int count = 0;
        for (Temporada temporada : temporadas) {
            if (temporada.isAssistido()) {
                soma += temporada.getMediaAvaliacoes();
                count++;
            }
        }
        return count == 0 ? 0.0 : soma / count;
    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Temporada> getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(List<Temporada> temporadas) {
        this.temporadas = temporadas;
    }

    public String getOnde_assistir() {
        return onde_assistir;
    }

    public void setOnde_assistir(String onde_assistir) {
        this.onde_assistir = onde_assistir;
    }


    public String getElenco() {
        return elenco;
    }

    public void setElenco(String elenco) {
        this.elenco = elenco;
    }

    public int getAno_lancamento() {
        return ano_lancamento;
    }

    public void setAno_lancamento(int ano_lancamento) {
        this.ano_lancamento = ano_lancamento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }




}
