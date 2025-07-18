package test;

import model.Livro;
import model.Review;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class LivroTest {

     @Test
    public void testCriacaoLivro() {
        Livro livro = new Livro("1984", "George Orwell", "Companhia das Letras",
                "978-85-359-0277-2", 1949, "Distopia", true);

        assertEquals("1984", livro.getTitulo());
        assertEquals("George Orwell", livro.getAutor());
        assertEquals("Companhia das Letras", livro.getEditora());
        assertEquals("978-85-359-0277-2", livro.getISBN());
        assertEquals(1949, livro.getAno_lancamento());
        assertEquals("Distopia", livro.getGenero());
        assertFalse(livro.isLido());
    }

    @Test
    public void testAdicionarAvaliacao() {
        Livro livro = new Livro("1984", "George Orwell", "Companhia das Letras",
                "978-85-359-0277-2", 1949, "Distopia", true);
        Date dataAvaliacao = new Date(); // Data e hora atuais
        Review review = new Review(5, dataAvaliacao, "Leitor");
        livro.adicionarAvaliacao(review);

        assertEquals(1, livro.getAvaliacoes().size());
        assertEquals(review, livro.getAvaliacoes().getFirst());
        assertTrue(livro.isLido());
    }




    @Test
    void getMediaAvaliacoes() {
        Livro l1 = new Livro("Christine", "Stephen King", "Objetiva",
                "978-85-60280-87-2", 2013, "Terror", true);

        Review review1 = new Review(5, new Date(2000, 05, 19),
                "livro excelente, muito suspense");

        Review review2 = new Review(3, new Date(2000, 05, 19),
                "livro excelente, muito suspense");

        Review review3 = new Review(4, new Date(2000, 05, 19),
                "livro excelente, muito suspense");
        l1.adicionarAvaliacao(review1);
        l1.adicionarAvaliacao(review2);
        l1.adicionarAvaliacao(review3);

        Assertions.assertEquals(4, l1.getMediaAvaliacoes());

    }

    @Test
    public void testMediaAvaliacoesSemAvaliacoes() {
        Livro livro = new Livro("1984", "George Orwell", "Companhia das Letras",
                "978-85-359-0277-2", 1949, "Distopia", true);

        assertEquals(0.0, livro.getMediaAvaliacoes());
    }

    @Test
    public void testSettersELidos() {
        Livro livro = new Livro("Antigo título", "Autor", "Editora",
                "ISBN", 2000, "Gênero", true);

        livro.setTitulo("Novo título");
        livro.setAutor("Novo Autor");
        livro.setEditora("Nova Editora");
        livro.setISBN("Novo ISBN");
        livro.setAno_lancamento(2024);
        livro.setGenero("Novo Gênero");
        livro.setLido(true);

        assertEquals("Novo título", livro.getTitulo());
        assertEquals("Novo Autor", livro.getAutor());
        assertEquals("Nova Editora", livro.getEditora());
        assertEquals("Novo ISBN", livro.getISBN());
        assertEquals(2024, livro.getAno_lancamento());
        assertEquals("Novo Gênero", livro.getGenero());
        assertTrue(livro.isLido());
    }


}
