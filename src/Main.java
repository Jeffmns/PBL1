import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
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

        Filme f1 = new Filme("Interestelar", "Ficção Científica", 2013,
                150, "Cristopher Nolan", "xxx", "Anne Hateway",
                "Interestelar", "Netflix", false);

        Serie s1 = new Serie("Stranger Things", "Ficção",2016, 2025,
                "Sadie Sink", "Stranger Things", "Netflix");

        dc.cadastrarFilme(f1);
        dc.cadastrarSerie(s1);

        List<Livro> resultadoBusca = dc.buscarLivros(2013);
    }
}