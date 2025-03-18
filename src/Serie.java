public class Serie {
    private String titulo;
    private String genero;
    private int ano_lancamento;
    private int ano_encerramento;
    private String elenco;
    private String titulo_original;
    private String onde_assistir;
    private Temporada[] temporadas;

    public Serie(String titulo, String genero, int ano_lancamento, int ano_encerramento, String elenco, String titulo_original, String onde_assistir) {
        this.titulo = titulo;
        this.genero = genero;
        this.ano_lancamento = ano_lancamento;
        this.ano_encerramento = ano_encerramento;
        this.elenco = elenco;
        this.titulo_original = titulo_original;
        this.onde_assistir = onde_assistir;

    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Temporada[] getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(Temporada[] temporadas) {
        this.temporadas = temporadas;
    }

    public String getOnde_assistir() {
        return onde_assistir;
    }

    public void setOnde_assistir(String onde_assistir) {
        this.onde_assistir = onde_assistir;
    }

    public String getTitulo_original() {
        return titulo_original;
    }

    public void setTitulo_original(String titulo_original) {
        this.titulo_original = titulo_original;
    }

    public String getElenco() {
        return elenco;
    }

    public void setElenco(String elenco) {
        this.elenco = elenco;
    }

    public int getAno_encerramento() {
        return ano_encerramento;
    }

    public void setAno_encerramento(int ano_encerramento) {
        this.ano_encerramento = ano_encerramento;
    }

    public int getAno_lancamento() {
        return ano_lancamento;
    }

    public void setAno_lancamento(int ano_lancamento) {
        this.ano_lancamento = ano_lancamento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }




}
