package vizual;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;
import java.util.Map;

public class Controller {

    @FXML private Button Lava;
    @FXML private Button Prava;
    @FXML private Button Rovno;
    @FXML private Button Nazad;
    @FXML private Button inv;
    @FXML private VBox itemyVBox;

    private final Button[] sipky = new Button[4];

    @FXML
    public void initialize() {
        sipky[0] = Rovno;
        sipky[1] = Prava;
        sipky[2] = Nazad;
        sipky[3] = Lava;
        aktualizuj();
    }

    private void aktualizuj() {
        aktualizujSipky();
        aktualizujItemy();
    }

    private void aktualizujSipky() {
        List<String> exits = Application.engine.getExitList();
        for (Button b : sipky) {
            b.setVisible(false);
            b.setText("");
        }
        for (int i = 0; i < exits.size() && i < sipky.length; i++) {
            sipky[i].setVisible(true);
            sipky[i].setText(exits.get(i));
        }
    }

    private void aktualizujItemy() {
        itemyVBox.getChildren().clear();
        Map<String, String> items = Application.engine.getRoomItems();
        for (String nazov : items.keySet()) {
            Button btn = new Button(nazov);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setOnAction(e -> {
                Application.engine.takeGui(nazov);
                aktualizuj();
            });
            itemyVBox.getChildren().add(btn);
        }
    }

    @FXML private void goRovno() { pohyb(Rovno.getText()); }
    @FXML private void goPrava() { pohyb(Prava.getText()); }
    @FXML private void goNazad() { pohyb(Nazad.getText()); }
    @FXML private void goLava()  { pohyb(Lava.getText());  }

    private void pohyb(String smer) {
        if (smer == null || smer.isEmpty()) return;
        Application.engine.goGui(smer);
        aktualizuj();
    }

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