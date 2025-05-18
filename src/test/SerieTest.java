package test;

import model.Serie;
import model.Temporada;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

class SerieTest {
    @Test
    public void testCriacaoSerie() {
        Serie serie = new Serie("The Good Doctor", "Drama", 2017, "Freddie Highmore", "ABC");

        Assertions.assertEquals("The Good Doctor", serie.getTitulo());
        Assertions.assertEquals("Drama", serie.getGenero());
        Assertions.assertEquals(2017, serie.getAnoLancamento());
        Assertions.assertEquals("Freddie Highmore", serie.getElenco());
        Assertions.assertEquals("ABC", serie.getOnde_assistir());
        Assertions.assertTrue(serie.getTemporadas().isEmpty());
    }

    @Test
    public void testAdicionarTemporada() {
        Serie serie = new Serie("The Good Doctor", "Drama", 2017, "Freddie Highmore", "ABC");
        Temporada t1 = new Temporada(1, 2017, 8);
        serie.adicionarTemporada(t1);
        List<Temporada> temporadas = serie.getTemporadas();

        Assertions.assertEquals(1, temporadas.size());
        Assertions.assertTrue(temporadas.contains(t1));
    }


    @Test
    void getMediaAvaliacoes() {

        Serie s1 = new Serie("Stranger Things", "Ficção", 2016, "Sadie Sink",
                "Netflix");

        Temporada t1 = new Temporada(1, 1998, 26);

        s1.adicionarTemporada(t1);

        t1.avaliarTemporada(4, new Date(1976, 03, 10), "temporada chata");

        t1.avaliarTemporada(2, new Date(1976, 03, 10), "temporada chata");

        t1.avaliarTemporada(5, new Date(1976, 03, 10), "temporada chata");

        Assertions.assertEquals(3.67, s1.getMediaAvaliacoes());
    }
}