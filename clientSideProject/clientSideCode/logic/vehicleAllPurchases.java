package logic;

import java.time.LocalDate;
import java.time.LocalTime;

import controller.FuelType;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class vehicleAllPurchases {
	
    private String carLIcenseNumber;
    private String gasStationName;
    private FuelType fuelType;
    private	Float fuelAmount;
    private Float payment;
    private LocalDate purchaseDate;
    private LocalTime purchaseTime;


	public vehicleAllPurchases(String carLIcenseNumber, String gasStationName, FuelType fuelType, Float fuelAmount,
			Float payment, LocalDate purchaseDate, LocalTime purchaseTime) {
		super();
		this.carLIcenseNumber = carLIcenseNumber;
		gasStationName = gasStationName;
		this.fuelType = fuelType;
		this.fuelAmount = fuelAmount;
		this.payment = payment;
		this.purchaseDate = purchaseDate;
		this.purchaseTime = purchaseTime;
	}
	/**
	 * @return the carLIcenseNumber
	 */
	public String getCarLIcenseNumber() {
		return carLIcenseNumber;
	}
	/**
	 * @param carLIcenseNumber the carLIcenseNumber to set
	 */
	public void setCarLIcenseNumber(String carLIcenseNumber) {
		this.carLIcenseNumber = carLIcenseNumber;
	}
	/**
	 * @return the gasStationName
	 */
	public String getGasStationName() {
		return gasStationName;
	}
	/**
	 * @param gasStationName the gasStationName to set
	 */
	public void setGasStationName(String gasStationName) {
		gasStationName = gasStationName;
	}
	/**
	 * @return the fuelType
	 */
	public FuelType getFuelType() {
		return fuelType;
	}
	/**
	 * @param fuelType the fuelType to set
	 */
	public void setFuelType(FuelType fuelType) {
		this.fuelType = fuelType;
	}
	/**
	 * @return the fuelAmount
	 */
	public Float getFuelAmount() {
		return fuelAmount;
	}
	/**
	 * @param fuelAmount the fuelAmount to set
	 */
	public void setFuelAmount(Float fuelAmount) {
		this.fuelAmount = fuelAmount;
	}
	/**
	 * @return the payment
	 */
	public Float getPayment() {
		return payment;
	}
	/**
	 * @param payment the payment to set
	 */
	public void setPayment(Float payment) {
		this.payment = payment;
	}
	/**
	 * @return the purchaseDate
	 */
	public LocalDate getPurchaseDate() {
		return purchaseDate;
	}
	/**
	 * @param purchaseDate the purchaseDate to set
	 */
	public void setPurchaseDate(LocalDate purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	/**
	 * @return the purchaseTime
	 */
	public LocalTime getPurchaseTime() {
		return purchaseTime;
	}
	/**
	 * @param purchaseTime the purchaseTime to set
	 */
	public void setPurchaseTime(LocalTime purchaseTime) {
		this.purchaseTime = purchaseTime;
	}
	@Override
	public String toString() {
		return "vehicleAllPurchases [carLIcenseNumber=" + carLIcenseNumber + ", GasStationName=" + gasStationName
				+ ", fuelType=" + fuelType + ", fuelAmount=" + fuelAmount + ", payment=" + payment + ", purchaseDate="
				+ purchaseDate + ", purchaseTime=" + purchaseTime + "]";
	}
    
    

}
