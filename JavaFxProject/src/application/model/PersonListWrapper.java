package application.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Helper class to wrap a list of persons. this is used for saving the
 * list of person to XML
 * @author huhuanpu
 *
 */

@XmlRootElement
public class PersonListWrapper {

	private List<Person> persons;
	
	@XmlElement(name = "Person")
	public List<Person> getPersons(){
		return persons;
	}
	
	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}
}
