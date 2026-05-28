package vizual;

import adventurka.Engine;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {

    public static Engine engine = new Engine();

    @Override
    public void start(Stage primaryStage) throws Exception {
        engine.load("src/assets/hra12.json");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("primary.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Hlavné okno");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        // engine.play() NEVOLAJ tu – GUI preberá kontrolu
    }
}