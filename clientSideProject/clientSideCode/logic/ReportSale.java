package logic;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReportSale {
	
	private String saleName;
	private String fuelType;
	private Float discount;
	private LocalTime startHour;
	private LocalTime endHour;
	private LocalDate startDate;
	private LocalDate endDate;
	
	public ReportSale(String saleName, String fuelType, Float discount, LocalTime startHour, LocalTime endHour,
			LocalDate startDate, LocalDate endDate) {
		super();
		this.saleName = saleName;
		this.fuelType = fuelType;
		this.discount = discount;
		this.startHour = startHour;
		this.endHour = endHour;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public String getSaleName() {
		return saleName;
	}

	public void setSaleName(String saleName) {
		this.saleName = saleName;
	}

	public String getFuelType() {
		return fuelType;
	}

	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}

	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	public LocalTime getStartHour() {
		return startHour;
	}

	public void setStartHour(LocalTime startHour) {
		this.startHour = startHour;
	}

	public LocalTime getEndHour() {
		return endHour;
	}

	public void setEndHour(LocalTime endHour) {
		this.endHour = endHour;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "ReportSale [saleName=" + saleName + ", fuelType=" + fuelType + ", discount=" + discount + ", startHour="
				+ startHour + ", endHour=" + endHour + ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}
	
	

}
