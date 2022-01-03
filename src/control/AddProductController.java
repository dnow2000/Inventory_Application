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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {

    public TextField idField;
    public TextField nameField;
    public TextField inventoryField;
    public TextField priceField;
    public TextField maxField;
    public TextField minField;
    public TextField productPartSearch;
    public Button addPartButton;
    public Button removeAssociatedButton;
    public Button saveButton;
    public Button cancelButton;
    public TableView<Part> partsTable;
    public TableColumn partIdCol;
    public TableColumn partNameCol;
    public TableColumn invLevelCol;
    public TableColumn costCol;
    public TableView<Part> addedPartsTable;
    public TableColumn addedPartIdCol;
    public TableColumn addedPartNameCol;
    public TableColumn addedPartInvCol;
    public TableColumn addedPartCostCol;

    /**
     * variables used to display list of parts in the parts table
     */
    public List<Part> partsList = new ArrayList<Part>();
    public ObservableList<Part> parts = FXCollections.observableList(partsList);

    /**
     * removes part from the added parts table
     * @param actionEvent listens for onclick even on the remove button
     */
    public void onRemove(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "delete?");
        Part selectedItem = addedPartsTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            alert.showAndWait();
            if (alert.getResult().getText().equals("OK")) {
                parts.remove(selectedItem);
                addedPartsTable.setItems(parts);
            }
        } else {
            alert.setContentText("a part must be selected in order to delete it");
            alert.showAndWait();
        }



    }

    /**
     * saves product to the inventory
     * add associated parts to the product
     * validates user input
     * @param actionEvent listens for onclick event on save button
     * @throws IOException
     */
    public void onSave(ActionEvent actionEvent) throws IOException {
        //Check if all fields are filled correctly
        List<TextField> textFieldList = List.of(nameField, inventoryField,
                priceField, minField, maxField);
        boolean noFieldError = FieldValidation.fieldValidation(textFieldList);


        if (noFieldError) {
                //create product and add to Inventory
                int genId = 0;
                if (Inventory.getAllProducts().size() == 0) {
                    genId = 1;
                } else {
                    genId = Inventory.getAllProducts().size() + 1;
                }

                Product product = new Product(
                        genId,
                        nameField.getText(),
                        Double.parseDouble(priceField.getText()),
                        Integer.parseInt(inventoryField.getText()),
                        Integer.parseInt(minField.getText()),
                        Integer.parseInt(maxField.getText())

                );
                Inventory.addProduct(product);

                //add part to the product
                List<Part> addedParts = addedPartsTable.getItems();
                for (Part part : addedParts) {
                    product.addAssociatedPart(part);
                }

                mainPage(actionEvent);

            }

        }


    /**
     * shows the main page. repeated code used throughout AddProductController
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
     * redirects user back to the main screen
     * @param actionEvent listens for onclick event on cancel button
     * @throws IOException FXMLLoader.load throws an IOException
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        mainPage(actionEvent);
    }

    /**
     * adds part to the added parts table
     * @param actionEvent listens for add button event
     */
    public void onAddPart(ActionEvent actionEvent) {
            Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
            if (selectedPart != null) {
                parts.add(selectedPart);

                addedPartsTable.setItems(parts);
                addedPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
                addedPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
                addedPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
                addedPartCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

            }

    }

    /**
     * initializes the parts table with all the parts from the main page
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Part> parts = Inventory.getAllParts();

        partsTable.setItems(parts);

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        invLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        costCol.setCellValueFactory(new PropertyValueFactory<>("price"));


    }

    /**
     * searches parts table based on id or string text
     * @param keyEvent listens for on key event
     */
    public void onSearch(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            String searchedItem = productPartSearch.getText();
            try {
                Integer.parseInt(searchedItem);
                Part id = Inventory.lookupPart(Integer.parseInt(searchedItem));
                ObservableList<Part> filteredList = FXCollections.observableArrayList();
                filteredList.add(id);
                partsTable.setItems(filteredList);

            } catch (Exception e) {
                ObservableList<Part> filteredList = Inventory.lookupPart(searchedItem);
                partsTable.setItems(filteredList);
            }
        }
    }
}
