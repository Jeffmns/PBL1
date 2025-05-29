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
        Assertions.assertEquals(esperado, dc.buscarLivros(null, null, null, 2013, null));
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
        Assertions.assertEquals(esperado, dc.buscarSeries(null, "Ficção", null));
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
        DiarioCultural dc = new DiarioCultural();
        Livro l1 = new Livro("Christine", "Stephen King", "Objetiva",
                "978-85-60280-87-2", 2013, "Terror", true);

        dc.cadastrarLivro(l1);
        dc.removerLivro("Christine", new Scanner(System.in));
        String output = outputStream.toString().trim();
        Assertions.assertTrue(output.contains("Livro removido com sucesso!"), "Mensagem de erro esperada não foi impressa");

    }

    @Test
    void removerLivroNaoEncontrado(){
        DiarioCultural dc = new DiarioCultural();
        Livro l1 = new Livro("Christine", "Stephen King", "Objetiva",
                "978-85-60280-87-2", 2013, "Terror", true);

        dc.cadastrarLivro(l1);
        dc.removerLivro("Joyland", new Scanner(System.in));
        String output = outputStream.toString().trim();
        Assertions.assertTrue(output.contains("Nenhum livro encontrado com esse título."), "Mensagem de erro esperada não foi impressa");

    }
    @Test
    void removerFilme(){
        DiarioCultural dc = new DiarioCultural();

        Filme f1 = new Filme("Interestelar", "Ficção Científica", 2013,
                150, "Cristopher Nolan", "xxx", "Netflix");

        dc.cadastrarFilme(f1);
        dc.removerFilme("Interestelar", new Scanner(System.in));
        String output = outputStream.toString().trim();
        Assertions.assertTrue(output.contains("Filme removido com sucesso!"), "Mensagem de erro esperada não foi impressa");

    }

    @Test
    void removerFilmeNaoEncontrado(){
        DiarioCultural dc = new DiarioCultural();

        Filme f1 = new Filme("Interestelar", "Ficção Científica", 2013,
                150, "Cristopher Nolan", "xxx", "Netflix");

        dc.cadastrarFilme(f1);
        dc.removerFilme("Zathura", new Scanner(System.in));
        String output = outputStream.toString().trim();
        Assertions.assertTrue(output.contains("Nenhum filme encontrado com esse título."), "Mensagem de erro esperada não foi impressa");

    }


    @Test
    void removerSerie(){
        DiarioCultural dc = new DiarioCultural();
        Serie s1 = new Serie("Stranger Things", "Ficção", 2016, "Sadie Sink",
                "Netflix");
        dc.cadastrarSerie(s1);
        dc.removerSerie("Stranger Things", new Scanner(System.in));
        String output = outputStream.toString().trim();
        Assertions.assertTrue(output.contains("Série removida com sucesso!"), "Mensagem de erro esperada não foi impressa");

    }

    @Test
    void removerSerieNaoEncontrada(){
        DiarioCultural dc = new DiarioCultural();
        Serie s1 = new Serie("Stranger Things", "Ficção", 2016, "Sadie Sink",
                "Netflix");
        dc.cadastrarSerie(s1);
        dc.removerSerie("Dark", new Scanner(System.in));
        String output = outputStream.toString().trim();
        Assertions.assertTrue(output.contains("Nenhuma série encontrada com esse título."), "Mensagem de erro esperada não foi impressa");

    }
}