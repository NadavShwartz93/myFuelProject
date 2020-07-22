package logic;

import java.time.LocalTime;

public class Sales {
	public String saleName;
	public String fuelType;
	public Float discount;
	public LocalTime startHour;
	public LocalTime endHour;
	public Boolean isActive;

	public Sales(String saleName, String fuelType, Float discount, LocalTime startHour, LocalTime endHour,
			Boolean isActive) {
		super();
		this.saleName = saleName;
		this.fuelType = fuelType;
		this.discount = discount;
		this.startHour = startHour;
		this.endHour = endHour;
		this.isActive = isActive;
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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}
