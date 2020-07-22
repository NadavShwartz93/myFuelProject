package controller.marketingRepresentative;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.controlsfx.control.CheckComboBox;

import controller.MainDesplayController;
import emClient.ClientHandlerSingle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import logic.CheckComponentInput;
import logic.Company;

public class AddClientAssociationWithPurchasePlanModelController2 implements Initializable {
	
	static ArrayList<String> list = new ArrayList<String>();
	public ClientHandlerSingle chs;
	public AddCustomerController ac;
	public AddClientAssociationWithPurchasePlanModelController2 acawp2;
	ArrayList<String> tempMsg = new ArrayList<String>();
	ArrayList<String> Station = new ArrayList<String>();
	ObservableList<String> availableChoices = FXCollections.observableArrayList("Normal Fueling", "Regular Monthly Subscription One Car", "Regular Monthly Subscription Some Cars", "Full Monthly Subscription One Car");

	ListView<String> listStation = new ListView<String>();
	
	public AddClientAssociationWithPurchasePlanModelController2() {
		chs = ClientHandlerSingle.getInstance();
		chs.setAcawp2(this);
		acawp2 = this;
	}

    @FXML
    private Button SaveBtn;
    @FXML
    private Label CustomerResultText;    
    @FXML
    private ChoiceBox<String> ModelChoiceBox;
    @FXML
    private ListView<String> StationListView;
    @FXML
    private Label lblChooseModelErr;
    
    @FXML

    /**
     * This method is responsible for displaying available fuel companies
     */
    public void setList(Object answer) {
    	ArrayList<Object> tempAnswer = (ArrayList<Object>) answer;
		int j = 0;		
    	ObservableList<String> strings = 
    		    FXCollections.observableArrayList();
   	 for (int i = 0; i <= tempAnswer.size()-1; i++) {
	     strings.add(((ArrayList<String>) tempAnswer.get(i)).get(j).toString());
	     j++;
	 }
   	StationListView.setItems(strings);
   	StationListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
    
    @FXML
    void SaveBtn(ActionEvent event) {
    	if(validation()) {
    		clearErrors();
    		sendDataToServer();
    	}
    }
    
    /**
	 * Send all the relevant data to server.
	 */
	private void sendDataToServer() {
    	ObservableList selectedItems = StationListView.getSelectionModel().getSelectedItems();
    	if(selectedItems.size() != 0) {
        	tempMsg.add("add model type");
           	tempMsg.add(ac.list.get(0));
           	tempMsg.add(getModelName(ModelChoiceBox.getValue()));

           	
           	if(selectedItems.size()==3) {//The case customer select 3 different gas station.
           		tempMsg.add(selectedItems.get(0).toString());
           		tempMsg.add(selectedItems.get(1).toString());
               	tempMsg.add(selectedItems.get(2).toString());
           	}
           	else if(selectedItems.size()==2) {//The case customer select 2 different gas station.
           		tempMsg.add(selectedItems.get(0).toString());
           		tempMsg.add(selectedItems.get(1).toString());
               	tempMsg.add("");
           	}else {//The case customer select only 1 gas station.
           		tempMsg.add(selectedItems.get(0).toString());
               	tempMsg.add("");
               	tempMsg.add("");
           	}
           
           	System.out.println(tempMsg);
        	chs.client.handleMessageFromClientUI(tempMsg);
        	changeWindowToAddVehicle();
    	}	
	}
	
  	/**This is helper method that convert some GUI phrases to the DB phrases. 
     * For instance, the method get "Normal Fueling" and return "NormalFueling".
  	 * @param str
  	 * @return
  	 */
	private String getModelName(String str) {
		if(str.equals("Normal Fueling"))
			return "NormalFueling";
		
		else if(str.equals("Regular Monthly Subscription One Car"))
			return "RegularMonthlySubscriptionOneCar";
		
		else if(str.equals("Regular Monthly Subscription Some Cars"))
			return "RegularMonthlySubscriptionSomeCars";
		
		return "FullMonthlySubscriptionOneCar";
		
	}
  	/**
  	 * this method Passing you to the window that is located.
  	 */
    public void changeWindowToAddVehicle() {
    	Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(MainDesplayController.class.getResource("/fxml/MarketingRepresentative/AddVehicle2.fxml"));
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
    
    private boolean validation() {
		boolean isInputValid = true;
		
		if(!CheckComponentInput.notEmptyChoiceBoxValidation(ModelChoiceBox, lblChooseModelErr))
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
				lblChooseModelErr.setText("");
			}
		});
	}
	/**
	 * Controller and fxml Elements initialize.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//CompanyNameTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		//CompanyNameCol.setCellValueFactory(new PropertyValueFactory<>("CompamyName"));
		String tempString = ac.list.get(0);
		tempString += "\n"; 
		tempString += ac.list.get(1);
		tempString += " ";
		tempString += ac.list.get(2);
		CustomerResultText.setText(tempString);	
		ModelChoiceBox.setItems(availableChoices);				
		ArrayList<String> newMsg = new ArrayList<String>();
		newMsg.add("GetCompany");
    	newMsg.add("showCompanyInList2");
    	chs.client.handleMessageFromClientUI(newMsg);
    	
    	clearErrors();
       			
	}

}
