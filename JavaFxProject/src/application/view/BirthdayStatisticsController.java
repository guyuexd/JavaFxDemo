package application.view;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import application.model.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;

/**
 * the controller for the birthday statistics view
 * 
 * @author huhuanpu
 *
 */
public class BirthdayStatisticsController {
	
	@FXML
	private BarChart<String, Integer> barChart;
	
	@FXML
	private CategoryAxis xAxis;
	
	private ObservableList<String> monthNames = FXCollections.observableArrayList();
	
	/**
	 * Initializes the controller class. this method is automatically called
	 * after the fxml file has been loaded
	 */
	@FXML
	private void initialize() {
		// Get an array with the english moth names
		String[] moths = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
		// convert it to a list and add it to our observablelist of moths
		monthNames.addAll(Arrays.asList(moths));
		
		// Assign the moth names as categories for the horizontal axis.
		xAxis.setCategories(monthNames);
	}

	/**
	 * Sets the persons to show the statistics for
	 */
	public void setPersonData(List<Person> persons) {
		// count the number of each moth
		int[] monthCounter = new int[12];
		for(Person person : persons) {
			monthCounter[person.getBirthday().getMonthValue() - 1]++;
		}
		
		XYChart.Series<String, Integer> series = new XYChart.Series<>();
		
		// Create a XYChart.Data object for each moth. Add it to the series
		for(int i = 0; i < monthCounter.length; i++) {
			series.getData().add(new XYChart.Data<>(monthNames.get(i), monthCounter[i]));
		}
		
		barChart.getData().add(series);
	}
}
