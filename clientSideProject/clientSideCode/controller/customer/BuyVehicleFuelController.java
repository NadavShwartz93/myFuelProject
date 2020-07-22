package controller.customer;

import java.awt.event.KeyListener;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;

import controller.ModelType;
import controller.customerModelType;
import logic.*;

import emClient.ClientHandlerSingle;
import javafx.application.Platform;
import javafx.beans.binding.When;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;


/**This class is for manage the BuyVehicleFuel.fxml file. 
 * the class is manage all the process of buying fuel for the vehicle.
 * @author Nadav Shwartz.
 *
 */
public class BuyVehicleFuelController implements Initializable  {
	
	public ClientHandlerSingle chs;
	public BuyVehicleFuelController bvfc;
	
	public BuyVehicleFuelController() {
		chs = ClientHandlerSingle.getInstance();
		chs.setBvfc(this);
		bvfc = this;
	}
	
	@FXML
	private TextField carNumberTxt;
	@FXML
	private TextField fuelAmountTxt;
	@FXML
	private TextField txtCreditCardNew;
	@FXML
	private TextField txtCvv;
	@FXML
	private RadioButton rdRegisteredCreditCard;
	@FXML
	private RadioButton rdCash;
	@FXML
	private ChoiceBox cmbMonth;
	@FXML
	private ChoiceBox cmbYear;
	@FXML
	private ComboBox cmbFuelType;
	@FXML
	private ComboBox cmbGasStationCompany;
	@FXML
	private ComboBox cmbGasStationName;
	@FXML
	private Label lblEstimatedPrice;
	@FXML
	private Button buyFuelBtn;
	@FXML
	private Label lblFuelingRes;
	@FXML
	private Label lblFuelingRes1;
	@FXML
	private Label lblFuelingRes2;
	@FXML
	private Label lblCarNumErr;
	@FXML
	private Label lblFuelTypeErr;
	@FXML
	private Label lblFuelAmountErr;
	@FXML
	private Label lblGasStationNameErr;
	@FXML
	private Label lblCreditCardErr;
	@FXML
	private Label lblCvvNumberErr;
	@FXML
	private Label lblMonthErr;
    @FXML
    private Button FuelPriceInfolbl;
    @FXML
    private ChoiceBox<String> cbPumpNumber;
    @FXML
    private Button btnFind;
    
	
	private ToggleGroup radioGroup;
	

	public ObservableList<String> fuelTypeObservableList;
	
	public ObservableList<String> gasStationNameObservableList;
	
	public String radioButtonSelectedStr = null, fuelMsgDetails = "";
	private ArrayList<String> customerPurchasePlan = new ArrayList<String>();
	private Hashtable<String, ArrayList<Float>> fuelTypePrice  = new Hashtable<>();
	private Hashtable<String, Float> fuelSales  = new Hashtable<>();
	private Hashtable<String, String> customerRegisteredCreditCard = new Hashtable<>();
	private String customerModelType;
	private ArrayList<String> gasStationCompany = new ArrayList<String>();	
	private ArrayList<ArrayList<Object>> gasStationCompanyAndGasStation = new ArrayList<>();
	private ArrayList<ArrayList<Object>> fuelTypesOfCustomer = new ArrayList<>();
	private int numberOfPump = 3;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		setToggleGroup();
		setPumpNumber();
		setMonthChoiceBox();
		setYearChoiceBox();
		setFuelType();
		setComponentDisableSituation(true);
		lblFuelingRes.setText("Fueling successfully made");
		lblFuelingRes.setVisible(false);
		
		lblFuelingRes1.setText("The car isn't registered");
		lblFuelingRes1.setTextFill(Color.web("#ff0000"));
		lblFuelingRes1.setVisible(false);
		
		lblFuelingRes2.setText("There isn't enough fuel at this gas station");
		lblFuelingRes2.setTextFill(Color.web("#ff0000"));
		lblFuelingRes2.setVisible(false);
		
		lblEstimatedPrice.setText("");
		
		 clearErrors();
		 FuelPriceInfolbl.getStyleClass().clear();
	}
	
    @FXML
    void btnFind(ActionEvent event) {
    	if(CheckComponentInput.numericTextFieldValidation(carNumberTxt, lblCarNumErr)) {
    		getCustomerData();
    	}
    }
    
    @FXML
    void carNumberTxt(ActionEvent event) {
    	if(CheckComponentInput.numericTextFieldValidation(carNumberTxt, lblCarNumErr)) {
    		getCustomerData();
    	}
    }


	/**
	 * Send to the server request for getting: 
	 * 1.The purchase plan of customer - 
	 * the names of the gas station company in his purchase plan.
	 * 2.The prices of the fuel types.
	 * 3.The customer model type.
	 * 4.The current sales.
	 * 5.The customer registered credit card.
	 */
	public void getCustomerData() {
		ArrayList<Object> answer = new ArrayList<>(); 
		answer.add("customer/BuyVehicleFuel");
		answer.add("/CustomerData");
		answer.add(carNumberTxt.getText().toString());
		
		LocalTime currentTime = LocalTime.now();
		answer.add(currentTime);
		
		chs.client.handleMessageFromClientUI(answer);
	}
	
	
	/**This method get all the relevant data from from server side, 
	 * and save it in different data structures in this class.
	 * @param message - contains all the data that received from the server.
	 */
	@SuppressWarnings("unchecked")
	public void setCustomerData(ArrayList<Object> message) {
		if(message.get(0).equals("VehicleNotExists")) {
			setLabelText(lblCarNumErr, "Vehicle Not Exists");
			setComponentDisableSituation(true);
		}
		else {
			setLabelText(lblCarNumErr, "");
			
			//Set the label to be invisible, for the case that user will press the buy fuel button twice, 
			//and the message to the screen will be different.
			lblFuelingRes.setVisible(false);
			lblFuelingRes1.setVisible(false);
			lblFuelingRes2.setVisible(false);
			
			setComponentDisableSituation(false);
			initializeGlobalData();
			
			String fuelType = null;
			Float fuelPrice = (float) 0, maxFuelPrice = (float)0;
			Float fuelDiscountPercent = (float) 0;
			
			ArrayList<Object> newMsg = (ArrayList<Object>) message.get(0);
			//Getting customer purchase plan.
			for(int i = 0; i < newMsg.size(); i++) 
				customerPurchasePlan.add(newMsg.get(i).toString());	
			
		
			newMsg = (ArrayList<Object>) message.get(1);
			//Getting all the fuel type and their price.
			for(int i = 0; i < newMsg.size(); i++) {  
				fuelType = (String) newMsg.get(i);
				fuelPrice = (Float) newMsg.get(i+1);
				maxFuelPrice = (Float) newMsg.get(i+2);
				ArrayList<Float> fuelPriceAndMaxFuelPrice = new ArrayList<>();
				fuelPriceAndMaxFuelPrice.add(fuelPrice);
				fuelPriceAndMaxFuelPrice.add(maxFuelPrice);
				fuelTypePrice.put(fuelType, fuelPriceAndMaxFuelPrice);
				i++;
				i++;
			}
			
			//Getting customer model type.
			customerModelType = message.get(2).toString();
			
			//Getting sales.
			newMsg = (ArrayList<Object>) message.get(3);
			for(int i = 0; i < newMsg.size(); i++) {
				fuelType = (String) newMsg.get(i);
				fuelDiscountPercent = fuelPrice = (Float) newMsg.get(i+1);
				fuelSales.put(fuelType, fuelDiscountPercent);
				i++;
			}
			
			
			newMsg = (ArrayList<Object>) message.get(4);
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
			
			//This method set credit card of customer in TextField.
			setCustomerCreditCard();
			
			//Set Gas Station Company and Gas Station.
			gasStationCompanyAndGasStation = (ArrayList<ArrayList<Object>>) message.get(5);
			if(gasStationCompanyAndGasStation.size() > 0)
				setGasStationCompany();//this method initialize the gas station company choiceBox.
						
			//Set car license number and there fuel types of customer
			fuelTypesOfCustomer = (ArrayList<ArrayList<Object>>) message.get(6);
			setFuelToCustomer();
		}
	}
	
	/**This method set all the component to be disable or to be not disable,
	 * depend on the boolean value bool.
	 * @param bool
	 */
	private void setComponentDisableSituation(Boolean bool) {
		cbPumpNumber.setDisable(bool);
		cmbFuelType.setDisable(bool);
		fuelAmountTxt.setDisable(bool);
		cmbGasStationCompany.setDisable(bool);
		cmbGasStationName.setDisable(bool);
		txtCreditCardNew.setDisable(bool);
		txtCvv.setDisable(bool);
		cmbMonth.setDisable(bool);
		cmbYear.setDisable(bool);
		buyFuelBtn.setDisable(bool);
		
		rdRegisteredCreditCard.setDisable(bool);
		rdCash.setDisable(bool);
	}
	
	private void setLabelText(Label label, String str) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				label.setText(str);
			}
		});
	}
	
	/**
	 * This method set the fuel type comboBox.
	 */
	@SuppressWarnings("unchecked")
	public void setFuelType() {
	ArrayList<String> fuelType = new ArrayList<String>();	
	fuelType.add("Petrol");
	fuelType.add("Diesel");
	fuelType.add("ScooterFuel");

	fuelTypeObservableList = FXCollections.observableArrayList(fuelType);
	cmbFuelType.setItems(fuelTypeObservableList);
	}
	
	
	/**This method initialize the global data structures. 
	 */
	private void initializeGlobalData() {
		customerPurchasePlan = new ArrayList<String>();
		fuelTypePrice = new Hashtable<String, ArrayList<Float>>();
		fuelSales = new Hashtable<String, Float>();
		customerRegisteredCreditCard = new Hashtable<String, String>();
		gasStationCompany = new ArrayList<String>();
	}
	
	
	/**This method make the component to be disable in case that the car number text field is empty.
	 * @param event KeyEvent
	 */
	@FXML
	void setCarNumberTextField(KeyEvent event) {
		//The carNumberTxt TextField is empty
		if(carNumberTxt.getText().isEmpty()) {
			setComponentDisableSituation(true);
			
			removeCustomerCreditCardInFieldse();

			cmbGasStationCompany.getSelectionModel().clearSelection();
			
			cmbFuelType.getSelectionModel().clearSelection();
			
			fuelAmountTxt.clear();
			
			lblEstimatedPrice.setText("");
			
			//Set the label to be invisible, for the case that user will press the buy fuel button twice, 
			//and the message to the screen will be different.
			lblFuelingRes.setVisible(false);
			lblFuelingRes1.setVisible(false);
			lblFuelingRes2.setVisible(false);

		}
		

	}
	
	/**This method set the fuel type comboBox of customer according to his fuel type.
	 */
	private void setFuelToCustomer() {
		Platform.runLater(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				ArrayList<Object> temp = new ArrayList<>();
				for (int i = 0; i < fuelTypesOfCustomer.size(); i++) {
					temp = fuelTypesOfCustomer.get(i);
					String carNumber = carNumberTxt.getText();
					
					if(carNumber.equals(temp.get(0).toString())) {
						
						cmbFuelType.setValue(temp.get(1).toString());				
					}
				}
			}
		});
		
		//The carNumberTxt TextField is empty
		if(carNumberTxt.getText().isEmpty())
			cmbFuelType.getSelectionModel().clearSelection();
	}
	
	@SuppressWarnings("unchecked")
	private void setPumpNumber() {
		String number = null;
		for (int i = 0; i < numberOfPump; i++) {
			number = String.format("%d", i+1);
			cbPumpNumber.getItems().add(number);

		}
		
		//set default value in pump number comboBox.
		cbPumpNumber.setValue("1");
	}
	
	@SuppressWarnings("unchecked")
	public void setGasStationCompany() {
	for (int i = 0; i < gasStationCompanyAndGasStation.size(); i++) {
			gasStationCompany.add((String) gasStationCompanyAndGasStation.get(i).get(0));
	}
	
	Platform.runLater(new Runnable() {
		@Override
		public void run() {
			ObservableList<String> gasStationCompanyObservableList = FXCollections.observableArrayList(gasStationCompany);
			cmbGasStationCompany.setItems(gasStationCompanyObservableList);
		}
	});


	}
	

	
	
	/**
	 * Description - set the gas stations for every  gas company.
	 * @param event
	 */	
	@FXML
    void setGasStationNameForGasCompany(ActionEvent event) {
		ArrayList<String> gasStationName = new ArrayList<String>();	

		String gasStationCompany =  (String)cmbGasStationCompany.getSelectionModel().getSelectedItem();
		
		String tempName = null;
		ArrayList<Object> tempArr = new ArrayList<>();
		for(int i = 0; i < gasStationCompanyAndGasStation.size(); i++) {
			//The name of gas Station company.
			tempName = (gasStationCompanyAndGasStation.get(i)).get(0).toString();
			
			if(tempName.equals(gasStationCompany)) {//It's a match.
			tempArr = (ArrayList<Object>) (gasStationCompanyAndGasStation.get(i));
				for (int j = 0; j < tempArr.size()-1; j++) {
					gasStationName.add(tempArr.get(j+1).toString());
				}
			}
		}
		
		gasStationNameObservableList = FXCollections.observableArrayList(gasStationName);
		cmbGasStationName.setItems(gasStationNameObservableList);
		
		//This method operate the calcFuelPrice method if the conditions statements are true. 
		operateCalcFuelPrice();
	}
		

	
	
	
	
	@SuppressWarnings("unchecked")
	private void setMonthChoiceBox() {
		for(int i = 1; i <= 12; i++) {
			String temp = String.format("%d", i);
			cmbMonth.getItems().add(temp);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void setYearChoiceBox() {
		for(int i = 2020; i <= 2040; i++) {
			String temp = String.format("%d", i);
			cmbYear.getItems().add(temp);
		}
	}
	
	@FXML
	private void setToggleGroup() {
		radioGroup = new ToggleGroup();
		rdRegisteredCreditCard.setToggleGroup(radioGroup);
		rdCash.setToggleGroup(radioGroup);
	}
	
	@FXML
	public void setRadioButton(ActionEvent event) {

		if(radioGroup.getSelectedToggle().equals(rdCash)) {
			removeCustomerCreditCardInFieldse();
		}else if(radioGroup.getSelectedToggle().equals(rdRegisteredCreditCard))
			setCustomerCreditCardInFields();
	}
		
	/**
	 * set the RadioButton of Registered credit card to be selected, and insert the customer 
	 * credit card data into the relevant fields.
	 */
	private void setCustomerCreditCard() {
		rdRegisteredCreditCard.setSelected(true);
		setCustomerCreditCardInFields();
	}
	
	/**
	 * Insert the customer credit card ,that received from the server, to the relevant fields on the screen.
	 */
	private void setCustomerCreditCardInFields() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				String creditCardNumber = customerRegisteredCreditCard.get("creditCardNumber");
				txtCreditCardNew.setText(creditCardNumber);
				txtCreditCardNew.setDisable(false);
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
	
	
	/**
	 * Set all the customer credit card fields to be clear and disable.  
	 */
	private void removeCustomerCreditCardInFieldse() {
		
		//Set the customer credit card to be disable.
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				txtCreditCardNew.clear();
				txtCreditCardNew.setDisable(true);
			}
		});
		
		//Set the customer cvv number to be disable.
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				txtCvv.clear();
				txtCvv.setDisable(true);
			}
		});
		
		//set cmbMonth to be disable.
		Platform.runLater(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				cmbMonth.valueProperty().set(null);
				cmbMonth.setDisable(true);
			}
		});
		
		//Set cmbYear to be disable.
		Platform.runLater(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				cmbYear.valueProperty().set(null);
				cmbYear.setDisable(true);
			}
		});
		
		Platform.runLater(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				lblCreditCardErr.setText("");
				lblCvvNumberErr.setText("");
				lblMonthErr.setText("");
			}
		});
	}
	
	
	@FXML
	private void cmbFuelType(ActionEvent event) {
		operateCalcFuelPrice();
	}
	
	@FXML
	private void fuelAmountTxt(ActionEvent event) {
		operateCalcFuelPrice();
	}
	
	/**
	 * If the next fields( Fuel amount textField,Gas Station Company comboBox, Fuel Type comboBox) 
	 * are not empty this method will generate an estimate fuel price that will be present on the screen. 
	 */
	private void operateCalcFuelPrice() {
		if(!fuelAmountTxt.getText().isEmpty() && !cmbGasStationCompany.getSelectionModel().isEmpty() 
				&& !cmbFuelType.getSelectionModel().isEmpty()) {
			String fuelAmount = fuelAmountTxt.getText();
			String fuelType =  (String)cmbFuelType.getSelectionModel().getSelectedItem();
			String gasStationCompany =  (String)cmbGasStationCompany.getSelectionModel().getSelectedItem();
			
			String temp = calcFuelPrice(fuelType, fuelAmount, gasStationCompany);
			
			FuelPriceInfolbl.setTooltip(new Tooltip(fuelMsgDetails));
			
			lblEstimatedPrice.setText(temp + "$");
			lblEstimatedPrice.setVisible(true);
			
		}
	}
	
	@FXML
	public void buyFuelBtn(ActionEvent event) {
		//call to validation method that will check that all the fields are not empty.
		if(validation()) {
			clearErrors();
			if(radioGroup.getSelectedToggle().equals(rdRegisteredCreditCard)) {
				if(validationForCreditCardFields()) {
					sendDataToServer();	
				}
			}else
				sendDataToServer();	
		}
		
		//Set the label to be invisible, for the case that user will press the buy fuel button twice, 
		//and the message to the screen will be different.
		lblFuelingRes.setVisible(false);
		lblFuelingRes1.setVisible(false);
		lblFuelingRes2.setVisible(false);


	}
	
	/**
	 * Send all the relevant data to server.
	 */
	private void sendDataToServer() {
		ArrayList<Object> answer = new ArrayList<>(); 
		answer.add("customer/BuyVehicleFuel");
		String carLicenseNumber = carNumberTxt.getText();
		String fuelAmount = fuelAmountTxt.getText();
		String fuelType =  (String)cmbFuelType.getSelectionModel().getSelectedItem();
		String gasStationCompany =  (String)cmbGasStationCompany.getSelectionModel().getSelectedItem();
		String gasStationName =  (String)cmbGasStationName.getSelectionModel().getSelectedItem();
		LocalTime buyTime = LocalTime.now();
		LocalDate buyDate = LocalDate.now();

		
		answer.add(carLicenseNumber);
		answer.add(fuelAmount);
		answer.add(fuelType);
		answer.add(gasStationCompany);
		answer.add(gasStationName);
		answer.add(calcFuelPrice(fuelType, fuelAmount, gasStationCompany)); //6
		
		String creditCard = null, cvvNumber = null, creditCardMonth = null, creditCardYear = null;
		if(radioGroup.getSelectedToggle().equals(rdCash)) {
			answer.add("Cash");
			answer.add(carLicenseNumber);//Problem
			answer.add(buyTime);
			answer.add(buyDate);
			 answer.add(cbPumpNumber.getValue());
			chs.client.handleMessageFromClientUI(answer);
		}else if(radioGroup.getSelectedToggle().equals(rdRegisteredCreditCard)) {
			answer.add("Credit_Card");
			answer.add(carLicenseNumber);//Problem
			answer.add(buyTime);
			answer.add(buyDate);
			 answer.add(cbPumpNumber.getValue());
			 
			chs.client.handleMessageFromClientUI(answer);
		}else {
			 creditCard = txtCreditCardNew.getText();
			 cvvNumber = txtCvv.getText();
			 creditCardMonth = (String)cmbMonth.getValue();
			 creditCardYear = (String)cmbYear.getValue();
		
			 answer.add("Credit_Card");
			 answer.add(carLicenseNumber);//Problem
			 
			 answer.add(cbPumpNumber.getValue());
			 
			 answer.add(buyTime);
			 answer.add(buyDate);
			 answer.add(creditCard);
			 answer.add(cvvNumber);
			 answer.add(creditCardMonth);
			 answer.add(creditCardYear);
			 chs.client.handleMessageFromClientUI(answer);
		}

		
	}
	
	
	/**
	 * check if the request for vehicle fuel passed successfully.
	 * @param message - getting data from the BuyVehicleFuelQuery.
	 * 
	 */
	public void handelMessages(ArrayList<Object> message) {
		String carStatus = message.get(1).toString();
		if(carStatus.equals("Erorr - carLicenseNumber don't existing in car table")) {	
			lblFuelingRes1.setVisible(true);
		}else {
			String gasStationFuel = message.get(2).toString();
			if(gasStationFuel.equals("Erorr-their isn't enough fuel in this gasStationName")) {
				lblFuelingRes2.setVisible(true);
			}else {
				String finalRes = message.get(3).toString();
				if(finalRes.equals("Success - data successfully inserted to the table")) {
					lblFuelingRes.setVisible(true);
				}
					
			}
		}
		
	}
	
	/**
	 * @param fuelType
	 * @param amountOfFuel
	 * @param gasStationCompany
	 * @return the fuel price that is adjust to customer's programmed.
	 */
	private String calcFuelPrice(String fuelType, String amountOfFuel, String gasStationCompany) {
			fuelMsgDetails = "";
		if(fuelType != null && amountOfFuel != null && gasStationCompany != null) {
			float fuelAmount = new Float(amountOfFuel);
			
			float fuelPrice = fuelTypePrice.get(fuelType).get(0);
			float maxFuelPrice = fuelTypePrice.get(fuelType).get(1);

			ModelType type = ModelType.valueOf(customerModelType);
			//Get the fuel price that adjust to the customer model.
			float finalPrice = new customerModelType(type, fuelAmount, fuelPrice, maxFuelPrice).getFinalPrice();
			//New
			fuelMsgDetails = String.format("Your Model:\n%s",returnModel(type));
			
			//Calculation fuel price by the purchase plan of customer
			int size = customerPurchasePlan.size();
			if(size > 1 && customerPurchasePlan.contains(gasStationCompany)) {
				//This is the first level - the customer is related to more than one gas company.
				finalPrice = (float) (finalPrice * 0.98);
				fuelMsgDetails+=String.format("\nGas Station In your plan:\n");
			}else if(size == 1 && customerPurchasePlan.contains(gasStationCompany)) {
				//This is the second level - the customer is related to one gas company.
				finalPrice = (float) (finalPrice * 0.96);
				fuelMsgDetails+=String.format("\nGas Station In your plan:\n");
			}
			
			
			if(customerPurchasePlan.contains(gasStationCompany)) {
			for (int i = 0; i < customerPurchasePlan.size(); i++) {
				if(i == customerPurchasePlan.size()-1)
					fuelMsgDetails+=String.format("%s", customerPurchasePlan.get(i));
				else
					fuelMsgDetails+=String.format("%s, ", customerPurchasePlan.get(i));
			}
		}
			
			Set<String> fuelSalesKeys = fuelSales.keySet();
			ArrayList<String> keyList = new ArrayList<>();
			keyList.addAll(fuelSalesKeys);
			
			if(keyList.contains(cmbFuelType.getSelectionModel().getSelectedItem())) {
				float discountPercent = fuelSales.get(fuelType);
				if(discountPercent != 0) {
					finalPrice = finalPrice * ((100-discountPercent)/100);
					fuelMsgDetails+=String.format("\nSale: You Got discount: %.1f\n", discountPercent);
				}
			}

			return String.format("%.2f", finalPrice);
		}else
			return null;


	}
	
	/**This method get ModelType enum value, and return string that represent this enum value.
	 * @param type is an enum value.
	 * @return
	 */
	private String returnModel(ModelType type) {
		if(type.equals(ModelType.NormalFueling))
			return "Normal Fueling";
		else if(type.equals(ModelType.RegularMonthlySubscriptionOneCar))
			return "Regular Monthly Subscription For One Car";
		else if(type.equals(ModelType.RegularMonthlySubscriptionSomeCars))
			return "Regular Monthly Subscription For Some Car";
		return "Full Monthly Subscription One Car";
	}
	
	/**
	 * @return return true when all the field aren't empty, 
	 * and fields that spouse to get numeric input, got the right input.
	 * when empty fields are exists the method will show error message on the screen. 
	 */
	private boolean validation() {
		boolean isInputValid = true;
			
		//Make validation to car number text field.
		if(!CheckComponentInput.numericTextFieldValidation(carNumberTxt, lblCarNumErr))
			isInputValid = false;
		
		//Make validation to fuel type comboBox.
		if(!CheckComponentInput.notEmptyComboBoxValidation(cmbFuelType, lblFuelTypeErr))
			isInputValid = false;
		
		//Make validation to fuel amount text field.
		if(!CheckComponentInput.numericTextFieldValidation(fuelAmountTxt, lblFuelAmountErr))
			isInputValid = false;
		
		//Make validation to gas station name comboBox.
		if(!CheckComponentInput.notEmptyComboBoxValidation(cmbGasStationName, lblGasStationNameErr))
			isInputValid = false;
		
		return isInputValid;
	}
	
	/**
	 * @return return true if and only if the customer credit card fields are validated.
	 */
	private boolean validationForCreditCardFields(){
		boolean isInputValid = true;
		
		//Make validation to the credit card number that customer inserted.
		if(!CheckComponentInput.numericTextFieldValidation(txtCreditCardNew, lblCreditCardErr))
			isInputValid = false;
		
		//Make validation to the Cvv number that customer inserted.
		if(!CheckComponentInput.numericTextFieldValidation(txtCvv, lblCvvNumberErr))
			isInputValid = false;
		
		return isInputValid;
	}

	/**
	 * Clears all label Errors.
	 */
	private void clearErrors() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				lblCarNumErr.setText("");
				lblFuelTypeErr.setText("");
				lblFuelAmountErr.setText("");
				lblGasStationNameErr.setText("");
				lblCreditCardErr.setText("");
				lblCvvNumberErr.setText("");
				lblMonthErr.setText("");
			}
		});
	}

}