package application.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

import java.io.File;

import javafx.event.ActionEvent;
import application.Main;

public class RootLayoutController {
	
	private Main main;
	
	/**
	 * Is called by the main application to give a reference back to itself
	 */
	public void setMainApp(Main main) {
		this.main = main;
	}

	// Event Listener on MenuItem.onAction
	@FXML
	public void hanleNew(ActionEvent event) {
		main.getPersonData().clear();
		main.setPersonFile(null);
	}
	
	// Event Listener on MenuItem.onAction
	@FXML
	public void handleOpen(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		
		//show file dialog
		File file = fileChooser.showOpenDialog(main.getPrimaryStage());
		if(file != null) {
			main.loadPersonDataFromFile(file);
		}
	}
	
	// Event Listener on MenuItem.onAction
	@FXML
	public void handleSave(ActionEvent event) {
		File file = main.getPersonFile();
		if(file != null) {
			main.savePersonDataToFile(file);
		} else {
			handleSaveAs(event);
		}
	}
	
	// Event Listener on MenuItem.onAction
	@FXML
	public void handleSaveAs(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		
		File file = fileChooser.showSaveDialog(main.getPrimaryStage());
		if(file != null) {
			// make sure it has the correct extention
			if(!file.getPath().endsWith(".xml")) {
				file = new File(file.getPath() + ".xml");
				System.out.println(file.getAbsolutePath() + "  " + file.getPath());
				main.savePersonDataToFile(file);
			}			
		}
	}
	
	// Event Listener on MenuItem.onAction
	@FXML
	public void handleExit(ActionEvent event) {
		System.exit(0);;
	}
	
	// Event Listener on MenuItem.onAction
	@FXML
	public void handleAbout(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
	}

	@FXML public void handleShowBirthdayStatistics(ActionEvent event) {
		main.showBirthdayStatistics();
	}
}
