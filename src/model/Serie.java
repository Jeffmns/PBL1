package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        return count == 0 ? 0.0 : Math.round((soma / count) * 100.0) / 100.0;
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

    public int getAnoLancamento() {
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Serie serie = (Serie) obj;

        return ano_lancamento == serie.ano_lancamento
                && titulo.equalsIgnoreCase(serie.titulo)
                && genero.equalsIgnoreCase(serie.genero)
                && elenco.equalsIgnoreCase(serie.elenco)
                && onde_assistir.equalsIgnoreCase(serie.onde_assistir);
    }
    @Override
    public int hashCode() {
        return Objects.hash(titulo.toLowerCase(), genero.toLowerCase(), ano_lancamento,elenco.toLowerCase(), onde_assistir.toLowerCase());
    }


}
