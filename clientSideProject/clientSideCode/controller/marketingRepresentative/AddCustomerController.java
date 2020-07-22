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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import logic.CheckComponentInput;

public class AddCustomerController implements Initializable{

	public ClientHandlerSingle chs;
	public AddCustomerController ac;
	private ToggleGroup radioGroup;
	ObservableList<String> availableMonthChoices = FXCollections.observableArrayList("01", "02", "03","04", "05", "06","07", "08", "09","10", "11", "12");
	ObservableList<String> availableYearChoices = FXCollections.observableArrayList("2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030");
	public static ArrayList<String> list = new ArrayList<String>();
	int temp7;

	public AddCustomerController() {
		chs = ClientHandlerSingle.getInstance();
		chs.setAcc(this);
		ac = this;
	}

    @FXML
    private TextField FirstNameBar;
    @FXML
    private TextField LastNameBar;
    @FXML
    private TextField IdNumberBar;
    @FXML
    private TextField MailBar;
    @FXML
    private TextField CreditCardBar;
    @FXML
    private Button SaveCustomerBtn;
    @FXML
    private TextField PasswordBar;
    @FXML
    private RadioButton PrivateSelection;
    @FXML
    private RadioButton CompanySelection;  
    @FXML
    private TextField CvvBar;  
    @FXML
    private TextField UserNameBar;  
    @FXML
    private ChoiceBox<String> MonthChoiceBox;
    @FXML
    private ChoiceBox<String> YearChoiceBox;
    @FXML
    private Button SaveCustomerAndAddBtn;
    @FXML
    private Label lblFirstNameErr;
    @FXML
    private Label lblLastNameErr;
    @FXML
    private Label lblIdNumberErr;
    @FXML
    private Label lblMailErr;
    @FXML
    private Label lblCreditCardErr;
    @FXML
    private Label lblCvvNumberErr;
    @FXML
    private Label lblMonthYearErr;
    @FXML
    private Label lblUserNameErr;
    @FXML
    private Label lblPasswordErr;
    
    
    /**
     * This method send insert request to the server side.
     */
    @FXML
    void SaveCustomerAndAddBtn(ActionEvent event) {
    	if(validation()) {
 			clearErrors();
        	list.clear();
        	ArrayList<Object> newMsg = new ArrayList<Object>();
        	String temp1 = IdNumberBar.getText();
        	String temp2 = FirstNameBar.getText();
        	String temp3 = LastNameBar.getText();
        	String temp4 = MailBar.getText();
        	String temp5 = CreditCardBar.getText();
        	String temp6 = CvvBar.getText();

    		if(radioGroup.getSelectedToggle().equals(PrivateSelection)) {
        			 temp7 = 0;
    		}
        			else {
        				 temp7 = 1; 
        			}
        	String temp8 = MonthChoiceBox.getValue();
        	String temp9 = YearChoiceBox.getValue();
        	String temp10 = UserNameBar.getText();
        	String temp11 = PasswordBar.getText();
        	
        	list.add(temp1);
        	list.add(temp2);
        	list.add(temp3);
        	
        	newMsg.add("saveNewCustomer");
        	newMsg.add(temp1);
        	newMsg.add(temp2);
        	newMsg.add(temp3);
        	newMsg.add(temp4);
        	newMsg.add(temp5);
        	newMsg.add(temp6);
        	newMsg.add(temp7);
        	newMsg.add(temp8);
        	newMsg.add(temp9);
        	newMsg.add(temp10);
        	newMsg.add(temp11);
      
        	chs.client.handleMessageFromClientUI(newMsg);
        	openAdd();
    	}
    }
  	/**
  	 * this method Passing you to the window that is located.
  	 */
    public void openModel() {
    	
    	Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(MainDesplayController.class.getResource("/fxml/MarketingRepresentative/AddClientAssociationWithPurchasePlanModel.fxml"));
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
  	 * this method Passing you to the window that is located.
  	 */  
 public void openAdd() {
    	
    	Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(MainDesplayController.class.getResource("/fxml/MarketingRepresentative/AddclientAssociationWithPurchasePlanModel2.fxml"));
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
 
    public void setToggleGroup() {
		radioGroup = new ToggleGroup();
		PrivateSelection.setToggleGroup(radioGroup);
		CompanySelection.setToggleGroup(radioGroup);
    }
 
 	/**
 	 * This method set the RadioButton to be on private customer. 
 	 * Then when the initialize of the page is made, the RadioButton is on private customer.
 	 */
 	private void setRadioButtonToPrivate() {
 		PrivateSelection.setSelected(true);
	}
 	
 	@FXML
 	public void SaveCustomerBtn(ActionEvent event) {
 		if(validation()) {
 			clearErrors();
 			sendDataToServer();
 		}
 	}
  	/**
  	 * this method passing data to the server.
  	 */
    private void sendDataToServer() {
    	list.clear();
    	ArrayList<Object> newMsg = new ArrayList<Object>();
    	String temp1 = IdNumberBar.getText();
    	String temp2 = FirstNameBar.getText();
    	String temp3 = LastNameBar.getText();
    	String temp4 = MailBar.getText();
    	String temp5 = CreditCardBar.getText();
    	String temp6 = CvvBar.getText();

		if(radioGroup.getSelectedToggle().equals(PrivateSelection)) {
    			 temp7 = 0;
		}
    			else {
    				 temp7 = 1; 
    			}
    	String temp8 = MonthChoiceBox.getValue();
    	String temp9 = YearChoiceBox.getValue();
    	String temp10 = UserNameBar.getText();
    	String temp11 = PasswordBar.getText();
    	
    	list.add(temp1);
    	list.add(temp2);
    	list.add(temp3);
    	
    	newMsg.add("saveNewCustomer");
    	newMsg.add(temp1);
    	newMsg.add(temp2);
    	newMsg.add(temp3);
    	newMsg.add(temp4);
    	newMsg.add(temp5);
    	newMsg.add(temp6);
    	newMsg.add(temp7);
    	newMsg.add(temp8);
    	newMsg.add(temp9);
    	newMsg.add(temp10);
    	newMsg.add(temp11);
  
    	chs.client.handleMessageFromClientUI(newMsg);
    	openModel();
	}
    
    /**
	 * @return Return true when all the field aren't empty, 
	 * fields that spouse to get numeric input, got the right input, and 
	 * fields that don't spouse to get numeric input, got the right input too.
	 * when empty fields are exists the method will show error message on the screen. 
	 */
	private boolean validation() {
		boolean isInputValid = true;
			
		//Make validation to First Name text field.
		if(!CheckComponentInput.notNumericTextFieldValidation(FirstNameBar, lblFirstNameErr))
			isInputValid = false;
		
		//Make validation to Last Name comboBox.
		if(!CheckComponentInput.notNumericTextFieldValidation(LastNameBar, lblLastNameErr))
			isInputValid = false;
		
		//Make validation to Id Number text field.
		if(!CheckComponentInput.numericTextFieldValidation(IdNumberBar, lblIdNumberErr))
			isInputValid = false;
		
		//Make validation to Mail text field.
		if(!CheckComponentInput.notEmptyTextFieldValidation(MailBar, lblMailErr))
			isInputValid = false;
		
		//Make validation to Credit Card text field.
		if(!CheckComponentInput.numericTextFieldValidation(CreditCardBar, lblCreditCardErr))
			isInputValid = false;
		
		//Make validation to Cvv number text field.
		if(!CheckComponentInput.numericTextFieldValidation(CvvBar, lblCvvNumberErr))
			isInputValid = false;
		
		//Make validation to User name text field.
		if(!CheckComponentInput.notEmptyTextFieldValidation(UserNameBar, lblUserNameErr))
			isInputValid = false;
		
		//Make validation to Password text field.
		if(!CheckComponentInput.notEmptyTextFieldValidation(PasswordBar, lblPasswordErr))
			isInputValid = false;
		
		//Make validation to Month choiceBox.
		if(!CheckComponentInput.notEmptyChoiceBoxValidation(MonthChoiceBox, lblMonthYearErr))
			isInputValid = false;
		
		//Make validation to Year choiceBox.

		
		return isInputValid;
	}
	
	/**
	 * Clears all label Errors.
	 */
	private void clearErrors() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				lblFirstNameErr.setText("");
				lblLastNameErr.setText("");
				lblIdNumberErr.setText("");
				lblMailErr.setText("");
				lblCreditCardErr.setText("");
				lblCvvNumberErr.setText("");
				lblMonthYearErr.setText("");
				lblUserNameErr.setText("");
				lblPasswordErr.setText("");
			}
		});
	}
	/**
	 * Controller and fxml Elements initialize.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MonthChoiceBox.setItems(availableMonthChoices);		
		YearChoiceBox.setItems(availableYearChoices);
		clearErrors();
		setToggleGroup();
		setRadioButtonToPrivate();

	}
}