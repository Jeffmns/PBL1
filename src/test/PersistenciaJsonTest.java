package test;
import controller.DiarioCultural;
import model.*;
import persistence.PersistenciaJson;
import org.junit.jupiter.api.*;
import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PersistenciaJsonTest {

    private static final String CAMINHO_TESTE = "src/test/dados_test.json";


    @Test
    public void testSalvarECarregarLivro() {
        DiarioCultural dc = new DiarioCultural();
        Livro livro = new Livro("Dom Casmurro", "Machado de Assis", "Editora A", "123", 1899, "Romance", true);
        dc.cadastrarLivro(livro);

        PersistenciaJson.salvar(dc, CAMINHO_TESTE);
        DiarioCultural carregado = PersistenciaJson.carregar(CAMINHO_TESTE);

        assertEquals(1, carregado.getLivros().size());
        assertEquals("Dom Casmurro", carregado.getLivros().get(0).getTitulo());
    }

    @Test
    public void testSalvarECarregarFilme() {
        DiarioCultural dc = new DiarioCultural();
        Filme filme = new Filme("Interestelar", "Ficcao", 2014, 169, "Christopher Nolan", "Elenco A", "Netflix");
        dc.cadastrarFilme(filme);

        PersistenciaJson.salvar(dc, CAMINHO_TESTE);
        DiarioCultural carregado = PersistenciaJson.carregar(CAMINHO_TESTE);

        assertEquals(1, carregado.getFilmes().size());
        assertEquals("Interestelar", carregado.getFilmes().get(0).getTitulo());
    }

    @Test
    public void testSalvarECarregarSerieComTemporada() {
        DiarioCultural dc = new DiarioCultural();
        Serie serie = new Serie("Dark", "Ficcao", 2017, "Elenco B", "Netflix");
        Temporada t1 = new Temporada(1, 2017, 10);
        t1.avaliarTemporada(5, new java.util.Date(), "Muito boa");
        serie.adicionarTemporada(t1);
        dc.cadastrarSerie(serie);

        PersistenciaJson.salvar(dc, CAMINHO_TESTE);
        DiarioCultural carregado = PersistenciaJson.carregar(CAMINHO_TESTE);

        assertEquals(1, carregado.getSeries().size());
        Serie serieCarregada = carregado.getSeries().get(0);
        assertEquals("Dark", serieCarregada.getTitulo());
        assertEquals(1, serieCarregada.getTemporadas().size());
        assertTrue(serieCarregada.getTemporadas().get(0).getAssistido());
    }

//    @AfterEach
//    public void limparArquivo() {
//        File arquivo = new File(CAMINHO_TESTE);
//        if (arquivo.exists()) {
//            arquivo.delete();
//        }
//    }
}
