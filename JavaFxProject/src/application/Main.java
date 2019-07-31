package application;
	
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import application.model.Person;
import application.model.PersonListWrapper;
import application.view.BirthdayStatisticsController;
import application.view.PersonEditDialogController;
import application.view.PersonOverviewController;
import application.view.RootLayoutController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	private ObservableList<Person> personData = FXCollections.observableArrayList();
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("AddressApp");
		
		// Set the application icon
		Image appIcon = new Image(Main.class.getResourceAsStream("resources/images/address_book.png"));
		this.primaryStage.getIcons().add(appIcon);
		
		personData.add(new Person("Hans", "Muster"));
		personData.add(new Person("Ruth","Muller"));
		personData.add(new Person("Heinz", "Kurz"));
		personData.add(new Person("Cornelia", "Meier"));
		personData.add(new Person("Werner", "Meyer"));
        personData.add(new Person("Lydia", "Kunz"));
        personData.add(new Person("Anna", "Best"));
        personData.add(new Person("Stefan", "Meier"));
        personData.add(new Person("Martin", "Mueller"));
		initRootLayout();
		showPersonOverview();
	}
	
	/**
	 * Returns the data as an observable list of Persons
	 * @return
	 */
	public ObservableList<Person> getPersonData() {
		return personData;
	}
	
	public void initRootLayout() {
		try {
			// load root layout from fxml file
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			
			// show the scene containing the root layout
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			
			// Give the controller access to the main app
			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);
			
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Try to load last opened person file.
		File file = getPersonFile();
		if(file != null) {
			loadPersonDataFromFile(file);
		}
	}
	
	/**
	 * show the person overview inside the root layout
	 * @param args
	 */
	public void showPersonOverview() {
		try {
			// load person overview
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/PersonOverview.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			
			// set person overview into the center of root layout
			rootLayout.setCenter(personOverview);
			
			// Give the controller access to the main app.
	        PersonOverviewController controller = loader.getController();
	        controller.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	/**
	 * Opens a dialog to edit details for the specified person. If the user
	 * clicks ok, the changes are saved into the provided person object and
	 * true is returned
	 * 
	 * @param args
	 */
	public boolean showPersonEditDialog(Person person) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/PersonEditDialog.fxml"));
			AnchorPane page = fxmlLoader.load();
			
			// create dialog stage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			
			// set person to controller
			PersonEditDialogController personEditDialogController = fxmlLoader.getController();
			personEditDialogController.setDialogStage(dialogStage);
			personEditDialogController.setPerson(person);		
			
			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();
			
			return personEditDialogController.isOkClicked();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
	
	public File getPersonFile() {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		String filePath = prefs.get("filePath", null);
		if(filePath == null) {
			return null;
		} else {
			return new File(filePath);
		}
	}
	
	public void setPersonFile(File file) {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		if(file != null) {
			prefs.put("filePath", file.getAbsolutePath());
		} else {
			prefs.remove("filePath");
		}
	}
	
	/**
	 * load person data from the specified file. the current person data will be replaced
	 * @param args
	 */
	public void loadPersonDataFromFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
			PersonListWrapper wrapper = (PersonListWrapper) context.createUnmarshaller().unmarshal(file);
			personData.clear();
			personData.addAll(wrapper.getPersons());
			
			// save person wrapper file path
			setPersonFile(file);
		} catch (JAXBException e) {			
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("could not load data from file: "+file.getPath());
			alert.showAndWait();
		}
	}
	
	
	/**
	 * save the current person data to the specified file
	 * @param args
	 */
	public void savePersonDataToFile(File file) {		
		try {
			JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			// Wrapping our person data
			PersonListWrapper wrapper = new PersonListWrapper();
			wrapper.setPersons(personData);
			
			// Marshalling and saving xml to the file
			m.marshal(wrapper, file);
			
			//save the file path to the registry
			setPersonFile(file);
		} catch (JAXBException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("could not save data to file:"+ file.getPath());
			alert.setContentText("check you file path");
			alert.showAndWait();
		}
		
	}
	
	/**
	 * Opens a dialog to show birthday statistics.
	 * @param args
	 */
	public void showBirthdayStatistics() {
		try {
			// load the fxml file and create a new stage for the popup.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/BirthdayStatistics.fxml"));
			AnchorPane page = loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Birthday Statistics");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			
			// Set the persons into the controller
			BirthdayStatisticsController controller = loader.getController();
			controller.setPersonData(personData);
			
			dialogStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
