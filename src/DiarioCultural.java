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

    /** Faz uma busca nos livros cadastrados
     * @param ano - ano dos livros que se quer buscar
     * @return lista de livros encontrados
     */
    public List<Livro> buscarLivros(int ano) {
        List<Livro> resultado = new ArrayList<Livro>();

        List<Livro> todosLivros = this.getLivros();
        for(int i = 0; i < todosLivros.size(); i++) {
            if (livros.get(i).getAno_lancamento() == ano) {
                resultado.add(livros.get(i));
            }
        }

        System.out.println("Livros encontrados do ano " + ano + ": ");
        for (Livro livro : resultado) {
            System.out.println(livro.getTitulo());
        }
        return resultado;
    }

    /** Cadastra um livro no diario cultural
     *
     * @param livro - livro que será cadastrado
     */
    public void cadastrarLivro(Livro livro){
        livros.add(livro);
        System.out.println("Livro cadastrado com sucesso: "+livro.getTitulo());
    }

    /** Cadastra filme no diario cultural
     *
     * @param filme - filme que será cadastrado
     */
    public void cadastrarFilme(Filme filme){
        filmes.add(filme);
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
