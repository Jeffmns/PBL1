import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Carrega o FXML (ex: cadastro.fxml)
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/TelaInicial.fxml")));

        // Configura e exibe a janela
        primaryStage.setResizable(false);
        primaryStage.setTitle("Diário Cultural");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Inicia o JavaFX
    }
}
