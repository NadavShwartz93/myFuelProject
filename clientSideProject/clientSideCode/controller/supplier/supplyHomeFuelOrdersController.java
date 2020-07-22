package controller.supplier;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import emClient.ClientHandlerSingle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import logic.HomeFuelOrder;
import logic.SupplyInventoryOrder;
import javafx.fxml.Initializable;

/**This class is for manage the supplyHomeFuelOrders.fxml file.
 * the class contain method that manage the operate of the TableView and method that 
 * manage: 1.the Order delivered Button, 2.the order is in process Button.
 * @author Nadav Shwartz.
 *
 */
public class supplyHomeFuelOrdersController implements Initializable {
	
	public ClientHandlerSingle chs;
	public supplyHomeFuelOrdersController shfoc;
	
	
	public supplyHomeFuelOrdersController(){
		chs = ClientHandlerSingle.getInstance();
		chs.setShfoc(this);
		shfoc = this;
	}
	
	
	
	 	@FXML
	    private TableView<HomeFuelOrder> homeFuelOrdersTable;
	    @FXML
	    private TableColumn<HomeFuelOrder, LocalDate> orderDateCol;
	    @FXML
	    private TableColumn<HomeFuelOrder, LocalDate> deliveryDateCol;
	    @FXML
	    private TableColumn<HomeFuelOrder, String> amountCol;
	    @FXML
	    private TableColumn<HomeFuelOrder, String> customerIdCol;
	    @FXML
	    private TableColumn<HomeFuelOrder, String> customerFirstNameCol;
	    @FXML
	    private TableColumn<HomeFuelOrder, String> customerLastNameCol;
	    @FXML
	    private TableColumn<HomeFuelOrder, String> customerAddressCol;
	    @FXML
	    private TableColumn<HomeFuelOrder, String> orderStatusCol;//Make this column to be sorted.
	    @FXML
	    private Button orderDeliveredBtn;
	    @FXML
	    private Label lblOrderDeliveredErr;
	    @FXML
	    private Button orderInProcessBtn;
	    
	
	    /**This method send request to the server for getting all the relevant data for 
	     * showing in the table.
	     */
	    private void getHomeFuelOrder() {
			ArrayList<Object> answer = new ArrayList<>(); 
			answer.add("supplier");
			answer.add("/getHomeFuelOrder");
			
			chs.client.handleMessageFromClientUI(answer);
	    	
	    	
		}
	
	    /**This method get data in ArrayList of type Object from the server side, 
	     * and insert all the data into homeFuelOrdersTable TableView.
	     * the homeFuelOrdersTable TableView.
	     * @param message contain the relevant data from server.
	     */
	    public void setHomeFuelOrder(ArrayList<Object> message ) {
			ObservableList<HomeFuelOrder> itemsForTable = FXCollections.observableArrayList();
			String tempDate = null;
			
	    	for (int i = 0; i < message.size(); i++) {
	    		ArrayList<Object> temp = (ArrayList<Object>) message.get(i);
	    		
	    		tempDate = ((ArrayList<Object>) message.get(i)).get(0).toString();	    		
	    		LocalDate orderDate = LocalDate.parse(tempDate);
	    		
	    		tempDate = ((ArrayList<Object>) message.get(i)).get(1).toString();
	    		LocalDate deliveryDate = LocalDate.parse(tempDate);
	    		
	    		String fuelAmount = temp.get(2).toString();
	    		String customerId = temp.get(3).toString();
	    		String customerFirstName = temp.get(4).toString();
	    		String customerLastName = temp.get(5).toString();
	    		int purchaseNumber = (int) temp.get(6);
	    		String customerAddress = temp.get(7).toString();
	    		String orderStatus = temp.get(8).toString();
	    		
	    		itemsForTable.add(new HomeFuelOrder(orderDate, fuelAmount, deliveryDate, 
	    				customerId, customerFirstName, customerLastName, customerAddress, purchaseNumber, orderStatus));
			}
	    	
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					homeFuelOrdersTable.setItems(itemsForTable);
					
					deliveryDateCol.setSortType(TableColumn.SortType.ASCENDING);
					homeFuelOrdersTable.getSortOrder().add(deliveryDateCol);
					homeFuelOrdersTable.sort();
				}
			});
		}
	
	
	    /**This method is operate after user press the order delivered button.
	     * the method let user(Supplier) send order just if the order delivered day is today or already passed.
	     * @param event the moment the customer select row, from table, and press the Button, 
	     * this method start work.
	     */
	    @FXML
	   void orderDeliveredBtn(ActionEvent event) {
	    	TableViewSelectionModel<HomeFuelOrder> selectionModel = homeFuelOrdersTable.getSelectionModel();
	    	
	    	// set selection mode to multiple rows
	    	selectionModel.setSelectionMode(SelectionMode.MULTIPLE);
	    	
	    	ObservableList<HomeFuelOrder> selectedItems = selectionModel.getSelectedItems();
	    	
	    	int size = selectedItems.size();
	    	ArrayList<ArrayList<Object>> message = new ArrayList<>();
	    	ArrayList<Object> temp = new ArrayList<>();
	    	String arrivalStatus = "Order delivered";
	    	
	    	for(int i = 0; i < size; i++) {
	    		HomeFuelOrder homeFuelOrder = selectedItems.get(i);
	    		LocalDate deliveryDate = homeFuelOrder.getDeliveryDate();
	    		
    			//only if the order delivery date is right now or it has already passed 
    			//then the supplier can deliver it.
	    		if(deliveryDate.isBefore(LocalDate.now()) || deliveryDate.equals(LocalDate.now())
	    				&& (homeFuelOrder.getOrderStatus().equals("Order is in process"))) {
	    			temp.add(homeFuelOrder.getPurchaseNumber());
	    			temp.add(arrivalStatus);
	    			message.add(temp);
	    			temp = new ArrayList<>();
	    			
	    		}else if(homeFuelOrder.getOrderStatus().equals("Order sent")){
	    			setLabel("The order have to be in process");
	    		}else {
	    			setLabel("Delivery day has not arrived yet");
	    		}

	    	}    	
	    	
	    	selectionModel.clearSelection();
	    	
			ArrayList<Object> answer = new ArrayList<>(); 
			answer.add("supplier");
			answer.add("/updateHomeFuelOrder");
			answer.add(message);
			chs.client.handleMessageFromClientUI(answer);
	    }
	    
	    /**This method is operate after user click the table, the method call to setLabel(String str)
	     * with empty string. so actually the method hide the lblOrderDeliveredErr Label.
	     * @param event
	     */
	    @FXML
	    void hideErrorLalel(MouseEvent event) {
	    	setLabel("");
	    }   
	    
	    /**This method change the status of order to "Order is in process", if the order is just 
	     * sent by the customer.
	     * @param event the moment the customer select row, from table, and press the Button, 
	     * this method start work.
	     */
	    @FXML
	    void orderInProcessBtn(ActionEvent event) {
	    	TableViewSelectionModel<HomeFuelOrder> selectionModel = homeFuelOrdersTable.getSelectionModel();
	    	
	    	// set selection mode to multiple rows
	    	selectionModel.setSelectionMode(SelectionMode.MULTIPLE);
	    	
	    	ObservableList<HomeFuelOrder> selectedItems = selectionModel.getSelectedItems();
	    	
	    	ArrayList<ArrayList<Object>> message = new ArrayList<>();
	    	ArrayList<Object> temp = new ArrayList<>();
	    	
	    	int size = selectedItems.size();
	    	for (int i = 0; i < size; i++) {
	    		HomeFuelOrder homeFuelOrder = selectedItems.get(i);
	    		if(homeFuelOrder.getOrderStatus().equals("Order is in process")) {
	    			//Throw error message.
	    			setLabel("Order is already in process");
	    		}else if(homeFuelOrder.getOrderStatus().equals("Order sent")) {
	    			temp.add(homeFuelOrder.purchaseNumber);
	    			temp.add("Order is in process");
	    			message.add(temp);
	    			temp = new ArrayList<>();
	    		}
			}
	    	
	    	selectionModel.clearSelection();
	    	
	    	if(message.size() > 0) {
				ArrayList<Object> answer = new ArrayList<>(); 
				answer.add("supplier");
				answer.add("/updateHomeFuelOrder");
				answer.add(message);
				chs.client.handleMessageFromClientUI(answer);	
	    	}
	    }
	    
	/**This method got string and insert it to the lblOrderDeliveredErr.
	 * @param str
	 */
	private void setLabel(String str) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				lblOrderDeliveredErr.setText(str);
			}
		});
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		homeFuelOrdersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		orderDateCol.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
		deliveryDateCol.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));
		amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
	    customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));	    
	    customerFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("customerFirstName"));	    
	    customerLastNameCol.setCellValueFactory(new PropertyValueFactory<>("customerLastName"));   
	    customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
	    orderStatusCol.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
	    
	    getHomeFuelOrder();
	    lblOrderDeliveredErr.setText("");

	}

}
