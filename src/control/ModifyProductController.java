package control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ModifyProductController implements Initializable {
    public TextField idField;
    public TextField nameField;
    public TextField inventoryField;
    public TextField priceField;
    public TextField maxField;
    public TextField minField;
    public TableColumn partIdCol;
    public TableColumn partNameCol;
    public TableColumn invLevelCol;
    public TableColumn costCol;
    public TextField productPartSearch;
    public TableView<Part> addedPartsTable;
    public TableColumn addedPartIdCol;
    public TableColumn addedPartNameCol;
    public TableColumn addedPartInvCol;
    public TableColumn addedPartCostCol;
    public Button addPartButton;
    public Button removeAssociatedButton;
    public Button saveButton;
    public Button cancelButton;
    public TableView<Part> partsTable;


    /**
     * user selected product data from main page
     */
    private static Product dataToModify = null;

    public static void passDataToModify(Product product) {
        dataToModify = product;
    }

    /**
     * list of parts associated with product
     */
    ObservableList<Part> associatedParts = dataToModify.getAllAssociatedParts();
    ObservableList<Part> parts = Inventory.getAllParts();

    /**
     * redirects user back to the main page
     *
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        mainPage(actionEvent);
    }

    /**
     * redirects to main page. Repeatedly used code throughout ModifyProductController
     * @param actionEvent listens for onclick event on cancel button
     * @throws IOException FXMLLoader.load throws an IOException
     */
    public void mainPage(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/main.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * updates product information
     * @param actionEvent listens for onclick event on cancel button
     * @throws IOException FXMLLoader.load throws an IOException
     */
    public void onSave(ActionEvent actionEvent) throws IOException {
        List<TextField> textFieldList = List.of(nameField, inventoryField,
                priceField, minField, maxField);
        boolean noFieldError = FieldValidation.fieldValidation(textFieldList);

        if (noFieldError) {
            //saved modified data
            dataToModify.setName(nameField.getText());
            dataToModify.setPrice(Double.parseDouble(priceField.getText()));
            dataToModify.setStock(Integer.parseInt(inventoryField.getText()));
            dataToModify.setMin(Integer.parseInt(minField.getText()));
            dataToModify.setMax(Integer.parseInt(maxField.getText()));

            int index = Inventory.getAllProducts().indexOf(dataToModify);
            Inventory.updateProduct(index, dataToModify);

            mainPage(actionEvent);
        }
    }

    /**
     * removes associated part from product
     * @param actionEvent
     */
    public void onRemove(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "delete?");
        Part selectedItem = addedPartsTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            alert.showAndWait();
            if (alert.getResult().getText().equals("OK")) {
                Product.deleteAssociatedPart(selectedItem);
                addedPartsTable.setItems(associatedParts);
            }
        } else {
            alert.setContentText("a part must be selected in order to delete it");
            alert.showAndWait();
        }
    }

    /**
     * adds associated part to added parts table
     * @param actionEvent
     */
    public void onAddPart(ActionEvent actionEvent) {

        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            associatedParts.add(selectedPart);

            addedPartsTable.setItems(associatedParts);
            addedPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            addedPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            addedPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
            addedPartCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        }
    }

    /**
     * searches parts table based on id or text
     * @param keyEvent
     */
    public void onSearch(KeyEvent keyEvent) {
        //variables used
        ObservableList<Part> filteredList = FXCollections.observableArrayList();
        String searchedItem = productPartSearch.getText();

        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            try {
                Integer.parseInt(searchedItem);
                Part id = Inventory.lookupPart(Integer.parseInt(searchedItem));
                filteredList.add(id);
                partsTable.setItems(filteredList);
            } catch (Exception e) {
                filteredList = Inventory.lookupPart(searchedItem);
                partsTable.setItems(filteredList);
            }
        }
    }

    /**
     * Initializes the modify product page with auto-filled products fields and parts
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //auto-filled product fields
        idField.setText("" + dataToModify.getId());
        nameField.setText(dataToModify.getName());
        inventoryField.setText("" + dataToModify.getStock());
        priceField.setText("" + dataToModify.getPrice());
        maxField.setText("" + dataToModify.getMax());
        minField.setText("" + dataToModify.getMin());

        //auto-filled Parts table


        partsTable.setItems(parts);

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        invLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        costCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        //auto-filled selected Parts table
        addedPartsTable.setItems(associatedParts);

        addedPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addedPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addedPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addedPartCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
}
