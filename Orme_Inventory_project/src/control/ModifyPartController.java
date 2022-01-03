package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ModifyPartController implements Initializable {

    public RadioButton inHouseRadioButton;
    public RadioButton outsourcedRadioButton;
    public TextField nameField;
    public TextField inventoryField;
    public TextField costField;
    public TextField maxField;
    public TextField machineIdField;
    public TextField minField;
    public Button saveButton;
    public Button cancelButton;
    public Label machineIdLabel;
    public TextField idField;

    /**
     * Receives user selected part data from MainPage Controller
     */
    private static Part dataToModify = null;

    public static void passDataToModify(Part part) {
        dataToModify = part;
    }

    /**
     * sets label text to Machine ID if user selects inHouse radio button
     * @param actionEvent listens for radio button to be pressed
     */
    public void onInHouse(ActionEvent actionEvent) {
        if (outsourcedRadioButton.isSelected()) {
            outsourcedRadioButton.setSelected(false);


            machineIdLabel.setText("Machine ID");
            machineIdLabel.setLayoutX(55);
        }
    }

    /**
     * sets label text to Company Name if user selects outsourced radio button
     * @param actionEvent listens for radio button to be pressed
     */
    public void onOutsourced(ActionEvent actionEvent) {
        if (inHouseRadioButton.isSelected()) {
            inHouseRadioButton.setSelected(false);

            machineIdLabel.setText("Company Name");
            machineIdLabel.setLayoutX(55);
        }
    }

    /**
     * redirects user back to the main page
     * @param actionEvent listens for onclick event on cancel button
     * @throws IOException FXMLLoader.load throws an IOException
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/main.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * validates data, saves it as outsourced or inHouse depending on what radio button the user presses
     * @param actionEvent listens for onclick event on save button
     * @throws IOException FXMLLoader.load throws an IOException
     */
    public void onSave(ActionEvent actionEvent) throws IOException {
        List<TextField> textFieldList = List.of(nameField, inventoryField,
                costField, minField, maxField, machineIdField);
        boolean noFieldError = FieldValidation.fieldValidation(textFieldList);


        if (noFieldError) {
            Part data = dataToModify;
            boolean isCompanyName = false;
            boolean isMachineId = false;
            // set changed data for an outsourced part
            if (data instanceof Outsourced && inHouseRadioButton.isSelected() || inHouseRadioButton.isSelected()) {
                isMachineId = FieldValidation.PartsFieldValidation(machineIdField, "machineId");
                int machineId = Integer.parseInt(machineIdField.getText());

                if (isMachineId) {

                    InHouse newInHouse = new InHouse(
                            data.getId(),
                            nameField.getText(),
                            Double.parseDouble(costField.getText()),
                            Integer.parseInt(inventoryField.getText()),
                            Integer.parseInt(maxField.getText()),
                            Integer.parseInt(minField.getText()),
                            machineId
                    );
                    int index = Inventory.getAllParts().indexOf(data);
                    Inventory.updatePart(index, newInHouse);
                }
                //set changed data of InHouse part
            } else if (data instanceof InHouse && outsourcedRadioButton.isSelected() || outsourcedRadioButton.isSelected()) {
                TextField companyName = machineIdField;
                isCompanyName = FieldValidation.PartsFieldValidation(companyName, "companyName");

                if (isCompanyName) { //validation
                    Outsourced newOutsourced = new Outsourced(
                    data.getId(),
                    nameField.getText(),
                    Double.parseDouble(costField.getText()),
                    Integer.parseInt(inventoryField.getText()),
                    Integer.parseInt(minField.getText()),
                    Integer.parseInt(maxField.getText()),
                    companyName.getText()

                    );

                    int index = Inventory.getAllParts().indexOf(data);
                    Inventory.updatePart(index, newOutsourced);
                }


            }
            if (isCompanyName || isMachineId) {
                //get back to main screen
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/main.fxml")));
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("Main Screen");
                stage.setScene(scene);
                stage.show();
            }
        }
    }


    /**
     * Initializes page with selected data from the main page
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Part data = dataToModify;

        //auto filled data
        idField.setText("" + data.getId());
        nameField.setText(data.getName());
        inventoryField.setText("" + data.getStock());
        costField.setText("" + data.getPrice());
        maxField.setText("" + data.getMax());
        minField.setText("" + data.getMin());

        //auto fill form with machineId or company name depending on object class
        if (data instanceof InHouse) {
            int machineId = ((InHouse) data).getMachineId();
            machineIdField.setText("" + machineId);
        } else {
            outsourcedRadioButton.setSelected(true);
            inHouseRadioButton.setSelected(false);
            String companyName = ((Outsourced)data).getCompanyName();
            machineIdField.setText(companyName);
            machineIdLabel.setText("Company Name");
            machineIdLabel.setLayoutX(55);
        }




    }


}
