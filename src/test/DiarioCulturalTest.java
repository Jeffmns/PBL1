package test;

import controller.DiarioCultural;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

class DiarioCulturalTest {
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUpStreams() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
     void buscarLivros() {
        DiarioCultural dc = new DiarioCultural();
        Livro l1 = new Livro("Christine", "Stephen King", "Objetiva",
                "978-85-60280-87-2", 2013, "Terror", true);
        Livro l2 = new Livro("Joyland", "Stephen King", "Objetiva",
                "978-85-60280-87-2", 2012, "Terror", true);
        Livro l3 = new Livro("A culpa é das estrelas", "John Green", "Objetiva",
                "978-85-60280-87-2", 2013, "Romance", true);

        dc.cadastrarLivro(l1);
        dc.cadastrarLivro(l2);
        dc.cadastrarLivro(l3);

        List<Livro> esperado = Arrays.asList(l1, l3);
        Assertions.assertEquals(esperado, dc.buscarLivros(null, null, null, null, 2013, null));
    }
    @Test
    void listarlivros(){
        DiarioCultural dc = new DiarioCultural();
        Livro l1 = new Livro("Christine", "Stephen King", "Objetiva",
                "978-85-60280-87-2", 2013, "Terror", true);
        Livro l2 = new Livro("Joyland", "Stephen King", "Objetiva",
                "978-85-60280-87-2", 2012, "Terror", true);
        Livro l3 = new Livro("A culpa é das estrelas", "John Green", "Objetiva",
                "978-85-60280-87-2", 2013, "Romance", true);

        dc.cadastrarLivro(l1);
        dc.cadastrarLivro(l2);
        dc.cadastrarLivro(l3);

        List<Livro> esperado = Arrays.asList(l1, l2, l3);
        Assertions.assertEquals(esperado, dc.listarlivros());
    }


    @Test
    void buscarFilmes() {
        DiarioCultural dc = new DiarioCultural();

        Filme f1 = new Filme("Interestelar", "Ficção Científica", 2013,
                150, "Cristopher Nolan", "xxx", "Netflix");
        Filme f2 = new Filme("Kill Bill", "Ação", 2003,
                111, "Quentin Tarantino", "xxx", "Netflix");
        Filme f3 = new Filme("Zathura", "Ficção Científica", 2005,
                101, "Jon Favreau", "xxx", "Netflix");

        dc.cadastrarFilme(f1);
        dc.cadastrarFilme(f2);
        dc.cadastrarFilme(f3);

        List<Filme> esperado = Arrays.asList(f1, f3);
        Assertions.assertEquals(esperado, dc.buscarFilmes(null, null, null, "Ficção Científica", null));
    }

    @Test
    void listarfilmes() {

        DiarioCultural dc = new DiarioCultural();

        Filme f1 = new Filme("Interestelar", "Ficção Científica", 2013,
                150, "Cristopher Nolan", "xxx", "Netflix");
        Filme f2 = new Filme("Kill Bill", "Ação", 2003,
                111, "Quentin Tarantino", "xxx", "Netflix");
        Filme f3 = new Filme("Zathura", "Ficção Científica", 2005,
                101, "Jon Favreau", "xxx", "Netflix");

        dc.cadastrarFilme(f1);
        dc.cadastrarFilme(f2);
        dc.cadastrarFilme(f3);

        List<Filme> esperado = Arrays.asList(f1, f2, f3);
        Assertions.assertEquals(esperado, dc.listarfilmes());
    }

    @Test
    void buscarSeries() {
        DiarioCultural dc = new DiarioCultural();
        Serie s1 = new Serie("Stranger Things", "Ficção", 2016, "Sadie Sink",
                "Netflix");
        Serie s2 = new Serie("Thats 70's Show", "Comédia", 1998, "Mila Kunis, Asthon Kutcher",
                "Netflix");
        Serie s3 = new Serie("Maniac", "Ficção", 2018, "Emma stone, Jonah Hill",
                "Netflix");

        dc.cadastrarSerie(s1);
        dc.cadastrarSerie(s2);
        dc.cadastrarSerie(s3);

        List<Serie> esperado = Arrays.asList(s1, s3);
        Assertions.assertEquals(esperado, dc.buscarSeries(null, "Ficção", null, null));
    }

    @Test
    void ordenarLivrosPorAvaliacao() {
        DiarioCultural dc = new DiarioCultural();
        Livro l1 = new Livro("Christine", "Stephen King", "Objetiva",
                "978-85-60280-87-2", 2013, "Terror", true);
        Livro l2 = new Livro("Joyland", "Stephen King", "Objetiva",
                "978-85-60280-87-2", 2012, "Terror", true);
        Livro l3 = new Livro("A culpa é das estrelas", "John Green", "Objetiva",
                "978-85-60280-87-2", 2013, "Romance", true);


        dc.cadastrarLivro(l1);
        dc.cadastrarLivro(l2);
        dc.cadastrarLivro(l3);

        Review review1 = new Review(5, new Date(2000, 05, 19),
                "livro excelente, muito suspense");

        Review review2 = new Review(3, new Date(2000, 05, 19),
                "livro excelente, muito suspense");

        Review review3 = new Review(4, new Date(2000, 05, 19),
                "livro excelente, muito suspense");

        l1.adicionarAvaliacao(review1);
        l2.adicionarAvaliacao(review2);
        l3.adicionarAvaliacao(review3);

        List<Livro> esperado = Arrays.asList(l2, l1);
        Assertions.assertEquals(esperado, dc.ordenarLivrosPorAvaliacao(2, "Terror", null));
    }

    @Test
    void ordenarFilmesPorAvaliacao() {
        DiarioCultural dc = new DiarioCultural();
        Filme f1 = new Filme("Interestelar", "Ficção Científica", 2013,
                150, "Cristopher Nolan", "xxx", "Netflix");
        Filme f2 = new Filme("Kill Bill", "Ação", 2003,
                111, "Quentin Tarantino", "xxx", "Netflix");
        Filme f3 = new Filme("Zathura", "Ficção Científica", 2003,
                101, "Jon Favreau", "xxx", "Netflix");

        dc.cadastrarFilme(f1);
        dc.cadastrarFilme(f2);
        dc.cadastrarFilme(f3);

        Review review1 = new Review(5, new Date(2000, 05, 19),
                "livro excelente, muito suspense");

        Review review2 = new Review(3, new Date(2000, 05, 19),
                "livro excelente, muito suspense");

        Review review3 = new Review(4, new Date(2000, 05, 19),
                "livro excelente, muito suspense");

        f1.adicionarAvaliacao(review1);
        f1.adicionarAvaliacao(review2);
        f1.adicionarAvaliacao(review3);

        List<Filme> esperado = Arrays.asList(f2, f3);
        Assertions.assertEquals(esperado, dc.ordenarFilmesPorAvaliacao(1, null, 2003));

    }

    @Test
    void ordenarSeriesPorAvaliacao() {
        DiarioCultural dc = new DiarioCultural();

        Serie s1 = new Serie("Stranger Things", "Ficção", 2016, "Sadie Sink",
                "Netflix");
        Serie s2 = new Serie("Thats 70's Show", "Comédia", 1998, "Mila Kunis, Asthon Kutcher",
                "Netflix");
        Serie s3 = new Serie("Maniac", "Ficção", 2018, "Emma stone, Jonah Hill",
                "Netflix");

        Temporada t1 = new Temporada(1, 1998, 26);

        s1.adicionarTemporada(t1);

        dc.cadastrarSerie(s1);
        dc.cadastrarSerie(s2);
        dc.cadastrarSerie(s3);

        t1.avaliarTemporada(4, new Date(1976, 03, 10), "temporada chata");

        t1.avaliarTemporada(2, new Date(1976, 03, 10), "temporada chata");

        t1.avaliarTemporada(5, new Date(1976, 03, 10), "temporada chata");

        List<Serie> esperado = Arrays.asList(s1, s3);
        Assertions.assertEquals(esperado, dc.ordenarSeriesPorAvaliacao(1, "Ficção", null));
    }

    @Test
    void cadastrarFilme() {
        DiarioCultural dc = new DiarioCultural();
        Filme f1 = new Filme("Kill Bill", "Ação", 2003,
                111, "Quentin Tarantino", "xxx", "Netflix");

        dc.cadastrarFilme(f1);

        String output = outputStream.toString().trim();
        Assertions.assertTrue(output.contains("Filme cadastrado com sucesso: Kill Bill"), "Mensagem de erro esperada não foi impressa");
    }

    @Test
    void cadastrarFilmeDuplicado() {
        DiarioCultural dc = new DiarioCultural();
        Filme f1 = new Filme("Interestelar", "Ficção Científica", 2013,
                150, "Cristopher Nolan", "xxx", "Netflix");
        Filme f2 = new Filme("Kill Bill", "Ação", 2003,
                111, "Quentin Tarantino", "xxx", "Netflix");

        dc.cadastrarFilme(f1);
        dc.cadastrarFilme(f2);

        Filme f3 = new Filme("Interestelar", "Ficção Científica", 2013,
                150, "Cristopher Nolan", "xxx", "Netflix");

        dc.cadastrarFilme(f3);
        String output = outputStream.toString().trim();
        Assertions.assertTrue(output.contains("Filme já cadastrado."), "Mensagem de erro esperada não foi impressa");
    }

    @Test
    void cadastrarLivro() {
        DiarioCultural dc = new DiarioCultural();
        Livro l1 = new Livro("Christine", "Stephen King", "Objetiva",
                "978-85-60280-87-2", 2013, "Terror", true);

        dc.cadastrarLivro(l1);

        String output = outputStream.toString().trim();
        Assertions.assertTrue(output.contains("Livro cadastrado com sucesso: Christine"), "Mensagem de erro esperada não foi impressa");
    }

    @Test
    void cadastrarLivroDuplicado() {
        DiarioCultural dc = new DiarioCultural();
        Livro l1 = new Livro("Christine", "Stephen King", "Objetiva",
                "978-85-60280-87-2", 2013, "Terror", true);

        dc.cadastrarLivro(l1);

        Livro l2 = new Livro("Christine", "Stephen King", "Objetiva",
                "978-85-60280-87-2", 2013, "Terror", true);

        dc.cadastrarLivro(l2);
        String output = outputStream.toString().trim();
        Assertions.assertTrue(output.contains("Livro já cadastrado."), "Mensagem de erro esperada não foi impressa");
    }

    @Test
    void cadastrarSerie() {
        DiarioCultural dc = new DiarioCultural();
        Serie s1 = new Serie("Stranger Things", "Ficção", 2016, "Sadie Sink",
                "Netflix");
        dc.cadastrarSerie(s1);

        String output = outputStream.toString().trim();
        Assertions.assertTrue(output.contains("Série cadastrada com sucesso: Stranger Things"), "Mensagem de erro esperada não foi impressa");
    }

    @Test
    void cadastrarSerieDuplicada() {
        DiarioCultural dc = new DiarioCultural();
        Serie s1 = new Serie("Stranger Things", "Ficção", 2016, "Sadie Sink",
                "Netflix");
        dc.cadastrarSerie(s1);

        Serie s2 = new Serie("Stranger Things", "Ficção", 2016, "Sadie Sink",
                "Netflix");
        dc.cadastrarSerie(s2);


        String output = outputStream.toString().trim();
        Assertions.assertTrue(output.contains("Série já cadastrada."), "Mensagem de erro esperada não foi impressa");
    }

    @Test
    void removerLivro(){
        // 1. Arrange (Preparar)
        DiarioCultural dc = new DiarioCultural();
        Livro livroParaRemover = new Livro("Christine", "Stephen King", "Objetiva", "978-85-60280-87-2", 2013, "Terror", true);
        dc.cadastrarLivro(livroParaRemover);

        // Garante que o livro foi adicionado antes de tentar remover
        Assertions.assertEquals(1, dc.getLivros().size(), "O livro deveria ter sido adicionado antes do teste de remoção.");

        // 2. Act (Agir)
        // Chama o NOVO método removerFilme, passando o objeto Livro diretamente.
        boolean foiRemovido = dc.removerLivro(livroParaRemover);

        // 3. Assert (Verificar)
        // Verifica se o método retornou 'true', indicando que a remoção foi bem-sucedida.
        Assertions.assertTrue(foiRemovido, "O método removerFilme deveria retornar true.");

        // A verificação mais importante: confirma que a lista de livros agora está vazia.
        Assertions.assertEquals(0, dc.getLivros().size(), "A lista de livros deveria estar vazia após a remoção.");

    }

    @Test
    void removerLivroNaoEncontrado(){
        // 1. Arrange
        DiarioCultural dc = new DiarioCultural();
        Livro livroExistente = new Livro("Christine", "Stephen King", "Objetiva", "978-85-60280-87-2", 2013, "Terror", true);
        Livro livroNaoExistente = new Livro("Outro Livro", "Outro Autor", "Outra Editora", "123-45", 2020, "Ficção", false);

        dc.cadastrarLivro(livroExistente);
        Assertions.assertEquals(1, dc.getLivros().size(), "A lista deveria ter um livro no início do teste.");

        // 2. Act
        // Tenta remover um livro que NUNCA foi adicionado à lista.
        boolean foiRemovido = dc.removerLivro(livroNaoExistente);

        // 3. Assert
        // Verifica se o método retornou 'false', como esperado.
        Assertions.assertFalse(foiRemovido, "O método removerFilme deveria retornar false para um livro não existente.");

        // Verifica se a lista ainda contém o livro original (não foi alterada).
        Assertions.assertEquals(1, dc.getLivros().size(), "A lista não deveria ter sido alterada.");
        Assertions.assertTrue(dc.getLivros().contains(livroExistente), "O livro original ainda deveria estar na lista.");

    }
    @Test
    void removerFilme(){
        // 1. Arrange (Preparar)
        DiarioCultural dc = new DiarioCultural();
        Filme filmeParaRemover = new Filme("Interestelar", "Ficção Científica", 2013,
                150, "Cristopher Nolan", "xxx", "Netflix");
        dc.cadastrarFilme(filmeParaRemover);

        // Garante que o livro foi adicionado antes de tentar remover
        Assertions.assertEquals(1, dc.getFilmes().size(), "O livro deveria ter sido adicionado antes do teste de remoção.");

        // 2. Act (Agir)
        // Chama o NOVO método removerFilme, passando o objeto Livro diretamente.
        boolean foiRemovido = dc.removerFilme(filmeParaRemover);

        // 3. Assert (Verificar)
        // Verifica se o método retornou 'true', indicando que a remoção foi bem-sucedida.
        Assertions.assertTrue(foiRemovido, "O método removerFilme deveria retornar true.");

        // A verificação mais importante: confirma que a lista de livros agora está vazia.
        Assertions.assertEquals(0, dc.getFilmes().size(), "A lista de filmes deveria estar vazia após a remoção.");
    }

    @Test
    void removerFilmeNaoEncontrado(){
        // 1. Arrange
        DiarioCultural dc = new DiarioCultural();
        Filme filmeExistente = new Filme("Interestelar", "Ficção Científica", 2013,
                150, "Cristopher Nolan", "xxx", "Netflix");
        Filme filmeNaoExistente = new Filme("Outro Filme", "Outro Gênero", 2010, 123, "Outro Diretor", "Outro Elenco", "Outro Lugar");

        dc.cadastrarFilme(filmeExistente);
        Assertions.assertEquals(1, dc.getFilmes().size(), "A lista deveria ter um filme no início do teste.");

        // 2. Act
        // Tenta remover um livro que NUNCA foi adicionado à lista.
        boolean foiRemovido = dc.removerFilme(filmeNaoExistente);

        // 3. Assert
        // Verifica se o método retornou 'false', como esperado.
        Assertions.assertFalse(foiRemovido, "O método removerFilme deveria retornar false para um filme não existente.");

        // Verifica se a lista ainda contém o livro original (não foi alterada).
        Assertions.assertEquals(1, dc.getFilmes().size(), "A lista não deveria ter sido alterada.");
        Assertions.assertTrue(dc.getFilmes().contains(filmeExistente), "O filme original ainda deveria estar na lista.");
    }


    @Test
    void removerSerie(){
        // 1. Arrange (Preparar)
        DiarioCultural dc = new DiarioCultural();
        Serie serieParaRemover = new Serie("Stranger Things", "Ficção", 2016, "Sadie Sink",
                "Netflix");
        dc.cadastrarSerie(serieParaRemover);

        // Garante que o livro foi adicionado antes de tentar remover
        Assertions.assertEquals(1, dc.getSeries().size(), "A série deveria ter sido adicionado antes do teste de remoção.");

        // 2. Act (Agir)
        // Chama o NOVO método removerFilme, passando o objeto Livro diretamente.
        boolean foiRemovido = dc.removerSerie(serieParaRemover);

        // 3. Assert (Verificar)
        // Verifica se o método retornou 'true', indicando que a remoção foi bem-sucedida.
        Assertions.assertTrue(foiRemovido, "O método removerSerie deveria retornar true.");

        // A verificação mais importante: confirma que a lista de livros agora está vazia.
        Assertions.assertEquals(0, dc.getSeries().size(), "A lista de séries deveria estar vazia após a remoção.");

    }

    @Test
    void removerSerieNaoEncontrada(){
        // 1. Arrange
        DiarioCultural dc = new DiarioCultural();
        Serie serieExistente = new Serie("Stranger Things", "Ficção", 2016, "Sadie Sink",
                "Netflix");
        Serie serieNaoExistente = new Serie("Outra", "Outro", 2010, "Outro", "Outro");

        dc.cadastrarSerie(serieExistente);
        Assertions.assertEquals(1, dc.getSeries().size(), "A lista deveria ter uma série no início do teste.");

        // 2. Act
        // Tenta remover um livro que NUNCA foi adicionado à lista.
        boolean foiRemovido = dc.removerSerie(serieNaoExistente);

        // 3. Assert
        // Verifica se o método retornou 'false', como esperado.
        Assertions.assertFalse(foiRemovido, "O método removerSerie deveria retornar false para um filme não existente.");

        // Verifica se a lista ainda contém o livro original (não foi alterada).
        Assertions.assertEquals(1, dc.getSeries().size(), "A lista não deveria ter sido alterada.");
        Assertions.assertTrue(dc.getSeries().contains(serieExistente), "A série original ainda deveria estar na lista.");

    }
}