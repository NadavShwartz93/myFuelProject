package logic;

import java.time.LocalDate;

public class HomeFuelOrder {
	
	public LocalDate orderDate;
	public String amount;
	public LocalDate deliveryDate;
	public String customerId;
	public String customerFirstName;
	public String customerLastName;
	public String customerAddress;
	public int purchaseNumber;
	public String orderStatus;
	
	/**
	 * @param orderDate
	 * @param amount
	 * @param deliveryDate
	 * @param customerId
	 * @param customerFirstName
	 * @param customerLastName
	 * @param customerAddress
	 */
	public HomeFuelOrder(LocalDate orderDate, String amount, LocalDate deliveryDate, String customerId,
			String customerFirstName, String customerLastName, String customerAddress, int purchaseNumber, 
			String orderStatus) {
		super();
		this.orderDate = orderDate;
		this.amount = amount;
		this.deliveryDate = deliveryDate;
		this.customerId = customerId;
		this.customerFirstName = customerFirstName;
		this.customerLastName = customerLastName;
		this.customerAddress = customerAddress;
		this.purchaseNumber = purchaseNumber;
		this.orderStatus = orderStatus;
	}

	/**
	 * @return the orderDate
	 */
	public LocalDate getOrderDate() {
		return orderDate;
	}

	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	/**
	 * @return the amount
	 */
	public String getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}

	/**
	 * @return the deliveryDate
	 */
	public LocalDate getDeliveryDate() {
		return deliveryDate;
	}

	/**
	 * @param deliveryDate the deliveryDate to set
	 */
	public void setDeliveryDate(LocalDate deliveryDate) {
		this.deliveryDate = deliveryDate;
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
	 * @return the customerFirstName
	 */
	public String getCustomerFirstName() {
		return customerFirstName;
	}

	/**
	 * @param customerFirstName the customerFirstName to set
	 */
	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}

	/**
	 * @return the customerLastName
	 */
	public String getCustomerLastName() {
		return customerLastName;
	}

	/**
	 * @param customerLastName the customerLastName to set
	 */
	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}

	/**
	 * @return the customerAddress
	 */
	public String getCustomerAddress() {
		return customerAddress;
	}

	/**
	 * @param customerAddress the customerAddress to set
	 */
	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}
	
	

	/**
	 * @return the purchaseNumber
	 */
	public int getPurchaseNumber() {
		return purchaseNumber;
	}

	/**
	 * @param purchaseNumber the purchaseNumber to set
	 */
	public void setPurchaseNumber(int purchaseNumber) {
		this.purchaseNumber = purchaseNumber;
	}
	
	

	/**
	 * @return the orderStatus
	 */
	public String getOrderStatus() {
		return orderStatus;
	}

	/**
	 * @param orderStatus the orderStatus to set
	 */
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Override
	public String toString() {
		return "HomeFuelOrder [orderDate=" + orderDate + ", amount=" + amount + ", deliveryDate=" + deliveryDate
				+ ", customerId=" + customerId + ", customerFirstName=" + customerFirstName + ", customerLastName="
				+ customerLastName + ", customerAddress=" + customerAddress + ", purchaseNumber=" + purchaseNumber
				+ ", orderStatus=" + orderStatus + "]";
	}

	

	
	

}
