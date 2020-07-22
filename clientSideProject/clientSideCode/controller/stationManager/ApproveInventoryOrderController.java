package controller.stationManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import emClient.ClientHandlerSingle;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class ApproveInventoryOrderController implements Initializable {
	public ClientHandlerSingle chs;
	public ApproveInventoryOrderController aioc;
	
    @FXML
    private TableView<Order> tblTable;

    @FXML
    private TableColumn<Order, String> tblFuelType;

    @FXML
    private TableColumn<Order, Integer> tblAmount;

    @FXML
    private TableColumn<Order, String> tblCreation;

    @FXML
    private TextField txtAmount;

    @FXML
    private DatePicker dtDate;

    @FXML
    private Button btnUpdateOrder;

    @FXML
    private Button btnApprove;

    @FXML
    private Label txtError;
    
    public class Order {
    	private String fuelType, deliveryDate;
    	private int amount;
		public Order(String fuelType, int amount, String deliveryDate) {
			super();
			this.fuelType = fuelType;
			this.deliveryDate = deliveryDate;
			this.amount = amount;
		}
		public String getFuelType() {
			return fuelType;
		}
		public void setFuelType(String fuelType) {
			this.fuelType = fuelType;
		}
		public String getDeliveryDate() {
			return deliveryDate;
		}
		public void setDeliveryDate(String deliveryDate) {
			this.deliveryDate = deliveryDate;
		}
		public int getAmount() {
			return amount;
		}
		public void setAmount(int amount) {
			this.amount = amount;
		}


    }
    
    public ApproveInventoryOrderController()
    {
		chs = ClientHandlerSingle.getInstance();
		chs.setAioc(this);
		aioc = this;
    }
    
    @FXML
    void approveOrder(MouseEvent event) {
    	Order selected = tblTable.getSelectionModel().getSelectedItem();
		if (selected == null)
    	{
    		txtError.setText("Please choose an order to approve");
    	}
    	else
    	{
        	ArrayList<String> newMsg = new ArrayList<String>();
        	newMsg.add("ApproveFuelOrderQuery");
        	newMsg.add(String.valueOf(chs.employeeNumber));
        	newMsg.add(selected.fuelType);
        	newMsg.add(String.valueOf(selected.amount));
        	chs.client.handleMessageFromClientUI(newMsg);		
    	}
    }
    
	/**
	 * The function is called after returning from server fill the data in the table.
	 * 
	 * @param answer - data for the table.
	 */
	@SuppressWarnings("unchecked")
	public void showDataInTable(Object answer) {
		ArrayList<Object> tempAnswer = (ArrayList<Object>) answer;
		tempAnswer = (ArrayList<Object>)tempAnswer.get(1);
		ArrayList<Object> tempString;
		ObservableList<Order> itemsForTable = FXCollections.observableArrayList();
		//DateFormat df = new SimpleDateFormat("dd/mm/yy");
		//LocalDate df = LocalDate.parse(tempString.get(1).toString());

		for (int i = 0; i < tempAnswer.size(); i++) {
			tempString = (ArrayList<Object>) (tempAnswer.get(i));
		//	itemsForTable.add(new Order((String)tempString.get(0),Integer.parseInt(String.valueOf(tempString.get(1))), df.format(tempString.get(2))));
			itemsForTable.add(new Order((String)tempString.get(0),Integer.parseInt(String.valueOf(tempString.get(1))), tempString.get(2).toString()));

			//itemsForTable.add(new Order("try1", 1, "try3"));
			
		}
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				tblTable.setItems(itemsForTable);
			}
		});

	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tblFuelType.setCellValueFactory(new PropertyValueFactory<>("fuelType"));
		tblAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
		tblCreation.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));
		
		tblFuelType.setStyle("-fx-alignment: CENTER;");
		tblAmount.setStyle("-fx-alignment: CENTER;");
		tblCreation.setStyle("-fx-alignment: CENTER;");
		
    	ArrayList<String> newMsg = new ArrayList<String>();
    	newMsg.add("GetApproveOrderQuery");
    	newMsg.add(String.valueOf(chs.employeeNumber));
    	chs.client.handleMessageFromClientUI(newMsg);		
	}

}
