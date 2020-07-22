package logic;

import javafx.beans.property.SimpleStringProperty;

public class Employee {
	
	private	SimpleStringProperty EmployeeNumber, FirstName, LastName, Position, Association, Email;

	

	public Employee(String employeeNumber, String firstName, String lastName, String position, String association, String email) {
		
		EmployeeNumber = new SimpleStringProperty(employeeNumber);
		FirstName = new SimpleStringProperty(firstName);
		LastName = new SimpleStringProperty(lastName);
		Position = new SimpleStringProperty(position);
		Association = new SimpleStringProperty(association);
		Email = new SimpleStringProperty(email);
	}

	public String getEmployeeNumber() {
		return EmployeeNumber.get();
	}

	public void setEmployeeNumber(SimpleStringProperty employeeNumber) {
		EmployeeNumber = employeeNumber;
	}

	public String getFirstName() {
		return FirstName.get();
	}

	public void setFirstName(SimpleStringProperty firstName) {
		FirstName = firstName;
	}

	public String getLastName() {
		return LastName.get();
	}

	public void setLastName(SimpleStringProperty lastName) {
		LastName = lastName;
	}

	public String getPosition() {
		return Position.get();
	}

	public void setPosition(SimpleStringProperty position) {
		Position = position;
	}

	public String getAssociation() {
		return Association.get();
	}

	public void setAssociation(SimpleStringProperty association) {
		Association = association;
	}

	public String getEmail() {
		return Email.get();
	}

	public void setEmail(SimpleStringProperty email) {
		Email = email;
	}
	
	
}
