import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TemporadaTest {

    @Test
    void getMediaAvaliacoes() {

        Temporada t1 = new Temporada(1, 1998, 26);

        t1.avaliarTemporada(4, new Date(1976, 03, 10), "temporada chata");

        t1.avaliarTemporada(2, new Date(1976, 03, 10), "temporada chata");

        t1.avaliarTemporada(5, new Date(1976, 03, 10), "temporada chata");

        Assertions.assertEquals(3.67, t1.getMediaAvaliacoes());
    }
}