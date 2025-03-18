import java.util.ArrayList;
import java.util.List;

public class DiarioCultural {
    private List<Livro> livros;
    private List<Filme> filmes;
    private List<Serie> series;

    public DiarioCultural() {
        this.livros = new ArrayList<Livro>();
        this.filmes = new ArrayList<Filme>();
        this.series = new ArrayList<Serie>();
    }

    /** Cadastra um livro no diario cultural
     *
     * @param livro - livro que será cadastrado
     */
    public void cadastrarLivro(Livro livro){
        livros.add(livro);
        System.out.println("Livro cadastrado com sucesso:"+livro.getTitulo());
    }

    /** Cadastra filme no diario cultural
     *
     * @param filme - filme que será cadastrado
     */
    public void cadastrarFilme(Filme filme){
        filmes.add(filme);
        System.out.println("Filme cadastrado com sucesso:" +filme.getTitulo());
    }

    /** cadastra serie no diario cultural
     *
     * @param serie - serie que será cadastrada
     */
    public void cadastrarSerie( Serie serie){
        series.add(serie);
        System.out.println("Serie cadastrada com sucesso:" +serie.getTitulo());

    }









}
