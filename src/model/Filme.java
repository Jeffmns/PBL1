package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Filme {
    private String titulo;
    private String genero;
    private int ano_lancamento;
    private int duracao;
    private String direcao;
    private String elenco;
    private String onde_assistir;
    private boolean assistido;
    private List<Review> avaliacoes = new ArrayList<>();


    public Filme(String titulo, String genero, int ano_lancamento, int duracao, String direcao, String elenco, String onde_assistir) {
        this.titulo = titulo;
        this.genero = genero;
        this.ano_lancamento = ano_lancamento;
        this.duracao = duracao;
        this.direcao = direcao;
        this.elenco = elenco;
        this.onde_assistir = onde_assistir;
        this.assistido = false;
    }

    public void adicionarAvaliacao(Review review) {
        avaliacoes.add(review);
        assistido = true;
    }

    public double getMediaAvaliacoes() {
        if (avaliacoes.isEmpty()) return 0;
        int soma = 0;
        for (Review review : avaliacoes) {
            soma += review.getAvaliacao();
        }

        return (double) Math.round((soma / avaliacoes.size()) * 100.0) / 100.0;
    }

    public List<Review> getAvaliacoes() {
        return avaliacoes;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean isAssistido() {
        return assistido;
    }

    public void setAssistido(boolean assistido) {
        this.assistido = assistido;
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


    public String getDirecao() {
        return direcao;
    }

    public void setDirecao(String direcao) {
        this.direcao = direcao;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getAno_lancamento() {
        return ano_lancamento;
    }

    public void setAno_lancamento(int ano_lancamento) {
        this.ano_lancamento = ano_lancamento;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Filme filme = (Filme) o;

        // Compara os campos primitivos (int) diretamente
        if (getAno_lancamento() != filme.getAno_lancamento()) return false;
        if (getDuracao() != filme.getDuracao()) return false;

        // Compara os campos String de forma segura (case-insensitive e à prova de nulos)
        // Objects.equals() trata os nulos corretamente, e comparamos as versões em minúsculas.
        return Objects.equals(getTitulo() != null ? getTitulo().toLowerCase() : null, filme.getTitulo() != null ? filme.getTitulo().toLowerCase() : null) &&
                Objects.equals(getGenero() != null ? getGenero().toLowerCase() : null, filme.getGenero() != null ? filme.getGenero().toLowerCase() : null) &&
                Objects.equals(getDirecao() != null ? getDirecao().toLowerCase() : null, filme.getDirecao() != null ? filme.getDirecao().toLowerCase() : null) &&
                Objects.equals(getElenco() != null ? getElenco().toLowerCase() : null, filme.getElenco() != null ? filme.getElenco().toLowerCase() : null) &&
                Objects.equals(getOnde_assistir() != null ? getOnde_assistir().toLowerCase() : null, filme.getOnde_assistir() != null ? filme.getOnde_assistir().toLowerCase() : null);
    }

    @Override
    public int hashCode() {
        // Cria o código hash usando os mesmos campos e lógica do equals (minúsculas e à prova de nulos)
        return Objects.hash(
                getTitulo() != null ? getTitulo().toLowerCase() : null,
                getGenero() != null ? getGenero().toLowerCase() : null,
                getAno_lancamento(),
                getDuracao(),
                getDirecao() != null ? getDirecao().toLowerCase() : null,
                getElenco() != null ? getElenco().toLowerCase() : null,
                getOnde_assistir() != null ? getOnde_assistir().toLowerCase() : null
        );
    }

}

