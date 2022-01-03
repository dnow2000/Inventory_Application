package control;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import model.Part;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FieldValidation {

    public List<Part> partsList = new ArrayList<Part>();

    /**
     * Validation code that is reused in several controllers
     * Checks if fields are filled in correctly
     * @param textFieldList takes in a list of type TextField
     * @return returns a boolean
     */

    public static boolean fieldValidation(List<TextField> textFieldList) {
        boolean noFieldError = true;

        String name = textFieldList.get(0).getText();
        int stock;
        double cost;
        int min;
        int max;


        for (TextField field : textFieldList) {
            if (field.getText().isEmpty()) {
                fieldAlertGenerator("Please fill missing fields");
                noFieldError = false;
                break;

            }

        }

        if (noFieldError) {

            try {
                stock = Integer.parseInt(textFieldList.get(1).getText());
                cost = Double.parseDouble(textFieldList.get(2).getText());
                min = Integer.parseInt(textFieldList.get(3).getText());
                max = Integer.parseInt(textFieldList.get(4).getText());
            } catch (Exception e){
                fieldAlertGenerator("inv, cost, min, and max need to be numbers.");
                return false;
            }

            try {
                Integer.parseInt(name);
                fieldAlertGenerator("name needs to be alphanumeric");
                return false;
            }catch (Exception e) {
                //
            }

            if (max < min) {
                fieldAlertGenerator("max needs to be higher than min");
                return false;
            } else if (!(stock < max && stock > min)) {
                fieldAlertGenerator("inventory level needs to be a number between max and min");
                return false;

            }


        }
        return noFieldError;
    }


    /**
     * repeated alert code throughout FieldValidation class
     * @param str takes in a custom alert string
     */
    public static void fieldAlertGenerator(String str) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, str);
        alert.showAndWait();
    }


    /**
     * function for validation company name or machine id fields in the part forms
     * @param textField takes in either an outsourced TextField object or inHouse
     * @param str takes in string to use to differentiate between inHouse and outsourced
     * @return returns a boolean
     */
    public static boolean PartsFieldValidation(TextField textField, String str) {
        if (str.equals("companyName")) {
            try {
                Integer.parseInt(textField.getText());
                fieldAlertGenerator("company name needs to be alphanumeric");
                return false;
            } catch (Exception e) {
                //
            }
            return true;
        } else if (str.equals("machineId")){
            try {
                Integer.parseInt(textField.getText());
            } catch (Exception e) {
                fieldAlertGenerator("machineId needs to be a number");
                return false;
            }
        }
        return true;
    }
}





