package logic;

import controller.FuelType;

public class Vehicle {
	
	private String carOwner, model, manufacturer, year, carNumber;
	private FuelType type;
	

	public Vehicle(String carOwner, String carNumber, String manufacturer, String model, String year, FuelType type ) {
		this.carOwner = carOwner;
		this.carNumber = carNumber;
		this.manufacturer = manufacturer;
		this.model = model;
		this.year = year;
		this.type = type;

	}


	public String getCarOwner() {
		return carOwner;
	}


	public void setCarOwner(String carOwner) {
		this.carOwner = carOwner;
	}


	public String getModel() {
		return model;
	}


	public void setModel(String model) {
		this.model = model;
	}


	public String getManufacturer() {
		return manufacturer;
	}


	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}


	public String getCarNumber() {
		return carNumber;
	}


	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}


	public String getYear() {
		return year;
	}


	public void setYear(String year) {
		this.year = year;
	}


	public FuelType getType() {
		return type;
	}


	public void setType(FuelType type) {
		this.type = type;
	}
}