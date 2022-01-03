package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class AddPartController {
    public TextField idField;
    public TextField nameField;
    public TextField InventoryField;
    public TextField costField;
    public TextField maxField;
    public TextField machineIdField;
    public TextField minField;
    public Button saveButton;
    public Button cancelButton;
    public RadioButton inHouseRadioButton;
    public RadioButton outsourcedRadioButton;
    public Label machineIdLabel;

    /**
     * checks to see whether part is outsourced or inHouse before saving it to the Inventory
     * @param actionEvent listens for click event on the save button
     * @throws IOException FXMLLoader.load causes an IOException
     */
    public void onSave(ActionEvent actionEvent) throws IOException {
        //check if text fields are filled correctly
        List<TextField> textFieldList = List.of(nameField, InventoryField,
                costField, minField, maxField, machineIdField);
        boolean noFieldError = FieldValidation.fieldValidation(textFieldList);

        if (noFieldError) {
            //auto generated Id
            int genId = 0;
            if (Inventory.getAllParts().size() == 0) {
                genId = 1;
            } else {
                genId = Inventory.getAllParts().size() + 1;
            }

            boolean isCompanyName = false; //variables for checking correct input for company name or machine id
            boolean isMachineId = false;
            //outsourced part selected
            if (outsourcedRadioButton.isSelected()) {
                TextField companyName = machineIdField;
                isCompanyName = FieldValidation.PartsFieldValidation(companyName, "companyName");
                if (isCompanyName) {
                    Outsourced newPart = new Outsourced(
                            genId,
                            nameField.getText(),
                            Double.parseDouble(costField.getText()),
                            Integer.parseInt(InventoryField.getText()),
                            Integer.parseInt(minField.getText()),
                            Integer.parseInt(maxField.getText()),
                            companyName.getText()

                    );
                    //add outsourced part to inventory
                    Inventory.addPart(newPart);
                }
            }
            // In-House Part selected
             if (inHouseRadioButton.isSelected()) {
                 isMachineId = FieldValidation.PartsFieldValidation(machineIdField, "machineId");
                InHouse newPart = new InHouse(
                        genId,
                        nameField.getText(),
                        Double.parseDouble(costField.getText()),
                        Integer.parseInt(InventoryField.getText()),
                        Integer.parseInt(maxField.getText()),
                        Integer.parseInt(minField.getText()),
                        Integer.parseInt(machineIdField.getText())
                );

                //add in-house part to inventory
                Inventory.addPart(newPart);

            }

             if (isCompanyName || isMachineId) {
                 //pull up main screen
                 showMainPage(actionEvent);
             }
        }
    }


    /**
     * shows the main page. repeated code used throughout AddPartController.
     * @param actionEvent param depends on what other function calls it
     * @throws IOException FXMLLoader.load throws and IOException
     */
    public void showMainPage(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/main.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * redirects user back to the main page. does not save any data
     * @param actionEvent listens for onclick even on the delete button
     * @throws IOException
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        showMainPage(actionEvent);
    }

    /**
     * changes label from "company name" to "Machine ID" if the inHouse radio button is selected
     * @param actionEvent listens for the radio button to be pressed
     */
    public void onInHouse(ActionEvent actionEvent) {
        if (outsourcedRadioButton.isSelected()) {
            outsourcedRadioButton.setSelected(false);

            machineIdLabel.setText("Machine ID");
            machineIdLabel.setLayoutX(55);
        }
    }


    /**
     * changes label to "company name" if the outsourced button is pressed
     * @param actionEvent listens for the radio button to be pressed
     */
    public void onOutsourced(ActionEvent actionEvent) {
        if (inHouseRadioButton.isSelected()) {
            inHouseRadioButton.setSelected(false);

            machineIdLabel.setText("Company Name");
            machineIdLabel.setLayoutX(30);
        }
    }

}
