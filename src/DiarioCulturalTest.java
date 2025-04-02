import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class DiarioCulturalTest {

    @Test
     void buscarLivrosPorAno() {
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

        List<Livro> esperado = Arrays.asList(l1, l3);
        Assertions.assertEquals(esperado, dc.buscarLivrosPorAno(2013));
    }
}