package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Livro implements Comparable<Livro> {
    private String titulo;
    private String autor;
    private String editora;
    private String ISBN;
    private int ano_lancamento;
    private String genero;
    private boolean lido;
    private List<Review> avaliacoes = new ArrayList<>();


    public Livro(String titulo, String autor, String editora, String ISBN, int ano_lancamento, String genero,  boolean lido) {
        this.titulo = titulo;
        this.autor = autor;
        this.editora = editora;
        this.ISBN = ISBN;
        this.ano_lancamento = ano_lancamento;
        this.genero = genero;
        this.lido = false;
    }

    public void adicionarAvaliacao(Review review) {
        avaliacoes.add(review);
        lido = true;
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

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
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

    public boolean isLido() {
        return lido;
    }

    public void setLido(boolean lido) {
        this.lido = lido;
    }

    @Override
    public int compareTo(Livro livro) {
        return this.getTitulo().compareTo(livro.getTitulo());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Livro livro = (Livro) obj;
        return ano_lancamento == livro.ano_lancamento &&
                titulo.equalsIgnoreCase(livro.titulo) &&
                autor.equalsIgnoreCase(livro.autor) &&
                editora.equalsIgnoreCase(livro.editora) &&
                ISBN.equalsIgnoreCase(livro.ISBN) &&
                genero.equalsIgnoreCase(livro.genero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo.toLowerCase(), autor.toLowerCase(), editora.toLowerCase(), ISBN.toLowerCase(), ano_lancamento, genero.toLowerCase());
    }
}
