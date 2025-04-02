import java.util.List;
import java.util.Scanner;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        DiarioCultural dc = new DiarioCultural();
        Livro l1 = new Livro("Christine", "Stephen King", "Objetiva",
                "978-85-60280-87-2", 2013, "Terror", true, true);
        Livro l2 = new Livro("Joyland", "Stephen King", "Objetiva",
                "978-85-60280-87-2", 2012, "Terror", true, true);
        Livro l3 = new Livro("A culpa é das estrelas", "John Green", "Objetiva",
                "978-85-60280-87-2", 2013, "Romance", true, true);

        dc.cadastrarLivro(l1);
        dc.cadastrarLivro(l2);
        dc.cadastrarLivro(l3);

        Filme f1 = new Filme("Interestelar", "Ficção Científica", 2013,
                150, "Cristopher Nolan", "xxx", "Anne Hateway",
                "Interestelar", "Netflix", false);

        Serie s1 = new Serie("Stranger Things", "Ficção", 2016, 2025,
                "Sadie Sink", "Stranger Things", "Netflix");

        dc.cadastrarFilme(f1);
        dc.cadastrarSerie(s1);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nEscolha uma opção de busca:");
            System.out.println("1 - Buscar Livros");
            System.out.println("2 - Buscar Filmes");
            System.out.println("3 - Buscar Séries");
            System.out.println("4 - Sair");
            System.out.print("Opção: ");
            String opcao = scanner.nextLine().trim();

            if (opcao.equals("4")) {
                System.out.println("Saindo...");
                break;
            }

            System.out.println("\nDigite os critérios de busca (pressione ENTER para pular):");
            String titulo = getInput(scanner, "Título: ");

            switch (opcao) {
                case "1" -> { // Livros
                    String autor = getInput(scanner, "Autor: ");
                    String genero = getInput(scanner, "Gênero: ");
                    String ISBN = getInput(scanner, "ISBN: ");
                    Integer ano = getIntegerInput(scanner, "Ano de lançamento: ");

                    List<Livro> resultados = dc.buscarLivros(titulo, autor, genero, ano, ISBN);
                    exibirResultadosLivros(resultados, titulo);
                }
                case "2" -> { // Filmes
                    String diretor = getInput(scanner, "Diretor: ");
                    String ator = getInput(scanner, "Ator no elenco: ");
                    String genero = getInput(scanner, "Gênero: ");
                    Integer ano = getIntegerInput(scanner, "Ano de lançamento: ");

                    List<Filme> resultados = dc.buscarFilmes(titulo, diretor, ator, genero, ano);
                    exibirResultadosFilmes(resultados, titulo);
                }
                case "3" -> { // Séries
                    String genero = getInput(scanner, "Gênero: ");
                    Integer ano = getIntegerInput(scanner, "Ano de lançamento: ");

                    List<Serie> resultados = dc.buscarSeries(titulo, genero, ano);
                    exibirResultadosSeries(resultados, titulo);
                }
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
        }

        scanner.close();
    }

    private static String getInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? null : input;
    }

    private static Integer getIntegerInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        try {
            return input.isEmpty() ? null : Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static void exibirResultadosLivros(List<Livro> livros, String criterio) {
        System.out.println("\nResultados encontrados para \"" + (criterio != null ? criterio : "Pesquisa") + "\":\n");
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro correspondente foi encontrado.");
        } else {
            for (Livro livro : livros) {
                System.out.println("[Livro] \"" + livro.getTitulo() + "\" (" + livro.getAno_lancamento() + ")");
                System.out.println("  Autor: " + livro.getAutor());
                System.out.println("  Gênero: " + livro.getGenero());
                System.out.println("  ISBN: " + livro.getISBN());
                System.out.println();
            }
        }
    }

    private static void exibirResultadosFilmes(List<Filme> filmes, String criterio) {
        System.out.println("\nResultados encontrados para \"" + (criterio != null ? criterio : "Pesquisa") + "\":\n");
        if (filmes.isEmpty()) {
            System.out.println("Nenhum filme correspondente foi encontrado.");
        } else {
            for (Filme filme : filmes) {
                System.out.println("[Filme] \"" + filme.getTitulo() + "\" (" + filme.getAno_lancamento() + ")");
                System.out.println("  Diretor: " + filme.getDirecao());
                System.out.println("  Gênero: " + filme.getGenero());
                System.out.println();
            }
        }
    }

    private static void exibirResultadosSeries(List<Serie> series, String criterio) {
        System.out.println("\nResultados encontrados para \"" + (criterio != null ? criterio : "Pesquisa") + "\":\n");
        if (series.isEmpty()) {
            System.out.println("Nenhuma série correspondente foi encontrada.");
        } else {
            for (Serie serie : series) {
                System.out.println("[Série] \"" + serie.getTitulo() + "\" (" + serie.getAno_lancamento() + ")");
                System.out.println("  Gênero: " + serie.getGenero());
                System.out.println();
            }
        }
    }
}

