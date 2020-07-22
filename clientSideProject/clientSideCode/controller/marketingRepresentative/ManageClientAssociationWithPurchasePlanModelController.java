package controller.marketingRepresentative;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ManageClientAssociationWithPurchasePlanModelController implements Initializable {

	ArrayList<String> tempMsg = new ArrayList<String>();
	public ClientHandlerSingle chs;
	static ArrayList<String> list = new ArrayList<String>();
	public ManageClientAssociationWithPurchasePlanModelController caw;
	 private String tempString1, tempString2;
	ObservableList<String> availableChoices = FXCollections.observableArrayList("Normal Fueling", "Regular Monthly Subscription One Car", "Regular Monthly Subscription Some Cars", "Full Monthly Subscription One Car");
	public ManageClientAssociationWithPurchasePlanModelController() {
		chs = ClientHandlerSingle.getInstance();
		chs.setCawc(this);
		caw = this;
	}
	 @FXML
	private Button UpdateCompaniesBtn;
	 @FXML
	 private Button UpdateModelBtn;

	@FXML
    private TextField CustomerIdBar;

    @FXML
    private Button CustomerIdSearchBtn;

    @FXML
    private ChoiceBox<String> ModelChoiceBox;

    @FXML
    private TextArea ResultArea;

    @FXML
    private ListView<String> StationListView;
    @FXML
    private Label lblError;
    

    /**This method set text on text area.
     * @param msg contain the relevant data from server.
     */
    public void setOnLabel(Object msg) {
    	list.clear();
    	ArrayList<ArrayList> oldMsg = (ArrayList<ArrayList>)msg;
    	if(!oldMsg.isEmpty()) {
    		setLabelStr("");
    		UpdateModelBtn.setDisable(false);
    		UpdateCompaniesBtn.setDisable(false);
    		
        	tempString1 = String.format("%s %s %s\n", oldMsg.get(0).get(0), oldMsg.get(0).get(1), oldMsg.get(0).get(2));
        	tempString1 += getModelName(oldMsg.get(0).get(3).toString());//New
        	tempString1 += "\n";
        	if(oldMsg.size()>2) {
        		tempString2 = String.format("%s \n%s \n%s", oldMsg.get(0).get(4), oldMsg.get(1).get(4), oldMsg.get(2).get(4));
        		list.add(oldMsg.get(0).get(4).toString());
        		list.add(oldMsg.get(1).get(4).toString());
        		list.add(oldMsg.get(2).get(4).toString());
        	}else if(oldMsg.size() == 2) {
        		tempString2 = String.format("%s \n%s \n", oldMsg.get(0).get(4), oldMsg.get(1).get(4));
        		list.add(oldMsg.get(0).get(4).toString());
        		list.add(oldMsg.get(1).get(4).toString());
        	}else {
           	 tempString2 = oldMsg.get(0).get(4).toString();
           	 list.add(oldMsg.get(0).get(4).toString());
        	}

        	tempString1 += tempString2;
        	Platform.runLater(new Runnable() {
    			@Override
    			public void run() {
    		
    				ResultArea.setText(tempString1);

    	
    			}
    		});
    	}else {
    		setLabelStr("Customer not exist");
    		UpdateModelBtn.setDisable(true);
    		UpdateCompaniesBtn.setDisable(true);
    	}

    	
    }
    
  	/**This is helper method that convert some GUI phrases to the DB phrases. 
     * For instance, the method get "Normal Fueling" and return "NormalFueling".
  	 * @param str
  	 * @return
  	 */
  	private String getModelName(String str) {
		if(str.equals("NormalFueling"))
			return "Normal Fueling";
		
		else if(str.equals("RegularMonthlySubscriptionOneCar"))
			return "Regular Monthly Subscription One Car";
		
		else if(str.equals("RegularMonthlySubscriptionSomeCars"))
			return "Regular Monthly Subscription Some Cars";
		
		return "Full Monthly Subscription One Car";
		
	}
    
    /**
     * This method send search request to the server side.
     */
    @FXML
    void CustomerIdSearchBtn(ActionEvent event) {
    	ArrayList<String> newMsg = new ArrayList<String>();
    	newMsg.add("findCustomer");
    	newMsg.add(CustomerIdBar.getText());
    	chs.client.handleMessageFromClientUI(newMsg);
    }

    /**This method is responsible for displaying available fuel companies
     * @param answer contain the relevant data from server.
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
    /**
     * This method send update request to the server side.
     */
    @FXML
    void UpdateCompaniesBtn(ActionEvent event) {
    	
    	tempMsg = new ArrayList<String>();

    	ObservableList selectedItems = StationListView.getSelectionModel().getSelectedItems();
    	if(selectedItems.size() >= 1 && selectedItems.size() <= 3) {
    		setLabelStr("");
        	tempMsg.add("updateCompanie");
        	tempMsg.add(CustomerIdBar.getText());
        	
        	if(selectedItems.size() == 1){
           		tempMsg.add(selectedItems.get(0).toString());
           		tempMsg.add("");
               	tempMsg.add("");
        	}
        	
           	if(selectedItems.size() == 2) {
           		tempMsg.add(selectedItems.get(0).toString());
           		tempMsg.add(selectedItems.get(1).toString());
               	tempMsg.add("");
           	}
        	
        	if(selectedItems.size()==3) {
           		tempMsg.add(selectedItems.get(0).toString());
           		tempMsg.add(selectedItems.get(1).toString());
               	tempMsg.add(selectedItems.get(2).toString());
           	}
        	

        	chs.client.handleMessageFromClientUI(tempMsg);
    	}else {
    		setLabelStr("Chooce between 1 - 3 stations");
    	}
    }
    /**
     * This method send update request to the server side.
     */
    @FXML
    void UpdateModelBtn(ActionEvent event) {
    	tempMsg = new ArrayList<String>();
    	if(!ModelChoiceBox.getSelectionModel().isEmpty()) {
    		setLabelStr("");
    		
        	tempMsg.add("updateModel");
        	tempMsg.add(CustomerIdBar.getText());
        	if(ModelChoiceBox.getValue().equals("Normal Fueling")) {
        		tempMsg.add("NormalFueling");
        	}else if(ModelChoiceBox.getValue().equals("Regular Monthly Subscription One Car")) {
        		tempMsg.add("RegularMonthlySubscriptionOneCar");
        	}else if(ModelChoiceBox.getValue().equals("Regular Monthly Subscription Some Cars")) {
        		tempMsg.add("RegularMonthlySubscriptionSomeCars");
        	}else {
        		tempMsg.add("FullMonthlySubscriptionOneCar");
        	}
           	System.out.println(tempMsg);
           	chs.client.handleMessageFromClientUI(tempMsg);
    	}else {
    		setLabelStr("Chooce model");
    	}

    }
    
    private void setLabelStr(String str) {	
    	Platform.runLater(new Runnable() {
			@Override
			public void run() {	
				lblError.setText(str);
			}
		});
	}
    /**
     * Controller and fxml Elements initialize.
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ModelChoiceBox.setItems(availableChoices);
		ArrayList<String> newMsg = new ArrayList<String>();
		newMsg.add("GetCompany");
    	newMsg.add("showCompanyInListMnage");
    	chs.client.handleMessageFromClientUI(newMsg);
    	setLabelStr("");
		UpdateModelBtn.setDisable(true);
		UpdateCompaniesBtn.setDisable(true);
		
	}

}
