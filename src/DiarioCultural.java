import java.util.ArrayList;
import java.util.List;
import java.text.Normalizer;


public class DiarioCultural {
    private List<Livro> livros;
    private List<Filme> filmes;
    private List<Serie> series;

    // Método para remover acentos de uma string
    private String removerAcentos(String str) {
        if (str == null) return null;
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", ""); // Remove os acentos
    }

    public DiarioCultural() {
        this.livros = new ArrayList<Livro>();
        this.filmes = new ArrayList<Filme>();
        this.series = new ArrayList<Serie>();
    }

    /** Faz uma busca nos livros cadastrados
     * @param ano - ano dos livros que se quer buscar
     * @return lista de livros encontrados
     */
    public List<Livro> buscarLivrosPorAno(int ano) {
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


    /** Faz uma busca nos livros cadastrados
     * @param titulo, autor, genero, ano, ISBN - ano dos livros que se quer buscar
     * @return lista de livros encontrados
     */
    public List<Livro> buscarLivros(String titulo, String autor, String genero, Integer ano, String ISBN) {
        List<Livro> resultado = new ArrayList<>();
        for (Livro livro : livros) {
            boolean matches = true;
            if (titulo != null && !removerAcentos(livro.getTitulo()).equalsIgnoreCase(removerAcentos(titulo))) matches = false;
            if (autor != null && !removerAcentos(livro.getAutor()).equalsIgnoreCase(removerAcentos(autor))) matches = false;
            if (genero != null && !removerAcentos(livro.getGenero()).equalsIgnoreCase(removerAcentos(genero))) matches = false;
            if (ano != null && livro.getAno_lancamento() != ano) matches = false;
            if (ISBN != null && !livro.getISBN().equalsIgnoreCase(ISBN)) matches = false;
            if (matches) resultado.add(livro);
        }
        return resultado;
    }

    public List<Filme> buscarFilmes(String titulo, String diretor, String ator, String genero, Integer ano) {
        List<Filme> resultado = new ArrayList<>();
        for (Filme filme : filmes) {
            boolean matches = true;
            if (titulo != null && !removerAcentos(filme.getTitulo()).equalsIgnoreCase(removerAcentos(titulo))) matches = false;
            if (diretor != null && !removerAcentos(filme.getDirecao()).equalsIgnoreCase(removerAcentos(diretor))) matches = false;
            if (ator != null && !removerAcentos(filme.getElenco()).contains(removerAcentos(ator))) matches = false;
            if (genero != null && !removerAcentos(filme.getGenero()).equalsIgnoreCase(removerAcentos(genero))) matches = false;
            if (ano != null && filme.getAno_lancamento() != ano) matches = false;
            if (matches) resultado.add(filme);
        }
        return resultado;
    }

    public List<Serie> buscarSeries(String titulo, String genero, Integer ano) {
        List<Serie> resultado = new ArrayList<>();
        for (Serie serie : series) {
            boolean matches = true;
            if (titulo != null && !removerAcentos(serie.getTitulo()).equalsIgnoreCase(removerAcentos(titulo))) matches = false;
            if (genero != null && !removerAcentos(serie.getGenero()).equalsIgnoreCase(removerAcentos(genero))) matches = false;
            if (ano != null && serie.getAno_lancamento() != ano) matches = false;
            if (matches) resultado.add(serie);
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
