import java.util.Date;

public class Livro {
    private String titulo;
    private String autor;
    private String editora;
    private String ISBN;
    private int ano_lancamento;
    private String genero;
    private boolean exemplar;
    private boolean lido;

    public Livro(String titulo, String autor, String editora, String ISBN, int ano_lancamento, String genero, boolean exemplar, boolean lido) {
        this.titulo = titulo;
        this.autor = autor;
        this.editora = editora;
        this.ISBN = ISBN;
        this.ano_lancamento = ano_lancamento;
        this.genero = genero;
        this.exemplar = exemplar;
        this.lido = lido;
    }



    public void avaliarLivro (Date data_review, String review, int avaliacao){

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

    public boolean isExemplar() {
        return exemplar;
    }

    public void setExemplar(boolean exemplar) {
        this.exemplar = exemplar;
    }

    public boolean isLido() {
        return lido;
    }

    public void setLido(boolean lido) {
        this.lido = lido;
    }


}


