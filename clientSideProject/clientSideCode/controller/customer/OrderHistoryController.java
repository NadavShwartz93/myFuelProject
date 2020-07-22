package controller.customer;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import emClient.ClientHandlerSingle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import logic.CheckComponentInput;
import logic.OrderHistory;


/**This class is for manage the @OrderHistory.fxml file. 
 * the class is manage the process of showing the customer all of his 
 * orders for home fuel over a period of time
 * @author Nadav Shwartz.
 *
 */
public class OrderHistoryController  implements Initializable   {
	
	public ClientHandlerSingle chs;
	public OrderHistoryController ohc;
	
	public OrderHistoryController() {
		chs = ClientHandlerSingle.getInstance();
		chs.setOhc(this);
		ohc = this;
		
	}
	
	@FXML
	private CheckBox inProgressCB;
	@FXML
	private CheckBox deliveredCB;
	@FXML
	private DatePicker fromDate;
	@FXML
	private DatePicker toDate;
	@FXML
	private Button btnFilter;
	@FXML
	private TableView<OrderHistory> orderHistoryTable;
	@FXML
	private TableColumn<LocalDate, OrderHistory> orderDateCol;
	@FXML
	private TableColumn<String, OrderHistory> amountCol;
	@FXML
	private TableColumn<LocalDate, OrderHistory> deliveryDateCol;
	@FXML
	private TableColumn<String, OrderHistory> statusCol;
    @FXML
    private Label lblDataPickerErr;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		orderHistoryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		orderDateCol.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
		amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
		deliveryDateCol.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));
		statusCol.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
		
		setLabelText(lblDataPickerErr, "");
		//btnInfo.setTooltip(new Tooltip("This is an information button"));
	}
	
	/**This is helper method that get DatePicker component, and return String 
	 * that represent the value of this DatePicker.
	 * @param date is a DatePicker component.
	 * @return String that represent the value of the DatePicker.
	 */
	public String getDate(DatePicker date) {
		LocalDate value = date.getValue();
		return value.toString();
	}
	
	@FXML
    void btnFilter(ActionEvent event) {
		if(validation()) {
			setLabelText(lblDataPickerErr, "");
			sendSearchOrder();
		}
    }
	
	/**This method send data to the server side, 
	 * in order to receive relevant data back.
	 */
	private void sendSearchOrder() {
		Boolean inProgressSelected = inProgressCB.isSelected();
		Boolean deliveredSelected = deliveredCB.isSelected();
		LocalDate from_Date = fromDate.getValue();
		LocalDate to_Date = toDate.getValue();
		
		ArrayList<Object> answer = new ArrayList<>(); 
		answer.add("customer/OrderHistory");
		answer.add(inProgressSelected);
		answer.add(deliveredSelected);
		answer.add(from_Date);
		answer.add(to_Date);
		answer.add(chs.customerid);

		chs.client.handleMessageFromClientUI(answer);
	}
	
	
	/**This method get relevant data from the server side, and insert it into the {@link #orderHistoryTable}.
	 * @param msg contain all the relevant data that {@link #orderHistoryTable}
	 * will contain.
	 */
	@SuppressWarnings("unchecked")
	public void setOrderInTable(ArrayList<Object> msg)
	{
		ArrayList<Object> answer = (ArrayList<Object>) msg.get(1);
		ObservableList<OrderHistory> itemsForTable = FXCollections.observableArrayList();
		int j = 0;		
		
		for(int i = 0; i < answer.size()/4; i++) {
			LocalDate orderDate = LocalDate.parse(answer.get(j).toString());
			j++;
			String amount = answer.get(j).toString();
			j++;
			LocalDate deliveryDate = LocalDate.parse(answer.get(j).toString());
			j++;
			String orderStatus = answer.get(j).toString();
			j++;
		
			itemsForTable.add(new OrderHistory(orderDate, amount, deliveryDate, orderStatus));	
		}
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				orderHistoryTable.setItems(itemsForTable);
			}
		});
		
		
	}
	
	/**This method make validation test to this page GUI components.
	 * The method call {@link CheckComponentInput} for the validation test.
	 * @return true if and only if all the components of the page passed the validation test.
	 */
	private boolean validation() {
		boolean isInputValid = true;
		
		if(!CheckComponentInput.notEmptyDatePickerValidation(fromDate, lblDataPickerErr))
			isInputValid = false;
		
		if(!CheckComponentInput.notEmptyDatePickerValidation(toDate, lblDataPickerErr))
			isInputValid = false;
		
		return isInputValid;
	}
	
	/**This method set String into the the Label.
	 * @param label will contain String.
	 * @param str is the String that will insert to the label.
	 */
	private void setLabelText(Label label, String str) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				label.setText(str);
			}
		});
	}
	

	

}
