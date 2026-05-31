package vizual;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
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
    @FXML private VBox inventarVBox;
    @FXML private Label roomTitleLabel;
    @FXML private TextArea storyTextArea;
    @FXML private ImageView roomImageView;

    private final Button[] sipky = new Button[4];

    @FXML
    public void initialize() {
        sipky[0] = Rovno;
        sipky[1] = Prava;
        sipky[2] = Nazad;
        sipky[3] = Lava;
        
        // Show intro dialog on start
        showIntroDialog();
        
        aktualizuj();
    }

    private void aktualizuj() {
        // Skontroluj game over stav
        if (Application.engine.isGameOver()) {
            showGameOver();
            return;
        }
        
        aktualizujSipky();
        aktualizujItemy();
        aktualizujInventar();
        aktualizujRoomInfo();
    }

    private void aktualizujRoomInfo() {
        if (roomTitleLabel != null) {
            roomTitleLabel.setText(Application.engine.getCurrentRoomLabel());
        }
        if (storyTextArea != null) {
            storyTextArea.setText(Application.engine.getCurrentRoomDesc());
        }
        if (roomImageView != null) {
            updateRoomImage();
        }
    }

    private void updateRoomImage() {
        String imageUrl = Application.engine.getCurrentRoomImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                Image image = new Image(imageUrl, true);
                roomImageView.setImage(image);
            } catch (Exception e) {
                System.err.println("Failed to load image: " + imageUrl);
                e.printStackTrace();
            }
        }
    }

    private void showIntroDialog() {
        String intro = Application.engine.getIntro();
        String endingGuide = "\n\n===== AKO SA DOSTAŤ K ENDINGOM =====\n\n" +
                "🏆 DOBRÝ ENDING (Záchrana mesta):\n" +
                "1. Nájdi kartu na okraji mesta (room 1)\n" +
                "2. Choď do veže (room 3) a nájdi baterku\n" +
                "3. Použi baterku vo veži → otvorí sa cesta do brány\n" +
                "4. Choď z veže do brány (room 5)\n" +
                "5. Použi kartu v bráne → brána sa otvorí\n" +
                "6. Choď do centra (room 7) a potom do jadra (room 6)\n" +
                "7. Nájdi náradie v sklade (room 2)\n" +
                "8. Použi náradie v jadre → mesto je zachránené!\n\n" +
                "💰 NAJLEPŠÍ ENDING (Záchrana + Poklad):\n" +
                "1. Najprv zachraň mesto (pozri vyššie)\n" +
                "2. Nájdi mapu v sklade\n" +
                "3. Použi mapu v sklade → otvorí sa tajná miestnosť\n" +
                "4. Choď do hlbky (cez jadro, musí byť stabilizované)\n" +
                "5. Nájdeš poklad A mesto je zachránené!\n\n" +
                "☠️ ZLÉ ENDINGY (Smrť):\n" +
                "• Ísť do centra (z veže) bez baterky = smrť v tme\n" +
                "• Ísť do centra (z brány) bez karty = systém a zlikviduje\n" +
                "• Ísť do hlbky bez stabilizácie jadra = výbuch\n" +
                "• Nájsť poklad bez stabilizácie jadra = poklad máš, ale mesto vybuchlo\n\n" +
                "Tip: Vždy si najprv prehliadni miestnosť a nájdi všetky predmety!";
        
        String fullText = intro + endingGuide;
        
        if (!fullText.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Vitaj v Ravenrocku");
            alert.setHeaderText("Úvod a Návod");
            alert.setContentText(fullText);
            alert.getDialogPane().setPrefSize(600, 500);
            alert.showAndWait();
        }
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
            Button btn = new Button("Take: " + nazov);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setOnAction(e -> {
                Application.engine.takeGui(nazov);
                aktualizuj();
            });
            itemyVBox.getChildren().add(btn);
        }
    }

    private void aktualizujInventar() {
        inventarVBox.getChildren().clear();
        for (String item : Application.engine.getInventory()) {
            Button btn = new Button("Use: " + item);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setOnAction(e -> {
                boolean used = Application.engine.useGui(item);
                if (!used) {
                    showCannotUseAlert();
                }
                aktualizuj();
            });
            inventarVBox.getChildren().add(btn);
        }
    }

    private void showGameOver() {
        boolean isWin = Application.engine.isWinCondition();
        String message = Application.engine.getGameOverMessage();
        String outro = Application.engine.getOutro();
        int steps = Application.engine.getSteps();
        
        String fullMessage = message + "\n\n" + outro + "\n\nPočet tahov: " + steps;
        
        Alert alert = new Alert(isWin ? Alert.AlertType.INFORMATION : Alert.AlertType.WARNING);
        alert.setTitle(isWin ? "Víťazstvo!" : "Koniec Hry");
        alert.setHeaderText(isWin ? "Vyhral si!" : "Zomrel si!");
        
        // Add room image to the alert
        String imageUrl = Application.engine.getCurrentRoomImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                Image image = new Image(imageUrl, true);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(400);
                imageView.setFitHeight(250);
                imageView.setPreserveRatio(true);
                
                GridPane content = new GridPane();
                content.setMaxWidth(Double.MAX_VALUE);
                content.add(imageView, 0, 0);
                
                Label textLabel = new Label(fullMessage);
                textLabel.setWrapText(true);
                textLabel.setStyle("-fx-font-size: 14px;");
                content.add(textLabel, 0, 1);
                
                alert.getDialogPane().setContent(content);
            } catch (Exception e) {
                alert.setContentText(fullMessage);
                System.err.println("Failed to load game over image: " + imageUrl);
            }
        } else {
            alert.setContentText(fullMessage);
        }
        
        alert.getDialogPane().setPrefSize(500, 500);
        alert.showAndWait();
        
        // Disable all controls
        for (Button b : sipky) {
            b.setDisable(true);
        }
        inv.setDisable(true);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showCannotUseAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Nemožno použiť");
        alert.setHeaderText(null);
        alert.setContentText("Tento predmet sa tu nedá použiť.");
        alert.showAndWait();
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
