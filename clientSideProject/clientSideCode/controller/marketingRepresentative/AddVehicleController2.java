package controller.marketingRepresentative;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import controller.MainDesplayController;
import emClient.ClientHandlerSingle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import logic.CheckComponentInput;

public class AddVehicleController2 implements Initializable {

	public ClientHandlerSingle chs;
	public AddVehicleController2 avc2;
	private String tempString1, tempString2;
	public AddVehicleController2() {
		
		chs = ClientHandlerSingle.getInstance();
		chs.setAvc2(this);
		avc2 = this;
	}

    @FXML
    private Label ResultArea;
    @FXML
    private TextField LicenseNumberBar;
    @FXML
    private TextField ManufacturerBar;
    @FXML
    private TextField modelBar;
    @FXML
    private TextField yearBar;
    @FXML
    private ChoiceBox<String> fuelTypeChoiceBox;
    @FXML
    private Button addVehicleBtn;
    @FXML
    private TextField CarOwnerBar;
    @FXML
    private Button saveAndAddVehicleBtn;    
    @FXML
    private Label lblCarOwnerErr;
    @FXML
    private Label lblLicenseNumberErr;
    @FXML
    private Label lblManufacturerErr;
    @FXML
    private Label lblModelErr;
    @FXML
    private Label lblManufactureYeaErr;
    @FXML
    private Label lblFuelTypeErr;
    
    @FXML
    void addVehicleBtn(ActionEvent event) {
    	if(validation()) {
    		clearErrors();
    		sendDataToServer();
    	}
    }
    
    /**
     * This method send all the data to the server side.
     */
    private void sendDataToServer(){
    	ArrayList<String> newMsg = new ArrayList<String>();
		newMsg.add("addVehicle2");
		newMsg.add(AddCustomerController.list.get(0));
		newMsg.add(LicenseNumberBar.getText());
		newMsg.add(fuelTypeChoiceBox.getValue());
		newMsg.add(ManufacturerBar.getText());
		newMsg.add(modelBar.getText());
		newMsg.add(yearBar.getText());
		newMsg.add(CarOwnerBar.getText());
	
		chs.client.handleMessageFromClientUI(newMsg);
		changeWindow();
    }
    
  	/**
  	 * this method Passing you to the window that is located.
  	 */
    private void changeWindow() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(MainDesplayController.class.getResource("/fxml/MarketingRepresentative/ManageCustomer.fxml"));
					AnchorPane mainItems;
					mainItems = loader.load();
					MainDesplayController.mainBorderPane.setCenter(mainItems);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
    }
    /**
     * 
     */
    /**This method set text on label.
     * @param msg contain the relevant data.
     */
    public void setOnLabel(Object msg) {
    	ArrayList<ArrayList> oldMsg = (ArrayList<ArrayList>)msg;
    	tempString1 = String.format("%s\n%s %s\n", oldMsg.get(0).get(0), oldMsg.get(0).get(1), oldMsg.get(0).get(2));
    	Platform.runLater(new Runnable() {
			@Override
			public void run() {
		
				ResultArea.setText(tempString1);

	
			}
		});
    	
    }
    
    /**
   	 * @return return true when all the field aren't empty, 
   	 * fields that spouse to get numeric input, got the right input, and 
   	 * fields that don't spouse to get numeric input, got the right input too.
   	 * when empty fields are exists the method will show error message on the screen. 
   	 */
       private boolean validation() {
       	boolean isInputValid = true;		
    	
       	if(!CheckComponentInput.notEmptyTextFieldValidation(CarOwnerBar, lblCarOwnerErr))
       		isInputValid = false;
       	
       	if(!CheckComponentInput.numericTextFieldValidation(LicenseNumberBar, lblLicenseNumberErr))
       		isInputValid = false;
       	
       	if(!CheckComponentInput.notEmptyTextFieldValidation(ManufacturerBar, lblManufacturerErr))
       		isInputValid = false;
       	
       	if(!CheckComponentInput.notEmptyTextFieldValidation(modelBar, lblModelErr))
       		isInputValid = false;
       	
       	if(!CheckComponentInput.numericTextFieldValidation(yearBar, lblManufactureYeaErr))
       		isInputValid = false;
       	
       	if(!CheckComponentInput.notEmptyChoiceBoxValidation(fuelTypeChoiceBox, lblFuelTypeErr))
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
				lblCarOwnerErr.setText("");
				lblLicenseNumberErr.setText("");
				lblManufacturerErr.setText("");
				lblModelErr.setText("");
				lblManufactureYeaErr.setText("");
				lblFuelTypeErr.setText("");
			}
		});
	}
	/**
	 * Controller and fxml Elements initialize.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<String> availableChoices = FXCollections.observableArrayList("Petrol", "Diesel", "ScooterFuel");
		fuelTypeChoiceBox.setItems(availableChoices);
		fuelTypeChoiceBox.setValue("Petrol");
		ResultArea.setText(AddCustomerController.list.get(0)+  "\n" + AddCustomerController.list.get(1) + " " +AddCustomerController.list.get(2));
		clearErrors();
	}

}