package controller.marketingManager;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import com.sun.javafx.collections.MappingChange.Map;

import emClient.ClientHandlerSingle;
import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import logic.Sales;
/**
 * This Class is for the Marketing Manager to activate and disable Sales.
 * @author Boaz Trauthwein
 */
public class InitiateSaleController implements Initializable {
	public ClientHandlerSingle chs;
	private ArrayList<Object> salesTable;
	private java.util.Map<String, Integer> mapTableCoulmn;
	ObservableList<Sales> itemsForTable;

	public InitiateSaleController() {
		chs = ClientHandlerSingle.getInstance();
		chs.setIsc(this);
	}

	@FXML
	private ComboBox<String> cbColumn;

	@FXML
	private ComboBox<String> cbField;

	@FXML
	private TableView<Sales> tblSales;

	@FXML
	private TableColumn<String, Sales> saleNameCol;

	@FXML
	private TableColumn<String, Sales> fuelTypeCol;

	@FXML
	private TableColumn<String, Sales> discountCol;

	@FXML
	private TableColumn<String, Sales> startHourCol;

	@FXML
	private TableColumn<String, Sales> endHourCol;

	@FXML
	private TableColumn<String, Sales> isActiveCol;

	@FXML
	private Button btnSetSale;

	@FXML
	private Button btnDisableSale;

	@FXML
	private Label lblErr;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		saleNameCol.setCellValueFactory(new PropertyValueFactory<>("saleName"));
		fuelTypeCol.setCellValueFactory(new PropertyValueFactory<>("fuelType"));
		discountCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
		startHourCol.setCellValueFactory(new PropertyValueFactory<>("startHour"));
		endHourCol.setCellValueFactory(new PropertyValueFactory<>("endHour"));
		isActiveCol.setCellValueFactory(new PropertyValueFactory<>("isActive"));

		lblErr.setText("");

		ArrayList<Object> request = new ArrayList<>();
		request.add("marketingManager/initiateSale");
		request.add("GetAllSales");

		chs.client.handleMessageFromClientUI(request);

		mapTableCoulmn = new HashMap<String, Integer>();

		cbColumn.getItems().addAll("Sale Name", "Fuel Type", "Discount %", "Start Hour", "End Hour", "Is Active");
		for (int i = 0; i < cbColumn.getItems().size(); i++)//For later use to fill cbField.
			mapTableCoulmn.put(cbColumn.getItems().get(i), new Integer(i));

	}
/**
 * This function organizes sales table for the next step.
 * @param msg - Content from database with sales table
 */
	public void setSalesInTableInitialize(ArrayList<Object> msg) {
		// TODO Auto-generated method stub
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				lblErr.setText("");
			}
		});
		salesTable = (ArrayList<Object>) msg.get(1);
		setSaleTable(salesTable);
	}

	/**
	 * This function is activated after clicking the button Disable Sale
	 * It checks if sale selected from table is not null,
	 * and if sale was activated before.
	 * If everything is valid setActivateStatus is called to disable Sale.
	 * @param event - ActionEvent
	 */
	@FXML
	void DisableSale(ActionEvent event) {
		Sales selectedSale = tblSales.getSelectionModel().getSelectedItem();
		if (selectedSale != null && selectedSale.getIsActive() == true)
			setActivateStatus(0);
	}

	
	/**
	 * This function is activated after clicking the button Activate Sale
	 * It checks if sale selected from table is not null,
	 * and if sale is not active.
	 * If everything is valid setActivateStatus is called to activate Sale.
	 * @param event - ActionEvent
	 */
	@FXML
	void SetSale(ActionEvent event) {
		Sales selectedSale = tblSales.getSelectionModel().getSelectedItem();
		if (selectedSale != null && selectedSale.getIsActive() == false)
			setActivateStatus(1);
	}

	@FXML
	/**
	 * This function is activated when the Choose Column combobox value is changed/
	 * It clears the values of Field combobox and calls the function chooseColumn.
	 * @param event - ActionEvent
	 */
	void chooseColumn(ActionEvent event) {
		cbField.getItems().clear();
		setCbFieldsWithValues();
	}
	
/**
 * This function is activated when the value of Field combobox is changed
 * it genarates a new table to be presented and sends it to function setSaleTable.
 * @param event - ActionEvent
 */
	@FXML
	void chooseField(ActionEvent event) {
		if (cbField.getItems().size() > 0) {
			ObservableList<Sales> itemsForTableSearchBy = FXCollections.observableArrayList();
			int z = mapTableCoulmn.get(cbColumn.getValue().toString()); // Get Column in data table.

			ArrayList<Object> row;
			ArrayList<Object> rowsToBuildTable = new ArrayList<Object>();
			for (int i = 0; i < salesTable.size(); i++) {
				int j = 0;
				row = (ArrayList<Object>) salesTable.get(i);
				if (String.valueOf(row.get(z)).compareTo(cbField.getValue().toString()) == 0)
					rowsToBuildTable.add(row);
			}
			setSaleTable(rowsToBuildTable);
		}

	}

	/**
	 * This function is activated when the user wants again to see all the Data.
	 * @param event - 
	 */
	@FXML
	void SeeAllSales(ActionEvent event) {
		setSaleTable(salesTable);
	}

	/**
	 * This function is activated when there are some sales to present in the tableview.
	 * @param table - Sales Table from database.
	 */
	private void setSaleTable(ArrayList<Object> table) {
		ArrayList<Object> row;
		itemsForTable = FXCollections.observableArrayList();

		for (int i = 0; i < table.size(); i++) {
			int j = 0;
			row = (ArrayList<Object>) table.get(i);
			String saleName = String.valueOf(row.get(j++));
			String fuelType = String.valueOf(row.get(j++));
			Float disscount = Float.valueOf(String.valueOf(row.get(j++)));
			LocalTime startHour = LocalTime.parse(String.valueOf(row.get(j++)));
			LocalTime endHour = LocalTime.parse(String.valueOf(row.get(j++)));
			Boolean isActive = Boolean.valueOf(String.valueOf(row.get(j++)));
			itemsForTable.add(new Sales(saleName, fuelType, disscount, startHour, endHour, isActive));
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				tblSales.setItems(itemsForTable);
			}
		});
	}

	/**
	 * This function is activated when Column combobox value is changed.
	 * It generates the field values that matches column value int field combobox
	 */
	private void setCbFieldsWithValues() {
		// cbField.getItems().clear();
		int z = mapTableCoulmn.get(cbColumn.getValue().toString()); // Get Column in data table.
		ArrayList<Object> row;
		for (int i = 0; i < salesTable.size(); i++) {
			row = (ArrayList<Object>) salesTable.get(i);
			if (!cbField.getItems().contains(String.valueOf(row.get(z))))
				cbField.getItems().add(String.valueOf(row.get(z)));
		}
	}

	/**
	 * This function is activated to change in the database the activation status of one sale.
	 * Before it checks if the sale that wants to be activated is overlapping with other sales.
	 * @param status - 0 disable, 1 activate
	 */
	private void setActivateStatus(int status) {
		Sales selectedSale = tblSales.getSelectionModel().getSelectedItem();
		if (selectedSale != null && SaleOverlapp(selectedSale, status) == false) {
			ArrayList<Object> request = new ArrayList<>();
			request.add("marketingManager/initiateSale");
			request.add("SetActivayeStatus");
			request.add(status);
			request.add(LocalDate.now());
			request.add(selectedSale.saleName);

			chs.client.handleMessageFromClientUI(request);
		}
	}

	/**
	 * This function is activated to check if sales are overlapping.
	 * @param activateSale - the sale that needs to be checked with other sales
	 * @param status - 0 - disable, 1 - activate
	 * @return if sale is overlaping other sales (true) else (false).
	 */
	private boolean SaleOverlapp(Sales activateSale, int status) {
		boolean isOverlapping = false;
		LocalTime timeStart = activateSale.startHour;
		LocalTime timeEnd = activateSale.endHour;

		if (status == 0)
			return isOverlapping;

		for (Sales sale : itemsForTable) {
			if ((activateSale.fuelType.compareTo(sale.fuelType) == 0) && sale.isActive == true
					&& activateSale.saleName.compareTo(sale.saleName) != 0) {

				if (timeStart.isAfter(sale.startHour) && timeStart.isBefore(sale.endHour)
						&& timeEnd.isAfter(sale.endHour))
					isOverlapping = true;
				else if (timeStart.isBefore(sale.startHour) && timeEnd.isAfter(sale.startHour)
						&& timeEnd.isBefore(sale.endHour))
					isOverlapping = true;
				else if (timeStart.isBefore(sale.startHour) && timeEnd.isAfter(sale.endHour))
					isOverlapping = true;
				else if (timeStart.isAfter(sale.startHour) && timeEnd.isBefore(sale.endHour))
					isOverlapping = true;
				if (timeStart.equals(sale.startHour) && timeEnd.equals(sale.endHour))
					isOverlapping = true;
				if (isOverlapping) {
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							lblErr.setText("Time is overlapping with other Sales");
						}
					});
				}
			}
		}
		return isOverlapping;

	}

}
