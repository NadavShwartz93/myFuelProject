package controller.marketingRepresentative;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import controller.MainDesplayController;
import controller.ModelType;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import logic.CheckComponentInput;
import logic.Customer;

public class ManageCustomerController implements Initializable {
	
	public ClientHandlerSingle chs;
	public AddCustomerController ac;
	private Stage thisStage; 
	private ManageCustomerController mcc;
	private Stage parentStage;
	private ArrayList<String> lastSearch;
//	ObservableList<String> availableChoices = FXCollections.observableArrayList("Id", "FirstName", "LastName");
	ObservableList<String> availableChoices = FXCollections.observableArrayList("Id", "First Name", "Last Name");

	private ArrayList<Customer> updateList;
	
	public ManageCustomerController() {
		lastSearch = new ArrayList<String>(); 
		updateList = new ArrayList<Customer>();
		chs = ClientHandlerSingle.getInstance();
		chs.setMcc(this);
		mcc=this;
		}
	
	public void saveStage(Stage thisStage) {
		this.thisStage = thisStage;
	
	}
	
	public void saveStage(Stage thisStage, Stage parentStage,ManageCustomerController mcc) {
		this.thisStage = thisStage;
		this.parentStage = parentStage;
		this.mcc = mcc;
	}

    @FXML
    private ChoiceBox<String> SearchByChoiceBox;

    @FXML
    private TextField SearchCustomerBar;

    @FXML
    private Button SearchCustomerBtn;

    @FXML
    private TableView<Customer> CustomerTable;

    @FXML
    private TableColumn<String, Customer> IdCol;
    
    @FXML
    private TableColumn<Customer,String > FirstNameCol;
    
    @FXML
    private TableColumn<Customer,String> LastNameCol;
    
    @FXML
    private TableColumn<Customer,String> EmailCol;

    @FXML
    private TableColumn<Customer,String> CreditCardCol;

    @FXML
    private TableColumn<Customer,String> CvvCol;

    @FXML
    private TableColumn<Customer,String> MonthCol;

    @FXML
    private TableColumn<Customer,String> YearCol;

    @FXML
    private TableColumn<Customer,String> PasswordCol;

    @FXML
    private TableColumn<Customer, ModelType> TypeCol;
    
    @FXML
    private Button AddCustomerBtn;

    @FXML
    private Button DeleteCustomerBtn;

    @FXML
    private Button UpdateCustomerBtn;
  	/**
  	 * this method Passing you to the window that is located.
  	 */
    @FXML
    void AddCustomerBtn(ActionEvent event) {
    	
    	Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(MainDesplayController.class.getResource("/fxml/MarketingRepresentative/AddCustomer.fxml"));
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
	 * This method set the Delete Customer button to be disable.
	 */
	private void setDeleteBtnDisable() {
		DeleteCustomerBtn.setDisable(true);
	}
    
	/**This method set the Delete Customer button to be active after event of mouse clicked 
	 * on the CustomerTable TableView.
	 * @param event
	 */
	@FXML
    private void setDeleteBtnActive(MouseEvent event) {
    	if(CustomerTable.getSelectionModel().getSelectedItem() != null) {
    		DeleteCustomerBtn.setDisable(false);
    	}
    }
	
    
    /**
     * This method send delete request to the server side.
     */
    @FXML
    void DeleteCustomerBtn(ActionEvent event) {
    	String choose = CustomerTable.getSelectionModel().getSelectedItem().getId();
    	ArrayList<String> newMsg = new ArrayList<String>();
    	newMsg.add("deleteCustomer");
		newMsg.add(choose);
    	chs.client.handleMessageFromClientUI(newMsg);
    	updateTable();
    	setDeleteBtnDisable();//set the Delete Customer button to be disable again.
    }
    
    /**This method start to operate after user press on enter in SearchCustomerBar TextFields 
     * the method call to sendSearchRequestToServer() that is send the search request to server.
     * @param event
     */
    @FXML
    void SearchCustomerBar(ActionEvent event) {
    	sendSearchRequestToServer();
    }
    
    /**This method start to operate after user press on Search Button 
     * the method call to sendSearchRequestToServer() that is send the search request to server.
     * @param event
     */
    @FXML
    void SearchCustomerBtn(ActionEvent event) {
    	sendSearchRequestToServer();
    }
    
    /**This method read the user input and send search customer request to the server.
     */
    private void sendSearchRequestToServer() {
    	lastSearch.clear();
    	if(SearchCustomerBar.getText().equals(""))
    		lastSearch.add("");
    	else
    		lastSearch.add(SearchCustomerBar.getText());
    	
    	lastSearch.add(getSearchByName(SearchByChoiceBox.getValue()));
    	
		ArrayList<String> newMsg = new ArrayList<String>();
		newMsg.add("searchCustomer");
		newMsg.add(lastSearch.get(1));
		newMsg.add(lastSearch.get(0));
		newMsg.add("showCustomerInTable");
		chs.client.handleMessageFromClientUI(newMsg);
		updateList.clear();
	}
    
    /**This is helper method that convert some GUI phrases to the DB phrases. 
     * For instance, the method get "First Name" and return "FirstName".
     * @param str
     * @return
     */
    private String getSearchByName(String str) {
		
    	if(str.equals("First Name"))
    		return "FirstName";
    	else if(str.equals("Last Name"))
    		return "LastName";
    	return "Id";
    }
    
    @FXML
    void UpdateCustomerBtn(ActionEvent event) {
    	
    	ArrayList<String> newMsg = new ArrayList<String>();
    	for(int i = 0; i<updateList.size();i++) {
    		if(validationForUpdate(updateList.get(i))) {
    			
        		newMsg.add("updateCustomer");
        		newMsg.add(updateList.get(i).getId());
        		newMsg.add(updateList.get(i).getFirstName());
        		newMsg.add(updateList.get(i).getLastName());
        		newMsg.add(updateList.get(i).getEmail());
        		newMsg.add(updateList.get(i).getCreditCard());
        		newMsg.add(updateList.get(i).getCvv());
        		newMsg.add(updateList.get(i).getMonth());
        		newMsg.add(updateList.get(i).getYear());
        		newMsg.add(updateList.get(i).getPassword());
        		newMsg.add(updateList.get(i).getType().toString());
        		chs.client.handleMessageFromClientUI(newMsg);
        		newMsg.clear();
    			
    		}
    	}
    	updateList.clear();
    }
    
    /**This method make validation test to the input.  
     * When the user insert values, in the process of table update.
     * @param customer
     * @return
     */
    private boolean validationForUpdate(Customer customer) {
    	boolean isInputValid = true;
    	
    	//Make validation to Id Number.
    	if(!CheckComponentInput.isNumber(customer.getId()))
    		isInputValid = false;
    	
    	//Make validation to First Name.
    	if(!CheckComponentInput.notContainNumbers(customer.getFirstName()))
    		isInputValid = false;
    	
    	//Make validation to Last Name.
    	if(!CheckComponentInput.notContainNumbers(customer.getLastName()))
    		isInputValid = false;
    	
    	//Make validation to Cvv number.
    	if(!CheckComponentInput.checkCreditCardCvvNumber(customer.getCvv()))
    		isInputValid = false;
    	
    	//Make validation to Month.
    	if(!CheckComponentInput.isMonth(customer.getMonth()))
    		isInputValid = false;
    	
    	//Make validation to Password.
    	if((customer.getPassword()).equals(""))
    		isInputValid = false;
    	
    	//Make validation to Mail.
    	if((customer.getEmail()).equals(""))
    		isInputValid = false;
    	
    	//Make validation to Year.
    	if(!CheckComponentInput.isYear(customer.getYear()))
    		isInputValid = false;
    	
		return isInputValid;
	}
    /**
     * This method send table update request to the server side.
     */
    public void updateTable() {
    	ArrayList<String> newMsg = new ArrayList<String>();
		newMsg.add("searchCustomer");
		newMsg.add(lastSearch.get(1));
		newMsg.add(lastSearch.get(0));
		newMsg.add("showCustomerInTable");
		chs.client.handleMessageFromClientUI(newMsg);
    }
    
    /**This method get customer from the server side, and show it on CustomerTable TableView.
     * @param answer contain the relevant data from server.
     */
    public void showDataInTable(Object answer) {
    	ArrayList<Object> tempAnswer = (ArrayList<Object>) answer;
		ObservableList<Customer> itemsForTable = FXCollections.observableArrayList();
		int j = 0;		
		
		for(int i = 0; i <tempAnswer.size(); i++) {
			
			String id = ((ArrayList<Object>) tempAnswer.get(i)).get(j).toString();
			j++;
			String firstName = ((ArrayList<Object>) tempAnswer.get(i)).get(j).toString();
			j++;
			String lastName = ((ArrayList<Object>) tempAnswer.get(i)).get(j).toString();
			j++;
			String email = ((ArrayList<Object>) tempAnswer.get(i)).get(j).toString();
			j++;
			String creditCard = ((ArrayList<Object>) tempAnswer.get(i)).get(j).toString();
			j++;
			String cvv = ((ArrayList<Object>) tempAnswer.get(i)).get(j).toString();
			j++;
			String month = ((ArrayList<Object>) tempAnswer.get(i)).get(j).toString();
			j++;
			String year = ((ArrayList<Object>) tempAnswer.get(i)).get(j).toString();
			LocalDate temp = LocalDate.parse(year);
			year = String.valueOf(temp.getYear());
			j++;
			String password = ((ArrayList<Object>) tempAnswer.get(i)).get(j).toString();
			j++;
			String tempS = ((ArrayList<Object>) tempAnswer.get(i)).get(j).toString();
			ModelType type = ModelType.valueOf(tempS);
			j=0;

       	itemsForTable.add(new Customer(id, firstName, lastName, email, creditCard, cvv, month, year, password, type));
       	}
		Platform.runLater(new Runnable() {
				@Override
				public void run() {
					CustomerTable.setItems(itemsForTable);
				}
		});
  	} 
    
    /**This method generate toolTip to every component.
     */
    private void setComponentToolTip() {
    	UpdateCustomerBtn.setTooltip(new Tooltip("Press the relevant cell\n"
    			+ "Insert new value\nPress enter keyboard\nPress update button"));
    	
    	AddCustomerBtn.setTooltip(new Tooltip("This will lead to the add customer page"));
    	
    	DeleteCustomerBtn.setTooltip(new Tooltip("Select the row to delete\nPress delete button"));
    	
    	//SearchCustomerBtn.setTooltip(new Tooltip("Insert "));

	}
    /**
     * Controller and fxml Elements initialize.
     */
    @Override
   	public void initialize(URL arg0, ResourceBundle arg1) {
    	if(SearchCustomerBar.getText().equals(""))
    		lastSearch.add("");
    	else
    		lastSearch.add(SearchCustomerBar.getText());
    	lastSearch.add(SearchByChoiceBox.getValue());
       	SearchByChoiceBox.setItems(availableChoices);		
       	CustomerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
       	IdCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
   	    FirstNameCol.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
   	    LastNameCol.setCellValueFactory(new PropertyValueFactory<>("LastName"));
   	    EmailCol.setCellValueFactory(new PropertyValueFactory<>("Email"));
   	    CreditCardCol.setCellValueFactory(new PropertyValueFactory<>("CreditCard"));
   	    CvvCol.setCellValueFactory(new PropertyValueFactory<>("Cvv"));
   	    MonthCol.setCellValueFactory(new PropertyValueFactory<>("Month"));
   	    YearCol.setCellValueFactory(new PropertyValueFactory<>("Year"));
   	    PasswordCol.setCellValueFactory(new PropertyValueFactory<>("Password"));
   		TypeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
		SearchByChoiceBox.setValue("Id");
		FirstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
		FirstNameCol.setOnEditCommit(e->{
			e.getTableView().getItems().get(e.getTablePosition().getRow()).setFirstName(e.getNewValue());
			updateList.add(e.getTableView().getItems().get(e.getTablePosition().getRow()));
		});
		
		LastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
		LastNameCol.setOnEditCommit(e->{
			e.getTableView().getItems().get(e.getTablePosition().getRow()).setLastName(e.getNewValue());
			updateList.add(e.getTableView().getItems().get(e.getTablePosition().getRow()));
		});
		
		EmailCol.setCellFactory(TextFieldTableCell.forTableColumn());
		EmailCol.setOnEditCommit(e->{
			e.getTableView().getItems().get(e.getTablePosition().getRow()).setEmail(e.getNewValue());
			updateList.add(e.getTableView().getItems().get(e.getTablePosition().getRow()));
		});
		
		CreditCardCol.setCellFactory(TextFieldTableCell.forTableColumn());
		CreditCardCol.setOnEditCommit(e->{
			e.getTableView().getItems().get(e.getTablePosition().getRow()).setCreditCard(e.getNewValue());
			updateList.add(e.getTableView().getItems().get(e.getTablePosition().getRow()));
		});
		
		CvvCol.setCellFactory(TextFieldTableCell.forTableColumn());
		CvvCol.setOnEditCommit(e->{
			e.getTableView().getItems().get(e.getTablePosition().getRow()).setCvv(e.getNewValue());
			updateList.add(e.getTableView().getItems().get(e.getTablePosition().getRow()));
		});
		
		MonthCol.setCellFactory(TextFieldTableCell.forTableColumn());
		MonthCol.setOnEditCommit(e->{
			e.getTableView().getItems().get(e.getTablePosition().getRow()).setMonth(e.getNewValue());
			updateList.add(e.getTableView().getItems().get(e.getTablePosition().getRow()));
		});
		
		YearCol.setCellFactory(TextFieldTableCell.forTableColumn());
		YearCol.setOnEditCommit(e->{
			e.getTableView().getItems().get(e.getTablePosition().getRow()).setYear(e.getNewValue());
			updateList.add(e.getTableView().getItems().get(e.getTablePosition().getRow()));
		});
		
		PasswordCol.setCellFactory(TextFieldTableCell.forTableColumn());
		PasswordCol.setOnEditCommit(e->{
			e.getTableView().getItems().get(e.getTablePosition().getRow()).setPassword(e.getNewValue());
			updateList.add(e.getTableView().getItems().get(e.getTablePosition().getRow()));
		});
		

		CustomerTable.setEditable(true);
		updateTable();
		setDeleteBtnDisable();
		setComponentToolTip();
}

}
