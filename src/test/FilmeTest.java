package test;

import model.Filme;
import model.Review;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Date;

class FilmeTest {

    @Test
    void getMediaAvaliacoes() {
        Filme f1 = new Filme("Interestelar", "Ficção Científica", 2013,
                150, "Cristopher Nolan", "xxx", "Netflix");

        Review review1 = new Review(1, new Date(2000, 05, 19),
                "livro excelente, muito suspense");

        Review review2 = new Review(3, new Date(2000, 05, 19),
                "livro excelente, muito suspense");

        Review review3 = new Review(5, new Date(2000, 05, 19),
                "livro excelente, muito suspense");


        f1.adicionarAvaliacao(review1);
        f1.adicionarAvaliacao(review2);
        f1.adicionarAvaliacao(review3);

        Assertions.assertEquals(3, f1.getMediaAvaliacoes());
    }


    @Test
    public void testCriacaoFilme() {
        Filme filme = new Filme("Inception", "Ficção", 2010, 148, "Nolan", "DiCaprio", "Netflix");

        Assertions.assertEquals("Inception", filme.getTitulo());
        Assertions.assertEquals("Ficção", filme.getGenero());
        Assertions.assertEquals(2010, filme.getAno_lancamento());
        Assertions.assertEquals(148, filme.getDuracao());
        Assertions.assertEquals("Nolan", filme.getDirecao());
        Assertions.assertEquals("DiCaprio", filme.getElenco());
        Assertions.assertEquals("Netflix", filme.getOnde_assistir());
        Assertions.assertEquals(0, filme.getAvaliacoes().size());
    }

    @Test
    public void testAdicionarAvaliacaoMarcaComoAssistido() {
        Filme filme = new Filme("Inception", "Ficção", 2010, 148, "Nolan", "DiCaprio", "Netflix");
        Review review = new Review(5, new Date(), "Excelente!");

        filme.adicionarAvaliacao(review);

        Assertions.assertTrue(filme.isAssistido());
        Assertions.assertEquals(1, filme.getAvaliacoes().size());
        Assertions.assertEquals(5.0, filme.getMediaAvaliacoes());
    }

    @Test
    public void testEqualsEHashCode() {
        Filme f1 = new Filme("Matrix", "Ficção", 1999, 136, "Wachowski", "Keanu Reeves", "HBO");
        Filme f2 = new Filme("Matrix", "Ficção", 1999, 136, "Wachowski", "Keanu Reeves", "HBO");

        Assertions.assertEquals(f1, f2);
        Assertions.assertEquals(f1.hashCode(), f2.hashCode());
    }
}



