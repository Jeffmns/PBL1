package test;

import model.Serie;
import model.Temporada;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

class SerieTest {

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