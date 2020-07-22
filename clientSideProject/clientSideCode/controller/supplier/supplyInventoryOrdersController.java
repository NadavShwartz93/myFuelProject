package controller.supplier;

import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ResourceBundle;

import emClient.ClientHandlerSingle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import logic.OrderHistory;
import logic.SupplyInventoryOrder;

/**This class is for manage the supplyInventoryOrders.fxml file.
 * The class contain method the manage the operate of the {@link #inventoryOrderTable},
 * and method that manage the {@link #orderDeliveredBtn}.
 * @author Nadav Shwartz.
 *
 */
public class supplyInventoryOrdersController implements Initializable {
	
	public ClientHandlerSingle chs;
	public supplyInventoryOrdersController sioc;
	
	public supplyInventoryOrdersController() {
		chs = ClientHandlerSingle.getInstance();
		chs.setSioc(this);
		sioc = this;
		
	}
	
    @FXML
    private TableView<SupplyInventoryOrder> inventoryOrderTable;
    @FXML
    private TableColumn<String, SupplyInventoryOrder> gasStationNameCol;
    @FXML
    private TableColumn<String, SupplyInventoryOrder> gasStationAddressCol;
    @FXML
    private TableColumn<String, SupplyInventoryOrder> fuelTypeCol;
    @FXML
    private TableColumn<Integer, SupplyInventoryOrder> fuelAmountCol;
    @FXML
    private Button orderDeliveredBtn;
	private Hashtable<Integer,  ArrayList<String>> ordersHash = new Hashtable<>();
	private ObservableList<SupplyInventoryOrder> itemsForTable = FXCollections.observableArrayList();


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		getSupplierDate();
		SetTableAndColumn();

		
	}
	
	
	/**This method initialize the {@link #inventoryOrderTable} and the Column of this table.
	 */
	private void SetTableAndColumn() {
		inventoryOrderTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		gasStationNameCol.setCellValueFactory(new PropertyValueFactory<>("gasStationName"));
		gasStationAddressCol.setCellValueFactory(new PropertyValueFactory<>("gasStationAddress"));
		fuelTypeCol.setCellValueFactory(new PropertyValueFactory<>("fuelType"));
		fuelAmountCol.setCellValueFactory(new PropertyValueFactory<>("fuelAmount"));
	}
	
	/**
	 * This method call to the server side to bring some relevant data for this class.
	 */
	private void getSupplierDate() {
		ArrayList<Object> answer = new ArrayList<>(); 
		answer.add("supplier");
		answer.add("/inventoryOrders");
		
		chs.client.handleMessageFromClientUI(answer);
	}
	
	/**This method get the relevant data that arrived from the server.
	 * @param message contain the relevant data that will be use in this class.
	 */
	public void setSupplierData(ArrayList<Object> message) {
		int key = 0;
		ArrayList<String> order = new ArrayList<>();
		for (int i = 0; i < message.size(); i++) {
			if(i%4 == 0) {
				String gasStationName = message.get(i).toString();
				order.add(gasStationName);
				
			}else if(i%4 == 1) {
				String gasStationAddress = message.get(i).toString();
				order.add(gasStationAddress);
				
			}else if(i%4 == 2) {
				String fuelType = message.get(i).toString();
				order.add(fuelType);
				
			}else {
				int temp = (int) message.get(i);
				String fuelAmount = String.valueOf(temp);
				order.add(fuelAmount);
				ordersHash.put(key, order);
				key++;
				order = new ArrayList<>();
				
			}
		}
		insertOrdersToTable();//Insert the data to the tableView.
		
	}
	
	/**
	 * This method insert the orders in to the {@link #inventoryOrderTable}.
	 */
	private void insertOrdersToTable() {
		
		itemsForTable.clear();
		
		ArrayList<String> tempArrayList = new ArrayList<>();
		for (int i = 0; i < ordersHash.size(); i++) {
			tempArrayList = ordersHash.get(i);
			String gasStationName = tempArrayList.get(0);
			String gasStationAddress = tempArrayList.get(1);
			String fuelType = tempArrayList.get(2);
			Integer fuelAmount = Integer.parseInt(tempArrayList.get(3));
			SupplyInventoryOrder sio = new SupplyInventoryOrder(gasStationName, gasStationAddress, 
					fuelType, fuelAmount);
			
			itemsForTable.add(sio);
		
		}
		ordersHash.clear();
		
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				inventoryOrderTable.setItems(itemsForTable);
			}
		});
		
		tableViewValidation();
		
	}
	
  
	/**This method read the order from the {@link #inventoryOrderTable}, 
	 * that the supplier provided, 
     * and send it to the server side to updates.
     * @param event is the event of press {@link #orderDeliveredBtn}.
     */
    @FXML
    public void orderDeliveredBtn(ActionEvent event) {
       	TableViewSelectionModel<SupplyInventoryOrder> selectionModel = inventoryOrderTable.getSelectionModel();
    	
    	// set selection mode to multiple rows
    	selectionModel.setSelectionMode(SelectionMode.MULTIPLE);
    	
    	ObservableList<SupplyInventoryOrder> selectedItems = selectionModel.getSelectedItems();
    	
    	int size = selectedItems.size();
    	ArrayList<ArrayList<String>> message = new ArrayList<>();
    	ArrayList<String> temp = new ArrayList<>();
    	for(int i = 0; i < size; i++) {
    		SupplyInventoryOrder sio = selectedItems.get(i);
    		
    		temp.add(sio.getGasStationName());
    		temp.add(sio.getGasStationAddress());
    		temp.add(sio.getFuelType());
    		temp.add(String.valueOf(sio.getFuelAmount()));
    		message.add(temp);
    		temp = new ArrayList<>();
    	}    	
    	selectionModel.clearSelection();
    	
		ArrayList<Object> answer = new ArrayList<>(); 
		answer.add("supplier");
		answer.add("/insertAndUpdateOrders");
		answer.add(message);
		chs.client.handleMessageFromClientUI(answer);

    }
    
    
    /**This method check if the TableView has some data to show, if not then return false.
     * @return
     */
	private boolean tableViewValidation() {
    	boolean isTableViewFull = true;
		if(itemsForTable.isEmpty()) {
			isTableViewFull = false;
    		orderDeliveredBtn.setDisable(true);
		}else {
    		orderDeliveredBtn.setDisable(false);
		}
		return isTableViewFull;
	}
	

}
