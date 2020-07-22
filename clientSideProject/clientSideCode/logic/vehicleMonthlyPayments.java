package logic;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

public class vehicleMonthlyPayments {
	private Integer year;
	private Integer month;
    private Float totalMonthlyPayment;
    

	public vehicleMonthlyPayments(Integer year, Integer month, Float totalMonthlyPayment) {
		super();
		this.year = year;
		this.month = month;
		this.totalMonthlyPayment = totalMonthlyPayment;
	}
	/**
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
	}
	/**
	 * @return the month
	 */
	public Integer getMonth() {
		return month;
	}
	/**
	 * @param month the month to set
	 */
	public void setMonth(Integer month) {
		this.month = month;
	}
	/**
	 * @return the totalMonthlyPayment
	 */
	public Float getTotalMonthlyPayment() {
		return totalMonthlyPayment;
	}
	/**
	 * @param totalMonthlyPayment the totalMonthlyPayment to set
	 */
	public void setTotalMonthlyPayment(Float totalMonthlyPayment) {
		this.totalMonthlyPayment = totalMonthlyPayment;
	}
	@Override
	public String toString() {
		return "vehicleMonthlyPayments [year=" + year + ", month=" + month + ", totalMonthlyPayment="
				+ totalMonthlyPayment + "]";
	}
    
    




























}
