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
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainPage implements Initializable {

    public TableColumn PartIDCol;
    public TableColumn PartNameCol;
    public TableColumn PartInventoryCol;
    public TableColumn PartCostCol;
    public Button PartAddButton;
    public Button PartModifyButton;
    public Button PartDeleteButton;
    public Label PartsLabel;
    public TableColumn ProductID;
    public TableColumn ProductName;
    public TableColumn ProductsInventoryLevelCol;
    public TableColumn ProductCostCol;
    public Label ProductsLabel;
    public Button ProductAddButton;
    public Button ProductModifyButton;
    public Button ProductDeleteButton;
    public TableView<Product> ProductsTable;
    public TableView<Part> PartsTable;
    public TextField partSearch;
    public TextField productSearch;
    public Stage primaryStage;
    public Button exitButton;

    /**
     * Initializes the main page with added parts and products
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Initialized main screen with added parts and products
        PartsTable.setItems(Inventory.getAllParts());

        PartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        PartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        PartCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        PartInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        ProductsTable.setItems(Inventory.getAllProducts());

        ProductID.setCellValueFactory(new PropertyValueFactory<>("id"));
        ProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        ProductCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        ProductsInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

    }

    /**
     * shows the add part screen
     * @param actionEvent
     * @throws IOException
     */
    public void onAddPart(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/addPart.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 560);
        stage.setTitle("second Screen");
        stage.setScene(scene);
        stage.show();
        


    }

    /**
     * shows the modify part screen and passes selected part data to
     * the ModifyPartController
     * @param actionEvent listens for click event on the modify button
     * @throws IOException FXMLLoader.load throws an IOException
     */
    public void OnModifyPart(ActionEvent actionEvent) throws IOException {
        boolean isSelected = PartsTable.getSelectionModel().getSelectedItem() == null;

        if (!isSelected) {
            //pass part object to modify controller
            ModifyPartController.passDataToModify(PartsTable.getSelectionModel().getSelectedItem());

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/modifyPart.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 600, 560);
            stage.setTitle("second Screen");
            stage.setScene(scene);
            stage.show();
        }else {
            Alert alert = alertGenerator("a part must be selected in order to modify it");
            alert.showAndWait();

        }
    }

    /**
     * deletes selected part from the parts table
     * also disables modify button if there are no products selected to modify
     * @param actionEvent
     */
    public void OnDeletePart(ActionEvent actionEvent) {
        boolean isSelected = (PartsTable.getSelectionModel().getSelectedItem() == null);
        if (!isSelected) {
            Alert alert = alertGenerator("Delete?");
            alert.showAndWait();
            if (alert.getResult().getText().equals("OK")) {
                Part partToDelete = PartsTable.getSelectionModel().getSelectedItem();
                Inventory.deletePart(partToDelete);
            }
        } else {
            Alert alert = alertGenerator("a part must be selected in order to delete it");
            alert.showAndWait();
        }
    }


    /**
     * shows the addProduct page
     * @param actionEvent listens for click event on the add button
     * @throws IOException FXMLLoader.load throws an IOException
     */
    public void onAddProduct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/addProduct.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 850, 620);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * shows the modify product page.
     * disables button if there is nothing selected to modify.
     * passes user selected product to the modify product controller
     * @param actionEvent listens for click event on the modify button
     * @throws IOException FXMLLoader.load throws an IOException
     */
    public void OnModifyProduct(ActionEvent actionEvent) throws IOException {
        boolean isSelected = (ProductsTable.getSelectionModel().getSelectedItem() == null);
        if (!isSelected) {
            //pass user selected product data to the modify product controller
            ModifyProductController.passDataToModify(ProductsTable.getSelectionModel().getSelectedItem());

            //display modify product page
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/modifyProduct.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 850, 620);
            stage.setTitle("Add Product");
            stage.setScene(scene);
            stage.show();
        } else {
            Alert alert = alertGenerator("a product must be selected in order to modify it");
            alert.showAndWait();
        }
    }

    /**
     * deletes product if it has no associated parts
     * @param actionEvent listens for click event on the delete button
     */
    public void OnDeleteProduct(ActionEvent actionEvent) {
        boolean isNotSelected = (ProductsTable.getSelectionModel().getSelectedItem() == null);
        Alert alert;
        Product product;
        if (isNotSelected) {
            alert = alertGenerator("a product must be selected in order to delete it");
            alert.showAndWait();
        } else {
            product = ProductsTable.getSelectionModel().getSelectedItem();
            if (product.getAllAssociatedParts().size() == 0) {
                alert = alertGenerator("Delete?");
                alert.showAndWait();
                if (alert.getResult().getText().equals("OK")) {
                    Inventory.deleteProduct(product);
                }
            } else {
                alert = alertGenerator("Cannot delete product because it has associated parts");
                alert.showAndWait();

            }
        }
    }



    /**
     * repeated alert code used throughout the MainPage controller
     * @param str alert string argument
     * @return
     */
    public Alert alertGenerator(String str) {
        return new Alert(Alert.AlertType.CONFIRMATION, str);
    }

    /**
     * searches for parts based on id or text input from the user
     * @param keyEvent listens for the on key pressed event
     */
    public void onSearchPart(KeyEvent keyEvent) {
        //variables used
        ObservableList<Part> filteredList = FXCollections.observableArrayList();
        String searchedItem = partSearch.getText();

        //search table when user presses enter
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            try {
                Integer.parseInt(searchedItem);
                Part id = Inventory.lookupPart(Integer.parseInt(searchedItem));
                filteredList.add(id);
                PartsTable.setItems(filteredList);
            } catch (Exception e) {
                filteredList = Inventory.lookupPart(searchedItem);
                PartsTable.setItems(filteredList);
            }
        }

        }

    /**
     * searches for product based on id or text input from the user
     * @param keyEvent listens for the on key pressed event
     */
    public void onSearchProduct(KeyEvent keyEvent) {
        //variables used
        ObservableList<Product> filteredList = FXCollections.observableArrayList();
        String searchText = productSearch.getText();

        //search product table when user presses enter
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            try {
                Product id = Inventory.lookupProduct(Integer.parseInt(searchText));
                filteredList.add(id);
                ProductsTable.setItems(filteredList);
            } catch (Exception e) {
                // catches IndexOutOfBoundException when user types on empty field
                // and NumberFormatException when user types a string instead of a number
                filteredList = Inventory.lookupProduct(searchText);
                ProductsTable.setItems(filteredList);
//
            }




        }

    }


    public void onExit(ActionEvent actionEvent) {
        System.exit(0);
    }
}
