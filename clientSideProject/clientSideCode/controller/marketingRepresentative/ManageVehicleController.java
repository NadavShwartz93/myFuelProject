package controller.marketingRepresentative;
import java.net.URL;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.ResourceBundle;

import controller.FuelType;
import controller.MainDesplayController;
import emClient.ClientHandlerSingle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import logic.CheckComponentInput;
import logic.Customer;
import logic.Vehicle;

public class ManageVehicleController implements Initializable {
	
	public ClientHandlerSingle chs;
	public AddVehicleController av;
	private Stage thisStage; 
	private ManageVehicleController mv;
	private ArrayList<String> lastSearch;
	ObservableList<String> availableChoices;
	private ArrayList<Vehicle> updateList;
	public static ArrayList<String> list = new ArrayList<String>();
	public ManageVehicleController() {
		lastSearch = new ArrayList<String>();
		updateList = new ArrayList<Vehicle>();
		chs = ClientHandlerSingle.getInstance();
		chs.setMvc(this);
		mv=this;
		}
	
	public void saveStage(Stage thisStage) {
		this.thisStage = thisStage;
	}

    @FXML
    private TextField SearchIdBar;

    @FXML
    private Button FindBtn;

    @FXML
    private ChoiceBox<String> SearchVehiclesByChoiceBox;

    @FXML
    private TextField SearchVehiclesBar;
    
    @FXML
    private TableView<Vehicle> VehiclesTable;

    @FXML
    private TableColumn<Vehicle,String> CarOwnerCol;

    @FXML
    private TableColumn<String, Vehicle> CarNumberCol;

    @FXML
    private TableColumn<String, Vehicle> ManufacturerCol;

    @FXML
    private TableColumn<String, Vehicle> ModelCol;

    @FXML
    private TableColumn<String, Vehicle> YearCol;

    @FXML
    private TableColumn<Vehicle, FuelType> FuelTypeCol;

    @FXML
    private Button AddVehicleBtn;

    @FXML
    private Button DeleteVehicleBtn;

    @FXML
    private Button UpdateVehicleBtn;
    
    @FXML
    private Label lblCustomerIdErr;

    @FXML
    void AddVehicleBtn(ActionEvent event) {
    	Platform.runLater(new Runnable() {
			@Override
			public void run() {
    	try {
    		FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainDesplayController.class.getResource("/fxml/MarketingRepresentative/AddVehicle.fxml"));
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
	 * This methods set buttons to be disable or enable.
	 */
	private void setDeleteBtnDisable() {
		DeleteVehicleBtn.setDisable(true);
	}
	
	public void setAddBtnDisable(boolean bool) {
		AddVehicleBtn.setDisable(bool);
	}
    
	/**This method set the Delete Vehicle button to be active after event of mouse clicked
	 * on the VehiclesTable TableView.
	 * @param event
	 */
	@FXML
    private void setDeleteBtnActive(MouseEvent event) {
    	if(VehiclesTable.getSelectionModel().getSelectedItem() != null) {
    		DeleteVehicleBtn.setDisable(false);
    	}
    }

    @FXML
    void DeleteVehicleBtn(ActionEvent event) {
    	String choose = VehiclesTable.getSelectionModel().getSelectedItem().getCarNumber();
    	ArrayList<String> newMsg = new ArrayList<String>();
    	newMsg.add("deleteVehicle");
		newMsg.add(choose);
    	chs.client.handleMessageFromClientUI(newMsg);
    	setDeleteBtnDisable();//set the Delete Vehicle button to be disable again.
    }
    
    /**This method start to operate after user press on enter in SearchIdBar TextFields 
     * the method call to sendSearchRequestToServer() that is send the search request to server.
     * @param event
     */
    @FXML
    void SearchIdBar(ActionEvent event) {
    	sendSearchRequestToServer();    
    	}
    
    /**This method start to operate after user press on enter in SearchVehiclesBar TextFields 
     * the method call to sendSearchRequestToServer() that is send the search request to server.
     * @param event
     */
    @FXML
    void SearchVehiclesBar(ActionEvent event) {
    	sendSearchRequestToServer();    
    	}
    
    /**This method start to operate after user press on Search Button 
     * the method call to sendSearchRequestToServer() that is send the search request to server.
     * @param event
     */
    @FXML
    void FindBtn(ActionEvent event) {
    	if(validation()) {
        	sendSearchRequestToServer();
    	}
    }
    
    /**This method read the user input and send search vehicles request to the server.
     */
    private  void sendSearchRequestToServer() {
    	list.clear();
    	ArrayList<String> newMsg = new ArrayList<String>();
		newMsg.add("searchVehicles");
		newMsg.add("FK_customerId");
		newMsg.add(SearchIdBar.getText());
		if(!SearchVehiclesBar.getText().equals("")) {
			newMsg.add(getSearchByName(SearchVehiclesByChoiceBox.getValue()));
			newMsg.add(SearchVehiclesBar.getText());
		}
		list.add(SearchIdBar.getText());
		newMsg.add("show vehicle in table");
		System.out.println(newMsg);
		chs.client.handleMessageFromClientUI(newMsg);
	}
    
    
    /**This is helper method that convert some GUI phrases to the DB phrases. 
     * For instance, the method get "Car Owner" and return "carOwner".
     * @param str
     * @return
     */
    private String getSearchByName(String str) {
		if(str.equals("Car Owner"))
			return "carOwner";
		else if(str.equals("Car License Number"))
			return "carLicenseNumber";
		else if(str.equals("Car Model"))
			return "carModel";
		else if(str.equals("Manufacturer Year"))
			return "manufacturerYear";
		return "Manufacturer";
	}
    

    /**This method get vehicle data from the server side, and show it on VehiclesTable TableView.
     * @param answer contain the relevant data from server.
     */
    public void showDataInTable(Object answer) {
    	ArrayList<Object> tempAnswer = (ArrayList<Object>) answer;
		ObservableList<Vehicle> itemsForTable = FXCollections.observableArrayList();
		int j = 0;	
		
		for(int i = 0; i < tempAnswer.size(); i++) {
			String carOwner = ((ArrayList<Object>) tempAnswer.get(i)).get(j).toString();
			j++;
			String carNumber = ((ArrayList<Object>) tempAnswer.get(i)).get(j).toString();
			j++;
			String manufacturer = ((ArrayList<Object>) tempAnswer.get(i)).get(j).toString();
			j++;
			String model = ((ArrayList<Object>) tempAnswer.get(i)).get(j).toString();
			j++;
			String year = ((ArrayList<Object>) tempAnswer.get(i)).get(j).toString();
			LocalDate temp = LocalDate.parse(year);
			year = String.valueOf(temp.getYear());
			j++;
			String tempS = ((ArrayList<Object>) tempAnswer.get(i)).get(j).toString();
			FuelType type = FuelType.valueOf(tempS);
			j=0;
			
       	itemsForTable.add(new Vehicle(carOwner, carNumber, manufacturer, model, year, type));
       	}
		Platform.runLater(new Runnable() {
				@Override
				public void run() {
					VehiclesTable.setItems(itemsForTable);
				}
		});
		
		av.carTableData.clear();//Clear the arrayList in AddVehicleController class
  	}   		
    
    /**This method get Vehicles from the server side.
     */
    
    @FXML
    void SearchVehiclesbtn(ActionEvent event) {//Is the method connect to someTing??
    	lastSearch.clear();
    	if(SearchVehiclesBar.getText().equals(""))
    		lastSearch.add("");
    	else
    		lastSearch.add(SearchVehiclesBar.getText());
    	//lastSearch.add(SearchVehiclesByChoiceBox.getValue());
    	lastSearch.add(getSearchByName(SearchVehiclesByChoiceBox.getValue()));
    	
		ArrayList<String> newMsg = new ArrayList<String>();
		newMsg.add("searchVehicles");
		newMsg.add(lastSearch.get(1));
		newMsg.add(lastSearch.get(0));
		newMsg.add("show Vehicles in table");
		chs.client.handleMessageFromClientUI(newMsg);
    	updateList.clear();
    }
    
    /**
     * This method send update request to the server side.
     */
    
    @FXML
    void UpdateVehicleBtn(ActionEvent event) {
    	ArrayList<String> newMsg = new ArrayList<String>();
    	for(int i = 0; i<updateList.size();i++) {
    		if(validationForUpdate(updateList.get(i))) {
    			newMsg.add("updateVehicles");
        		newMsg.add(updateList.get(i).getCarOwner());
        		newMsg.add(updateList.get(i).getCarNumber());
        		chs.client.handleMessageFromClientUI(newMsg);
        		newMsg.clear();
    		}
    	}
    	updateList.clear();
    }
    
    /**This method make validation to the value that the user insert to the cell.
     * @param vehicle
     * @return
     */
    private boolean validationForUpdate(Vehicle vehicle) {
    	boolean isInputValid = true;
    	
    	//Make validation to Car Owner name.
    	if(!CheckComponentInput.notContainNumbers(vehicle.getCarOwner()))
    		isInputValid = false;
    	
    	return isInputValid;
	}
    
    private boolean validation() {
    	boolean isInputValid = true;
    	
    	if(!CheckComponentInput.numericTextFieldValidation(SearchIdBar, lblCustomerIdErr))
    		isInputValid = false;
    	
    	return isInputValid;
    }
    
    private void setChoiceBoxValue() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				SearchVehiclesByChoiceBox.setValue(availableChoices.get(1));
			}
		});
	}
    
    private void setComponentToolTip() {
    	UpdateVehicleBtn.setTooltip(new Tooltip("Press the relevant cell\n"
    			+ "Insert new value\nPress enter keyboard\nPress update button"));
    	
    	AddVehicleBtn.setTooltip(new Tooltip("This will lead to the add vehicle page"));
    	
    	DeleteVehicleBtn.setTooltip(new Tooltip("Select the row to delete\nPress delete button"));
    	
    	//SearchCustomerBtn.setTooltip(new Tooltip("Insert "));

	}
    
/**
 * Controller and fxml Elements initialize.
 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		availableChoices = FXCollections.observableArrayList("Car Owner", "Car License Number", "Manufacturer", "Car Model", "Manufacturer Year");

		///availableChoices = FXCollections.observableArrayList("carOwner", "carLicenseNumber", "Manufacturer", "carModel", "manufacturerYear");
		SearchVehiclesByChoiceBox.setItems(availableChoices);	
		setChoiceBoxValue();
		VehiclesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
       	CarOwnerCol.setCellValueFactory(new PropertyValueFactory<>("CarOwner"));
       	CarNumberCol.setCellValueFactory(new PropertyValueFactory<>("CarNumber"));
       	ManufacturerCol.setCellValueFactory(new PropertyValueFactory<>("Manufacturer"));
       	ModelCol.setCellValueFactory(new PropertyValueFactory<>("Model"));
       	YearCol.setCellValueFactory(new PropertyValueFactory<>("Year"));
       	FuelTypeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
    
       	
       	CarOwnerCol.setCellFactory(TextFieldTableCell.forTableColumn());
       	CarOwnerCol.setOnEditCommit(e->{
			e.getTableView().getItems().get(e.getTablePosition().getRow()).setCarOwner(e.getNewValue());
			updateList.add(e.getTableView().getItems().get(e.getTablePosition().getRow()));
		});
		

       	VehiclesTable.setEditable(true);
       	setDeleteBtnDisable();
       	setComponentToolTip();
		setAddBtnDisable(true);
		setLabelText("");
		//The case the user add new car - the vehicle table have to be update.
		if(!av.carTableData.isEmpty()) {
			showDataInTable(av.carTableData);
			SearchIdBar.setText(av.customerIdString);
			av.customerIdString = "";
			setAddBtnDisable(false);
			
		}
	}

	public void customerNotExists() {
		setAddBtnDisable(true);
		setLabelText("Customer Not Exist");		
	}
	
	
	private void setLabelText(String str) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				lblCustomerIdErr.setText(str);
			}
	});
	}

}