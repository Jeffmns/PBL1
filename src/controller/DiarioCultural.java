package controller;

import java.util.*;
import java.text.Normalizer;
import model.Filme;
import model.Livro;
import model.Serie;
import model.Temporada;
import model.Review;
import persistence.PersistenciaJson;

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
        for (Livro livro : todosLivros) {
            if (livro.getAno_lancamento() == ano) {
                resultado.add(livro);
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


    /** Faz uma busca nos livros cadastrados
     * @param titulo, autor, genero, ano, ISBN - ano dos livros que se quer buscar
     * @return lista de livros encontrados
     */
    public List<Livro> buscarLivros(String titulo, String autor, String genero, Integer ano, String ISBN) {
        List<Livro> resultado = new ArrayList<>();
        for (Livro livro : livros) {
            boolean matches = true;

            if (titulo != null && !removerAcentos(livro.getTitulo()).toLowerCase().contains(removerAcentos(titulo).toLowerCase())) matches = false;
            if (autor != null && !removerAcentos(livro.getAutor()).toLowerCase().contains(removerAcentos(autor).toLowerCase())) matches = false;
            if (genero != null && !removerAcentos(livro.getGenero()).toLowerCase().contains(removerAcentos(genero).toLowerCase())) matches = false;
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

            if (titulo != null && !removerAcentos(filme.getTitulo()).toLowerCase().contains(removerAcentos(titulo).toLowerCase()))
                matches = false;
            if (diretor != null && !removerAcentos(filme.getDirecao()).toLowerCase().contains(removerAcentos(diretor).toLowerCase()))
                matches = false;
            if (ator != null && !removerAcentos(filme.getElenco()).toLowerCase().contains(removerAcentos(ator).toLowerCase()))
                matches = false;
            if (genero != null && !removerAcentos(filme.getGenero()).toLowerCase().contains(removerAcentos(genero).toLowerCase()))
                matches = false;
            if (ano != null && filme.getAno_lancamento()!= ano)
                matches = false;

            if (matches) resultado.add(filme);
        }
        return resultado;
    }

    public List<Serie> buscarSeries(String titulo, String genero, Integer ano) {
        List<Serie> resultado = new ArrayList<>();
        for (Serie serie : series) {
            boolean matches = true;

            if (titulo != null && !removerAcentos(serie.getTitulo()).toLowerCase().contains(removerAcentos(titulo).toLowerCase())) matches = false;
            if (genero != null && !removerAcentos(serie.getGenero()).toLowerCase().contains(removerAcentos(genero).toLowerCase())) matches = false;
            if (ano != null && serie.getAnoLancamento() != ano) matches = false;
            if (matches) resultado.add(serie);
        }
        return resultado;
    }

    /** Cadastra um livro no diario cultural
     *
     * @param livro - livro que será cadastrado
     */
    public void cadastrarLivro(Livro livro){
        if (!livros.contains(livro)) {
            livros.add(livro);
            PersistenciaJson.salvar(this);
            System.out.println("Livro cadastrado com sucesso: " + livro.getTitulo());
        } else {
            System.out.println("Livro já cadastrado.");
        }
    }


    /** Cadastra filme no diario cultural
     *
     * @param filme - filme que será cadastrado
     */
    public void cadastrarFilme(Filme filme){
        if (!filmes.contains(filme)) {
            filmes.add(filme);
            PersistenciaJson.salvar(this);
            System.out.println("Filme cadastrado com sucesso: " + filme.getTitulo());
        } else {
            System.out.println("Filme já cadastrado.");
        }
    }

    /** cadastra serie no diario cultural
     *
     * @param serie - serie que será cadastrada
     */
    public void cadastrarSerie(Serie serie){
        if (!series.contains(serie)) {
            series.add(serie);
            PersistenciaJson.salvar(this);
            System.out.println("Série cadastrada com sucesso: " + serie.getTitulo());
        } else {
            System.out.println("Série já cadastrada.");
        }
    }


    // Avaliar Livro
    public void avaliarLivro(String titulo, int avaliacao, String comentario, Date data) {
        for (Livro livro : livros) {
            if (livro.getTitulo().equalsIgnoreCase(titulo)) {
                Review review = new Review(avaliacao, data, comentario);
                livro.getAvaliacoes().add(review);
                livro.setLido(true);
                PersistenciaJson.salvar(this);
                System.out.println("Livro avaliado com sucesso!");
                return;
            }
        }
        System.out.println("Livro não encontrado.");
    }

    // Avaliar Filme
    public void avaliarFilme(String titulo, int avaliacao, String comentario, Date data) {
        for (Filme filme : filmes) {
            if (filme.getTitulo().equalsIgnoreCase(titulo)) {
                Review review = new Review(avaliacao, data, comentario);
                filme.getAvaliacoes().add(review);
                filme.setAssistido(true);
                PersistenciaJson.salvar(this);
                System.out.println("Filme avaliado com sucesso!");
                return;
            }
        }
        System.out.println("Filme não encontrado.");
    }

    // Avaliar Temporada de Série
    public void avaliarTemporadaSerie(String tituloSerie, int numeroTemporada, int avaliacao, String comentario, Date data) {
        for (Serie serie : series) {
            if (serie.getTitulo().equalsIgnoreCase(tituloSerie)) {
                if (numeroTemporada > 0 && numeroTemporada <= serie.getTemporadas().size()) {
                    Temporada temporada = serie.getTemporadas().get(numeroTemporada - 1);
                    temporada.avaliarTemporada(avaliacao, data, comentario);
                    temporada.setAssistido(true);
                    PersistenciaJson.salvar(this);
                    System.out.println("Temporada avaliada com sucesso!");
                    return;
                } else {
                    System.out.println("Número da temporada inválido.");
                    return;
                }
            }
        }
        System.out.println("Série não encontrada.");
    }

    public List<Livro> ordenarLivrosPorAvaliacao(int order, String genero, Integer ano) {
        int desc = 2;

        List<Livro> todosLivros = this.getLivros();
        List<Livro> resultado = new ArrayList<>();

        for (Livro livro : todosLivros) {
            boolean matches = true;

            if (genero != null && !removerAcentos(livro.getGenero()).toLowerCase().contains(removerAcentos(genero).toLowerCase())) matches = false;
            if (ano != null && livro.getAno_lancamento() != ano) matches = false;

            if (matches) resultado.add(livro);
        }

        if (order == desc) {
            resultado.sort(Comparator.comparingDouble(Livro::getMediaAvaliacoes));
        } else {
            resultado.sort(Comparator.comparingDouble(Livro::getMediaAvaliacoes).reversed());
        }

        return resultado;
    }

    public List<Filme> ordenarFilmesPorAvaliacao(int order, String genero, Integer ano) {
        int desc = 2;

        List<Filme> todosFilmes = this.getFilmes();
        List<Filme> resultado = new ArrayList<>();
        for (Filme filme : todosFilmes) {
            boolean matches = true;

            if (genero != null && !removerAcentos(filme.getGenero()).toLowerCase().contains(removerAcentos(genero).toLowerCase()))
                matches = false;
            if (ano != null && filme.getAno_lancamento()!= ano)
                matches = false;

            if (matches) resultado.add(filme);
        }

        if (order == desc) {
            resultado.sort(Comparator.comparingDouble(Filme::getMediaAvaliacoes));
        } else {
            resultado.sort(Comparator.comparingDouble(Filme::getMediaAvaliacoes).reversed());
        }

        return resultado;
    }

    public List<Serie> ordenarSeriesPorAvaliacao(int order, String genero, Integer ano) {
        int desc = 2;

        List<Serie> todasSeries = this.getSeries();
        List<Serie> resultado = new ArrayList<>();
        for (Serie serie : todasSeries) {
            boolean matches = true;

            if (genero != null && !removerAcentos(serie.getGenero()).toLowerCase().contains(removerAcentos(genero).toLowerCase())) matches = false;
            if (ano != null && serie.getAnoLancamento() != ano) matches = false;
            if (matches) resultado.add(serie);
        }

        if (order == desc) {
            resultado.sort(Comparator.comparingDouble(Serie::getMediaAvaliacoes));
        } else {
            resultado.sort(Comparator.comparingDouble(Serie::getMediaAvaliacoes).reversed());
        }

        return resultado;
    }

    //Remover livro
    public void removerLivro(String titulo, Scanner scanner) {
        List<Livro> encontrados = new ArrayList<>();
        for (Livro livro : livros) {
            if (livro.getTitulo().equalsIgnoreCase(titulo)) {
                encontrados.add(livro);
            }
        }

        if (encontrados.isEmpty()) {
            System.out.println("Nenhum livro encontrado com esse título.");
            return;
        }

        if (encontrados.size() == 1) {
            livros.remove(encontrados.get(0));
            System.out.println("Livro removido com sucesso!");
        } else {
            System.out.println("\nLivros encontrados:");
            for (int i = 0; i < encontrados.size(); i++) {
                Livro l = encontrados.get(i);
                System.out.println((i + 1) + " - " + l.getTitulo() + " (" +l.getAno_lancamento() + ") - Autor: " + l.getAutor() + " - Editora: " + l.getEditora() + " - ISBN: " + l.getISBN());
            }

            System.out.print("Digite o número do livro que deseja remover: ");
            try {
                int escolha = Integer.parseInt(scanner.nextLine());
                if (escolha >= 1 && escolha <= encontrados.size()) {
                    livros.remove(encontrados.get(escolha - 1));
                    System.out.println("Livro removido com sucesso!");
                } else {
                    System.out.println("Número inválido.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida.");
            }
        }
    }

    //Remover filme
    public void removerFilme(String titulo, Scanner scanner) {
        List<Filme> encontrados = new ArrayList<>();
        for (Filme filme : filmes) {
            if (filme.getTitulo().equalsIgnoreCase(titulo)) {
                encontrados.add(filme);
            }
        }

        if (encontrados.isEmpty()) {
            System.out.println("Nenhum filme encontrado com esse título.");
            return;
        }

        if (encontrados.size() == 1) {
            filmes.remove(encontrados.get(0));
            System.out.println("Filme removido com sucesso!");
        } else {
            System.out.println("\nFilmes encontrados:");
            for (int i = 0; i < encontrados.size(); i++) {
                Filme f = encontrados.get(i);
                System.out.println((i + 1) + " - " + f.getTitulo() + " (" + f.getAno_lancamento() + ") - Direção: " + f.getDirecao() + " - Plataforma: " + f.getOnde_assistir());
            }

            System.out.print("Digite o número do filme que deseja remover: ");
            try {
                int escolha = Integer.parseInt(scanner.nextLine());
                if (escolha >= 1 && escolha <= encontrados.size()) {
                    filmes.remove(encontrados.get(escolha - 1));
                    System.out.println("Filme removido com sucesso!");
                } else {
                    System.out.println("Número inválido.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida.");
            }
        }
    }

    public void removerSerie(String titulo, Scanner scanner) {
        List<Serie> encontrados = new ArrayList<>();
        for (Serie serie : series) {
            if (serie.getTitulo().equalsIgnoreCase(titulo)) {
                encontrados.add(serie);
            }
        }

        if (encontrados.isEmpty()) {
            System.out.println("Nenhuma série encontrada com esse título.");
            return;
        }

        if (encontrados.size() == 1) {
            series.remove(encontrados.get(0));
            System.out.println("Série removida com sucesso!");
        } else {
            System.out.println("\nSéries encontradas:");
            for (int i = 0; i < encontrados.size(); i++) {
                Serie s = encontrados.get(i);
                System.out.println((i + 1) + " - " + s.getTitulo() + " (" + s.getAnoLancamento() + ") - Elenco: " + s.getElenco() + " - Plataforma: " + s.getOnde_assistir());
            }

            System.out.print("Digite o número da série que deseja remover: ");
            try {
                int escolha = Integer.parseInt(scanner.nextLine());
                if (escolha >= 1 && escolha <= encontrados.size()) {
                    series.remove(encontrados.get(escolha - 1));
                    System.out.println("Série removida com sucesso!");
                } else {
                    System.out.println("Número inválido.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida.");
            }
        }
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
