import controller.DiarioCultural;
import model.Filme;
import model.Livro;
import model.Serie;
import model.Temporada;
import persistence.PersistenciaJson;

import java.util.Date;
import java.util.List;
import java.util.Scanner;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

/**
 * Classe principal que inicia a execução do sistema Diário Cultural.
 * Exibe o menu principal com opções para cadastro, busca, avaliação, listagem e exclusão de obras.
 */
public class Main {
    public static void main(String[] args) {
        DiarioCultural dc = PersistenciaJson.carregar();
        PersistenciaJson.salvar(dc);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Menu Principal ===");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Buscar");
            System.out.println("3 - Avaliar");
            System.out.println("4 - Listar");
            System.out.println("5 - Excluir");
            System.out.println("6 - Sair");
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1" -> menuCadastro(scanner, dc);
                case "2" -> menuBusca(scanner, dc);
                case "3" -> menuAvaliacao(scanner, dc);
                case "4" -> menuListagem(scanner, dc);
                case "5" -> menuExcluir(scanner,dc);
                case "6" -> {
                    System.out.println("Saindo...");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

    // Submenu de Cadastro
    private static void menuCadastro(Scanner scanner, DiarioCultural dc) {
        while (true) {
            System.out.println("\n=== Menu Cadastro ===");
            System.out.println("1 - Cadastrar Livro");
            System.out.println("2 - Cadastrar Filme");
            System.out.println("3 - Cadastrar Série");
            System.out.println("4 - Voltar");
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1" -> cadastrarLivro(scanner, dc);
                case "2" -> cadastrarFilme(scanner, dc);
                case "3" -> cadastrarSerie(scanner, dc);
                case "4" -> { return; }
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

    // Submenu de Busca
    private static void menuBusca(Scanner scanner, DiarioCultural dc) {
        while (true) {
            System.out.println("\n=== Menu Busca ===");
            System.out.println("1 - Buscar Livros");
            System.out.println("2 - Buscar Filmes");
            System.out.println("3 - Buscar Séries");
            System.out.println("4 - Voltar");
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1" -> buscarLivros(scanner, dc);
                case "2" -> buscarFilmes(scanner, dc);
                case "3" -> buscarSeries(scanner, dc);
                case "4" -> { return; }
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

    // Submenu de Avaliação
    private static void menuAvaliacao(Scanner scanner, DiarioCultural dc) {
        while (true) {
            System.out.println("\n=== Menu Avaliação ===");
            System.out.println("1 - Avaliar Livro");
            System.out.println("2 - Avaliar Filme");
            System.out.println("3 - Avaliar Série");
            System.out.println("4 - Voltar");
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1" -> avaliarLivro(scanner, dc);
                case "2" -> avaliarFilme(scanner, dc);
                case "3" -> avaliarSerie(scanner, dc);
                case "4" -> { return; }
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

    // Submenu de Listagem
    private static void menuListagem(Scanner scanner, DiarioCultural dc) {
        while (true) {
            System.out.println("\n=== Menu Listagem ===");
            System.out.println("1 - Listar Livros");
            System.out.println("2 - Listar Filmes");
            System.out.println("3 - Listar Séries");
            System.out.println("4 - Voltar");
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1" -> listarLivros(scanner, dc);
                case "2" -> listarFilmes(scanner, dc);
                case "3" -> listarSeries(scanner, dc);
                case "4" -> { return; }
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

    private static void menuExcluir(Scanner scanner, DiarioCultural dc) {
        while (true) {
            System.out.println("\n=== Menu Exclusão ===");
            System.out.println("1 - Excluir Livro");
            System.out.println("2 - Excluir Filme");
            System.out.println("3 - Excluir Série");
            System.out.println("4 - Voltar");
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1" -> {
                    System.out.print("Digite o título do livro a ser removido: ");
                    String titulo = scanner.nextLine();
                    dc.removerLivro(titulo, scanner);
                    PersistenciaJson.salvar(dc);
                }
                case "2" -> {
                    System.out.print("Digite o título do filme a ser removido: ");
                    String titulo = scanner.nextLine();
                    dc.removerFilme(titulo, scanner);
                    PersistenciaJson.salvar(dc);
                }
                case "3" -> {
                    System.out.print("Digite o título da série a ser removida: ");
                    String titulo = scanner.nextLine();
                    dc.removerSerie(titulo, scanner);
                    PersistenciaJson.salvar(dc);
                }
                case "4" -> {
                    return;
                }
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }


    // Métodos de Cadastro
    private static void cadastrarLivro(Scanner scanner, DiarioCultural dc) {
        System.out.println("\nCadastro de Livro:");
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Autor: ");
        String autor = scanner.nextLine();
        System.out.print("Editora: ");
        String editora = scanner.nextLine();
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Ano de lançamento: ");
        int ano = Integer.parseInt(scanner.nextLine());
        System.out.print("Gênero: ");
        String genero = scanner.nextLine();
        System.out.print("Lido? (true/false): ");
        boolean lido = Boolean.parseBoolean(scanner.nextLine());

        Livro livro = new Livro(titulo, autor, editora, isbn, ano, genero, lido);
        dc.cadastrarLivro(livro);
    }

    private static void cadastrarFilme(Scanner scanner, DiarioCultural dc) {
        System.out.println("\nCadastro de Filme:");
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Gênero: ");
        String genero = scanner.nextLine();
        System.out.print("Ano de lançamento: ");
        int ano = Integer.parseInt(scanner.nextLine());
        int duracao;
        while (true) {
            System.out.print("Duração (minutos): ");
            String entrada = scanner.nextLine().trim();

            try {
                duracao = Integer.parseInt(entrada);
                break; // saída do laço se deu certo
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite apenas números.");
            }
        }
        System.out.print("Diretor: ");
        String diretor = scanner.nextLine();
        System.out.print("Ator principal: ");
        String ator = scanner.nextLine();
        System.out.print("Onde asssitir: ");
        String plataforma = scanner.nextLine();

        Filme filme = new Filme(titulo, genero, ano, duracao, diretor, ator, plataforma);
        dc.cadastrarFilme(filme);
    }

    private static void cadastrarSerie(Scanner scanner, DiarioCultural dc) {
        System.out.print("Título da série: ");
        String titulo = scanner.nextLine();

        System.out.print("Gênero: ");
        String genero = scanner.nextLine();

        int ano;
        while (true) {
            try {
                System.out.print("Ano de lançamento da série: ");
                ano = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Por favor, insira um ano válido (somente números).");
            }
        }

        System.out.print("Elenco: ");
        String elenco = scanner.nextLine();

        System.out.print("Onde assistir: ");
        String ondeAssistir = scanner.nextLine();

        Serie serie = new Serie(titulo, genero, ano, elenco, ondeAssistir);

        int numTemporadas;
        while (true) {
            try {
                System.out.print("Quantas temporadas a série possui? ");
                numTemporadas = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Por favor, insira um número válido de temporadas.");
            }
        }

        for (int i = 1; i <= numTemporadas; i++) {
            System.out.println("\nCadastrando a " + i + "ª temporada:");

            int anoTemporada;
            while (true) {
                try {
                    System.out.print("Ano de lançamento da temporada: ");
                    anoTemporada = Integer.parseInt(scanner.nextLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Por favor, insira um ano válido para a temporada.");
                }
            }

            int qtdEpisodios;
            while (true) {
                try {
                    System.out.print("Quantidade de episódios: ");
                    qtdEpisodios = Integer.parseInt(scanner.nextLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Por favor, insira um número válido de episódios.");
                }
            }

            Temporada temporada = new Temporada(i, anoTemporada, qtdEpisodios);
            serie.adicionarTemporada(temporada);
        }

        dc.cadastrarSerie(serie);
    }


    // Métodos de Busca
    private static void buscarLivros(Scanner scanner, DiarioCultural dc) {
        System.out.println("\nDigite os critérios de busca (pressione ENTER para pular):");
        String titulo = getInput(scanner, "Título: ");
        String autor = getInput(scanner, "Autor: ");
        String genero = getInput(scanner, "Gênero: ");
        String ISBN = getInput(scanner, "ISBN: ");
        Integer ano = getIntegerInput(scanner, "Ano de lançamento: ");

        List<Livro> resultados = dc.buscarLivros(titulo, autor, genero, ano, ISBN);
        exibirResultadosLivros(resultados, titulo);

    }

    private static void buscarFilmes(Scanner scanner, DiarioCultural dc) {
        System.out.println("\nDigite os critérios de busca (pressione ENTER para pular):");
        String titulo = getInput(scanner, "Título: ");
        String diretor = getInput(scanner, "Diretor: ");
        String ator = getInput(scanner, "Ator no elenco: ");
        String genero = getInput(scanner, "Gênero: ");
        Integer ano = getIntegerInput(scanner, "Ano de lançamento: ");

        List<Filme> resultados = dc.buscarFilmes(titulo, diretor, ator, genero, ano);
        exibirResultadosFilmes(resultados, titulo);
    }

    private static void buscarSeries(Scanner scanner, DiarioCultural dc) {
        System.out.println("\nDigite os critérios de busca (pressione ENTER para pular):");
        String titulo = getInput(scanner, "Título: ");
        String genero = getInput(scanner, "Gênero: ");
        Integer ano = getIntegerInput(scanner, "Ano de lançamento: ");
        System.out.print("\nDigite o título da série que deseja buscar: ");
        List<Serie> resultados = dc.buscarSeries(titulo, genero, ano);
        exibirResultadosSeries(resultados, titulo);
    }

    // Métodos de Avaliação
    private static void avaliarLivro(Scanner scanner, DiarioCultural dc) {
        System.out.print("\nDigite o título do livro que deseja avaliar: ");
        String titulo = scanner.nextLine();
        System.out.print("Digite a nota de avaliação (0 a 5): ");
        int nota = Integer.parseInt(scanner.nextLine());
        System.out.print("\nDigite um comentário: ");
        String comentario = scanner.nextLine();
        Date data = new Date();
        dc.avaliarLivro(titulo, nota, comentario, data);
    }

    private static void avaliarFilme(Scanner scanner, DiarioCultural dc) {
        System.out.print("\nDigite o título do filme que deseja avaliar: ");
        String titulo = scanner.nextLine();
        System.out.print("Digite a nota de avaliação (0 a 5): ");
        int nota = Integer.parseInt(scanner.nextLine());
        System.out.print("\nDigite um comentário: ");
        String comentario = scanner.nextLine();
        Date data = new Date();
        dc.avaliarFilme(titulo, nota, comentario, data);
    }

    private static void avaliarSerie(Scanner scanner, DiarioCultural dc) {
        System.out.print("\nDigite o título da série que deseja avaliar: ");
        String titulo = scanner.nextLine();
        System.out.print("Digite o número da temporada que deseja avaliar: ");
        int temporada = Integer.parseInt(scanner.nextLine());
        System.out.print("Digite a nota de avaliação (0 a 5): ");
        int nota = Integer.parseInt(scanner.nextLine());
        System.out.print("\nDigite um comentário: ");
        String comentario = scanner.nextLine();
        Date data = new Date();
        dc.avaliarTemporadaSerie(titulo, temporada, nota, comentario, data);
    }

    // Métodos de Listagem
    private static void listarLivros(Scanner scanner, DiarioCultural dc) {
        Integer order = null;
        while (true) {
            System.out.println("\nDigite o tipo de ordenação (pressione ENTER para pular):");
            System.out.print("Ordenação: \n[1] Maior avaliação\n[2] Menor Avaliação\n> ");
            String entrada = scanner.nextLine().trim();

            try {
                int opcao = Integer.parseInt(entrada);
                if (opcao == 1 || opcao == 2) {
                    order = opcao;
                    break;
                } else {
                    System.out.println("Opção inválida. Digite apenas 1, 2 ou pressione ENTER.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite apenas 1, 2 ou pressione ENTER.");
            }
        }
        String genero = getInput(scanner, "Gênero: ");
        Integer ano = getIntegerInput(scanner, "Ano de lançamento: ");
        List<Livro> resultados = dc.ordenarLivrosPorAvaliacao(order, genero, ano);
        exibirLivros(resultados);
    }

    private static void listarFilmes(Scanner scanner, DiarioCultural dc) {
        Integer order = null;
        while (true) {
            System.out.println("\nDigite o tipo de ordenação (pressione ENTER para pular):");
            System.out.print("Ordenação: \n[1] Maior avaliação\n[2] Menor Avaliação\n> ");
            String entrada = scanner.nextLine().trim();

            try {
                int opcao = Integer.parseInt(entrada);
                if (opcao == 1 || opcao == 2) {
                    order = opcao;
                    break;
                } else {
                    System.out.println("Opção inválida. Digite apenas 1, 2 ou pressione ENTER.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite apenas 1, 2 ou pressione ENTER.");
            }
        }

        String genero = getInput(scanner, "Gênero: ");
        Integer ano = getIntegerInput(scanner, "Ano de lançamento: ");

        List<Filme> resultados = dc.ordenarFilmesPorAvaliacao(order, genero, ano);
        exibirFilmes(resultados);
    }

    private static void listarSeries(Scanner scanner, DiarioCultural dc) {
        Integer order = null;
        while (true) {
            System.out.println("\nDigite o tipo de ordenação (pressione ENTER para pular):");
            System.out.print("Ordenação: \n[1] Maior avaliação\n[2] Menor Avaliação\n> ");
            String entrada = scanner.nextLine().trim();

            try {
                int opcao = Integer.parseInt(entrada);
                if (opcao == 1 || opcao == 2) {
                    order = opcao;
                    break;
                } else {
                    System.out.println("Opção inválida. Digite apenas 1, 2 ou pressione ENTER.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite apenas 1, 2 ou pressione ENTER.");
            }
        }

        String genero = getInput(scanner, "Gênero: ");
        Integer ano = getIntegerInput(scanner, "Ano de lançamento: ");

        List<Serie> resultados = dc.ordenarSeriesPorAvaliacao(order, genero, ano);
        exibirSeries(resultados);
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

    private static void exibirLivros(List<Livro> livros) {
        for (Livro livro : livros) {
            System.out.println("[Livro] \"" + livro.getTitulo() + "\" (" + livro.getAno_lancamento() + ")");
            System.out.println("  Autor: " + livro.getAutor());
            System.out.println("  Gênero: " + livro.getGenero());
            System.out.println("  ISBN: " + livro.getISBN());
            System.out.println("  Avaliação: " + livro.getMediaAvaliacoes());
            System.out.println();
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

    private static void exibirFilmes(List<Filme> filmes) {
        for (Filme filme : filmes) {
            System.out.println("[Filme] \"" + filme.getTitulo() + "\" (" + filme.getAno_lancamento() + ")");
            System.out.println("  Diretor: " + filme.getDirecao());
            System.out.println("  Gênero: " + filme.getGenero());
            System.out.println("  Avaliação: " + filme.getMediaAvaliacoes());
            System.out.println();
        }
    }

    private static void exibirResultadosSeries(List<Serie> series, String criterio) {
        System.out.println("\nResultados encontrados para \"" + (criterio != null ? criterio : "Pesquisa") + "\":\n");
        if (series.isEmpty()) {
            System.out.println("Nenhuma série correspondente foi encontrada.");
        } else {
            for (Serie serie : series) {
                System.out.println("[Série] \"" + serie.getTitulo() + "\" (" + serie.getAnoLancamento() + ")");
                System.out.println("  Gênero: " + serie.getGenero());
                System.out.println();
            }
        }
    }

    private static void exibirSeries(List<Serie> series) {
        for (Serie serie : series) {
            System.out.println("[Série] \"" + serie.getTitulo() + "\" (" + serie.getAnoLancamento() + ")");
            System.out.println("  Gênero: " + serie.getGenero());
            System.out.println("  Avaliação: " + serie.getMediaAvaliacoes());
            System.out.println();
        }
    }
}




