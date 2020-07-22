package logic;

import java.time.LocalTime;
import java.util.ArrayList;

import controller.FuelType;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

public class AnalysisReport {
	
	private String customerId;
	private String fuelTypes; //Problem
	private String customerType;
	private String totalPurchaseAmount; 
	private LocalTime refuelingTimeAvg;
	private Integer customerRank;


	public AnalysisReport(String customerId, String fuelTypes, String customerType,
			String totalPurchaseAmount, LocalTime refuelingTimeAvg, Integer customerRank) {
		super();
		this.customerId = customerId;
		this.fuelTypes = fuelTypes;
		this.customerType = customerType;
		this.totalPurchaseAmount = totalPurchaseAmount;
		this.refuelingTimeAvg = refuelingTimeAvg;
		this.customerRank = customerRank;
	}
	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	/**
	 * @return the fuelTypes
	 */
	public String getFuelTypes() {
		return fuelTypes;
	}
	/**
	 * @param fuelTypes the fuelTypes to set
	 */
	public void setFuelTypes(String fuelTypes) {
		this.fuelTypes = fuelTypes;
	}

	/**
	 * @return the customerType
	 */
	public String getCustomerType() {
		return customerType;
	}
	/**
	 * @param customerType the customerType to set
	 */
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	/**
	 * @return the totalPurchaseAmount
	 */
	public String getTotalPurchaseAmount() {
		return totalPurchaseAmount;
	}
	/**
	 * @param totalPurchaseAmount the totalPurchaseAmount to set
	 */
	public void setTotalPurchaseAmount(String totalPurchaseAmount) {
		this.totalPurchaseAmount = totalPurchaseAmount;
	}
	/**
	 * @return the refuelingTimeAvg
	 */
	public LocalTime getRefuelingTimeAvg() {
		return refuelingTimeAvg;
	}
	/**
	 * @param refuelingTimeAvg the refuelingTimeAvg to set
	 */
	public void setRefuelingTimeAvg(LocalTime refuelingTimeAvg) {
		this.refuelingTimeAvg = refuelingTimeAvg;
	}
	/**
	 * @return the customerRank
	 */
	public Integer getCustomerRank() {
		return customerRank;
	}
	/**
	 * @param customerRank the customerRank to set
	 */
	public void setCustomerRank(Integer customerRank) {
		this.customerRank = customerRank;
	}
	@Override
	public String toString() {
		return "AnalysisReport [customerId=" + customerId + ", fuelTypes=" + fuelTypes + ", customerType="
				+ customerType + ", totalPurchaseAmount=" + totalPurchaseAmount + ", refuelingTimeAvg="
				+ refuelingTimeAvg + ", customerRank=" + customerRank + "]";
	}
	
	
	
	

}
