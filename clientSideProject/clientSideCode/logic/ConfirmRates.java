package logic;

public class ConfirmRates {
	private String fuelType;
	private String priceType;
	private float priceBefore;
	private float priceAfter;
	
	public ConfirmRates(String fuelType, String priceType, float priceBefore, float priceAfter) {
		super();
		this.fuelType = fuelType;
		this.priceType = priceType;
		this.priceBefore = priceBefore;
		this.priceAfter = priceAfter;
	}

	public String getFuelType() {
		return fuelType;
	}

	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public float getPriceBefore() {
		return priceBefore;
	}

	public void setPriceBefore(float priceBefore) {
		this.priceBefore = priceBefore;
	}

	public float getPriceAfter() {
		return priceAfter;
	}

	public void setPriceAfter(float priceAfter) {
		this.priceAfter = priceAfter;
	}
	

}
