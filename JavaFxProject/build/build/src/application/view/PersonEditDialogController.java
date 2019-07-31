package application.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.text.NumberFormat;

import application.model.Person;
import application.util.DateUtil;
import javafx.event.ActionEvent;

public class PersonEditDialogController {
	@FXML
	private TextField firstNameId;
	@FXML
	private TextField lastNameId;
	@FXML
	private TextField streetId;
	@FXML
	private TextField cityId;
	@FXML
	private TextField postalCodeId;
	@FXML
	private TextField birthdayId;
	
	private Stage dialogStage;
	private Person person;
	private boolean okClicked = false;

	@FXML
	private void initialize() {
		
	}
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	public void setPerson(Person person) {
		this.person = person;
		
		firstNameId.setText(person.getFirstName());
		lastNameId.setText(person.getLastName());
		streetId.setText(person.getStreet());
		cityId.setText(person.getStreet());
		postalCodeId.setText(Integer.toString(person.getPostalCode()));
		birthdayId.setText(DateUtil.format(person.getBirthday()));
		birthdayId.setPromptText("dd.mm.yyyy");
		
	}
	
	public boolean isOkClicked() {
		return okClicked;
	}
	// Event Listener on Button.onAction
	@FXML
	public void handleOk(ActionEvent event) {
		if(isInputValid()) {
			person.setFirstName(firstNameId.getText());
			person.setLastName(lastNameId.getText());
			person.setStreet(streetId.getText());
			person.setCity(cityId.getText());
			person.setPostalCode(Integer.parseInt(postalCodeId.getText()));
			person.setBirthday(DateUtil.parse(birthdayId.getText()));
			okClicked = true;
			dialogStage.close();
		}
	}
	// Event Listener on Button.onAction
	@FXML
	public void handleCancel(ActionEvent event) {
		dialogStage.close();
	}
	
	/**
	 * Validates the user input in the text fields
	 */
	private boolean isInputValid() {
		String errorMessage = "";
		if(firstNameId.getText() == null || firstNameId.getText().isEmpty()) {
			errorMessage += "No valid first name!\n";
		}
		if(lastNameId.getText() == null || lastNameId.getText().isEmpty()) {
			errorMessage += "No valid last name!\n";
		}
		if(streetId.getText() == null || streetId.getText().isEmpty()) {
			errorMessage += "No valid street!\n";
		}
		if(postalCodeId.getText() == null || postalCodeId.getText().isEmpty()) {
			errorMessage += "No valid postal code!\n";
		} else {
			try {
				Integer.parseInt(postalCodeId.getText());
			} catch(NumberFormatException e) {
				errorMessage += "No valid postal code(must be a integer)!\n";
			}
		}
		if (cityId.getText() == null || cityId.getText().length() == 0) {
            errorMessage += "No valid city!\n"; 
        }

        if (birthdayId.getText() == null || birthdayId.getText().length() == 0) {
            errorMessage += "No valid birthday!\n";
        } else {
            if (!DateUtil.validDate(birthdayId.getText())) {
                errorMessage += "No valid birthday. Use the format dd.mm.yyyy!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
        	Alert alert = new Alert(AlertType.WARNING);
        	alert.setTitle("Invalid fields");
        	alert.setHeaderText("Please correct invalid fields");
        	alert.setContentText(errorMessage);
        	alert.showAndWait();
            return false;
        }
	}
}
