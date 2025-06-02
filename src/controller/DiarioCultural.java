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
        if (str == null) return "";
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", ""); // Remove os acentos
    }

    public DiarioCultural() {
        this.livros = new ArrayList<Livro>();
        this.filmes = new ArrayList<Filme>();
        this.series = new ArrayList<Serie>();
    }


    /**
     * Realiza uma busca na lista de livros cadastrados com base nos critérios informados.
     *
     * @param titulo Parte ou nome completo do título do livro.
     * @param autor Nome do autor do livro.
     * @param genero Gênero do livro.
     * @param ano Ano de lançamento do livro.
     * @param ISBN ISBN do livro.
     * @return Lista de livros que correspondem aos critérios de busca.
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

    /**
     * Realiza uma busca na lista de filmes cadastrados com base nos critérios informados.
     *
     * @param titulo   Parte ou nome completo do título do filme.
     * @param diretor  Nome do diretor do filme.
     * @param ator     Nome de um ator do elenco.
     * @param genero   Gênero do filme.
     * @param ano      Ano de lançamento do filme.
     * @return Lista de filmes que correspondem aos critérios de busca.
     */

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

    /**
     * Realiza uma busca na lista de séries cadastradas com base nos critérios informados.
     *
     * @param titulo Parte ou nome completo do título da série.
     * @param genero Gênero da série.
     * @param ano Ano de lançamento da série.
     * @return Lista de séries que correspondem aos critérios de busca.
     */
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
     * @param livro Livro que será cadastrado
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
     * @param filme Filme que será cadastrado
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

    /** Cadastra serie no diario cultural
     *
     * @param serie  Série que será cadastrada.
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

    /**
     * Avalia um livro com base no título informado.
     *
     * @param titulo O título do livro a ser avaliado.
     * @param avaliacao A nota da avaliação (0 a 5).
     * @param comentario Comentário do usuário sobre o livro.
     * @param data Data da avaliação.
     */
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

    /**
     * Avalia um filme com base no título informado.
     *
     * @param titulo O título do filme a ser avaliado.
     * @param avaliacao A nota da avaliação (0 a 5).
     * @param comentario Comentário do usuário sobre o filme.
     * @param data Data da avaliação.
     */
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

    /**
     * Avalia uma temporada de uma série com base no título informado e no número da temporada.
     *
     * @param tituloSerie O título da série a ser avaliado.
     * @param numeroTemporada O número da temporada da série a ser avaliada.
     * @param avaliacao A nota da avaliação (0 a 5).
     * @param comentario Comentário do usuário sobre a série.
     * @param data Data da avaliação.
     */
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

    /**
     * Ordena uma lista de livros pela ordem de maior ou menor avaliado.
     *
     * @param order Ordem escolhida pelo usuário (Maior para menor ou Menor para maior)
     * @param genero O gênero do livro para filtrar ainda mais a busca.
     * @param ano O ano de lançamento do livro para filtrar ainda mais a busca.
     * @return Lista de livros na ordem escolhida.
     */
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

    /**
     * Ordena uma lista de filmes pela ordem de maior ou menor avaliado.
     *
     * @param order Ordem escolhida pelo usuário (Maior para menor ou Menor para maior)
     * @param genero O gênero do filme para filtrar ainda mais a busca.
     * @param ano O ano de lançamento do filme para filtrar ainda mais a busca.
     * @return Lista de filmes na ordem escolhida.
     */
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
    /**
     * Ordena uma lista de séries pela ordem de maior ou menor avaliado.
     *
     * @param order Ordem escolhida pelo usuário (Maior para menor ou Menor para maior)
     * @param genero O gênero da série para filtrar ainda mais a busca.
     * @param ano O ano de lançamento da série para filtrar ainda mais a busca.
     * @return Lista de séries na ordem escolhida.
     */
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

    /**
     * Remove um livro da lista de livros cadastrados com base no título informado.
     * Se houver mais de um livro com o mesmo título, exibe as opções para o usuário
     * escolher qual deseja remover.
     *
     * @param titulo Título do livro a ser removido.
     */
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

    /**
     * Remove um filme da lista de filmes cadastrados com base no título informado.
     * Se houver mais de um filme com o mesmo título, exibe as opções para o usuário
     * escolher qual deseja remover.
     *
     * @param titulo Título do filme a ser removido.
     */
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
    /**
     * Remove uma série da lista de séries cadastradas com base no título informado.
     * Se houver mais de uma série com o mesmo título, exibe as opções para o usuário
     * escolher qual deseja remover.
     *
     * @param titulo Título da série a ser removido.
     */
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
