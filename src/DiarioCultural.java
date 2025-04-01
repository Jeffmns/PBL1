import java.util.ArrayList;
import java.util.Collections;
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

    /** Faz uma busca nos livros cadastrados
     * @param ano - ano dos livros que se quer buscar
     * @return lista de livros encontrados
     */
    public List<Livro> buscarLivros(int ano) {
        List<Livro> resultado = new ArrayList<Livro>();

        List<Livro> todosLivros = this.getLivros();
        for(int i = 0; i < todosLivros.size(); i++) {
            if (livros.get(i).getAnoLancamento() == ano) {
                resultado.add(livros.get(i));
            }
        }

        System.out.println("Livros encontrados do ano " + ano + ": ");
        for (Livro livro : resultado) {
            System.out.println(livro.getTitulo());
        }
        return resultado;
    }
    public List<Livro> listarlivros(){
        List<Livro> todosLivros = this.getLivros();
        System.out.println("Livros cadastrados: ");
        for (Livro livro : todosLivros) {
            System.out.println(livro.getTitulo());
        }

        return todosLivros;
    }
    public List<Filme> listarfilmes(){
        List<Filme> todosFilmes = this.getFilmes();
        System.out.println("Filmes cadastrados: ");
        for (Filme filme : todosFilmes) {
            System.out.println(filme.getTitulo());
        }

        return todosFilmes;
    }
    /** Cadastra um livro no diario cultural
     *
     * @param livro - livro que será cadastrado
     */
    public void cadastrarLivro(Livro livro){
        livros.add(livro);
        Collections.sort(this.livros);
        System.out.println("Livro cadastrado com sucesso: "+livro.getTitulo());
    }

    /** Cadastra filme no diario cultural
     *
     * @param filme - filme que será cadastrado
     */
    public void cadastrarFilme(Filme filme){
        filmes.add(filme);
        Collections.sort(this.filmes);
        System.out.println("Filme cadastrado com sucesso: " +filme.getTitulo());
    }

    /** cadastra serie no diario cultural
     *
     * @param serie - serie que será cadastrada
     */
    public void cadastrarSerie( Serie serie){
        series.add(serie);
        System.out.println("Serie cadastrada com sucesso: " +serie.getTitulo());

    }

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }

    public List<Filme> getFilmes() {
        return filmes;
    }

    public void setFilmes(List<Filme> filmes) {
        this.filmes = filmes;
    }

    public List<Serie> getSeries() {
        return series;
    }

    public void setSeries(List<Serie> series) {
        this.series = series;
    }
}
