package vizual;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private Button inv;

    @FXML
    private void openInventar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("inventar.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Inventár");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}