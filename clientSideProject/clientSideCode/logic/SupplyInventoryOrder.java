package logic;

public class SupplyInventoryOrder {
	
	public String gasStationName;
	public String gasStationAddress;
	public String fuelType;
	public Integer fuelAmount;
	

	public SupplyInventoryOrder(String gasStationName, String gasStationAddress, String fuelType, Integer fuelAmount) {
		super();
		this.gasStationName = gasStationName;
		this.gasStationAddress = gasStationAddress;
		this.fuelType = fuelType;
		this.fuelAmount = fuelAmount;
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
		this.gasStationName = gasStationName;
	}

	/**
	 * @return the gasStationAddress
	 */
	public String getGasStationAddress() {
		return gasStationAddress;
	}

	/**
	 * @param gasStationAddress the gasStationAddress to set
	 */
	public void setGasStationAddress(String gasStationAddress) {
		this.gasStationAddress = gasStationAddress;
	}

	/**
	 * @return the fuelType
	 */
	public String getFuelType() {
		return fuelType;
	}

	/**
	 * @param fuelType the fuelType to set
	 */
	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}

	/**
	 * @return the fuelAmount
	 */
	public int getFuelAmount() {
		return fuelAmount;
	}

	/**
	 * @param fuelAmount the fuelAmount to set
	 */
	public void setFuelAmount(Integer fuelAmount) {
		this.fuelAmount = fuelAmount;
	}

	@Override
	public String toString() {
		return "SupplyInventoryOrder [gasStationName=" + gasStationName + ", gasStationAddress=" + gasStationAddress
				+ ", fuelType=" + fuelType + ", fuelAmount=" + fuelAmount + "]";
	}
	
	
	
	

}
