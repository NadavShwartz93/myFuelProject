package logic;

import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;

public class OrderHistory {
	
	public LocalDate orderDate;
	public String amount;
	public LocalDate deliveryDate;
	public String orderStatus;
	/**
	 * @param orderDate
	 * @param amount
	 * @param deliveryDate
	 * @param orderStatus
	 */
	public OrderHistory(LocalDate orderDate, String amount, LocalDate deliveryDate, String orderStatus) {
		super();
		this.orderDate = orderDate;
		this.amount = amount;
		this.deliveryDate = deliveryDate;
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
		return "OrderHistory [orderDate=" + orderDate + ", amount=" + amount + ", deliveryDate=" + deliveryDate
				+ ", orderStatus=" + orderStatus + "]";
	}
}
