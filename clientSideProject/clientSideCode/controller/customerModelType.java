package controller;

public class customerModelType {
	
	private ModelType type;
	private float fuelPrice;
	private float fuelAmount;
	private float maxFuelPrice;
	private float finalPrice;
	

	public customerModelType(ModelType type, float fuelAmount, float fuelPrice, float maxFuelPrice) {
		this.type = type;
		this.fuelAmount = fuelAmount;
		this.fuelPrice = fuelPrice;
		this.maxFuelPrice = maxFuelPrice;
		
		switch (this.type)
		{
		
		//The price for 1L of fuel is the maxFuelPrice
		case NormalFueling : {
			finalPrice = maxFuelPrice * fuelAmount;
		}break;
		
		//finalPrice - - is the price that marketingManager set.
		//In this model, in the end of the month, the customer pay for the total fuel.
		case RegularMonthlySubscriptionOneCar : {
			//finalPrice = fuelPrice * fuelAmount;
			finalPrice = (float) ((fuelPrice * fuelAmount)*0.96);
		}break;
		
		//fuelPrice - is the price that marketingManager set
		//In this model,in the end of the month, the customer pay for the total fuel after
		//get discount 10% from the total price of fueling.
		case RegularMonthlySubscriptionSomeCars : {
			//finalPrice = fuelPrice * fuelAmount;
			finalPrice = (float) (((fuelPrice * fuelAmount)*0.96)*0.9);
		}break;
		
		case FullMonthlySubscriptionOneCar : {
			finalPrice = (float) ((((fuelPrice * fuelAmount)*0.96)*0.9)*0.97);
		}break;
		
		default:
			break;
		
		}
		
		
	}
	
	/**
	 * @return the finalPrice
	 */
	public float getFinalPrice() {
		return finalPrice;
	}
	
	
}
