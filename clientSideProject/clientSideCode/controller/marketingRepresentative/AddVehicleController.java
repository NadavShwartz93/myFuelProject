package controller.marketingRepresentative;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.jws.soap.SOAPBinding.Style;

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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import logic.CheckComponentInput;

public class AddVehicleController implements Initializable {

	public ClientHandlerSingle chs;
	public AddVehicleController av;
	public ManageVehicleController mvc;
	private String tempString1, tempString2;
	public static ArrayList<Object> carTableData = new ArrayList<Object>();//New
	public static String customerIdString;
	public AddVehicleController() {
		
		chs = ClientHandlerSingle.getInstance();
		chs.setAvc(this);
		av = this;
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
    private Label lblCarOwnerErr;
    @FXML
    private Label lblLicenseNumberErr;
    @FXML
    private Label lblManufacturerErr;
    @FXML
    private Label lblModelErr;
    @FXML
    private Label lblManufactureYearErr;
    @FXML
    private Label lblFuelTypeErr;

    
    /**This method start to operate after user press on enter in SearchIdBar TextFields 
     * the method call to sendSearchRequestToServer() that is send the search request to server.
     * @param event
     */
    
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
	private void sendDataToServer() {
    	ArrayList<String> newMsg = new ArrayList<String>();
		newMsg.add("addVehicle");
		newMsg.add(mvc.list.get(0));
		newMsg.add(LicenseNumberBar.getText());
		newMsg.add(fuelTypeChoiceBox.getValue());
		newMsg.add(ManufacturerBar.getText());
		newMsg.add(modelBar.getText());
		newMsg.add(yearBar.getText());
		newMsg.add(CarOwnerBar.getText());
	
		chs.client.handleMessageFromClientUI(newMsg);
		//changeWindow();
    }
    
	
  	/**
  	 * this method passing you to the window that is located.
  	 */
    private void changeWindow() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(MainDesplayController.class.getResource("/fxml/MarketingRepresentative/ManageVehicle.fxml"));
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
    
    /**This is a helper method that insert text into Label.
     * @param label 
     * @param str
     */
    private void setTextInlabel(Label label, String str) {
       	Platform.runLater(new Runnable() {
    			@Override
    			public void run() {
    		
    				label.setText(str);

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
    	
    	if(!CheckComponentInput.numericTextFieldValidation(yearBar, lblManufactureYearErr))
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
				lblManufactureYearErr.setText("");
				lblFuelTypeErr.setText("");
			}
		});
	}
    /**
     * This method set text on label.
     */
	private void setResultAreaLabel(String str) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ResultArea.setText(str);
			}
		});
	}
    /**
     * This method send request to the server side and change window.
     */
	public void getDataFromServer(Object newMsg) {//New
		carTableData = (ArrayList<Object>) newMsg;
		customerIdString = mvc.list.get(0);
		changeWindow();
	}

	/**
	 * Controller and fxml Elements initialize.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<String> availableChoices = FXCollections.observableArrayList("Petrol", "Diesel", "ScooterFuel");
		fuelTypeChoiceBox.setItems(availableChoices);
		fuelTypeChoiceBox.setValue("Petrol");
		clearErrors();
		setResultAreaLabel(mvc.list.get(0));
		carTableData = new ArrayList<Object>();
	}

}