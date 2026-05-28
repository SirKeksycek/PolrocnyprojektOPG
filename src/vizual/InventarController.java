package vizual;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.util.Map;

public class InventarController {

    @FXML private TableView<InventarPolozka> inventarTable;
    @FXML private TableColumn<InventarPolozka, String> colNazov;
    @FXML private TableColumn<InventarPolozka, String> colMnozstvo;
    @FXML private TableColumn<InventarPolozka, String> colTyp;
    @FXML private TableColumn<InventarPolozka, ImageView> colObrazok;
    @FXML private Button exit;

    // Mapovanie item názvu na cestu k obrázku
    private static final Map<String, String> OBRAZKY = Map.of(
            "karta",    "vizual/img/karta.png",
            "baterka",  "vizual/img/baterka.png",
            "naradie",  "vizual/img/naradie.png",
            "mapa",     "vizual/img/mapa.png",
            "paka",     "vizual/img/paka.png"
    );

    // Popisy itemov
    private static final Map<String, String> POPISY = Map.of(
            "karta",    "Prístupová karta do mestských objektov",
            "baterka",  "Silná baterka osvetľujúca tmavé oblasti",
            "naradie",  "Ťažké náradie vhodné na opravy",
            "mapa",     "Mapa tajných chodieb pod mestom",
            "paka",     "Ovládacia páka energetického jadra"
    );

    @FXML
    public void initialize() {
        colNazov.setCellValueFactory(new PropertyValueFactory<>("nazov"));
        colMnozstvo.setCellValueFactory(new PropertyValueFactory<>("mnozstvo"));
        colTyp.setCellValueFactory(new PropertyValueFactory<>("popis"));

        // Stĺpec s obrázkom
        colObrazok.setCellValueFactory(new PropertyValueFactory<>("obrazok"));
        colObrazok.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(ImageView img, boolean empty) {
                super.updateItem(img, empty);
                setGraphic(empty || img == null ? null : img);
            }
        });

        nacitajInventar();
    }

    private void nacitajInventar() {
        ObservableList<InventarPolozka> polozky = FXCollections.observableArrayList();

        for (String item : Application.engine.getInventory()) {
            ImageView obrazok = vytvorObrazok(item);
            String popis = POPISY.getOrDefault(item, "Neznámy predmet");
            polozky.add(new InventarPolozka(item, "1", popis, obrazok));
        }

        inventarTable.setItems(polozky);
    }

    private ImageView vytvorObrazok(String itemNazov) {
        String cesta = OBRAZKY.get(itemNazov);
        if (cesta != null) {
            try {
                Image img = new Image(getClass().getClassLoader().getResourceAsStream(cesta));
                ImageView iv = new ImageView(img);
                iv.setFitWidth(64);
                iv.setFitHeight(64);
                iv.setPreserveRatio(true);
                return iv;
            } catch (Exception e) {
                // obrázok sa nenašiel, vráť null
            }
        }
        return null;
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }

    public static class InventarPolozka {
        private final String nazov;
        private final String mnozstvo;
        private final String popis;
        private final ImageView obrazok;

        public InventarPolozka(String nazov, String mnozstvo, String popis, ImageView obrazok) {
            this.nazov = nazov;
            this.mnozstvo = mnozstvo;
            this.popis = popis;
            this.obrazok = obrazok;
        }

        public String getNazov()      { return nazov; }
        public String getMnozstvo()   { return mnozstvo; }
        public String getPopis()      { return popis; }
        public ImageView getObrazok() { return obrazok; }
    }
}