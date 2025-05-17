import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

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

}