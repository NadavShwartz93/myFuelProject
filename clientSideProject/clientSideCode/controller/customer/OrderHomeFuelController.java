package controller.customer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.CheckComponentInput;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import emClient.ClientHandlerSingle;
import controller.MainDesplayController;


/**This class is for manage the OrderHomeFuel.fxml file.
 * the class is manage the process of getting the data from the customer window, 
 * and transfer it to the OrderHomeFuelPriceController.java .
 * @author Nadav Shwartz.
 *
 */
public class OrderHomeFuelController implements Initializable {
	
	public ClientHandlerSingle chs;
	public OrderHomeFuelController ohfc;
	public OrderHomeFuelPriceController ohfpc;


	/**Is the constructor of this class.
	 */
	public OrderHomeFuelController() {
		chs = ClientHandlerSingle.getInstance();
		chs.setOhfc(this);
		ohfc = this;
	}
    @FXML
    private RadioButton rdRegularOrderType;
    @FXML
    private RadioButton rdUrgentOrderType;
    @FXML
    private TextField FuelAmountTxt;
    @FXML
    private DatePicker DeliveryDateSelection;
    @FXML
    private Button ContinueBtn;
    @FXML
    private TextField AddressTxt;
    @FXML
    private Label lblFuelAmountErr;
    @FXML
    private Label lblAddressErr;
    @FXML
    private Label lblDeliveryDateErr;
    
	private ToggleGroup radioGroup;
	private float homeFuelPrice = 0;
	private float maxHomeFuelPrice = 0;
	public static ArrayList<String> list = new ArrayList<String>();



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		getHomeFuelPrice();
		restrictDatePicker();
		setToggleGroup();
		clearErrors();
	}
	
	/**
	 * create a call to the DB to get the current fuel price 
	 * and the max home fuel price  from  the fuel table in the DB.
	 * The call return to the meted setHomeFuelPrice(float x,float y).
	 */
	public void getHomeFuelPrice() {
		ArrayList<Object> answer = new ArrayList<>(); 
		answer.add("customer/OrderHomeFuel");
		answer.add("/getHomeFuelPrice");
		chs.client.handleMessageFromClientUI(answer);
	}
	

	/**This method get two float values and set them in to {@link OrderHomeFuelController} fields.
	 * @param homeFuelPrice contains the home fuel price to set in to {@link #homeFuelPrice}.
	 * @param maxHomeFuelPrice - contains the max home fuel price to set in to {@link #maxHomeFuelPrice}.
	 */
	public void setHomeFuelPrice(float homeFuelPrice, float maxHomeFuelPrice) {
		this.homeFuelPrice = homeFuelPrice;
		this.maxHomeFuelPrice = maxHomeFuelPrice;
	}
	
	/**
	 * Restrict the {@link #DeliveryDateSelection} to show only future dates.
	 */
	private void restrictDatePicker() {
		LocalDate currentDate = LocalDate.now(); //Getting the current date.
		int year = currentDate.getYear();
		int month = currentDate.getMonth().getValue();
		int dayOfMonth = currentDate.getDayOfMonth();

		LocalDate minDate = LocalDate.of(year, month, dayOfMonth);
		
		DeliveryDateSelection.setDayCellFactory(datePicker ->
		           new DateCell() {
		               @Override public void updateItem(LocalDate item, boolean empty) {
		                   super.updateItem(item, empty);
		                   if(radioGroup.getSelectedToggle().equals(rdUrgentOrderType)) {
		                	   setDisable(true);
		                   }else if(radioGroup.getSelectedToggle().equals(rdRegularOrderType)) {
			                   setDisable(item.isBefore(minDate.plusDays(1)));		                	   
		                   }
		               }});
		
	}
	
	
	/**this method set the {@link #radioGroup} to contain two RadioButtons:
	 * {@link #rdUrgentOrderType} and {@link #rdRegularOrderType}.
	 */
	private void setToggleGroup() {
		radioGroup = new ToggleGroup();
		rdRegularOrderType.setToggleGroup(radioGroup);
		rdUrgentOrderType.setToggleGroup(radioGroup);
		
		//Set the rdUrgentOrderType radio button to be on urgent order.
		rdUrgentOrderType.setSelected(true);
		DeliveryDateSelection.setValue(LocalDate.now()); //After order type select , the previous selected date will delete.
	}
	
	/**This method set the {@link #DeliveryDateSelection} state according to which RadioButton 
	 * {@link #rdRegularOrderType} or {@link #rdUrgentOrderType} selected.
	 * @param event - ActionEvent
	 */
	@FXML
	public void setDateAccordingToRadio(ActionEvent event) {
		if(radioGroup.getSelectedToggle().equals(rdUrgentOrderType)) {
			DeliveryDateSelection.setValue(LocalDate.now());
			CheckComponentInput.notEmptyDatePickerValidation(DeliveryDateSelection, lblDeliveryDateErr);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblFuelAmountErr.setText("");
					lblAddressErr.setText("");
				}
			});
		}else {
			DeliveryDateSelection.getEditor().clear(); //Clear the datePicker textField
			DeliveryDateSelection.setValue(null); //Set the value of the datePicker to null.
			
			clearErrors();
		}
	}

	

	/**This method is for the {@link #ContinueBtn} Button, just if {@link #validation()} method return true,
	 * then this window will be replace by OrderHomeFuelPrice.fxml .
	 * @param event - ActionEvent
	 */
	@FXML
	public void ContinueBtn(ActionEvent event) {
		if(validation()) {
			clearErrors();
			updateList();
		}
		

	}
	
	/**
	 * This method collect all the data from the customer and send it to OrderHomeFuelPrice.fxml 
	 * which is manage by OrderHomeFuelPriceController.java.
	 */
	private void updateList() {
		
		String tempfuelAmount = FuelAmountTxt.getText();
		tempfuelAmount = String.format("%s Liters", tempfuelAmount);
		
		
		float fuelAmount = Float.parseFloat(FuelAmountTxt.getText()); //Getting the amount of fuel from the user.
		float fuelCost = 0;
		
		String address = AddressTxt.getText();
		LocalDate deliveryDate = DeliveryDateSelection.getValue();
		
		
		if(radioGroup.getSelectedToggle().equals(rdRegularOrderType)) {//This invitation is not urgent.
			list.add(deliveryDate.toString()); 
			
			list.add(tempfuelAmount);
			fuelCost = calcFuelFinalCost(fuelAmount);
			String finCost = String.format("%.2f $", fuelCost);

			list.add(finCost);
			
			String additionalFees = calcFees("regular", fuelAmount);
			list.add(additionalFees);
			
			finCost = String.format("%.2f", fuelCost);
			list.add(finCost);
			list.add("regular");
			
		}else {//This invitation is urgent.
			list.add(LocalDate.now().toString()); //The shipping date will be today.
	
			
			list.add(tempfuelAmount);
			fuelCost = calcFuelFinalCost(fuelAmount);
			String tmp = String.format("%.2f $", fuelAmount * homeFuelPrice);
			list.add(tmp);
			
			String additionalFees = calcFees("urgent", fuelAmount);
			list.add(additionalFees);
			String finCost = String.format("%.2f", fuelCost);
			list.add(finCost);
			list.add("urgent");
			
		}
		
		list.add(address);
		list.add(FuelAmountTxt.getText());
		
		changeWindow();
	}
	
	/**
	 * Change this window to OrderHomeFuelPrice.fxml window.
	 */
	private void changeWindow() {
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
    	try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainDesplayController.class.getResource("/fxml/customer/OrderHomeFuelPrice.fxml"));
			AnchorPane mainItems;
			mainItems = loader.load();
			MainDesplayController.mainBorderPane.setCenter(mainItems);

			
			} catch(Exception e) {
				e.printStackTrace();
			}
			}
    	});
		
	}
	
	
	/**This method calculate the total fees that the customer should get, and return it as {@link String}.
	 * @param isUrgent contain {@link String} value that represent the selected RadioButton.
	 * @param fuelAmount contain float number that represent the fuel amount the customer selected.
	 * @return return {@link String} that contain all the fees that the customer should get.
	 */
	private String calcFees(String isUrgent, float fuelAmount) {
		String str;
		if(isUrgent.equals("urgent")) {
			if(fuelAmount >= 600 && fuelAmount <= 800)
				str = "Extra 2% for the fuel price - Urgent order\n"
						+ "Discount of 3% for the fuel amount";
			else if(fuelAmount > 800)
				str = "Extra 2% for the fuel price - Urgent order\n"
						+ "Discount of 4% for the fuel amount";
			else
				str = "Extra 2% for the fuel price - Urgent order";
		}else {
			if(fuelAmount >= 600 && fuelAmount <= 800) {
				str = "Discount of 3% for the fuel amount";
			}
			else if(fuelAmount > 800)
				str = "Discount of 4% for the fuel amount";
			else
				str = String.format("0.0 $");
		}
		return str;
	}

	/**This method calculate the final cost of the customer order to home fuel. 
	 * @param fuelAmount the amount of fuel from the user.
	 * @return the fuel cost that adjusted to the invitation type.
	 */
	private float calcFuelFinalCost(float fuelAmount) {
		float fuelCost = 0;
		if(radioGroup.getSelectedToggle().equals(rdRegularOrderType)) {//This invitation is not urgent.
			if(fuelAmount >= 600 && fuelAmount <= 800) {
				fuelCost = (float) ((fuelAmount * homeFuelPrice)*0.97);
			}else if(fuelAmount > 800) {
				fuelCost = (float) ((fuelAmount * homeFuelPrice)*0.96);
			}else
				fuelCost = (fuelAmount * homeFuelPrice);
			
			return fuelCost;
		}else //This invitation is urgent.
			//Add the fees for this urgent invitation.
			fuelCost = (float) ((fuelAmount * homeFuelPrice) + (homeFuelPrice * 0.02)); 
			if(fuelAmount >= 600 && fuelAmount <= 800)
				fuelCost = (float) (fuelCost * 0.97);
			else if (fuelAmount > 800)
				fuelCost = (float) (fuelCost * 0.96);
			
			return fuelCost;
	}
	
	/**This method make validation test to all the GUI component in this window.
	 * The method call {@link CheckComponentInput} for the validation test.
	 * @return return true if and only if the customer insert all the text field correctly.
	 */
	private boolean validation() {
		boolean isInputValid = true;
		
		//Make validation for the fuel amount text field.
		if(!CheckComponentInput.numericTextFieldValidation(FuelAmountTxt, lblFuelAmountErr))
			isInputValid = false;
		else if(true) {//FuelAmountTxt contain number.
			float fuelAmount = Float.parseFloat(FuelAmountTxt.getText());
			if(fuelAmount == 0 || fuelAmount < 0) {
				isInputValid = false;
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						lblFuelAmountErr.setText("Error Fuel Amount");
					}
				});
			}
		}

		
		
		
		//Make validation for the address text field
		if(!CheckComponentInput.notEmptyTextFieldValidation(AddressTxt, lblAddressErr))
			isInputValid = false;
		
		String orderType;
		if(radioGroup.getSelectedToggle().equals(rdRegularOrderType))
			orderType = "Regular";
		else
			orderType = "Urgent";

		
		if(!CheckComponentInput.notEmptyDatePickerValidation(DeliveryDateSelection, lblDeliveryDateErr)) {
			isInputValid = false;
		}else if(!CheckComponentInput.DatePickerValueValidation(DeliveryDateSelection, orderType, lblDeliveryDateErr)) {
			isInputValid = false;
		}
		
		

		
		return isInputValid;
	}
	
	/**
	 * Clears the errors of {@link #lblAddressErr}, {@link #lblDeliveryDateErr}, {@link #lblDeliveryDateErr}
	 * labels components.
	 */
	private void clearErrors() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				lblFuelAmountErr.setText("");
				lblAddressErr.setText("");
				lblDeliveryDateErr.setText("");
			}
		});
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}