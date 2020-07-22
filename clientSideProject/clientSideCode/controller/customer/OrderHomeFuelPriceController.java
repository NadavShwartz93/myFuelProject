package controller.customer;

import javafx.application.Platform;
import javafx.beans.binding.When;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import logic.CheckComponentInput;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

import controller.MainDesplayController;
import emClient.ClientHandlerSingle;

/**This class show the customer order details. The customer can approve the order by press {@link #placeOrderBtn}
 * or to cancel the order by press the {@link #previousBtn} and return to the previous window.
 * @author Nadav Shwartz.
 *
 */
public class OrderHomeFuelPriceController implements Initializable  {
	
	public ClientHandlerSingle chs;
	public OrderHomeFuelPriceController ohfp;
	public OrderHomeFuelController ohfc;

	public OrderHomeFuelPriceController() {
		chs = ClientHandlerSingle.getInstance();
		chs.setOhfpc(this);
		ohfp = this;
	}

    @FXML
    private Button placeOrderBtn;
    @FXML
    private Button previousBtn;
    @FXML
    private Label lblShippingDate;
    @FXML
    private Label lblFuelAmount;
    @FXML
    private Label lblFuelCost;
    @FXML
    private Label labAdditionalFees;
    @FXML
    private Label lblTotalCost;
    @FXML
    private Label lblOrderSuccess;
    @FXML
    private Label lblOrderFaild;
    @FXML
    private Label lblCreditCardErr;
    @FXML
    private Label lblCvvNumberErr;
    @FXML
    private TextField txtCreditCard;
    @FXML
    private TextField txtCvv;
    @SuppressWarnings("rawtypes")
	@FXML
    private ChoiceBox cmbMonth;
    @SuppressWarnings("rawtypes")
	@FXML
    private ChoiceBox cmbYear;
    private String orderType;
    private String fuelAmount;
    private String Address;
	private Hashtable<String, String> customerRegisteredCreditCard = new Hashtable<>();
	Integer numberOfClickOnPlaceOrderButton;



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		getCustomerData();
		setDataInLabel();	
        
        orderType = OrderHomeFuelController.list.get(5);
        Address = OrderHomeFuelController.list.get(6);//new
        fuelAmount = OrderHomeFuelController.list.get(7);
        setMonthChoiceBox();
        setYearChoiceBox();
        setLabel();
        clearErrors();
        setCustomerCreditCardInFields();
        
        OrderHomeFuelController.list.clear(); // Clear the list for the next home fuel invention.
        numberOfClickOnPlaceOrderButton = 0;
	}
	
	/**This method set data to Labels, form ArrayList that is located in {@link OrderHomeFuelController}.
	 */
	private void setDataInLabel() {
		lblShippingDate.setText(OrderHomeFuelController.list.get(0));
        lblFuelAmount.setText(OrderHomeFuelController.list.get(1));
        lblFuelCost.setText(OrderHomeFuelController.list.get(2));
        labAdditionalFees.setText(OrderHomeFuelController.list.get(3)); 
        lblTotalCost.setText(OrderHomeFuelController.list.get(4));	
	}
	
	/**This method is call the server side to bring some relevant data.
	 */
	private void getCustomerData() {
		ArrayList<Object> answer = new ArrayList<>();

		answer.add("customer/OrderHomeFuel");
		answer.add("/getCustomerCreditCard");
		answer.add(chs.customerid); //Send the customer id to the server.
		chs.client.handleMessageFromClientUI(answer); 		
	}
	
	@SuppressWarnings("unchecked")
	public void setCustomerData(ArrayList<Object> message) {
		ArrayList<Object> newMsg = (ArrayList<Object>) message.get(0);
		//Getting customer registered credit card
		for(int i = 0; i < newMsg.size(); i++) {
			String temp = newMsg.get(i).toString();
			if(i == 0)
				customerRegisteredCreditCard.put("creditCardNumber", temp);
			else if(i == 1)
				customerRegisteredCreditCard.put("creditCard_CVV", temp);
			else if(i == 2)
				customerRegisteredCreditCard.put("creditCard_Month", temp);
			else {
		        LocalDate localDate = LocalDate.parse(temp);
		        temp = String.format("%d", localDate.getYear());
		        //temp =  String.valueOf(localDate.getYear());
				customerRegisteredCreditCard.put("creditCard_year", temp);	

			}
		}
	}
	
	/**
	 * This method insert the customer credit card ,that received from the server, 
	 * to the relevant fields: {@link #txtCreditCard}, {@link #txtCvv}, 
	 * {@link #cmbMonth}, {@link #cmbYear} and then display them on the screen.
	 */
	private void setCustomerCreditCardInFields() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				String creditCardNumber = customerRegisteredCreditCard.get("creditCardNumber");
				txtCreditCard.setText(creditCardNumber);
				txtCreditCard.setDisable(false);
			}
		});
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				String creditCard_CVV = customerRegisteredCreditCard.get("creditCard_CVV");
				txtCvv.setText(creditCard_CVV);
				txtCvv.setDisable(false);
			}
		});
		
		Platform.runLater(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				String creditCard_Month = customerRegisteredCreditCard.get("creditCard_Month");
				cmbMonth.setValue(creditCard_Month);
				cmbMonth.setDisable(false);
			}
		});
		
		Platform.runLater(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				String creditCard_year = customerRegisteredCreditCard.get("creditCard_year").toString();
				cmbYear.setValue(creditCard_year);
				cmbYear.setDisable(false);
			}
		});
	}
	
	
	
	/**This method set text of {@link #lblOrderSuccess} and {@link #lblOrderFaild} to be empty {@link String}.
	 */
	private void setLabel() {
		lblOrderSuccess.setText("");
		lblOrderFaild.setText("");
	}
	
	/**This method set the values of the {@link #cmbMonth}.
	 */
	@SuppressWarnings("unchecked")
	private void setMonthChoiceBox() {
		for(int i = 1; i <= 12; i++) {
			String temp = String.format("%d", i);
			cmbMonth.getItems().add(temp);
		}
	}
	
	
	/**This method set the values of {@link #cmbYear}.
	 */
	@SuppressWarnings("unchecked")
	private void setYearChoiceBox() {
		for(int i = 2020; i <= 2040; i++) {
			String temp = String.format("%d", i);
			cmbYear.getItems().add(temp);
		}
	}
	
    /** This method call to {@link #sendDataToServer()}  
     * just if {@link #validation()} return true.
     * @param event is the event of press the {@link #placeOrderBtn}.
     * 
     */
    @FXML
    public void placeOrderBtn(ActionEvent event) {	
    	if(validation()) {
    		clearErrors();
    		numberOfClickOnPlaceOrderButton++;
    		if(numberOfClickOnPlaceOrderButton > 1) {
    			Platform.runLater(new Runnable() {
    				@Override
    				public void run() {
    					lblOrderSuccess.setText("Order already sent");
    					lblOrderSuccess.setTextFill(Color.web("#ff0000"));
    				}
    			});
    		}else {
        		sendDataToServer();
    		}
    	}
    }
    
    
    /**This method send the customer oder details to homefuelpurchases table in the DB.
     */
    private void sendDataToServer() {
		ArrayList<Object> answer = new ArrayList<>();

		answer.add("customer/OrderHomeFuel");
		answer.add("/insertHomeFuelOrder");
		
		answer.add(chs.customerid); //The customer id who ordered home fuel.
		answer.add(LocalDate.now());	//Getting the current date - this will be the order date.
		answer.add(lblShippingDate.getText()); //This will be the delivery date - depends on the customer choose.
		answer.add(getDeliveryTime()); //This will be the delivery time.
		answer.add(fuelAmount); //The home fuel amount. 
		answer.add("Order sent"); //Represent the order arrival status.
		answer.add(Address); //This is the address of the order.
				
		chs.client.handleMessageFromClientUI(answer); 
		
    }
    
    /**if the customer want to return the order home fuel page - 
     * this method will change this window to OrderHomeFuel.fxml window.
     * @param event is the event of press the {@link #previousBtn}.
     */
    @FXML
    public void previousWindow(ActionEvent event) { 
		//Change this window to OrderHomeFuel.fxml window.
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
    	try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainDesplayController.class.getResource("/fxml/customer/OrderHomeFuel.fxml"));
			AnchorPane mainItems;
			mainItems = loader.load();
			MainDesplayController.mainBorderPane.setCenter(mainItems);

			
			} catch(Exception e) {
				e.printStackTrace();
			}
			}
    	});
    }

	/**
	 * @return the delivery time - depends on the customer order type;
	 */
	private LocalTime getDeliveryTime() {
		Random rand = new Random();

		int hour;
		if(orderType.equals("urgent")) {
			int currentHour = LocalTime.now().getHour();
			//The customer will get the order within 6 hour.
			if((currentHour + 1) > 23) {
				hour = ThreadLocalRandom.current().nextInt(currentHour, currentHour);  
			}else if((currentHour + 2) > 23) {
				hour = ThreadLocalRandom.current().nextInt(currentHour, currentHour + 1);  
			}else if((currentHour + 3) > 23) {
				hour = ThreadLocalRandom.current().nextInt(currentHour, currentHour + 2);  
			}else
				hour = ThreadLocalRandom.current().nextInt(currentHour, currentHour + 3);  
				
		}else
			hour = ThreadLocalRandom.current().nextInt(8, 19); 
		int min = rand.nextInt(59);
		int sec = rand.nextInt(59);
		
		return LocalTime.of(hour, min, sec);
	}
	
	
	/**
	 * check if the request for home fuel passed successfully.
	 * @param message contain the relevant data that come back from the server side.
	 */
	public void HandleData(String message) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
    	try {
			
    		if(message.equals("Success - order Home fuel has been successfully passed")) {
    			lblOrderSuccess.setText("Order successfully made");
    			lblOrderSuccess.setVisible(true);
    			
    		}
			
			} catch(Exception e) {
				e.printStackTrace();
			}
			}
    	});
	}
	
	/**
	 * @return return true if and only if the customer credit card fields are validated.
	 */
	private boolean validation(){
		boolean isInputValid = true;
		
		//Make validation for the credit card number.
		if(!CheckComponentInput.numericTextFieldValidation(txtCreditCard, lblCreditCardErr))
			isInputValid = false;
		
		//Make validation for the credit card's Cvv number.
		if(!CheckComponentInput.numericTextFieldValidation(txtCvv, lblCvvNumberErr))
			isInputValid = false;
		
	return isInputValid;
	}
	
	/**
	 * Clears all label errors include: {@link #lblCreditCardErr} and {@link #lblCvvNumberErr}.
	 */
	private void clearErrors() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				lblCreditCardErr.setText("");
				lblCvvNumberErr.setText("");
			}
		});
	}

}
