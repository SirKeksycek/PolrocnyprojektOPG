package vizual;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class InventarController {

    @FXML
    private TableView<?> inventarTable;

    @FXML
    private TableColumn<?, ?> colNazov;

    @FXML
    private TableColumn<?, ?> colMnozstvo;

    @FXML
    private TableColumn<?, ?> colTyp;

    @FXML
    private Button exit;

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }
}