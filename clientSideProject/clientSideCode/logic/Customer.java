package logic;

import controller.ModelType;

public class Customer {
	
	private String firstName, lastName, id, email, cvv, month, password, year, creditCard;
	private ModelType type;
	

	public Customer(String id, String firstName, String lastName, String email, String creditCard, String cvv, String month, String year, String password, ModelType type) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.creditCard = creditCard;
		this.cvv = cvv;
		this.month = month;
		this.year = year;
		this.password = password;
		this.type = type;
	}



	public String getYear() {
		return year;
	}


	public void setYear(String year) {
		this.year = year;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getCvv() {
		return cvv;
	}


	public void setCvv(String cvv) {
		this.cvv = cvv;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	public String getCreditCard() {
		return creditCard;
	}


	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}


	public String getMonth() {
		return month;
	}


	public void setMonth(String month) {
		this.month = month;
	}

	public ModelType getType() {
		return type;
	}


	public void setType(ModelType type) {
		this.type = type;
	}
}
