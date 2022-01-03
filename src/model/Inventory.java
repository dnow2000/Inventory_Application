package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;

public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    //methods
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    public static Part lookupPart(int partID) {
        ObservableList<Part> filteredList = FXCollections.observableArrayList();
        for (Part part : allParts) {

            //search by id
                if (partID == part.getId()) {

                    filteredList.add(part);


                }
        }
        return filteredList.get(0);
    }

    public static Product lookupProduct(int productID) {
        ObservableList<Product> filteredList = FXCollections.observableArrayList();
        for (Product product : allProducts) {

            //search by id
            if (productID == product.getId()) {

                filteredList.add(product);


            }
        }
        return filteredList.get(0);
    }

    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> filteredList = FXCollections.observableArrayList();
        for (Part part: allParts) {
            String name = part.getName();
            if (name.contains(partName)) {
                filteredList.add(part);
            }
        }
        if (filteredList.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Part not found");
            alert.showAndWait();
            return allParts;
        }
        return filteredList;
    }

    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> filteredList = FXCollections.observableArrayList();
        for (Product product: allProducts) {
            String name = product.getName();
            if (name.contains(productName)) {
                filteredList.add(product);
            }
        }
        if (filteredList.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Product not found");
            alert.showAndWait();
            return allProducts;
        }
        return filteredList;
    }
    public static void updatePart(int index, Part selectedPart) {
        Inventory.addPart(selectedPart);
        Inventory.getAllParts().remove(index);
    }
    public static void updateProduct(int index, Product newProduct) {
        Inventory.addProduct(newProduct);
        Inventory.getAllProducts().remove(index);
    }
    public static boolean deletePart(Part selectedPart) {
        return Inventory.getAllParts().remove(selectedPart);
    }
    public static boolean deleteProduct(Product selectedProduct) {
        return Inventory.getAllProducts().remove(selectedProduct);
    }
    //getters
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

}
