import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class LivroTest {

    @Test
    void getMediaAvaliacoes() {
        Livro l1 = new Livro("Christine", "Stephen King", "Objetiva",
                "978-85-60280-87-2", 2013, "Terror", true, true);

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
}